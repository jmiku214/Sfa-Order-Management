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
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.service.UnitConversionService;
import com.sfa.stock_management.service.ValidationService;

@RestController
public class UnitConversionController {

	@Autowired
	private UnitConversionService unitConversionService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/save/subunit")
	public ResponseEntity<?> saveSubUnit(@RequestBody UnitConversionMapping unitConversionMapping) {
		Response<?> validationResponse = validationService.checkForUnitConversionAddData(unitConversionMapping);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = unitConversionService.saveSubUnit(unitConversionMapping);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

	@GetMapping("/get/all/subunit")
	public ResponseEntity<?> getAllSubunitByUnitId(@RequestParam("unitId") Long unitId) {
		Response<?> response = unitConversionService.getAllSubunitByUnitId(unitId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/subunit/by/id")
	public ResponseEntity<?> getSubunitById(@RequestParam("subunitId") Long subunitId) {
		Response<?> response = unitConversionService.getAllSubunitById(subunitId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@PostMapping("/update/subunit")
	public ResponseEntity<?> updateSubUnit(@RequestParam("subUnitId") Long subUnitId,
			@RequestBody UnitConversionMapping unitConversionMapping) {
		Response<?> response = unitConversionService.updateSubunitData(subUnitId, unitConversionMapping);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/update/subunit/status")
	public ResponseEntity<?> updateSubUnitStatus(@RequestParam("subUnitId") Long subUnitId) {
		Response<?> response = unitConversionService.updateSubUnitStatus(subUnitId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/all/units/by/productId")
	public ResponseEntity<?> getAllUnitsByProductId(@RequestParam("productId") Long productId) {
		Response<?> response = unitConversionService.getAllUnitByProductId(productId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
}
