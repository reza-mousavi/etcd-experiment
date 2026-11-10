package com.reza.learning.contract.product.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.reza.learning.contract.product.repository")
@EntityScan("com.reza.learning.contract.product.entity")
public class JpaConfig {

}