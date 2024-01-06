package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.UnitConversionMapping;

public interface UnitConversionService {

	Response<?> saveSubUnit(UnitConversionMapping unitConversionMapping);

	Response<?> getAllSubunitByUnitId(Long unitId);

	Response<?> getAllSubunitById(Long subunitId);

	Response<?> updateSubunitData(Long subUnitId, UnitConversionMapping unitConversionMapping);

	Response<?> updateSubUnitStatus(Long subUnitId);

	Response<?> getAllUnitByProductId(Long productId);

}
