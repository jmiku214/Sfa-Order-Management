package com.sfa.stock_management.dto;

import java.util.Date;

import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.model.Product;

public class OrderDto {

	private Long id;

	private Long clientId;

	private String clientName;

	private Product product;

	private Double quantity;

	private Double totalPrice;

	private OrderStatus status;

	private Date createdAt;

	private Date updatedAt;

	private Long createdBy;

	private Long updateBy;

	private String orderId;

	private String deliveryDate;

	private String deliveryAddress;

	private String clientEmailId;

	private String createdUserName;

	private String updatedUserName;

	private Double totalCgstPrice;

	private Double totalSgstPrice;

	private Double totalIgstPrice;

	private String orderObjectData;

	private Boolean isPriceEdited;

	private Double mrp;

	private Boolean isSample;

	private Long unitId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getClientEmailId() {
		return clientEmailId;
	}

	public void setClientEmailId(String clientEmailId) {
		this.clientEmailId = clientEmailId;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public Double getTotalCgstPrice() {
		return totalCgstPrice;
	}

	public void setTotalCgstPrice(Double totalCgstPrice) {
		this.totalCgstPrice = totalCgstPrice;
	}

	public Double getTotalSgstPrice() {
		return totalSgstPrice;
	}

	public void setTotalSgstPrice(Double totalSgstPrice) {
		this.totalSgstPrice = totalSgstPrice;
	}

	public Double getTotalIgstPrice() {
		return totalIgstPrice;
	}

	public void setTotalIgstPrice(Double totalIgstPrice) {
		this.totalIgstPrice = totalIgstPrice;
	}

	public String getOrderObjectData() {
		return orderObjectData;
	}

	public void setOrderObjectData(String orderObjectData) {
		this.orderObjectData = orderObjectData;
	}

	public Boolean getIsPriceEdited() {
		return isPriceEdited;
	}

	public void setIsPriceEdited(Boolean isPriceEdited) {
		this.isPriceEdited = isPriceEdited;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

}
