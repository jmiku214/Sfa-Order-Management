package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Firm;

public interface FirmService {

	Response<?> saveFirm(Firm firm);

	Response<?> updateFirm(Firm firm);

	Response<?> getAllFirmByCompanyId(Long companyId);

	Response<?> getFirmById(Long firmId);

}
