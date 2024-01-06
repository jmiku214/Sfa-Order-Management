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

import com.sfa.stock_management.dto.OrderDispatchDetailsDto;

@Entity
@Table(name = "dispatch_details")
public class OrderDispatchDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "is_sample")
	private Boolean isSample;

	@OneToOne
	@JoinColumn(name = "order_prod_map_id")
	private OrderProductMapping orderProductMapping;

	@Column(name = "dispatch_attempt_no")
	private Long dispatchAttemptNo;

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public OrderDispatchDetails(Long id, Product product, Double quantity, Date createdAt) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}

	public OrderDispatchDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderDispatchDetailsDto convertToDto() {
		// TODO Auto-generated method stub
		return new OrderDispatchDetailsDto(this.id != null ? this.id : null, this.product != null ? this.product : null,
				this.orderId != null ? this.orderId : null, this.quantity != null ? this.quantity : null,
				this.createdAt != null ? this.createdAt : null, this.isSample != null ? this.isSample : null,
				this.orderProductMapping != null ? this.orderProductMapping : null,
				this.dispatchAttemptNo != null ? this.dispatchAttemptNo : null);
	}

}
