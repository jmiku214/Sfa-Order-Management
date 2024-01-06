package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "coupon_code")
	private String couponCode;

	@Column(name = "discount_percentage")
	private Integer discountPercentage;

	@Column(name = "min_order_value")
	private Double minOrderValue;

	@Column(name = "max_discount_amount")
	private Double maxDiscountAmount;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "list_of_customers_id")
	private String customerIds;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_user_name")
	private String createdUserName;
	
	@Column(name = "company_id")
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

	public Coupon(Long id, String couponCode, Integer discountPercentage, Double minOrderValue,
			Double maxDiscountAmount, Date expiryDate, String customerIds, Boolean isActive) {
		super();
		this.id = id;
		this.couponCode = couponCode;
		this.discountPercentage = discountPercentage;
		this.minOrderValue = minOrderValue;
		this.maxDiscountAmount = maxDiscountAmount;
		this.expiryDate = expiryDate;
		this.customerIds = customerIds;
		this.isActive = isActive;
	}

	public Coupon(String couponCode, Integer discountPercentage, Double minOrderValue, Double maxDiscountAmount,
			Date expiryDate, String customerIds, Boolean isActive, Date createdAt, Long createdBy,
			String createdUserName, Long companyId) {
		this.couponCode = couponCode;
		this.discountPercentage = discountPercentage;
		this.minOrderValue = minOrderValue;
		this.maxDiscountAmount = maxDiscountAmount;
		this.expiryDate = expiryDate;
		this.customerIds = customerIds;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.companyId = companyId;
	}

	public Coupon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coupon(String couponCode, Integer discountPercentage, Double minOrderValue, Double maxDiscountAmount,
			Date expiryDate, Boolean isActive, Date createdAt, Long createdBy, String createdUserName, Long companyId) {
		super();
		this.couponCode = couponCode;
		this.discountPercentage = discountPercentage;
		this.minOrderValue = minOrderValue;
		this.maxDiscountAmount = maxDiscountAmount;
		this.expiryDate = expiryDate;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.companyId = companyId;
	}

	

}
