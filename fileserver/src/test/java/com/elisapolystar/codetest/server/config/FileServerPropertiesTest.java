package com.elisapolystar.codetest.server.config;

import com.elisapolystar.codetest.server.config.FileServerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "server.port=1234",
        "server.filePath=src/test/resources/example.txt"
})
@EnableConfigurationProperties(FileServerProperties.class)
class FileServerPropertiesTest {

    @Autowired
    private FileServerProperties properties;

    @Test
    void shouldLoadConfiguration() {
        assertEquals(1234, properties.port());
        assertEquals("src/test/resources/example.txt", properties.filePath());
    }
}