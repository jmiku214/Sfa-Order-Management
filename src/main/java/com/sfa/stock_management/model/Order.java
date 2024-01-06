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
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "client_name")
	private String clientName;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(name = "qty")
	private Integer quantity;

	@Column(name = "total_price")
	private Double totalPrice;

	@OneToOne
	@JoinColumn(name = "status_id")
	private OrderStatus status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "update_at")
	private Date updatedAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "update_by")
	private Long updateBy;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "delivery_date")
	private String deliveryDate;

	@Column(name = "delivery_address")
	private String deliveryAddress;

	@Column(name = "client_email_id")
	private String clientEmailId;

	@Column(name = "created_user_name")
	private String createdUserName;

	@Column(name = "updated_user_name")
	private String updatedUserName;

	@Column(name = "cgst_price")
	private Double totalCgstPrice;

	@Column(name = "sgst_price")
	private Double totalSgstPrice;

	@Column(name = "igst_price")
	private Double totalIgstPrice;

	@Column(name = "coupon_price")
	private Double couponPrice;

	@Column(name = "company_logo_url")
	private String companyLogoUrl;

	@OneToOne
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;

	@Column(name = "remarks")
	private String remarks;

	@OneToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;

	@OneToOne
	@JoinColumn(name = "category_id")
	private ProductCatagory productCatagory;

	@Column(name = "locality_id")
	private Long localityId;

	@Column(name = "locality_name")
	private String localityName;

	@Column(name = "no_of_delivery_attempts")
	private Long noOfDeliveryAttempts;

	@Column(name = "is_partially_packed")
	private Boolean isPartiallyPacked;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getClientEmailId() {
		return clientEmailId;
	}

	public void setClientEmailId(String clientEmailId) {
		this.clientEmailId = clientEmailId;
	}

	public Double getTotalCgstPrice() {
		return totalCgstPrice;
	}

	public void setTotalCgstPrice(Double totalCgstPrice) {
		this.totalCgstPrice = totalCgstPrice;
	}

	public Double getTotalSgstPrice() {
		return totalSgstPrice;
	}

	public void setTotalSgstPrice(Double totalSgstPrice) {
		this.totalSgstPrice = totalSgstPrice;
	}

	public Double getTotalIgstPrice() {
		return totalIgstPrice;
	}

	public void setTotalIgstPrice(Double totalIgstPrice) {
		this.totalIgstPrice = totalIgstPrice;
	}

	public Double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}

	public void setCompanyLogoUrl(String companyLogoUrl) {
		this.companyLogoUrl = companyLogoUrl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	public ProductCatagory getProductCatagory() {
		return productCatagory;
	}

	public void setProductCatagory(ProductCatagory productCatagory) {
		this.productCatagory = productCatagory;
	}

	public Long getLocalityId() {
		return localityId;
	}

	public void setLocalityId(Long localityId) {
		this.localityId = localityId;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public Long getNoOfDeliveryAttempts() {
		return noOfDeliveryAttempts;
	}

	public void setNoOfDeliveryAttempts(Long noOfDeliveryAttempts) {
		this.noOfDeliveryAttempts = noOfDeliveryAttempts;
	}

	public Boolean getIsPartiallyPacked() {
		return isPartiallyPacked;
	}

	public void setIsPartiallyPacked(Boolean isPartiallyPacked) {
		this.isPartiallyPacked = isPartiallyPacked;
	}

	public Order(Long id, Long clientId, Product product, Integer quantity, Double totalPrice, OrderStatus status,
			Date createdAt, Date updatedAt, String clientName, String deliveryAddress) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.product = product;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;

		this.clientName = clientName;
		this.deliveryAddress = deliveryAddress;
	}

}
