package com.nickermancy.spring.web;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class MediaTypeSupportTests {

    @Test
    void getMediaTypeFromFileName() {
        assertThat(MediaTypeSupport.getMediaType("pom.xml"))
            .isEqualTo(MediaType.valueOf("application/xml"));
    }

    @Test
    void getMediaTypeFromPath() {
        assertThat(MediaTypeSupport.getMediaType(Path.of("package.json")))
            .isEqualTo(MediaType.valueOf("application/json"));
    }

    @Test
    void probeMediaTypeForExistingFile() {
        assertThat(MediaTypeSupport.probeMediaType(Path.of("pom.xml")))
            .isEqualTo(MediaType.valueOf("application/xml"));
    }

    @Test
    void probeMediaTypeForMissingFile() {
        assertThat(MediaTypeSupport.probeMediaType(Path.of("package.json")))
            .isEqualTo(MediaType.valueOf("application/json"));
    }
}
