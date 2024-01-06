package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.service.ProductCatagoryService;
import com.sfa.stock_management.service.ValidationService;

@RestController
public class ProductCatagoryController {

	@Autowired
	private ProductCatagoryService productCatagoryService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/create/product/catagory")
	public ResponseEntity<?> createProductCatagory(@RequestBody ProductCatagory productCatagory) {
		Response<?> validationResponse = validationService.checkForProductCatagoryAdd(productCatagory);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = productCatagoryService.saveProductCatagory(productCatagory);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

	@GetMapping("/get/all/product/category")
	public ResponseEntity<?> getAllCategory(@RequestParam("companyId") Long companyId,@RequestParam(value="firmId",required = false)Long firmId) {
		Response<?> response = productCatagoryService.getAllProductCategory(companyId,firmId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@PutMapping("/update/category")
	public ResponseEntity<?> updateProductCategory(@RequestBody ProductCatagory catagory){
		Response<?> validationResponse = validationService.checkForProductCatagoryAdd(catagory);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = productCatagoryService.updateProductCatagory(catagory);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}
}
