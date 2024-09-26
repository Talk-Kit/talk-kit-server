package com.canal;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class CanalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanalApiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder pwdEncoder() { return new BCryptPasswordEncoder(); }

	@Bean
	public ModelMapper modelMapper() { return new ModelMapper(); }

}
