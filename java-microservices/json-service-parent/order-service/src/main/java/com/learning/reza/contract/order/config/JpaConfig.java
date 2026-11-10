package com.reza.learning.contract.order.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.reza.learning.contract.order.repository")
@EntityScan("com.reza.learning.contract.order.entity")
public class JpaConfig {

}