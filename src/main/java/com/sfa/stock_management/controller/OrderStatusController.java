package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.service.OrderStatusService;

@RestController
@CrossOrigin
public class OrderStatusController {

	@Autowired
	private OrderStatusService orderStatusService;
	
	@GetMapping("/get/all/order/status")
	public ResponseEntity<?> getAllOrderStatus(){
		Response<?> response=orderStatusService.getAllOrderStatus();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
