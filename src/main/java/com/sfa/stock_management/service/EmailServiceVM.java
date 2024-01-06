package com.sfa.stock_management.service;

import java.util.List;

import javax.mail.MessagingException;

import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.OrderDto;
import com.sfa.stock_management.dto.OrderSaveRequestDto;

public interface EmailServiceVM {

	void sendEmail();

	void sendOrderCreatedEmail(OrderSaveRequestDto orderSaveRequestDto);

	void sendOrderUpdateStatusMail(String name, String orderId, String email);

	void sendPaymentReceivedMail(CreateNewTransactionDto createNewTransactionDto) throws MessagingException;

	void sendDispatchProductsMail(String name, String orderId, String clientEmailId, List<OrderDto> orderDispatchList);
}
