package com.sfa.stock_management.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.dto.ProductResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.UnitResponseDto;

public interface ProductService {

	Response<?> saveProduct(ProductResponseDto product) throws JsonProcessingException;

	Response<?> getAllProducts(Long companyId,Long categoryId,Long firmId) throws JsonMappingException, JsonProcessingException;

	Response<?> getAllTransactions(String sku);

	Response<?> updateProduct(ProductResponseDto product);

	Response<?> getProductBySku(String sku, int pageNo, int pageSize, String searchKey, String activationKey, Long firmId, Long companyId)
			throws JsonMappingException, JsonProcessingException;

	Response<?> getColumnDetailsForProduct(Long productId);

	Response<?> addListOfProductByExcel(MultipartFile file, Long createdby, String createdUserName, Long companyId);

	Response<?> getAllUnit(Long companyId);

	Response<?> addListOfProductByExcelV2(MultipartFile file, Long createdBy, String createdUserName, Long companyId,Long firm,Long category);

	Response<?> saveListOfUnits(UnitResponseDto unitResponseDto);

	Response<?> getAllProductsByIds(List<Long> ids);

}
