package com.sfa.stock_management.service;

import java.net.URISyntaxException;

import com.sfa.stock_management.dto.ClientDataGetRequestBody;
import com.sfa.stock_management.dto.Response;

public interface ClientService {

	Response<?> getAllClients(ClientDataGetRequestBody clientDataGetRequestBody) throws URISyntaxException;

	Response<?> getAllClientsV2(Long userId, Long hrmsUserId);

	Response<?> getCompanyConfiguration(Long companyId);

}
