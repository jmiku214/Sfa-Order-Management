package com.sfa.stock_management.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sfa.stock_management.model.Coupon;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.OrderDispatchDetails;
import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.model.OrderTrail;
import com.sfa.stock_management.model.ProductCatagory;

public class OrderResponseDto {

	private String orderId;
	private String clientName;
	private Double totalPrice;
	private OrderStatus orderStatus;
	private Date createdAt;
	private Double totalNumberOfProducts;
	private List<ProductDto> productDto;
	private String deliveryDate;
	private Long Id;
	private String createdBy;
	private Date lastStatusUpdatedOn;
	private String clientAddress;
	private List<OrderTrail> trailDate;
	private Double totalCgstPrice;
	private Double totalSgstPrice;
	private Double totalIgstPrice;
	private Double totalProductPriceWithGst;
	private Double couponPrice;
	private String companyLogoUrl;
	private Double pendingAmount;
	private String orderRemarks;
	private Coupon coupon;
	private Double mrp;
	private Firm firm;
	private ProductCatagory productCatagory;
	private Long localityId;
	private String localityName;
	private Long noOfDeliveryAttempts;
	private List<OrderDispatchDetails> orderDispatchDetails;
//	private Map<Long, List<OrderDispatchDetails>> orderDispatchMap;
	private Map<Long, List<OrderDispatchDetailsDto>> orderDispatchMap;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Double getTotalNumberOfProducts() {
		return totalNumberOfProducts;
	}

	public void setTotalNumberOfProducts(Double totalNumberOfProducts) {
		this.totalNumberOfProducts = totalNumberOfProducts;
	}

	public List<ProductDto> getProductDto() {
		return productDto;
	}

	public void setProductDto(List<ProductDto> productDto) {
		this.productDto = productDto;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastStatusUpdatedOn() {
		return lastStatusUpdatedOn;
	}

	public void setLastStatusUpdatedOn(Date lastStatusUpdatedOn) {
		this.lastStatusUpdatedOn = lastStatusUpdatedOn;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public List<OrderTrail> getTrailDate() {
		return trailDate;
	}

	public void setTrailDate(List<OrderTrail> trailDate) {
		this.trailDate = trailDate;
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

	public Double getTotalProductPriceWithGst() {
		return totalProductPriceWithGst;
	}

	public void setTotalProductPriceWithGst(Double totalProductPriceWithGst) {
		this.totalProductPriceWithGst = totalProductPriceWithGst;
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

	public Double getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(Double pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
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

	public List<OrderDispatchDetails> getOrderDispatchDetails() {
		return orderDispatchDetails;
	}

	public void setOrderDispatchDetails(List<OrderDispatchDetails> orderDispatchDetails) {
		this.orderDispatchDetails = orderDispatchDetails;
	}

	public Map<Long, List<OrderDispatchDetailsDto>> getOrderDispatchMap() {
		return orderDispatchMap;
	}

	public void setOrderDispatchMap(Map<Long, List<OrderDispatchDetailsDto>> orderDispatchMap) {
		this.orderDispatchMap = orderDispatchMap;
	}

//	public Map<Long, List<OrderDispatchDetails>> getOrderDispatchMap() {
//		return orderDispatchMap;
//	}
//
//	public void setOrderDispatchMap(Map<Long, List<OrderDispatchDetails>> orderDispatchMap) {
//		this.orderDispatchMap = orderDispatchMap;
//	}

}
