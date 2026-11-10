package com.reza.learning.contract.customer.repository;

import com.reza.learning.contract.customer.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

public interface CustomerRepository extends CrudRepository<CustomerEntity, UUID> {
}
