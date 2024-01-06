package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ClientProductPriceConfig;

public interface ClientProductPriceConfigService {

	Response<?> saveClientProductPrice(ClientProductPriceConfig clientProductPriceConfig);

	Double getClientWiseProductPrice(Long productId, Long clientId);

	Response<?> updateClientWiseProductPrice(ClientProductPriceConfig clientProductPriceConfig);

	Response<?> getAllClientProductsPrice(Long clientId);

	Response<?> deleteClientWiseProductPrice(ClientProductPriceConfig clientProductPriceConfig);

	Response<?> getClientWiseProductPriceV2(Long productId, Long clientId, Long clientStateId, Long userStateId,
			Integer totalQuantity,Boolean isSample);

	Response<?> getClientWiseProductPriceV3(Long productId, Long clientId, Long clientStateId, Long userStateId,
			Integer totalQuantity, Boolean isSample, Long unitId);

}
