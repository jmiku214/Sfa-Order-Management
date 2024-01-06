package com.sfa.stock_management.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.CouponRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Coupon;
import com.sfa.stock_management.repository.CouponRepository;
import com.sfa.stock_management.service.CouponService;

import ch.qos.logback.core.encoder.ByteArrayUtil;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Override
	public Response<?> createCouponCode(CouponRequestDto coupon) {
		Optional<Coupon> couponObj = couponRepository.getCouponDataByCouponCode(coupon.getCouponCode());
		if (couponObj != null && couponObj.isPresent()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Coupon code already exist.", null);
		} else {
			if (coupon.getCreatedAt() == null) {
				coupon.setCreatedAt(new Date());
			}
			if(coupon.getIsActive()==null) {
				coupon.setIsActive(true);
			}
			
			Coupon couponDataObj=coupon.convertToEntity();
			couponRepository.save(couponDataObj);
			return new Response<>(HttpStatus.CREATED.value(), "Coupon code created successfully.", null);
		}

	}

	@Override
	public Response<?> updateCouponCode(CouponRequestDto coupon) {
		Optional<Coupon> couponObj = couponRepository.findById(coupon.getId());
		if (couponObj == null ) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Coupon code not exist.", null);
		} else {
			couponObj.get().setCouponCode(coupon.getCouponCode());
			couponObj.get().setCreatedBy(coupon.getCreatedBy());
			couponObj.get().setCustomerIds(coupon.getCustomerIds());
			couponObj.get().setExpiryDate(coupon.getExpiryDate());
			couponObj.get().setDiscountPercentage(coupon.getDiscountPercentage());
			couponObj.get().setMaxDiscountAmount(coupon.getMaxDiscountAmount());
			couponObj.get().setMinOrderValue(coupon.getMinOrderValue());
			couponObj.get().setCreatedUserName(coupon.getCreatedUserName());
			couponObj.get().setCompanyId(coupon.getCompanyId());
			couponObj.get().setIsActive(coupon.getIsActive());
			couponRepository.save(couponObj.get());
			return new Response<>(HttpStatus.CREATED.value(), "Coupon code data updated successfully.", null);
		}

	}

	@Override
	public Response<?> changeCouponStatus(Coupon coupon) {

		Optional<Coupon> couponObj = couponRepository.findByCouponCodeForStatuschange(coupon.getCouponCode());
		if (couponObj != null && couponObj.isPresent()) {
			if(!couponObj.get().getExpiryDate().before(new Date())) {
				if (couponObj.get().getIsActive().booleanValue() == true) {
					couponObj.get().setIsActive(false);
				} else {
					couponObj.get().setIsActive(true);
				}
				couponRepository.save(couponObj.get());
				return new Response<>(HttpStatus.CREATED.value(), "Status updated successfully.", null);
			}
			else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please update the coupon expiry date.", null);
			}
			
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Coupon code not found.", null);
		}

	}

	@Override
	public Response<?> getAllCouponsByCompanyId(Long companyId) {
		List<Coupon> couponsList = couponRepository.findAllByCompanyId(companyId);
		return new Response<>(HttpStatus.OK.value(), "Coupon List.", couponsList);
	}

	@Override
	public Response<?> getCouponPrice(Double totalPrice, String couponCode) {
		Optional<Coupon> couponObj = couponRepository.findByCouponCode(couponCode);
		if (couponObj != null && couponObj.isPresent() && couponObj.get().getExpiryDate().after(new Date())) {
			if (totalPrice >= couponObj.get().getMinOrderValue()) {
				Double percentagePrice = totalPrice * couponObj.get().getDiscountPercentage() / 100;
				if (percentagePrice > couponObj.get().getMaxDiscountAmount()) {
					return new Response<>(HttpStatus.OK.value(), "Coupon applied successfully.",
							couponObj.get().getMaxDiscountAmount());
				} else {
					return new Response<>(HttpStatus.OK.value(), "Coupon applied successfully.", percentagePrice);
				}
			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(),
						"This coupon is applicable on min order value of " + couponObj.get().getMinOrderValue(), null);
			}
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Coupon code is expired/invalid.", null);
		}
	}

	@Override
	public Response<?> getCouponByCouponCode(String couponCode) {
		Optional<Coupon> couponObj=couponRepository.getCouponDataByCouponCode(couponCode);
		return new Response<>(HttpStatus.OK.value(),"Coupon data..",couponObj.get());
	}

	@Override
	public void setExpiredCouponAsDeactivate() {
		Date todayDate=new Date();
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=formatter.format(todayDate);
		List<Coupon> expiredCouponList=couponRepository.findAllExpiredCoupon(date);
		List<Coupon> updatedCouponList=new ArrayList<>();
		if(expiredCouponList!=null) {
			for(Coupon coupon:expiredCouponList) {
				if(coupon.getIsActive().booleanValue()==true) {
					coupon.setIsActive(false);
					updatedCouponList.add(coupon);
					System.out.println("OK");
				}
			}
		}
		if(updatedCouponList!=null && updatedCouponList.size()>0) {
			couponRepository.saveAll(updatedCouponList);
		}
		
	}

	@Override
	public Response<?> getCouponListUnderPrice(Double price) {
	
		List<Coupon> coupon=couponRepository.findAllCouponUnderPrice(price);
		List<String> couponsList=new ArrayList<>();
		if(coupon!=null) {
			couponsList=coupon.stream().map(e->e.getCouponCode()).collect(Collectors.toList());
		}
		return new Response<>(HttpStatus.OK.value(),"Coupon List..",couponsList);
	}

}
