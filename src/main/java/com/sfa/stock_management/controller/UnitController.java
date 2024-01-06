package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.UnitResponseDto;
import com.sfa.stock_management.service.ProductService;

@RestController
@RequestMapping("api")
public class UnitController {

	@Autowired
	private ProductService productService;

	@GetMapping("/get/company/units")
	public ResponseEntity<?> getAllUnitsCompanyWise(@RequestParam("companyId") Long companyId) {

		Response<?> respList = productService.getAllUnit(companyId);
		return new ResponseEntity<>(respList, HttpStatus.OK);
	}

	@PostMapping("/create/unit/list")
	public ResponseEntity<?> createUnitList(@RequestBody UnitResponseDto unitResponseDto) {
		Response<?> response = productService.saveListOfUnits(unitResponseDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
}
