package com.sfa.stock_management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.model.OrderDeliveryDetails;

public interface OrderService {

	Response<?> saveOrder(OrderSaveRequestDto orderRequestDto) throws JsonMappingException, JsonProcessingException;

	Response<?> getAllOrdersByUserId(Long userId);

	Response<?> getOrderByOrderId(String orderId);

	Response<?> updateOrderStatus(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException;

	Response<?> getOrderById(String orderId) throws JsonMappingException, JsonProcessingException;

	Response<?> saveDeliveryDetails(OrderDeliveryDetails orderDeliveryDetails);

	Response<?> getDispatchDetails(String orderId);

	Response<?> getAllOrdersByUserIdV3(Long hrmsUserId, Long userId, SearchDto searchDto, int pageNo, int pageSize)
			throws JsonMappingException, JsonProcessingException;

	Response<?> getOrderPagedataList(Long hrmsUserId, Long userId);

	Response<?> getAllOrdersByUserIdV2(Long hrmsUserId, Long userId);

	Response<?> getAllState();

	Response<?> checkForBlockStockNoProduct(Long productId, Long quantity);

	Response<?> getClientWiseProductPriceAfterPriceChange(Long productId, Long clientId, Long clientStateId,
			Long userStateId, Double totalQuantity, Double totalPrice, Double originalPrice, Long unitId);

	Response<?> getAllOrderByClientId(String clienName);

	Response<?> saveOrderV2(OrderSaveRequestDto orderRequestDto) throws JsonMappingException, JsonProcessingException;

	Response<?> getAllLocalityByCompanyId(Long companyId);

	Response<?> updateOrderStatusV2(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException;

	Response<?> updateOrderStatusV3(OrderSaveRequestDto orderSaveRequestDto) throws JsonMappingException, JsonProcessingException;

}
