
package com.reza.learning.contract.order.service;

import com.reza.learning.contract.product.client.api.ProductControllerApi;
import com.reza.learning.contract.product.client.invoker.ApiClient;
import com.reza.learning.contract.product.client.model.ProductResponse;
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
public class ProductService {

	private final ProductControllerApi api;
	public ProductResponse find(UUID customerId) {
		log.info("Finding product by id : {}", customerId);
		final var response = api.get(customerId.toString());
		log.info("Got rest response as : {}", response);
		return response;
	}

	@Configuration
	public static class ApiClientConfiguration {

		@Value("${app.integration.product-service.endpoint}")
		private String endpoint;

		@Bean
		public ProductControllerApi productControllerApi(RestTemplateBuilder builder){
			var apiClient = productApiClient(builder);
			return new ProductControllerApi(apiClient);
		}

		private ApiClient productApiClient(RestTemplateBuilder builder) {
			log.info("Calling customer service at : {}", endpoint);
			final var apiClient = new ApiClient(builder.build());
			apiClient.setBasePath(endpoint);
			apiClient.setUserAgent("Java Client");
			return apiClient;
		}

	}

}
