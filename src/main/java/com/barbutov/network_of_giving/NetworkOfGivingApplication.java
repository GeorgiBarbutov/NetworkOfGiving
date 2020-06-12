package com.barbutov.network_of_giving;

import com.barbutov.network_of_giving.data.contracts.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class NetworkOfGivingApplication {

	//TODO: Fix number rounding, validation of input, search bars, icons
	public static void main(String[] args) {
		SpringApplication.run(NetworkOfGivingApplication.class, args);
	}
}
