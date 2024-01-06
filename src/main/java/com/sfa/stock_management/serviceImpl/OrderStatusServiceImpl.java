package com.sfa.stock_management.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.repository.OrderStatusRepository;
import com.sfa.stock_management.service.OrderStatusService;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

	@Autowired
	private OrderStatusRepository statusRepository;

	@Override
	public Response<?> getAllOrderStatus() {
		List<OrderStatus> orderStatus = statusRepository.findAll();
		return new Response<>(HttpStatus.OK.value(), "OK", orderStatus);
	}

}
