package com.reza.learning.contract.product.repository;

import com.reza.learning.contract.product.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

public interface ProductRepository extends CrudRepository<ProductEntity, UUID> {
}
