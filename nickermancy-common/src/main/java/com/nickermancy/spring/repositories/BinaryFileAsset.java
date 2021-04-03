package com.nickermancy.spring.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

public class BinaryFileAsset extends FileAsset {

    @Nullable
    private Long size;

    @Nullable
    private String sha256;

    @Nullable
    private String mediaType;

    @Nullable
    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    public void setSize(@Nullable Long size) {
        this.size = size;
    }

    @Nullable
    @JsonProperty("sha-256")
    public String getSha256() {
        return sha256;
    }

    public void setSha256(@Nullable String sha256) {
        this.sha256 = sha256;
    }

    @Nullable
    @JsonProperty("media-type")
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(@Nullable String mediaType) {
        this.mediaType = mediaType;
    }
}
