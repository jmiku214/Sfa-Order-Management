package com.sfa.stock_management;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WatsooStockManagemntApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(WatsooStockManagemntApplication.class, args);
	}
	
	@PostConstruct
	public void init(){
	    // Setting Spring Boot SetTimeZone
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
