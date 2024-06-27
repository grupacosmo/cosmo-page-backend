package com.webdev.cosmo.cosmobackend.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "images")
@Data
public class ImagesConfig {
    private List<String> types;
}
