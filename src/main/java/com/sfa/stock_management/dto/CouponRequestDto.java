package com.sfa.stock_management.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sfa.stock_management.model.Coupon;

public class CouponRequestDto {

	private Long id;

	private String couponCode;

	private Integer discountPercentage;

	private Double minOrderValue;

	private Double maxDiscountAmount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expiryDate;

	private String customerIds;

	private Boolean isActive;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	private Long createdBy;

	private String createdUserName;

	private Long companyId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Integer discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getMinOrderValue() {
		return minOrderValue;
	}

	public void setMinOrderValue(Double minOrderValue) {
		this.minOrderValue = minOrderValue;
	}

	public Double getMaxDiscountAmount() {
		return maxDiscountAmount;
	}

	public void setMaxDiscountAmount(Double maxDiscountAmount) {
		this.maxDiscountAmount = maxDiscountAmount;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(String customerIds) {
		this.customerIds = customerIds;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Coupon convertToEntity() {
		
		return new Coupon(this.couponCode,this.discountPercentage,this.minOrderValue,this.maxDiscountAmount,this.expiryDate,this.isActive,this.createdAt,this.createdBy,
				this.createdUserName,this.companyId);
	}

	
	
}
