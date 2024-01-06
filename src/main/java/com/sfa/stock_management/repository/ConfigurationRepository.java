package com.sfa.stock_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

	Configuration findByConfigurationKey(String string);

}
