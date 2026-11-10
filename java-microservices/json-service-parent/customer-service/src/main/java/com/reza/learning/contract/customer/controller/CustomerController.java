package com.reza.learning.contract.customer.controller;

import com.reza.learning.contract.customer.api.CustomerApi;
import com.reza.learning.contract.customer.entity.CustomerEntity;
import com.reza.learning.contract.customer.mapper.CustomerMapper;
import com.reza.learning.contract.customer.model.CustomerRequest;
import com.reza.learning.contract.customer.model.CustomerResponse;
import com.reza.learning.contract.customer.service.CustomerService;
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
public class CustomerController implements CustomerApi {

	private final CustomerMapper customerMapper;

	private final CustomerService customerService;

	@Override
	public ResponseEntity<List<CustomerResponse>> getAll() {
		log.info("Get all customers");
		final Iterable<CustomerEntity> customers = customerService.findAll();
		return ResponseEntity.ok(customerMapper.toDto(customers));
	}

    @Override
	public ResponseEntity<CustomerResponse> save(@RequestBody CustomerRequest customerRequest) {
		log.info("Persist customer : {}", customerRequest);
		final CustomerEntity customerEntity = customerMapper.toEntity(customerRequest);
		final CustomerEntity entity = customerService.save(customerEntity);
		var dto = customerMapper.toDto(entity);
		return ResponseEntity.created(URI.create("/" + entity.getId())).body(dto);
	}

    @Override
	public ResponseEntity<CustomerResponse> get(@PathVariable(name = "id") String uuid) {
		log.info("Get customer by id : {}", uuid);
		final CustomerEntity entity = customerService.find(uuid);
		return ResponseEntity.ok(customerMapper.toDto(entity));
	}

    @Override
	public ResponseEntity<Void> delete(@PathVariable(name = "id") String uuid) {
		log.info("Delete customer by id : {}", uuid);
		return ResponseEntity.noContent().build();
	}

}
