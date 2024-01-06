package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_balance_details")
public class ClientBalanceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "client_id")
	private Long clientId;
	
	@Column(name = "opening_balance")
	private Double openingBalance;
	
	@Column(name = "current_balance")
	private Double currentBalance;
	
	@Column(name = "last_transaction_date")
	private Date lastTransactionDate;
	
	@Column(name = "last_transaction_amount")
	private Double lastTransactionAmount;
	
	@Column(name = "last_transaction_remark")
	private String lastTransactionRemark;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "last_transaction_id")
	private String lastTransactionId;

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

	public String getLastTransactionRemark() {
		return lastTransactionRemark;
	}

	public void setLastTransactionRemark(String lastTransactionRemark) {
		this.lastTransactionRemark = lastTransactionRemark;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public ClientBalanceDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLastTransactionId() {
		return lastTransactionId;
	}

	public void setLastTransactionId(String lastTransactionId) {
		this.lastTransactionId = lastTransactionId;
	}

	public ClientBalanceDetails(Long id, Long clientId, Double openingBalance, Double currentBalance,
			Date lastTransactionDate, Double lastTransactionAmount) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.openingBalance = openingBalance;
		this.currentBalance = currentBalance;
		this.lastTransactionDate = lastTransactionDate;
		this.lastTransactionAmount = lastTransactionAmount;
	}

}
