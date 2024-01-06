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

import com.sfa.stock_management.dto.CouponRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Coupon;
import com.sfa.stock_management.service.CouponService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@CrossOrigin
public class CouponController {

	@Autowired
	private CouponService couponService;
	
	@Autowired
	private ValidationService validationService;
	
	@PostMapping("/create/coupon/code")
	public ResponseEntity<?> createCouponCode(@RequestBody CouponRequestDto coupon){
		Response<?> validationResponse=validationService.checkForCouponCodeCreate(coupon);
		if(validationResponse.getResponseCode()==HttpStatus.OK.value()) {
			Response<?> response=couponService.createCouponCode(coupon);
			return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
		}
		else {
			return new ResponseEntity<>(validationResponse,HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
		
	}
	
	@PutMapping("/update/coupon/code")
	public ResponseEntity<?> updateCouponCode(@RequestBody CouponRequestDto coupon){
		Response<?> validationResponse=validationService.checkForCouponCodeCreate(coupon);
		if(validationResponse.getResponseCode()==HttpStatus.OK.value()) {
			Response<?> response=couponService.updateCouponCode(coupon);
			return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
		}
		else {
			return new ResponseEntity<>(validationResponse,HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}
	
	@PostMapping("/change/coupon/status")
	public ResponseEntity<?> changeCouponStatus(@RequestBody Coupon coupon){
		Response<?> response=couponService.changeCouponStatus(coupon);
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@GetMapping("/get/all/coupon/by/company/id")
	public ResponseEntity<?> getAllCoupon(@RequestParam("companyId")Long companyId){
		Response<?> response=couponService.getAllCouponsByCompanyId(companyId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/get/coupon/price")
	public ResponseEntity<?> getCouponPrice(@RequestParam("totalPrice")Double totalPrice,@RequestParam("couponCode")String couponCode){
	     Response<?> response=couponService.getCouponPrice(totalPrice,couponCode);
	     return new ResponseEntity<>(response,HttpStatus.valueOf(response.getResponseCode()));
	}
	
	@GetMapping("/get/coupon/by/coupon/code")
	public ResponseEntity<?> getCouponByCouponCode(@RequestParam("couponCode")String couponCode){
		Response<?> response=couponService.getCouponByCouponCode(couponCode);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/get/coupon/list/under/price")
	public ResponseEntity<?> getCouponListUnderPrice(@RequestParam("price")Double price){
		Response<?> response=couponService.getCouponListUnderPrice(price);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
