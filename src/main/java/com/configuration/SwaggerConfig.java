package com.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes("Bearer Authentication", createAPTScheme()))
				.info(new Info().title("Master Schema Api's")
						.description("this is for global"));
				
	}
	
	private SecurityScheme createAPTScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
	}
	
//	 @Bean
//	    public Docket api() {
//	        return new Docket(DocumentationType.SWAGGER_2)
//	                .select()
//	                .apis(RequestHandlerSelectors.any())
//	                .paths(PathSelectors.any())
//	                .build();
//	    }

}
