package com.webdev.cosmo.cosmobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableR2dbcRepositories
@SpringBootApplication
public class CosmoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmoBackendApplication.class, args);
	}

}
