package com.sfa.stock_management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;

public interface TransactionService {

	Response<?> getAllTransactionByProductId(String sku, int pageNo, int pageSize, Long firmId, Long companyId) throws JsonMappingException, JsonProcessingException;
	
	Response<?> updateStock(String sku, StockUpdateDto stockUpdateDto, Long firmId);

	Double getOutStandingPriceForClient(Long clientId, String orderId);
}
