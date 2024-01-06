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
@Table(name = "client_product_price_configuration")
public class ClientProductPriceConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product productId;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "price")
	private Double price;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "update_at")
	private Date updateAt;

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

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public ClientProductPriceConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientProductPriceConfig(Long id, Product productId, Long clientId, Long companyId, Double price) {
		super();
		this.id = id;
		this.productId = productId;
		this.clientId = clientId;
		this.companyId = companyId;
		this.price = price;
	}

}
