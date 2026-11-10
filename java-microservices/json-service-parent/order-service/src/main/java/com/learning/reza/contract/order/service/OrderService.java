package com.reza.learning.contract.order.service;

import com.reza.learning.contract.order.entity.OrderEntity;
import com.reza.learning.contract.order.entity.OrderStatus;
import com.reza.learning.contract.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderEntity find(String uuidString) {
        final UUID uuid = UUID.fromString(uuidString);
        return orderRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("No entity found with given id : " + uuidString));
    }

    public OrderEntity save(OrderEntity order) {
        log.info("Finding customer by id : {}", order.getCustomerId());
        final var customerResponse = customerService.find(order.getCustomerId());
        final var productResponse = productService.find(order.getProductId());
        order.setCustomerId(customerResponse.getId());
        order.setProductId(productResponse.getId());
        order.setCreateDate(OffsetDateTime.now());
        order.setStatus(OrderStatus.NEW);
        return orderRepository.save(order);
    }

}
