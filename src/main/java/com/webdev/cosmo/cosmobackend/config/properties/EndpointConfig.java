package com.webdev.cosmo.cosmobackend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "endpoint")
public class EndpointConfig {
    private List<String> secured;
}
