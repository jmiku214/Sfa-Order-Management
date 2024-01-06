package com.sfa.stock_management.config;

import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

	private String location;

	public FileUploadProperties(String location) throws IOException {
		super();
		System.out.println(location);
		this.location = location;
	}

	public FileUploadProperties() throws IOException {
		super();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}