package com.reza.learning.contract.customer.entity;

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
@Table(name = "CUSTOMER")
public class CustomerEntity implements Serializable{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ID")
	private UUID id;

	@Version
	@Column(name = "VERSION")
	private Integer version;

	@Column(name = "NAME")
	private String name;

	@Column(name = "FAMILY")
	private String family;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CELLPHONE")
	private String cellphone;

}

