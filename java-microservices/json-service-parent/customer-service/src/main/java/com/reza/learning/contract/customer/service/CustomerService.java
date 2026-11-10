package com.reza.learning.contract.customer.service;

import com.reza.learning.contract.customer.entity.CustomerEntity;
import com.reza.learning.contract.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Iterable<CustomerEntity> findAll() {
		return customerRepository.findAll();
	}

	public CustomerEntity find(String uuidString) {
		final UUID uuid = UUID.fromString(uuidString);
		return customerRepository.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("No entity found with given id : " + uuidString));
	}

	public CustomerEntity save(CustomerEntity entity) {
		return customerRepository.save(entity);
	}

	public void delete(String uuidString) {
		final UUID uuid = UUID.fromString(uuidString);
		customerRepository.deleteById(uuid);
	}
}
