package com.user.getway.configuration;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public String uuidGeneratCode() {
		return UUID.randomUUID().toString().replace("-", "").substring(12);
	}

//	@Bean("webFluxRequestDataValueProcessor")
//	public RequestDataValueProcessor webFluxRequestDataValueProcessor() {
//	    // ... your custom request data value processor logic (if needed)
//	    return new RequestDataValueProcessor(); // Example
//	}

}
