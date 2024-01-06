package com.sfa.stock_management.dto;

import java.util.List;

import com.sfa.stock_management.model.OrderStatus;

public class OrderSaveRequestDto {

	private Long clientId;
	private String clientName;
	private Long companyId;
	private List<OrderDto> order;
	private Long createdByUserId;
	private String createdByUserName;
	private String orderId;
	private OrderStatus orderStatus;
	private String clientAddress;
	private String clientEmailAddress;
	private String images;
	private String remarks;
	private Long clientStateId;
	private Long userStateId;
	private Double totalPriceAfterAddedGst;
	private Double couponPrice;
	private String[] ccMails;
	private String orderRemarks;
	private String couponCode;
	private Long firmId;
	private Long categoryId;
	private Long localityId;
	private String localityName;
	private List<OrderDto> orderDispatchList;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public List<OrderDto> getOrder() {
		return order;
	}

	public void setOrder(List<OrderDto> order) {
		this.order = order;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByUserName() {
		return createdByUserName;
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getClientEmailAddress() {
		return clientEmailAddress;
	}

	public void setClientEmailAddress(String clientEmailAddress) {
		this.clientEmailAddress = clientEmailAddress;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getClientStateId() {
		return clientStateId;
	}

	public void setClientStateId(Long clientStateId) {
		this.clientStateId = clientStateId;
	}

	public Long getUserStateId() {
		return userStateId;
	}

	public void setUserStateId(Long userStateId) {
		this.userStateId = userStateId;
	}

	public Double getTotalPriceAfterAddedGst() {
		return totalPriceAfterAddedGst;
	}

	public void setTotalPriceAfterAddedGst(Double totalPriceAfterAddedGst) {
		this.totalPriceAfterAddedGst = totalPriceAfterAddedGst;
	}

	public Double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public String[] getCcMails() {
		return ccMails;
	}

	public void setCcMails(String[] ccMails) {
		this.ccMails = ccMails;
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Long getFirmId() {
		return firmId;
	}

	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public List<OrderDto> getOrderDispatchList() {
		return orderDispatchList;
	}

	public void setOrderDispatchList(List<OrderDto> orderDispatchList) {
		this.orderDispatchList = orderDispatchList;
	}

}
