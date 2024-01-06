package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.EmailNotificationConfiguration;
import com.sfa.stock_management.service.EmailNotificationConfigurationService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@CrossOrigin
public class EmailNotificationController {

	@Autowired
	private EmailNotificationConfigurationService emailNotificationConfigurationService;
	
	@Autowired
	private ValidationService validationService;
	
	@PostMapping("/save/email/notification/for/client")
	public ResponseEntity<?> saveEmailNotificationForClient(@RequestBody EmailNotificationConfiguration emailNotificationConfiguration){
		Response<?> response=emailNotificationConfigurationService.saveEmailNotification(emailNotificationConfiguration);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@GetMapping("/get/all/email/config/for/client")
	public ResponseEntity<?> getAllEmailConfigDataForClient(@RequestParam("clientId")Long clientId){
		Response<?> response=emailNotificationConfigurationService.getAllEmailConfigDataForClient(clientId);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@DeleteMapping("/delete/notification")
	public ResponseEntity<?> deleteNotificationForClient(@RequestBody EmailNotificationConfiguration emailNotificationConfiguration){
		Response<?> validationResponse=validationService.checkForNotificationDeletePayload(emailNotificationConfiguration);
		if(validationResponse.getResponseCode()==HttpStatus.OK.value()) {
			Response<?> response=emailNotificationConfigurationService.deleteEmailNotification(emailNotificationConfiguration);
			return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
		}
		else {
			return new ResponseEntity<>(validationResponse,HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}
	
	@GetMapping("/get/all/email/notification/for/client")
	public ResponseEntity<?> getClientEmailData(@RequestParam("clientId")Long clientId){
		Response<?> response=emailNotificationConfigurationService.getClientEmailNotificationData(clientId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
