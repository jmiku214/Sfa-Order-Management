package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_user_name")
	private String createdUserName;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "procured_quantity")
	private Double procuredQuantity;

	@Column(name = "disbursed_quantity")
	private Double disbursedQuantity;

	@Column(name = "token_number")
	private String tokenNumber;

	@Column(name = "is_order")
	private Boolean isOrder;

	@Column(name = "columns_object_data")
	private String columnsObjectData;

	@Column(name = "columns_ordered_data")
	private String orderColumnData;

	@Column(name = "order_id")
	private String orderId;

	@OneToOne
	@JoinColumn(name = "token_type")
	private TokenType tokenType;

	@Column(name = "is_sample")
	private Boolean isSample;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getProcuredQuantity() {
		return procuredQuantity;
	}

	public void setProcuredQuantity(Double procuredQuantity) {
		this.procuredQuantity = procuredQuantity;
	}

	public Double getDisbursedQuantity() {
		return disbursedQuantity;
	}

	public void setDisbursedQuantity(Double disbursedQuantity) {
		this.disbursedQuantity = disbursedQuantity;
	}

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Boolean getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Boolean isOrder) {
		this.isOrder = isOrder;
	}

	public String getColumnsObjectData() {
		return columnsObjectData;
	}

	public void setColumnsObjectData(String columnsObjectData) {
		this.columnsObjectData = columnsObjectData;
	}

	public String getOrderColumnData() {
		return orderColumnData;
	}

	public void setOrderColumnData(String orderColumnData) {
		this.orderColumnData = orderColumnData;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public Transaction(Long id, Long productId, Date createdAt, String remarks, Double procuredQuantity,
			Double disbursedQuantity) {
		super();
		this.id = id;
		this.productId = productId;
		this.createdAt = createdAt;
		this.remarks = remarks;
		this.procuredQuantity = procuredQuantity;
		this.disbursedQuantity = disbursedQuantity;
	}

	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

}
