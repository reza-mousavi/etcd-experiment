package com.reza.learning.contract.product.service;

import com.reza.learning.contract.product.entity.ProductEntity;
import com.reza.learning.contract.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Iterable<ProductEntity> findAll() {
		return productRepository.findAll();
	}

	public ProductEntity find(String uuidString) {
		final UUID uuid = UUID.fromString(uuidString);
		return productRepository.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("No entity found with given id : " + uuidString));
	}

	public ProductEntity save(ProductEntity entity) {
		return productRepository.save(entity);
	}

	public void delete(String uuidString) {
		final UUID uuid = UUID.fromString(uuidString);
		productRepository.deleteById(uuid);
	}
}
