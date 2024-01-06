package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.model.OrderDeliveryDetails;
import com.sfa.stock_management.service.OrderService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@CrossOrigin
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/create/order")
	public ResponseEntity<?> saveOrder(@RequestBody OrderSaveRequestDto orderRequestDto)
			throws JsonMappingException, JsonProcessingException {
		Response<?> response = orderService.saveOrderV2(orderRequestDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}



	@GetMapping("/getAll/ordersV2")
	public ResponseEntity<?> getAllOrdersV2(@RequestParam("hrmsUserId") Long hrmsUserId,
			@RequestParam("userId") Long userId) {
		Response<?> response = orderService.getAllOrdersByUserIdV2(hrmsUserId, userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}



	@PutMapping("/update/orderStatus")
	public ResponseEntity<?> updateOrderStatus(@RequestBody OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {
		Response<?> validationResponse = validationService.checkForDispatchStatusUpdate(orderSaveRequestDto);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
//			Response<?> response = orderService.updateOrderStatus(orderSaveRequestDto);
//			Response<?> response = orderService.updateOrderStatusV2(orderSaveRequestDto);
			Response<?> response = orderService.updateOrderStatusV3(orderSaveRequestDto);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}

	}

	@GetMapping("/get/order/by/orderID")
	public ResponseEntity<?> getOrderById(@RequestParam("orderId") String orderId)
			throws JsonMappingException, JsonProcessingException {
		Response<?> response = orderService.getOrderById(orderId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/provide/details/on/order/dispatch")
	public ResponseEntity<?> saveDispatchDetails(@RequestBody OrderDeliveryDetails orderDeliveryDetails) {
		Response<?> response = orderService.saveDeliveryDetails(orderDeliveryDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/dispatch/details")
	public ResponseEntity<?> getDispatchDetailsById(@RequestParam("orderId") String orderId) {
		Response<?> response = orderService.getDispatchDetails(orderId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@PostMapping("/getAll/ordersV3")
	public ResponseEntity<?> getAllOrdersV3(@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam("hrmsUserId") Long hrmsUserId, @RequestParam("userId") Long userId,
			@RequestBody SearchDto searchDto) throws JsonMappingException, JsonProcessingException {
		Response<?> response = orderService.getAllOrdersByUserIdV3(hrmsUserId, userId, searchDto, pageNo, pageSize);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/all/order/page/data/list")
	public ResponseEntity<?> getAllOrderPageListData(@RequestParam("hrmsUserId") Long hrmsUserId,
			@RequestParam("userId") Long userId) {
		Response<?> response = orderService.getOrderPagedataList(hrmsUserId, userId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/all/state")
	public ResponseEntity<?> getAllState() {
		Response<?> response = orderService.getAllState();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/check/for/block/stock/no/product")
	public ResponseEntity<?> checkForBlockStockNoProduct(@RequestParam("productId") Long productId,
			@RequestParam("quantity") Long quantity) {
		Response<?> response = orderService.checkForBlockStockNoProduct(productId, quantity);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/client/wise/product/price/after/edit")
	public ResponseEntity<?> getClientProductPriceAfterEdit(@RequestParam("productId") Long productId,
			@RequestParam("clientId") Long clientId, @RequestParam("clientStateId") Long clientStateId,
			@RequestParam("userStateId") Long userStateId, @RequestParam("totalQuantity") Double totalQuantity,
			@RequestParam("totalPrice") Double totalPrice, @RequestParam("originalPrice") Double originalPrice,@RequestParam(value = "unitId",required = false)Long unitId) {
		Response<?> response = orderService.getClientWiseProductPriceAfterPriceChange(productId, clientId,
				clientStateId, userStateId, totalQuantity, totalPrice, originalPrice,unitId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/order/list/by/clientName")
	public ResponseEntity<?> getOrderListByClientId(@RequestParam("clientName") String clienName) {
		Response<?> response = orderService.getAllOrderByClientId(clienName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/all/locality/by/company/id")
	public ResponseEntity<?> getAllLocalityByCompanyId(@RequestParam("companyId") Long companyId) {
		Response<?> response = orderService.getAllLocalityByCompanyId(companyId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
