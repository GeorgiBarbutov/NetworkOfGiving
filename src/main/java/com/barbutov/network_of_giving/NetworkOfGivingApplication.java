package com.barbutov.network_of_giving;

import com.barbutov.network_of_giving.data.contracts.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class NetworkOfGivingApplication {
	public static void main(String[] args) {
		SpringApplication.run(NetworkOfGivingApplication.class, args);
	}
}
