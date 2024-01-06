package com.sfa.stock_management.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.model.ProductCatagory;

public class SearchDto {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fromDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date toDate;
	private String createdUserName;
	private OrderStatus orderStatus;
	private String clientName;
	private String orderId;
	private Firm firmId;
	private ProductCatagory categoryId;
	private Long localityId;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Firm getFirmId() {
		return firmId;
	}

	public void setFirmId(Firm firmId) {
		this.firmId = firmId;
	}

	public ProductCatagory getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(ProductCatagory categoryId) {
		this.categoryId = categoryId;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

}
