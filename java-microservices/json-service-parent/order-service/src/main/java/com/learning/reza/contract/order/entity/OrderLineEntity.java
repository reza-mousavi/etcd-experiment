package com.reza.learning.contract.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author rza.mousavi@gmail.com
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDER_LINE")
public class OrderLineEntity implements Serializable{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID")
	private UUID id;

	@Version
	@Column(name = "VERSION")
	private Integer version;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "PRODUCT_ID")
	private UUID productId;

}

