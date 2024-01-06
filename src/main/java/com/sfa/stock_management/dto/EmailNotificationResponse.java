package com.sfa.stock_management.dto;

import com.sfa.stock_management.model.OrderStatus;

public class EmailNotificationResponse {

	private OrderStatus status;
	private Boolean isActive;

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
