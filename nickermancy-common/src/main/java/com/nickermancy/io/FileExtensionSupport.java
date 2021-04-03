package com.nickermancy.io;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileExtensionSupport {
    
    private static final Pattern pattern = Pattern.compile("^.+[.](?<extension>[^.]+)$");
    private static final String GROUP_EXTENSION = "extension";

    public static Optional<String> getExtension(Path path) {
        return getExtension(path.toString());
    }

    public static Optional<String> getExtension(String filename) {
        return Optional.of(pattern.matcher(filename))
            .filter(Matcher::matches)
            .map(matcher -> matcher.group(GROUP_EXTENSION).toLowerCase());
    }
}
