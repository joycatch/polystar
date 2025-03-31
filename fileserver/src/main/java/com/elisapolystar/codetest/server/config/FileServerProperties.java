package com.elisapolystar.codetest.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record FileServerProperties(int port, String filePath) { }