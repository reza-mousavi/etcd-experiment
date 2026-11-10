package com.reza.learning.contract.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_")
public class OrderEntity implements Serializable{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID")
	private UUID id;

	@Version
	@Column(name = "VERSION")
	private Integer version;

	@Column(name = "CREATED_DATE")
	private OffsetDateTime createDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderStatus status;

	@Column(name = "CUSTOMER_ID")
	private UUID customerId;

	@NotNull
	private UUID productId;

	@NotNull
	private Integer quantity;

}