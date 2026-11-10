package com.reza.learning.contract.customer.mapper;

import com.reza.learning.contract.customer.model.CustomerRequest;
import com.reza.learning.contract.customer.model.CustomerResponse;
import com.reza.learning.contract.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author rza.mousavi@gmail.com
 */

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	CustomerEntity toEntity(CustomerRequest dto);

	CustomerEntity toEntity(CustomerResponse dto);

	CustomerResponse toDto(CustomerEntity entity);

	List<CustomerResponse> toDto(Iterable<CustomerEntity> entityList);

}