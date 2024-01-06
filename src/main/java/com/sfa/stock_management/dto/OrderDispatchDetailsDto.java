package com.sfa.stock_management.dto;

import java.util.Date;

import com.sfa.stock_management.model.OrderProductMapping;
import com.sfa.stock_management.model.Product;

public class OrderDispatchDetailsDto {

	private Long id;

	private Product product;

	private String orderId;

	private Double quantity;

	private Date createdAt;

	private Boolean isSample;

	private OrderProductMapping orderProductMapping;

	private Long dispatchAttemptNo;

	private UnitResponseDto unit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public OrderProductMapping getOrderProductMapping() {
		return orderProductMapping;
	}

	public void setOrderProductMapping(OrderProductMapping orderProductMapping) {
		this.orderProductMapping = orderProductMapping;
	}

	public Long getDispatchAttemptNo() {
		return dispatchAttemptNo;
	}

	public void setDispatchAttemptNo(Long dispatchAttemptNo) {
		this.dispatchAttemptNo = dispatchAttemptNo;
	}

	public UnitResponseDto getUnit() {
		return unit;
	}

	public void setUnit(UnitResponseDto unit) {
		this.unit = unit;
	}

	public OrderDispatchDetailsDto(Long id, Product product, String orderId, Double quantity, Date createdAt,
			Boolean isSample, OrderProductMapping orderProductMapping, Long dispatchAttemptNo) {
		super();
		this.id = id;
		this.product = product;
		this.orderId = orderId;
		this.quantity = quantity;
		this.createdAt = createdAt;
		this.isSample = isSample;
		this.orderProductMapping = orderProductMapping;
		this.dispatchAttemptNo = dispatchAttemptNo;
	}

}
