package com.canal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableJpaRepositories(basePackages = "com.canal.domain")
//@ComponentScan({"com.canal.controller", "com.canal.service", "com.canal.domain","com.canal.dto","com.canal.security","com.canal.vo"})
public class CanalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanalApiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}
}
