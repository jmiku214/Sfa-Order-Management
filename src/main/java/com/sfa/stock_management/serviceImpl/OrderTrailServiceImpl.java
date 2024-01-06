package com.sfa.stock_management.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.OrderTrailDataResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.model.OrderTrail;
import com.sfa.stock_management.repository.OrderRepository;
import com.sfa.stock_management.repository.OrderTrailRepository;
import com.sfa.stock_management.service.OrderTrailService;

@Service
public class OrderTrailServiceImpl implements OrderTrailService {

	@Autowired
	private OrderTrailRepository orderTrailRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Response<?> getOrderDetails(String orderId) {
		List<OrderTrail> orderTrailData = orderTrailRepository.findAllByOrderId(orderId);
		OrderTrailDataResponseDto trailDataResponseDto = new OrderTrailDataResponseDto();
		List<Order> orderData = orderRepository.findAllByOrderId(orderId);
		trailDataResponseDto.setOrderId(orderId);
		for (Order ord : orderData) {
			trailDataResponseDto.setAddress(ord.getDeliveryAddress());
			trailDataResponseDto.setExpectedArrivaldate(ord.getDeliveryDate());
		}
		if (orderTrailData != null) {
			trailDataResponseDto.setOrderTrail(orderTrailData);
		} else {
			trailDataResponseDto.setOrderTrail(null);
		}
		return new Response<>(HttpStatus.OK.value(), "OK", trailDataResponseDto);

	}

}
