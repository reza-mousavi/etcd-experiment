package com.reza.learning.contract.order.repository;

import com.reza.learning.contract.order.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

public interface OrderRepository extends CrudRepository<OrderEntity, UUID> {
}