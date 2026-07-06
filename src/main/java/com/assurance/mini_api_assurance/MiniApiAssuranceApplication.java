package com.assurance.mini_api_assurance;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class MiniApiAssuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniApiAssuranceApplication.class, args);
	}


}