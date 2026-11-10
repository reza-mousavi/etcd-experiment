package com.reza.learning.contract.order.controller;

import com.reza.learning.contract.order.api.OrderApi;
import com.reza.learning.contract.order.entity.OrderEntity;
import com.reza.learning.contract.order.mapper.OrderMapper;
import com.reza.learning.contract.order.model.OrderRequest;
import com.reza.learning.contract.order.model.OrderResponse;
import com.reza.learning.contract.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController implements OrderApi {

	private final OrderMapper orderMapper;

	private final OrderService orderService;

	@Override
	public ResponseEntity<OrderResponse> save(@Valid @RequestBody OrderRequest orderRequest) {
		log.info("Persist order : {}", orderRequest);
		final OrderEntity orderEntity = orderMapper.toEntity(orderRequest);
		final OrderEntity entity = orderService.save(orderEntity);
		var dto = orderMapper.toDto(entity);
		return ResponseEntity.created(URI.create("/" + entity.getId())).body(dto);
	}

	public ResponseEntity<OrderResponse> get(@PathVariable(name = "id") String uuid) {
		log.info("Get order by id : {}", uuid);
		final OrderEntity entity = orderService.find(uuid);
		return ResponseEntity.ok(orderMapper.toDto(entity));
	}

}