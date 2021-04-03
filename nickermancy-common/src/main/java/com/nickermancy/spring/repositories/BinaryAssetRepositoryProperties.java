package com.nickermancy.spring.repositories;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.regex.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import static java.util.Objects.requireNonNull;

public class BinaryAssetRepositoryProperties {

    /**
     * The root URL path for image resources.
     */
    @Nullable
    private URI resourceRoot;

    /**
     * The root directory from which to look up image resources.
     */
    @Nullable
    private Path metaDataRoot;

    /**
     * A regular expression for matching files to be included when looking up image resources.
     */
    @Nullable
    private Pattern resourceRegex;

    /**
     * The algorithm for computing hashes of binary resources.
     */
    private String hashAlgorithm = "SHA-256";

    /**
     * The interval to scan for refreshing resource metadata.
     */
    private Duration refreshInterval = Duration.ofHours(24);

    @NotNull
    @javax.validation.constraints.Pattern(regexp = "(/[^/]+)+")
    public URI getResourceRoot() {
        return requireNonNull(resourceRoot, "resourceRoot must not be null");
    }

    public void setResourceRoot(@Nullable URI resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    @NotNull
    public Path getMetaDataRoot() {
        return requireNonNull(metaDataRoot, "metaDataRoot must not be null");
    }

    public void setMetaDataRoot(@Nullable Path metaDataRoot) {
        this.metaDataRoot = metaDataRoot;
    }

    @NotBlank
    public Pattern getResourceRegex() {
        return requireNonNull(resourceRegex, "resourceRegex must not be null");
    }

    public void setResourceRegex(@Nullable Pattern resourceRegex) {
        this.resourceRegex = resourceRegex;
    }

    @NotBlank
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public Duration getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
    }
}
