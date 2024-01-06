package com.sfa.stock_management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "configuration_key")
	private String configurationKey;

	@Column(name = "configuration_value")
	private String configurationValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConfigurationKey() {
		return configurationKey;
	}

	public void setConfigurationKey(String configurationKey) {
		this.configurationKey = configurationKey;
	}

	public String getConfigurationValue() {
		return configurationValue;
	}

	public void setConfigurationValue(String configurationValue) {
		this.configurationValue = configurationValue;
	}

	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Configuration(Long id, String configurationKey, String configurationValue) {
		super();
		this.id = id;
		this.configurationKey = configurationKey;
		this.configurationValue = configurationValue;
	}

}
