package com.sfa.stock_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.constant.ProductUnit;
import com.sfa.stock_management.dto.ProductResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.dto.UnitResponseDto;
import com.sfa.stock_management.service.ProductService;
import com.sfa.stock_management.service.ValidationService;

@RestController
@RequestMapping("api")
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/save/product")
	public ResponseEntity<?> saveProduct(@RequestBody ProductResponseDto product) throws JsonProcessingException {
		Response<?> validationResponse = validationService.checkValidation(product);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = productService.saveProduct(product);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}

	}

	@GetMapping("/getAll/units")
	public ResponseEntity<?> getAllUnits() {
		ProductUnit[] unitAll = ProductUnit.values();
		List<UnitResponseDto> unitResponseDtos = new ArrayList<>();
		for (ProductUnit unit : unitAll) {
			UnitResponseDto dto = new UnitResponseDto();
			dto.setId(unit.getId());
			dto.setUnitName(unit.getName());
			unitResponseDtos.add(dto);
		}
		return new ResponseEntity<>(unitResponseDtos, HttpStatus.OK);
	}

	@GetMapping("/get/all/products")
	public ResponseEntity<?> getAllProducts(@RequestParam("companyId") Long companyId,
			@RequestParam(required = false) Long categoryId, @RequestParam(required = false) Long firmId)
			throws JsonMappingException, JsonProcessingException {
		Response<?> response = productService.getAllProducts(companyId, categoryId, firmId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/getAll/transactions")
	public ResponseEntity<?> getAllTransaction(@RequestParam("sku") String sku) {
		Response<?> response = productService.getAllTransactions(sku);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/update/product")
	public ResponseEntity<?> updateProduct(@RequestBody ProductResponseDto product) {
		Response<?> validationResponse = validationService.checkValidation(product);
		if (validationResponse.getResponseCode() == HttpStatus.OK.value()) {
			Response<?> response = productService.updateProduct(product);
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		} else {
			return new ResponseEntity<>(validationResponse, HttpStatus.valueOf(validationResponse.getResponseCode()));
		}
	}

	@GetMapping("/get/product/by/sku")
	public ResponseEntity<?> getProductBySku(@RequestParam("sku") String sku,
			@RequestParam(required = false, defaultValue = "0") int pageNo,
			@RequestParam(required = false, defaultValue = "0") int pageSize,
			@RequestParam(required = false) String searchKey, @RequestParam(required = false) String activationKey,
			@RequestParam(value = "firmId", defaultValue = "0", required = false) Long firmId,
			@RequestParam(value = "companyId", defaultValue = "0", required = false) Long companyId)
			throws JsonMappingException, JsonProcessingException {
		Response<?> response = productService.getProductBySku(sku, pageNo, pageSize, searchKey, activationKey,firmId,companyId);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
	}

	@GetMapping("/get/column/details/for/product")
	public ResponseEntity<?> getColumnDetailsforProduct(@RequestParam("productId") Long productId) {
		Response<?> response = productService.getColumnDetailsForProduct(productId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/add/product/byExcel")
	public ResponseEntity<?> addListOfProductByExcel(@ModelAttribute StockUpdateDto stockUpdateDto) {
		if (stockUpdateDto.getFile() == null && stockUpdateDto.getFile().isEmpty()) {
			return new ResponseEntity<>(new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a file.", null),
					HttpStatus.BAD_REQUEST);
		} else {
			Response<?> response = productService.addListOfProductByExcelV2(stockUpdateDto.getFile(),
					stockUpdateDto.getCreatedBy(), stockUpdateDto.getCreatedUserName(), stockUpdateDto.getCompanyId(),
					stockUpdateDto.getFirm(), stockUpdateDto.getCategory());
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
		}

	}
	
	@PostMapping("/get/all/products/by/Ids")
	public ResponseEntity<?> getAllProductListByIds(@RequestBody List<Long> Ids){
		Response<?> response=productService.getAllProductsByIds(Ids);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
