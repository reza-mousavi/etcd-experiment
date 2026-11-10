package com.reza.learning.contract.order.mapper;

import com.reza.learning.contract.order.model.OrderRequest;
import com.reza.learning.contract.order.model.OrderResponse;
import com.reza.learning.contract.order.entity.OrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author rza.mousavi@gmail.com
 */

@Mapper(componentModel = "spring")
public interface OrderMapper {

	OrderEntity toEntity(OrderRequest dto);

	OrderEntity toEntity(OrderResponse dto);

	OrderResponse toDto(OrderEntity entity);

	List<OrderResponse> toDto(Iterable<OrderEntity> entityList);

}