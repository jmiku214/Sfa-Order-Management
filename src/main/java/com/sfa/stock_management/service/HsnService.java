package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.HsnGstMapping;

public interface HsnService {

	Response<?> getAllHsn();

	Response<?> saveHsnWiseGst(HsnGstMapping hsnGstMapping);

	Response<?> updateHsnWiseGst(HsnGstMapping hsnGstMapping);

}
