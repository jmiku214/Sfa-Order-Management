package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.service.OrderTrailService;

@RestController
@CrossOrigin
public class OrderTrailController {

	@Autowired
	private OrderTrailService orderTrailService;
	
	@GetMapping("/get/order/details/byOrderId")
	public ResponseEntity<?> getOrderDetailsByOrderId(@RequestParam("orderId")String orderId){
		Response<?> response=orderTrailService.getOrderDetails(orderId);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
}
