package com.reza.learning.contract.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		servers = {@Server(url = "/", description = "Default Server URL")},
		info = @Info(
				title ="OpenAPI Demo",
				description = "This is basic implementation of swagger API",
				version = "1.0",
				contact = @Contact(
						name = "Reza Mousavi",
						email = "rza.mousavi@gmail.com",
						url = "linkedin.com/reza-mousavi-developer"
				)
		)
)
public class OpenApiConfig {

	@Bean
	public GroupedOpenApi mainApi(){
		return GroupedOpenApi.builder()
				.group("Product API")
				.packagesToScan("com.reza.learning.contract")
				.build();
	}

	@Bean
	public GroupedOpenApi actuatorApi(){
		return GroupedOpenApi.builder()
				.group("Actuator")
				.packagesToScan("org.springframework.boot.actuate")
				.build();
	}


}