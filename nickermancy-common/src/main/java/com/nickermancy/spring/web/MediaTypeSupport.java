package com.nickermancy.spring.web;

import com.nickermancy.io.FileExtensionSupport;
import com.nickermancy.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.springframework.http.MediaType;

public class MediaTypeSupport {

    private static final Logger log = Logger.getLogger();

    private static final Map<String, String> mappings = new HashMap<>();
    private static final String resourceName = "MediaTypes.properties";

    static {
        try {
            var urls = Thread.currentThread().getContextClassLoader().getResources(resourceName);
            for (var url : Collections.list(urls)) {
                var properties = new Properties();
                try (var inputStream = url.openStream()) {
                    properties.load(inputStream);
                    properties.stringPropertyNames().forEach(key -> mappings.put(key, properties.getProperty(key)));
                }
            }
        } catch (IOException ex) {
            log.error("Failed to query resource: " + ex.getMessage(), ex);
        }
    }

    public static MediaType getMediaType(Path path) {
        return getMediaType(path.getFileName().toString());
    }

    public static MediaType getMediaType(String filename) {
        return MediaType.valueOf(FileExtensionSupport.getExtension(filename)
            .flatMap(extension -> Optional.ofNullable(mappings.get(extension)))
            .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    public static MediaType probeMediaType(Path path) {
        try {
            if (Files.isRegularFile(path) && Files.isReadable(path)) {
                return Optional.ofNullable(Files.probeContentType(path))
                    .map(MediaType::valueOf)
                    .orElseGet(() -> getMediaType(path));
            }
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
        return getMediaType(path);
    }
}
