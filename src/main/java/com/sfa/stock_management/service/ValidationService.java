package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.CouponRequestDto;
import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.ProductResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.model.Coupon;
import com.sfa.stock_management.model.EmailNotificationConfiguration;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.UnitConversionMapping;

public interface ValidationService {

	Response<?> checkValidation(ProductResponseDto product);

	Response<?> checkForUpdateStock(StockUpdateDto stockUpdateDto, String sku);

	Response<?> checkForNotificationDeletePayload(EmailNotificationConfiguration emailNotificationConfiguration);

	Response<?> checkForDispatchStatusUpdate(OrderSaveRequestDto orderSaveRequestDto);

	Response<?> checkForNewTransactionCreate(CreateNewTransactionDto createNewTransactionDto);

	Response<?> checkForCouponCodeCreate(CouponRequestDto coupon);

	Response<?> checkForProductCatagoryAdd(ProductCatagory productCatagory);

	Response<?> checkForCreateFirmPayload(Firm firm);

	Response<?> checkForUnitConversionAddData(UnitConversionMapping unitConversionMapping);

}
