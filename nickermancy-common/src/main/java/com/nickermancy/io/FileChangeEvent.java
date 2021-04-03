package com.nickermancy.io;

import java.nio.file.Path;

public class FileChangeEvent {

    public enum Type {
        CREATED, MODIFIED, DELETED
    }

    private final Path path;
    private final Type type;

    public FileChangeEvent(Type type, Path path) {
        this.path = path;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Path getPath() {
        return path;
    }
}
