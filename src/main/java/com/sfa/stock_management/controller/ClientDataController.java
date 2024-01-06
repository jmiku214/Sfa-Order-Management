package com.sfa.stock_management.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.ClientDataGetRequestBody;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.service.ClientService;

@RestController
@CrossOrigin
public class ClientDataController {

	@Autowired
	private ClientService clientService;

	@PostMapping("/getAll/clients/byUserAndHrmsId")
	public ResponseEntity<?> getAllClients(@RequestBody ClientDataGetRequestBody clientDataGetRequestBody)
			throws URISyntaxException {
		Response<?> response = clientService.getAllClients(clientDataGetRequestBody);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAll/clients/byUserAndHrmsIdV2")
	public ResponseEntity<?> getAllClientsV2(@RequestParam("userId") Long userId,
			@RequestParam("hrmsUserId") Long hrmsUserId) {
		Response<?> response = clientService.getAllClientsV2(userId, hrmsUserId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/company/configuration")
	public ResponseEntity<?> getCompanyConfiguration(@RequestParam("companyId") Long companyId) {
		Response<?> response = clientService.getCompanyConfiguration(companyId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
