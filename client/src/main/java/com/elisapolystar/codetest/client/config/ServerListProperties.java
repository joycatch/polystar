package com.elisapolystar.codetest.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties
public record ServerListProperties(List<ServerConfig> servers) {
}