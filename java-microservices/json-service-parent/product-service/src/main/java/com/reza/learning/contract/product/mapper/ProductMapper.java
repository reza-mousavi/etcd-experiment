package com.reza.learning.contract.product.mapper;

import com.reza.learning.contract.product.model.ProductRequest;
import com.reza.learning.contract.product.model.ProductResponse;
import com.reza.learning.contract.product.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author rza.mousavi@gmail.com
 */

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductEntity toEntity(ProductRequest dto);

	ProductEntity toEntity(ProductResponse dto);

	ProductResponse toDto(ProductEntity entity);

	List<ProductResponse> toDto(Iterable<ProductEntity> entityList);

}
