package com.sfa.stock_management.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CreateNewTransactionDto {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate;
	private Long transactionTypeId;
	private String orderId;
	private Double amount;
	private String remark;
	private Long clientId;
	private String tansactionId;
	

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getTansactionId() {
		return tansactionId;
	}

	public void setTansactionId(String tansactionId) {
		this.tansactionId = tansactionId;
	}

	
}
