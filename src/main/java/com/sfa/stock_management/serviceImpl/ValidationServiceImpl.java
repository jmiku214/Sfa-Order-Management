package com.sfa.stock_management.serviceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.CouponRequestDto;
import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.ProductResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.model.EmailNotificationConfiguration;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Override
	public Response<?> checkValidation(ProductResponseDto product) {
		if (product.getSku() == null || product.getSku().isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please Enter the SKU Code!!", null);
		} else if (product.getHsnCode() == null || product.getHsnCode().isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please Enter the HSN Code!!", null);
		} else if (product.getName() == null || product.getName().isEmpty() && product.getName() == "") {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please Enter the Product Name!!", null);
		} else if (product.getUnit() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please Enter the Unit", null);
		} else if (product.getMinReOrderQuantity() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter minimum reorder quantity value!!",
					null);
		} else if (product.getOpeningQuantity() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter opening quantity!!", null);
		} else if (product.getMinSellingPrice() != null
				&& product.getMinSellingPrice() > product.getStandardSellingPrice()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(),
					"Minimum selling price can't be greater then the standard selling price!!", null);
		} else if (product.getAllowOrderTillQuantity() != null
				&& product.getAllowOrderTillQuantity().toString() != "") {
			if (product.getAllowOrderTillQuantity() > product.getMinReOrderQuantity()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(),
						"Allow order till quantity can't be greater than the min reorder quantity!!", null);
			} else {
				return new Response<>(HttpStatus.OK.value(), "OK", null);
			}

		}

		else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}
	}

	@Override
	public Response<?> checkForUpdateStock(StockUpdateDto stockUpdateDto, String sku) {
		if (stockUpdateDto.getFile() == null
				&& (stockUpdateDto.getProcuredQuantity() != null || stockUpdateDto.getDisbursedQuantity() != null)) {
			if (stockUpdateDto.getDisbursedQuantity() == null && stockUpdateDto.getProcuredQuantity() == null) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(),
						"Please enter either procured/disbursed qauntity!!", null);
			} else if (stockUpdateDto.getDisbursedQuantity() != null && stockUpdateDto.getProcuredQuantity() != null) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(),
						"You can provide either procured or disbursed qty at a time.", null);
			} else if (sku == null) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter sku code!!", null);
			} else if (stockUpdateDto.getRemarks() == null || stockUpdateDto.getRemarks() == ""
					|| stockUpdateDto.getRemarks().isEmpty()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter remarks!!", null);
			} else {
				return new Response<>(HttpStatus.OK.value(), "OK", null);
			}
		} else {
			if (stockUpdateDto.getIsProcured() != null && stockUpdateDto.getIsProcured().booleanValue() == true) {
				if (stockUpdateDto.getIsDisbursed() == null && stockUpdateDto.getIsProcured() == null) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"Please enter either procured/disbursed qauntity!!", null);
				} else if (stockUpdateDto.getIsDisbursed() && stockUpdateDto.getIsProcured()) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"You can provide either procured or disbursed qty at a time.", null);
				} else if (sku == null) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter sku code!!", null);
				} else if (stockUpdateDto.getRemarks() == null && stockUpdateDto.getRemarks().isEmpty()) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please enter remarks!!", null);
				}

				else {
					return new Response<>(HttpStatus.OK.value(), "OK", null);
				}
			} else {
				return new Response<>(HttpStatus.OK.value(), "Ok", null);
			}

		}

	}

	@Override
	public Response<?> checkForNotificationDeletePayload(
			EmailNotificationConfiguration emailNotificationConfiguration) {
		if (emailNotificationConfiguration.getOrderStatus().getId() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please select the status..", null);
		} else if (emailNotificationConfiguration.getOrderStatus().getId() == 1) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "You are not able to delete the created status..",
					null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}

	}

	@Override
	public Response<?> checkForDispatchStatusUpdate(OrderSaveRequestDto orderSaveRequestDto) {
		if (orderSaveRequestDto.getOrderStatus().getId() == 5 || orderSaveRequestDto.getOrderStatus().getId() == 7) {
			if (orderSaveRequestDto.getRemarks() == null || orderSaveRequestDto.getRemarks() == ""
					|| orderSaveRequestDto.getRemarks().isEmpty()) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the courior details!!", null);
			} else {
				return new Response<>(HttpStatus.OK.value(), "OK", null);
			}
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}

	}

	@Override
	public Response<?> checkForNewTransactionCreate(CreateNewTransactionDto createNewTransactionDto) {
		if (createNewTransactionDto.getAmount() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the amount!!", null);
		} else if (createNewTransactionDto.getOrderId() == null || createNewTransactionDto.getOrderId().isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please select the orderID!!", null);
		} else if (createNewTransactionDto.getTransactionTypeId() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the transaction type!!", null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}
	}

	@Override
	public Response<?> checkForCouponCodeCreate(CouponRequestDto coupon) {
		if (coupon.getCouponCode() == null || coupon.getCouponCode() == "" || coupon.getCouponCode().isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the coupon code.", null);
		} else if (coupon.getDiscountPercentage() == null || coupon.getDiscountPercentage() == 0) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the discount percentage.", null);
		} else if (coupon.getMinOrderValue() == null || coupon.getMinOrderValue() == 0) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the min order value.", null);
		} else if (coupon.getMaxDiscountAmount() == null || coupon.getMaxDiscountAmount() == 0) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the maximum discount value.", null);
		} else if (coupon.getExpiryDate() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the coupon expiry date.", null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}

	}

	@Override
	public Response<?> checkForProductCatagoryAdd(ProductCatagory productCatagory) {
		if (productCatagory.getCategoryName() == null || productCatagory.getCategoryName().equals("")) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the category name.", null);
		} else if (productCatagory.getFirm() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please select the firm.", null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}

	}

	@Override
	public Response<?> checkForCreateFirmPayload(Firm firm) {
		if (firm.getFirmName() == "" || firm.getFirmName().equals("")) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the firm name", null);
		} else if (firm.getCompanyId() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the company name", null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}
	}

	@Override
	public Response<?> checkForUnitConversionAddData(UnitConversionMapping unitConversionMapping) {
		if (unitConversionMapping.getSubUnitName() == null || unitConversionMapping.getSubUnitName() == "") {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the sub unit name.", null);
		} else if (unitConversionMapping.getSubUnitQty() == null || unitConversionMapping.getSubUnitQty() < 0) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide the sub unit quantity.", null);
		} else if (unitConversionMapping.getUnit() == null) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please select the unit.", null);
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}
	}

}
