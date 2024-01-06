package com.sfa.stock_management.dto;

import java.util.List;

public class OrderPageResponseList {

	private List<String> orderIdList;
	private List<String> clientNameList;
	private List<String> createdByNameList;

	public List<String> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<String> orderIdList) {
		this.orderIdList = orderIdList;
	}

	public List<String> getClientNameList() {
		return clientNameList;
	}

	public void setClientNameList(List<String> clientNameList) {
		this.clientNameList = clientNameList;
	}

	public List<String> getCreatedByNameList() {
		return createdByNameList;
	}

	public void setCreatedByNameList(List<String> createdByNameList) {
		this.createdByNameList = createdByNameList;
	}

}
