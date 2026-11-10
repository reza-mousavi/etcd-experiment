package com.reza.learning.contract.order.service;

import com.reza.learning.contract.customer.client.api.CustomerControllerApi;
import com.reza.learning.contract.customer.client.invoker.ApiClient;
import com.reza.learning.contract.customer.client.model.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerControllerApi api;
	public CustomerResponse find(UUID customerId) {
		log.info("Finding customer by id : {}", customerId);
		final var customerResponse = api.get(customerId.toString());
		log.info("Got rest response as : {}", customerResponse);
		return customerResponse;
	}

	@Configuration
	public static class ApiClientConfiguration {

		@Value("${app.integration.customer-service.endpoint}")
		private String endpoint;

		@Bean
		public CustomerControllerApi customerControllerApi(RestTemplateBuilder builder) {
			var apiClient = apiClient(builder);
			return new CustomerControllerApi(apiClient);
		}

		private ApiClient apiClient(RestTemplateBuilder builder) {
			log.info("Calling customer service at : {}", endpoint);
			final var apiClient = new ApiClient(builder.build());
			apiClient.setBasePath(endpoint);
			apiClient.setUserAgent("Java Client");
			return apiClient;
		}

	}

}
