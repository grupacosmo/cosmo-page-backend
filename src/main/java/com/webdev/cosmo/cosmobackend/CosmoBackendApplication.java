package com.webdev.cosmo.cosmobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class CosmoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmoBackendApplication.class, args);
	}

}
