package com.sfa.stock_management.service;

import java.util.Date;

import javax.mail.MessagingException;

import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;

public interface PaymentTransactionService {

	Response<?> getAllClientAccountsDetails(Long userId, Long hrmsUserId, int pageNo, int pageSize, String clientName, Date fromDate, Date toDate);

	Response<?> getClientTransactionDetails(Long clientId, int pageNo, int pageSize, Date fromDate, Date toDate, String searchKey);

	Response<?> getAllOrderIdForClient(Long clientId);

	Response<?> createNewPayment(CreateNewTransactionDto createNewTransactionDto) throws MessagingException;

}
