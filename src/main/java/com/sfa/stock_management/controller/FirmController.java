package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.service.FirmService;
import com.sfa.stock_management.service.ValidationService;

@RestController
public class FirmController {

	@Autowired
	private FirmService firmService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/create/firm")
	public ResponseEntity<?> createFirm(@RequestBody Firm firm) {
		Response<?> validationResponse = validationService.checkForCreateFirmPayload(firm);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = firmService.saveFirm(firm);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

	@PostMapping("/update/firm")
	public ResponseEntity<?> updateFirm(@RequestBody Firm firm) {
		Response<?> validationResponse = validationService.checkForCreateFirmPayload(firm);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = firmService.updateFirm(firm);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

	@GetMapping("/get/all/firm")
	public ResponseEntity<?> getAllFirm(@RequestParam("companyId") Long companyId) {
		Response<?> response = firmService.getAllFirmByCompanyId(companyId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/firm/by/id")
	public ResponseEntity<?> getFirmById(@RequestParam("firmId") Long firmId) {
		Response<?> response = firmService.getFirmById(firmId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
}
