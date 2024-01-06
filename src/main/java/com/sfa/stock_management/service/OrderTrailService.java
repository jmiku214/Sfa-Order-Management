package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;

public interface OrderTrailService {

	Response<?> getOrderDetails(String orderId);

}
