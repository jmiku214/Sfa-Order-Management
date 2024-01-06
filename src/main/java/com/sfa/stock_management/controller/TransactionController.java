package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.service.TokenTypeService;
import com.sfa.stock_management.service.TransactionService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@RequestMapping("api")
@CrossOrigin
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private TokenTypeService tokenTypeService;

	@GetMapping("/getAll/transaction/by/productId")
	public ResponseEntity<?> getAllTransactionByProductId(
			@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "0") int pageSize, @RequestParam("sku") String sku,
			@RequestParam(value = "firmId", defaultValue = "0", required = false) Long firmId,
			@RequestParam(value = "companyId", defaultValue = "0", required = false) Long companyId)
			throws JsonMappingException, JsonProcessingException {
		Response<?> response = transactionService.getAllTransactionByProductId(sku, pageNo, pageSize, firmId,companyId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update/stock")
	public ResponseEntity<?> updateStock(@RequestParam("sku") String sku, @ModelAttribute StockUpdateDto stockUpdateDto,
			@RequestParam(value = "firmId", defaultValue = "0", required = false) Long firmId) {
		Response<?> validation = validationService.checkForUpdateStock(stockUpdateDto, sku);
		if (validation.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = transactionService.updateStock(sku, stockUpdateDto,firmId);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validation, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/get/token/type")
	public ResponseEntity<?> getTokenType(@RequestParam("companyId") Long companyId) {
		Response<?> response = tokenTypeService.getAllTokenType(companyId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/outstanding/amount")
	public ResponseEntity<?> getOutstandingAmountForClient(@RequestParam("clientId") Long clientId,
			@RequestParam("orderId") String orderId) {
		Double price = transactionService.getOutStandingPriceForClient(clientId, orderId);
		return new ResponseEntity<>(price, HttpStatus.OK);
	}
}
