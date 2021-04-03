package com.nickermancy.spring.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nickermancy.io.FileChangeListener;
import com.nickermancy.io.FileWatchService;
import com.nickermancy.logging.Logger;
import com.nickermancy.spring.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

/**
 * Encapsulates the storage, retrieval, and search behavior for a collection of {@link BinaryFileAsset} objects.
 */
public class BinaryAssetRepository {

    private static final Logger log = Logger.getLogger();

    //private final ExecutorService executor;
    private final MessageDigest sha256Digest;
    private final Pattern resourceRegex;
    private final Path metaDataRootPath;
    private final Duration refreshInterval;
    private final FileWatchService fileWatchService;
    private final ObjectMapper objectMapper;
    private final Map<Path, Map<Path, BinaryFileAsset>> binaryAssetMetaData = new ConcurrentHashMap<>();
    private final Map<UUID, Path> fileSystemRootPaths = new ConcurrentHashMap<>();

    public BinaryAssetRepository(BinaryAssetRepositoryProperties properties,
                                 FileWatchService fileWatchService,
                                 ObjectMapper objectMapper) {
        this.fileWatchService = fileWatchService;
        this.objectMapper = objectMapper;
        this.metaDataRootPath = properties.getMetaDataRoot();
        this.resourceRegex = properties.getResourceRegex();
        this.refreshInterval = properties.getRefreshInterval();
//        this.executor = Executors.newSingleThreadExecutor();

        try {
            this.sha256Digest = MessageDigest.getInstance(properties.getHashAlgorithm());
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(properties.getHashAlgorithm(), ex);
        }
    }

    /**
     * Returns a {@link Mono} that produces the number of folder assets under the given folder.
     *
     * @param uuid     the identifier of the file system that asset was imported from
     * @param assetUri the asset URI of the folder whose subfolders are to be counted
     *
     * @return a {@link Mono} producing the number of folder assets under the asset URI
     */
    @SuppressWarnings("unused")
    public Mono<Long> countFolderAssets(UUID uuid, URI assetUri) {
        return Mono.fromSupplier(() -> {
            final var folderPath = getPathToBinary(uuid, assetUri);
            return binaryAssetMetaData.keySet().stream().filter(p -> p.startsWith(folderPath)).count();
        });
    }

    /**
     * Returns a {@link Mono} that produces the number of binary assets under the given folder.
     *
     * @param uuid     the identifier of the file system that asset was imported from
     * @param assetUri the asset URI of the folder whose binary assets are to be counted
     *
     * @return a {@link Mono} producing the number of binary assets under the asset URI
     */
    public Mono<Long> countBinaryAssets(UUID uuid, URI assetUri) {
        return Mono.fromSupplier(() -> {
            final var fileSystemPath = getPathToBinary(uuid, assetUri);
            return Optional.ofNullable(binaryAssetMetaData.get(fileSystemPath)).stream().count();
        });
    }

    /**
     * Returns a {@link Flux} of folders that contain binary assets.
     *
     * @param uuid     the identifier of the file system that asset was imported from
     * @param assetUri the asset URI of the folder whose subfolders are to be listed
     *
     * @return a {@link Flux} of folder asset information
     */
    public Flux<FolderAsset> listFolderAssets(UUID uuid, URI assetUri) {
        return Flux.fromStream(() -> {
            final var folderPath = getPathToBinary(uuid, assetUri);
            return binaryAssetMetaData.keySet().stream().filter(p -> p.startsWith(folderPath));
        }).map(this::getFolderAsset);
    }

    /**
     * Returns a {@link Mono} that produces information about a single binary asset.
     *
     * @param rootId   the identifier of the root folder that asset was imported from
     * @param assetUri the URI of the desired asset
     *
     * @return a {@link Mono} of binary asset information
     */
    public Mono<BinaryFileAsset> getBinaryAsset(UUID rootId, URI assetUri) {
        return Mono.fromSupplier(() -> {
            final var path = getPathToMetadata(rootId, assetUri);
            var asset = binaryAssetMetaData.get(path);
            return asset == null ? null : asset.get(path);
        });
    }

    /**
     * Returns a sorted {@link Flux} of all binary assets that reside under the given folder.
     *
     * @param assetUri the asset URI of the folder whose contents are to be listed
     *
     * @return a {@link Flux} of binary asset information
     */
    public Flux<BinaryFileAsset> listBinaryAssets(UUID rootId, URI assetUri) {
        return listBinaryAssets(rootId, assetUri, Pageable.unpaged(), Sort.unsorted());
    }

    /**
     * Returns a sorted {@link Flux} of the binary assets that reside under the given folder.
     *
     * @param assetUri the asset URI of the folder whose contents are to be listed
     * @param sort     describes how the assets are to be sorted
     *
     * @return a sorted {@link Flux} of binary asset information
     */
    @SuppressWarnings("unused")
    public Flux<BinaryFileAsset> listBinaryAssets(UUID rootId, URI assetUri, Sort sort) {
        return listBinaryAssets(rootId, assetUri, Pageable.unpaged(), sort);
    }

    /**
     * Returns a paged {@link Flux} of the binary assets that reside under the given folder.
     *
     * @param assetUri the asset URI of the folder whose contents are to be listed
     * @param pageable describes how the assets are to be paged
     *
     * @return a paged {@link Flux} of binary asset information
     */
    public Flux<BinaryFileAsset> listBinaryAssets(UUID rootId, URI assetUri, Pageable pageable) {
        return listBinaryAssets(rootId, assetUri, pageable, pageable.getSort());
    }

    /**
     * Returns {@code true} if the supplied asset URI represents a folder.
     *
     * @param assetUri the URI of the asset to be tested
     *
     * @return {@code true} if the asset is a folder
     */
    public boolean isFolder(UUID rootId, URI assetUri) {
        return Files.isDirectory(getPathToBinary(rootId, assetUri), LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * Returns {@code true} if the supplied asset URI represents a binary file.
     *
     * @param assetUri the URI of the asset to be tested
     *
     * @return {@code true} if the asset is a file
     */
    public boolean isFile(UUID rootId, URI assetUri) {
        return Files.isRegularFile(getPathToBinary(rootId, assetUri), LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * Returns an {@link InputStream} for reading the binary file.
     *
     * @param assetUri the location of the asset to be read
     *
     * @return an input stream for reading the binary file
     *
     * @throws IOException if there is an error creating the input stream
     */
    public InputStream getInputStream(UUID rootId, URI assetUri) throws IOException {
        try {
            return Files.newInputStream(getPathToBinary(rootId, assetUri));
        } catch (NoSuchFileException ex) {
            throw new ResourceNotFoundException(assetUri.getPath(), ex);
        }
    }

    public UUID importFolder(Path root) {
        if (!Files.isDirectory(root)) throw new ResourceNotFoundException(root.toString());
        final var uuid = UUID.nameUUIDFromBytes(root.toString().getBytes(StandardCharsets.UTF_8));
        fileSystemRootPaths.put(uuid, root);
        //executor.submit(() -> {
        log.info(() -> format("Importing binary assets from '%s'", root));
        final var start = System.currentTimeMillis();
        Flux.fromStream(() -> this.getPathsToBinaryAssets(root))
            .doOnComplete(this::cleanupBinaryAssets)
            .doOnComplete(() -> listenForChanges(root))
            .doOnTerminate(() -> log.info(() -> {
                final var duration = System.currentTimeMillis() - start;
                final var count = binaryAssetMetaData.getOrDefault(root, emptyMap()).size();
                return format("Finished loading %,d binary resources in %,.3f seconds.", count, duration / 1000f);
            }))
            .map(this::getBinaryResource)
            .doOnNext(this::computeIdentifier)
            .doOnNext(this::computeDigest)
            .doOnNext(this::probeMediaType)
            .doOnNext(this::computeFileSize)
            .doOnNext(this::saveMetadataFile)
            .subscribe(this::addMetadataEntry);
        //});
        return uuid;
    }

    //-- Private Implementation --------------------------------------------------------------------------------------//

    private void listenForChanges(Path root) {
        try {
            fileWatchService.register(root, getFileChangeListener());
        } catch (IOException e) {
            log.error("Failed to register file change listener for root path: " + root, e);
        }
    }

    private FileChangeListener getFileChangeListener() {
        return event -> {
            switch (event.getType()) {
                case CREATED:
                    createBinaryAsset(event.getPath());
                    break;
                case MODIFIED:
                    updateBinaryAsset(event.getPath());
                    break;
                case DELETED:
                    deleteBinaryAsset(event.getPath());
                    break;
            }
        };
    }

    private Flux<BinaryFileAsset> listBinaryAssets(UUID rootId, URI assetUri, Pageable pageable, Sort sort) {
        final var path = getPathToBinary(rootId, assetUri);
        final var results = binaryAssetMetaData.get(path);
        if (results == null) {
            throw new ResourceNotFoundException(assetUri.getPath());
        }
        var binaryResourceFlux = Flux.fromIterable(results.values());
        if (!fileSystemRootPaths.containsValue(path)) {
            binaryResourceFlux = binaryResourceFlux.filter(resource -> resource.getPath() != null && resource.getPath().startsWith(path));
        }
        if (sort.isSorted()) {
            binaryResourceFlux = binaryResourceFlux.sort(Comparator.comparing(BinaryFileAsset::getPath));
        }
        if (pageable.isPaged()) {
            binaryResourceFlux = binaryResourceFlux.skip(pageable.getOffset()).limitRequest(pageable.getPageSize());
        }
        return binaryResourceFlux;
    }

    private void createBinaryAsset(Path path) {
        log.info(() -> "Created: " + path);
    }

    private void updateBinaryAsset(Path path) {
        log.info(() -> "Modified: " + path);
    }

    private void deleteBinaryAsset(Path path) {
        log.info(() -> "Deleted: " + path);
    }

    private void cleanupBinaryAssets() {
        Flux.interval(Duration.ZERO, refreshInterval).subscribe(iteration -> {
            final var start = System.currentTimeMillis();
            log.info(() -> format("Cleaning up dangling metadata from '%s'", metaDataRootPath));
            Flux.fromStream(this::getBinaryResourceMetaDataPaths)
                .doOnTerminate(() -> log.info(() -> {
                    final var duration = System.currentTimeMillis() - start;
                    return format("Finished cleaning up metadata in %,.3f milliseconds.", duration / 1000f);
                }))
                .flatMap(this::readBinaryResourceMetaDataOrEmpty)
                .filter(this::isResourceMissing)
                .doOnNext(this::deleteMetadataFile)
                .subscribe(this::removeMetaDataEntry);
        });
    }

    private boolean includeBinaryAsset(Path path) {
        if (!Files.isRegularFile(path)) return false;
        if (!Files.isReadable(path)) return false;
        final var filename = path.getFileName().toString();
        final var include = resourceRegex.matcher(filename).matches();
        if (include) {
            log.trace(() -> "Adding " + path);
        } else {
            log.debug(() -> "Excluding " + path);
        }
        return include;
    }

    private boolean includeMetaData(Path path) {
        if (!Files.isRegularFile(path)) return false;
        if (!Files.isReadable(path)) return false;
        final var filename = path.getFileName().toString();
        return filename.endsWith(".json");
    }

    private void saveMetadataFile(BinaryFileAsset binaryFileAsset) {
        if (binaryFileAsset.isDirty()) {
            Assert.notNull(binaryFileAsset.getId(), "BinaryResource id field cannot be null");
            Assert.notNull(binaryFileAsset.getRootId(), "BinaryResource rootId must not be null");

            final var rootId = binaryFileAsset.getRootId();
            final var assetId = binaryFileAsset.getId();
            final var path = getPathToMetadata(rootId, assetId);

            try {
                Files.createDirectories(path.getParent());
                objectMapper.writeValue(path.toFile(), binaryFileAsset);
            } catch (IOException e) {
                log.warn("Failed to write metadata for binary resource: {}", binaryFileAsset.getPath());
            }
        }
    }

    private void deleteMetadataFile(BinaryFileAsset binaryFileAsset) {
        Assert.notNull(binaryFileAsset.getId(), "BinaryResource id must not be null");
        Assert.notNull(binaryFileAsset.getRootId(), "BinaryResource rootId must not be null");

        final var rootId = binaryFileAsset.getRootId();
        final var assetId = binaryFileAsset.getId();
        final var path = getPathToMetadata(rootId, assetId);

        log.debug(() -> "Deleting metadata " + path);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error(() -> "Failed to delete " + path, e);
        }
    }

    private void addMetadataEntry(BinaryFileAsset binaryFileAsset) {
        var path = binaryFileAsset.getPath();
        while (path != null) {
            final var parent = path.getParent();
            if (parent != null) {
                binaryAssetMetaData.computeIfAbsent(parent, value -> new ConcurrentHashMap<>()).put(binaryFileAsset.getPath(), binaryFileAsset);
                if (fileSystemRootPaths.containsValue(parent)) break;
            }
            path = parent;
        }
    }

    private void removeMetaDataEntry(BinaryFileAsset binaryFileAsset) {
        var path = binaryFileAsset.getPath();
        while (path != null) {
            final var parent = path.getParent();
            if (parent != null) {
                final var assets = binaryAssetMetaData.get(parent);
                if (assets != null) {
                    assets.remove(path);
                }
                if (parent.equals(metaDataRootPath)) {
                    break;
                }
            }
            path = parent;
        }
    }

    private Stream<Path> getPathsToBinaryAssets(Path root) {
        try {
            return Files.walk(root).filter(this::includeBinaryAsset);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    private Stream<Path> getBinaryResourceMetaDataPaths() {
        try {
            return Files.walk(metaDataRootPath).filter(this::includeMetaData);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    @Nullable
    private BinaryFileAsset readBinaryResourceMetaData(UUID rootId, URI assetUri) {
        final var path = getPathToMetadata(rootId, BinaryFileAsset.generateId(assetUri));
        return readBinaryResourceMetaData(path);
    }

    @Nullable
    private BinaryFileAsset readBinaryResourceMetaData(Path path) {
        if (Files.exists(path)) {
            try {
                return objectMapper.readValue(path.toFile(), BinaryFileAsset.class);
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    private Mono<BinaryFileAsset> readBinaryResourceMetaDataOrEmpty(Path path) {
        final var metaData = readBinaryResourceMetaData(path);
        return metaData == null ? Mono.empty() : Mono.just(metaData);
    }

    private boolean isResourceMissing(BinaryFileAsset binaryFileAsset) {
        final var path = binaryFileAsset.getPath();
        final var missing = path == null || !Files.exists(path);
        if (missing) log.debug(() -> "File is missing: " + path);
        return missing;
    }

    private BinaryFileAsset getBinaryResource(Path path) {
        final var rootId = getRootId(path);
        final var assetUri = toResourceURI(path);
        var asset = readBinaryResourceMetaData(rootId, assetUri);
        if (asset == null) {
            asset = new BinaryFileAsset();
            asset.setId(BinaryFileAsset.generateId(assetUri));
            asset.setRootId(rootId);
            asset.setUri(assetUri);
            asset.setPath(path);
            asset.setDirty(true);
        }
        return asset;
    }

    private FolderAsset getFolderAsset(Path path) {
        final var rootId = getRootId(path);
        final var assetUri = toResourceURI(path);
        var asset = new FolderAsset();
        asset.setId(FolderAsset.generateId(assetUri));
        asset.setRootId(rootId);
        asset.setUri(assetUri);
        asset.setPath(path);
        asset.setDirty(true);
        return asset;
    }

    private void computeIdentifier(BinaryFileAsset binaryFileAsset) {
        if (binaryFileAsset.getId() == null && binaryFileAsset.getUri() != null) {
            binaryFileAsset.setId(BinaryFileAsset.generateId(binaryFileAsset.getUri()));
            binaryFileAsset.setDirty(true);
        }
    }

    private void computeFileSize(BinaryFileAsset binaryFileAsset) {
        if (binaryFileAsset.getSize() == null && binaryFileAsset.getPath() != null) {
            try {
                binaryFileAsset.setSize(Files.size(binaryFileAsset.getPath()));
                binaryFileAsset.setDirty(true);
            } catch (IOException e) {
                log.warn(() -> "Failed to read file size: " + e.getMessage(), e);
            }
        }
    }

    private void computeDigest(BinaryFileAsset binaryFileAsset) {
        if (binaryFileAsset.getSha256() == null && binaryFileAsset.getPath() != null) {
            try {
                log.info("Computing digest for {}", binaryFileAsset.getPath());
                final var digest = (MessageDigest) this.sha256Digest.clone();
                try (final var in = Files.newInputStream(binaryFileAsset.getPath())) {
                    final var buffer = new byte[4096];
                    var bytesRead = 0;
                    do {
                        bytesRead = in.read(buffer, 0, buffer.length);
                        if (bytesRead > 0) {
                            digest.update(buffer);
                        }
                    } while (bytesRead != -1);
                }
                binaryFileAsset.setSha256(Base64.getEncoder().encodeToString(digest.digest()));
                binaryFileAsset.setDirty(true);
            } catch (CloneNotSupportedException | IOException e) {
                log.warn(() -> "Failed to compute digest for " + binaryFileAsset.getPath(), e);
            }
        }
    }

    private void probeMediaType(BinaryFileAsset binaryFileAsset) {
        if (binaryFileAsset.getMediaType() == null && binaryFileAsset.getPath() != null) {
            try {
                binaryFileAsset.setMediaType(Files.probeContentType(binaryFileAsset.getPath()));
                binaryFileAsset.setDirty(true);
            } catch (IOException e) {
                log.warn(() -> "Failed to probe media type: " + e.getMessage(), e);
            }
            if (binaryFileAsset.getMediaType() == null) {
                binaryFileAsset.setMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
        }
    }

    private Path getPathToBinary(UUID rootId, URI assetUri) {
        final var resourcePath = assetUri.getPath().replaceFirst("^/", "");
        final var fileSystemRootPath = requireNonNull(fileSystemRootPaths.get(rootId), "No file system root having id " + rootId);
        final var fileSystemPath = fileSystemRootPath.resolve(resourcePath);
        if (!fileSystemPath.startsWith(fileSystemRootPath)) {
            throw new IllegalArgumentException(assetUri.getPath());
        }
        return fileSystemPath;
    }

    private URI toResourceURI(Path path) {
        final var fileSystemRootPath = getFileSystemRoot(path);
        final var resourcePath = path.toString().replace(fileSystemRootPath.toString(), "");
        try {
            return new URI(null, null, resourcePath, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(resourcePath);
        }
    }

    private Path getPathToMetadata(UUID rootId, URI assetUri) {
        return getPathToMetadata(rootId, BinaryFileAsset.generateId(assetUri));
    }

    private Path getPathToMetadata(UUID rootId, UUID assetId) {
        final var id = assetId.toString();
        final var id1 = id.substring(0, 1);
        final var id2 = id.substring(0, 4);
        return metaDataRootPath.resolve(Path.of(rootId.toString(), id1, id2, id + ".json"));
    }

    private UUID getRootId(Path path) {
        return fileSystemRootPaths.entrySet().stream()
            .filter(entry -> entry.getValue().startsWith(path))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("The path has not been imported: " + path));
    }

    private Path getFileSystemRoot(Path path) {
        return fileSystemRootPaths.values().stream()
            .filter(path::startsWith).findFirst()
            .orElseThrow(() -> new IllegalStateException("The path has not been imported: " + path));
    }
}
