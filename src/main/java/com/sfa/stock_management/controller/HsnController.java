package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.HsnGstMapping;
import com.sfa.stock_management.service.HsnService;

@RestController
@CrossOrigin
public class HsnController {

	@Autowired
	private HsnService hsnService;
	
	@GetMapping("/get/all/hsn")
	public ResponseEntity<?> getAllHsn(){
		Response<?> response=hsnService.getAllHsn();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/create/hsn/wise/gst")
	public ResponseEntity<?> createHsnWiseGst(@RequestBody HsnGstMapping hsnGstMapping){
		Response<?> response=hsnService.saveHsnWiseGst(hsnGstMapping);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@PutMapping("/update/hst/wise/gst")
	public ResponseEntity<?> updateHsnWiseGst(@RequestBody HsnGstMapping hsnGstMapping){
		Response<?> response=hsnService.updateHsnWiseGst(hsnGstMapping);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}
}
