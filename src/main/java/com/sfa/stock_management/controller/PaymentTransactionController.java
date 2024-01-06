package com.sfa.stock_management.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.constant.PaymentType;
import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.PaymentTypeResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.service.PaymentTransactionService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@CrossOrigin
public class PaymentTransactionController {

	@Autowired
	private PaymentTransactionService paymentTransactionService;

	@Autowired
	private ValidationService validationService;

	@GetMapping("/get/all/payment/type")
	public ResponseEntity<?> getAllPaymentStatus() {
		PaymentType[] allPaymentType = PaymentType.values();
		List<PaymentTypeResponseDto> paymentResponse = new ArrayList<>();
		for (PaymentType type : allPaymentType) {
			PaymentTypeResponseDto dto = new PaymentTypeResponseDto();
			dto.setId(type.getId());
			dto.setStatusName(type.getStatusName());
			paymentResponse.add(dto);
		}
		return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
	}

	@GetMapping("/get/all/client/account/details")
	public ResponseEntity<?> getAllClientAccountsDetails(@RequestParam("userId") Long userId,
			@RequestParam("hrmsUserId") Long hrmsUserId, @RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false, defaultValue = "") String clientName,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
		Response<?> response = paymentTransactionService.getAllClientAccountsDetails(userId, hrmsUserId, pageNo,
				pageSize, clientName, fromDate, toDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/client/payment/details/byId")
	public ResponseEntity<?> getClientPaymentDetailsById(@RequestParam("clientId") Long clientId,
			@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate,
			@RequestParam(required = false,defaultValue = "")String searchKey) {
		Response<?> response = paymentTransactionService.getClientTransactionDetails(clientId, pageNo, pageSize,fromDate,toDate,searchKey);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/client/orderIds")
	public ResponseEntity<?> getAllOrderIdByClientId(@RequestParam("clientId") Long clientId) {
		Response<?> response = paymentTransactionService.getAllOrderIdForClient(clientId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create/new/transaction")
	public ResponseEntity<?> createNewTransaction(@RequestBody CreateNewTransactionDto createNewTransactionDto)
			throws MessagingException {
		Response<?> validationResponse = validationService.checkForNewTransactionCreate(createNewTransactionDto);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = paymentTransactionService.createNewPayment(createNewTransactionDto);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

}
