package com.reza.learning.contract.product.controller;

import com.reza.learning.contract.product.api.ProductApi;
import com.reza.learning.contract.product.entity.ProductEntity;
import com.reza.learning.contract.product.mapper.ProductMapper;
import com.reza.learning.contract.product.model.ProductRequest;
import com.reza.learning.contract.product.model.ProductResponse;
import com.reza.learning.contract.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class ProductController implements ProductApi {

	private final ProductMapper productMapper;

	private final ProductService productService;

	@Override
	public ResponseEntity<List<ProductResponse>> getAll() {
		log.info("Get all products");
		final Iterable<ProductEntity> entities = productService.findAll();
		return ResponseEntity.ok(productMapper.toDto(entities));
	}

	@Override
	public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest) {
		log.info("Persist product : {}", productRequest);
		final ProductEntity productEntity = productMapper.toEntity(productRequest);
		final ProductEntity entity = productService.save(productEntity);
		var dto = productMapper.toDto(entity);
		return ResponseEntity.created(URI.create("/" + entity.getId())).body(dto);
	}

	@Override
	public ResponseEntity<ProductResponse> get(@PathVariable(name = "id") String uuid) {
		log.info("Get product by id : {}", uuid);
		final ProductEntity entity = productService.find(uuid);
		return ResponseEntity.ok(productMapper.toDto(entity));
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable(name = "id") String uuid) {
		log.info("Delete product by id : {}", uuid);
		productService.delete(uuid);
		return ResponseEntity.noContent().build();
	}

}
