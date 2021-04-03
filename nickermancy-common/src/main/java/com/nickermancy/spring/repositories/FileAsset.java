package com.nickermancy.spring.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.lang.Nullable;

public abstract class FileAsset {

    public static UUID generateId(URI assetUri) {
        return UUID.nameUUIDFromBytes(assetUri.getPath().getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    private UUID id;

    @Nullable
    private UUID rootId;

    @Nullable
    private Path path;

    @Nullable
    private URI uri;

    private boolean dirty;

    @Nullable
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Nullable
    @JsonProperty("root-id")
    public UUID getRootId() {
        return rootId;
    }

    public void setRootId(@Nullable UUID rootId) {
        this.rootId = rootId;
    }

    @Nullable
    @JsonProperty("path")
    public Path getPath() {
        return path;
    }

    public void setPath(@Nullable Path path) {
        this.path = path;
    }

    @JsonProperty("uri")
    @Nullable
    public URI getUri() {
        return uri;
    }

    public void setUri(@Nullable URI uri) {
        try {
            this.uri = uri == null ? null : new URI(null, null, uri.getPath(), null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(uri.getPath());
        }
    }

    @JsonIgnore
    boolean isDirty() {
        return dirty;
    }

    @SuppressWarnings("SameParameterValue")
    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
