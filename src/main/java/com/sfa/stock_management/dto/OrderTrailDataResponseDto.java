package com.sfa.stock_management.dto;

import java.util.List;

import com.sfa.stock_management.model.OrderTrail;

public class OrderTrailDataResponseDto {

	private String orderId;
	private String expectedArrivaldate;
	private String address;
	private List<OrderTrail> orderTrail;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	

	public String getExpectedArrivaldate() {
		return expectedArrivaldate;
	}

	public void setExpectedArrivaldate(String expectedArrivaldate) {
		this.expectedArrivaldate = expectedArrivaldate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderTrail> getOrderTrail() {
		return orderTrail;
	}

	public void setOrderTrail(List<OrderTrail> orderTrail) {
		this.orderTrail = orderTrail;
	}

}
