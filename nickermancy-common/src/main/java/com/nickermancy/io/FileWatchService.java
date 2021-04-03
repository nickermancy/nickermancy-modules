package com.nickermancy.io;

import com.nickermancy.logging.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.nickermancy.io.FileChangeEvent.Type.*;
import static java.lang.String.format;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;

public class FileWatchService {

    private static final Logger log = Logger.getLogger();

    private final Map<WatchKey, Path> registrations = new ConcurrentHashMap<>();
    private final Map<WatchKey, FileChangeListener> listeners = new ConcurrentHashMap<>();

    private final WatchService watchService;
    private final AtomicBoolean listening = new AtomicBoolean(false);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public FileWatchService(WatchService watchService) {
        this.watchService = watchService;
    }

    public void destroy() throws IOException {
        this.listening.set(false);
        watchService.close();
        executor.shutdownNow();
    }

    public void register(Path path, FileChangeListener fileChangeListener) throws IOException {
        final var key = path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        log.trace(() -> format("Registered with the file system watch service: %s", path));

        registrations.put(key, path);
        listeners.put(key, fileChangeListener);

        if (isDirectory(path)) registerChildren(path, fileChangeListener);
    }

    public void start() {
        this.listening.set(true);
        executor.submit(this::listen);
    }

    //-- Private Implementation --------------------------------------------------------------------------------------//

    private void listen() {
        try {
            while (listening.get()) {
                final var queuedKey = watchService.take();
                queuedKey.pollEvents().stream()
                    .filter(not(isEqual(OVERFLOW)))
                    .forEach(event -> {
                        final var childPath = (Path) event.context();
                        final var parentPath = registrations.get(queuedKey);
                        final var path = parentPath.resolve(childPath);
                        if (event.kind() == ENTRY_CREATE) {
                            handleFileCreated(queuedKey, new FileChangeEvent(CREATED, path));
                        } else if (event.kind() == ENTRY_MODIFY) {
                            handleFileModified(queuedKey, new FileChangeEvent(MODIFIED, path));
                        } else if (event.kind() == ENTRY_DELETE) {
                            handleFileDeleted(queuedKey, new FileChangeEvent(DELETED, path), parentPath);
                        }
                    });
                if (!queuedKey.reset()) registrations.remove(queuedKey);
                if (registrations.isEmpty()) break;
            }
        } catch (InterruptedException ex) {
            log.debug(ex.getMessage(), ex);
        } finally {
            this.listening.set(false);
        }
    }

    private void registerChildren(Path path, FileChangeListener fileChangeListener) {
        try {
            Files.list(path)
                .filter(this::isDirectory)
                .forEach(p -> {
                    try {
                        register(p, fileChangeListener);
                    } catch (IOException ex) {
                        log.error(ex.getMessage(), ex);
                    }
                });
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private void handleFileCreated(WatchKey queuedKey, FileChangeEvent event) {
        if (isDirectory(event.getPath())) {
            try {
                register(event.getPath(), listeners.get(queuedKey));
            } catch (IOException e) {
                log.error(() -> format("Failed to register with the file system watch service %s", event.getPath()));
            }
        }
        listeners.get(queuedKey).accept(event);
    }

    private void handleFileModified(WatchKey queuedKey, FileChangeEvent event) {
        listeners.get(queuedKey).accept(event);
    }

    private void handleFileDeleted(WatchKey queuedKey, FileChangeEvent event, Path parentPath) {
        if (event.getPath().equals(parentPath)) {
            registrations.remove(queuedKey);
            listeners.remove(queuedKey);
        }
        listeners.get(queuedKey).accept(event);
    }

    private boolean isDirectory(Path path) {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }
}
