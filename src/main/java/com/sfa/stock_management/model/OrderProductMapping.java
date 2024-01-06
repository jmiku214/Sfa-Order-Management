package com.sfa.stock_management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_product_mapping")
public class OrderProductMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id")
	private String orderId;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "total_price")
	private Double totalPrice;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "cgst")
	private Long cgst;

	@Column(name = "sgst")
	private Long sgst;

	@Column(name = "igst")
	private Long igst;

	@Column(name = "after_added_gst_total_price")
	private Double afterAddGstTotalPrice;

	@Column(name = "gst_price")
	private Double gstPrice;

	@Column(name = "is_price_edited")
	private Boolean isPriceEdited;

	@Column(name = "mrp")
	private Double mrp;

	@Column(name = "is_sample")
	private Boolean isSample;

	@Column(name = "unit_id")
	private Long unitId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Long getCgst() {
		return cgst;
	}

	public void setCgst(Long cgst) {
		this.cgst = cgst;
	}

	public Long getSgst() {
		return sgst;
	}

	public void setSgst(Long sgst) {
		this.sgst = sgst;
	}

	public Long getIgst() {
		return igst;
	}

	public void setIgst(Long igst) {
		this.igst = igst;
	}

	public Double getAfterAddGstTotalPrice() {
		return afterAddGstTotalPrice;
	}

	public void setAfterAddGstTotalPrice(Double afterAddGstTotalPrice) {
		this.afterAddGstTotalPrice = afterAddGstTotalPrice;
	}

	public Double getGstPrice() {
		return gstPrice;
	}

	public void setGstPrice(Double gstPrice) {
		this.gstPrice = gstPrice;
	}

	public OrderProductMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Boolean getIsPriceEdited() {
		return isPriceEdited;
	}

	public void setIsPriceEdited(Boolean isPriceEdited) {
		this.isPriceEdited = isPriceEdited;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public OrderProductMapping(Long id, String orderId, Product product, Double totalPrice) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.product = product;
		this.totalPrice = totalPrice;
	}

}
