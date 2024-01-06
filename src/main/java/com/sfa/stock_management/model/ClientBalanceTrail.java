package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_balance_trail")
public class ClientBalanceTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "transaction_details")
	private String transactionDetails;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "credit_amount")
	private Double creditAmount;

	@Column(name = "debit_amount")
	private Double debitAmount;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "transaction_id")
	private String transactionId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ClientBalanceTrail() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public ClientBalanceTrail(Long id, Date createdDate, String transactionDetails, Long clientId, Double creditAmount,
			Double debitAmount, String remark) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.transactionDetails = transactionDetails;
		this.clientId = clientId;
		this.creditAmount = creditAmount;
		this.debitAmount = debitAmount;
		this.remark = remark;
	}

}
