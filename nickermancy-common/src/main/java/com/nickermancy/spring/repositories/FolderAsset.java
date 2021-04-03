package com.nickermancy.spring.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

public class FolderAsset extends FileAsset {

    @Nullable
    private Long fileCount;

    @Nullable
    @JsonProperty("size")
    public Long getFileCount() {
        return fileCount;
    }

    public void setFileCount(@Nullable Long fileCount) {
        this.fileCount = fileCount;
    }
}
