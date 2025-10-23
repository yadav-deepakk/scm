package com.example.scm.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public @Configuration class AppConfig {

	@Bean
	ModelMapper mapper() {
		return new ModelMapper();
	}
}
