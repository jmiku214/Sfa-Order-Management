package com.sfa.stock_management.dto;

import java.util.Date;

public class ClientAccountDetailsResponse {

	private Long clientId;
	private Double openingBalance;
	private Double currentBalance;
	private Date lastTransactionDate;
	private Double lastTransactionAmount;
	private String clientName;
	private String lastTransactionRemark;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public Double getLastTransactionAmount() {
		return lastTransactionAmount;
	}

	public void setLastTransactionAmount(Double lastTransactionAmount) {
		this.lastTransactionAmount = lastTransactionAmount;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getLastTransactionRemark() {
		return lastTransactionRemark;
	}

	public void setLastTransactionRemark(String lastTransactionRemark) {
		this.lastTransactionRemark = lastTransactionRemark;
	}

}
