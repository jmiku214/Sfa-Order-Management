package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ClientProductPriceConfig;
import com.sfa.stock_management.service.ClientProductPriceConfigService;

@RestController
@CrossOrigin
public class ClientProductPriceConfigController {

	@Autowired
	private ClientProductPriceConfigService productPriceConfigService;

	@PostMapping("/save/clientwise/product/price")
	public ResponseEntity<?> saveClientProductPrice(@RequestBody ClientProductPriceConfig clientProductPriceConfig) {
		Response<?> response = productPriceConfigService.saveClientProductPrice(clientProductPriceConfig);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/clientwise/product/price")
	public ResponseEntity<?> getClientProductPrice(@RequestParam("productId") Long productId,
			@RequestParam("clientId") Long clientId) {
		Double price = productPriceConfigService.getClientWiseProductPrice(productId, clientId);
		return new ResponseEntity<>(price, HttpStatus.OK);
	}

	@PutMapping("/update/clientwise/product/price")
	public ResponseEntity<?> updateClientProductPrice(@RequestBody ClientProductPriceConfig clientProductPriceConfig) {
		Response<?> response = productPriceConfigService.updateClientWiseProductPrice(clientProductPriceConfig);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/client/all/product/price")
	public ResponseEntity<?> getAllClientProductsPrice(@RequestParam("clientId") Long clientId) {
		Response<?> clientProductPriceList = productPriceConfigService.getAllClientProductsPrice(clientId);
		return new ResponseEntity<>(clientProductPriceList, HttpStatus.OK);
	}

	@DeleteMapping("/delete/client/wise/product/price")
	public ResponseEntity<?> deleteClientWiseProductPrice(@RequestBody ClientProductPriceConfig clientProductPriceConfig){
		Response<?> response=productPriceConfigService.deleteClientWiseProductPrice(clientProductPriceConfig);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}

//	@GetMapping("/get/clientwise/product/priceV2")
//	public ResponseEntity<?> getClientProductPriceV2(@RequestParam("productId") Long productId,
//			@RequestParam("clientId") Long clientId,@RequestParam("clientStateId")Long clientStateId,@RequestParam("userStateId")Long userStateId,
//			@RequestParam("totalQuantity")Integer totalQuantity,@RequestParam("isSample")Boolean isSample) {
//		Response<?> response = productPriceConfigService.getClientWiseProductPriceV2(productId, clientId,clientStateId,userStateId,totalQuantity,isSample);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	@GetMapping("/get/clientwise/product/priceV2")
	public ResponseEntity<?> getClientProductPriceV2(@RequestParam("productId") Long productId,
			@RequestParam("clientId") Long clientId,@RequestParam("clientStateId")Long clientStateId,@RequestParam("userStateId")Long userStateId,
			@RequestParam("totalQuantity")Integer totalQuantity,@RequestParam("isSample")Boolean isSample,@RequestParam(value ="unitId",required = false)Long unitId) {
		Response<?> response = productPriceConfigService.getClientWiseProductPriceV3(productId, clientId,clientStateId,userStateId,totalQuantity,isSample,unitId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
