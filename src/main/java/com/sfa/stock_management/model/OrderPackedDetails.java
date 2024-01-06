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
@Table(name = "order_packed_mapping")
public class OrderPackedDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	@OneToOne
	@JoinColumn(name = "order_prod_map_id")
	private OrderProductMapping orderProdMapId;

	@Column(name = "quantity")
	private Double qty;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "is_sample")
	private Boolean isSample;

	@Column(name = "order_id")
	private String orderId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public OrderProductMapping getOrderProdMapId() {
		return orderProdMapId;
	}

	public void setOrderProdMapId(OrderProductMapping orderProdMapId) {
		this.orderProdMapId = orderProdMapId;
	}

	

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public OrderPackedDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderPackedDetails(Long id, Product productId, OrderProductMapping orderProdMapId, Double qty,
			Date createdAt) {
		super();
		this.id = id;
		this.productId = productId;
		this.orderProdMapId = orderProdMapId;
		this.qty = qty;
		this.createdAt = createdAt;
	}

}
