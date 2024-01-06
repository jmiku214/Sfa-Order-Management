package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.CouponRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Coupon;

public interface CouponService {

	Response<?> createCouponCode(CouponRequestDto coupon);

	Response<?> updateCouponCode(CouponRequestDto coupon);

	Response<?> changeCouponStatus(Coupon coupon);

	Response<?> getAllCouponsByCompanyId(Long companyId);

	Response<?> getCouponPrice(Double totalPrice, String couponCode);

	Response<?> getCouponByCouponCode(String couponCode);

	void setExpiredCouponAsDeactivate();

	Response<?> getCouponListUnderPrice(Double price);

}
