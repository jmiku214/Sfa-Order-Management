package com.sfa.stock_management.dto;

import java.util.ArrayList;
import java.util.List;

import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.ProductCatagory;

public class ProductDto {

	private Long id;
	private String skuCode;
	private String productName;
	private String unitName;
	private Double currentQuantity;
	private String hsnCode;
	private Long minReorderQuantity;
	private String createdBy;
	private Double numberOfQuantity;
	private Double totalPrice;
	private Double physicalAvailableQuantity;
	private Long allowOrderTillQuantity;
	private Double standardSellingPrice;
	private Double minSellingPrice;
	private Long cgst;
	private Long sgst;
	private Long igst;
	private Double afterAddedGstTotalPrice;
	private Double perUnitPrice;
	private Double gstPrice;
	private Double cgstPrice;
	private Double sgstPrice;
	private Double igstPrice;
	private List<TemplateHeaderRequest> templateHeaderDetails;
	private Boolean isDetails;
	private ArrayList<Object> templateData;
	private Double totalNoOfProducts;
	private ArrayList<Object> orderColumnData;
	private ProductCatagory category;
	private Firm firm;
	private Integer maxNoOfSamples;
	private Double mrp;
	private Boolean isSample;
	private Boolean allowOrderWithoutStock;
	private Double noOfProductQtyLeftToDispatch;
	private Double noOfQtyLeftToPacked;
	private String imageUrl;
	private Boolean blockStockOnOrderCreate;
	private Long openingQuantity;
	private Double recentlyPackedQty;
	private UnitResponseDto unit;
	private Double alreadyPackedQty;
	private Double alreadyDispatchedQty;
	private String perUnitName;
	private Boolean isUnitChanged;
	private Double perSubUnitPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(Double currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Long getMinReorderQuantity() {
		return minReorderQuantity;
	}

	public void setMinReorderQuantity(Long minReorderQuantity) {
		this.minReorderQuantity = minReorderQuantity;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Double getNumberOfQuantity() {
		return numberOfQuantity;
	}

	public void setNumberOfQuantity(Double numberOfQuantity) {
		this.numberOfQuantity = numberOfQuantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getPhysicalAvailableQuantity() {
		return physicalAvailableQuantity;
	}

	public void setPhysicalAvailableQuantity(Double physicalAvailableQuantity) {
		this.physicalAvailableQuantity = physicalAvailableQuantity;
	}

	public Long getAllowOrderTillQuantity() {
		return allowOrderTillQuantity;
	}

	public void setAllowOrderTillQuantity(Long allowOrderTillQuantity) {
		this.allowOrderTillQuantity = allowOrderTillQuantity;
	}

	public Double getStandardSellingPrice() {
		return standardSellingPrice;
	}

	public void setStandardSellingPrice(Double standardSellingPrice) {
		this.standardSellingPrice = standardSellingPrice;
	}

	public Double getMinSellingPrice() {
		return minSellingPrice;
	}

	public void setMinSellingPrice(Double minSellingPrice) {
		this.minSellingPrice = minSellingPrice;
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

	public Double getAfterAddedGstTotalPrice() {
		return afterAddedGstTotalPrice;
	}

	public void setAfterAddedGstTotalPrice(Double afterAddedGstTotalPrice) {
		this.afterAddedGstTotalPrice = afterAddedGstTotalPrice;
	}

	public Double getPerUnitPrice() {
		return perUnitPrice;
	}

	public void setPerUnitPrice(Double perUnitPrice) {
		this.perUnitPrice = perUnitPrice;
	}

	public Double getCgstPrice() {
		return cgstPrice;
	}

	public void setCgstPrice(Double cgstPrice) {
		this.cgstPrice = cgstPrice;
	}

	public Double getSgstPrice() {
		return sgstPrice;
	}

	public void setSgstPrice(Double sgstPrice) {
		this.sgstPrice = sgstPrice;
	}

	public Double getIgstPrice() {
		return igstPrice;
	}

	public void setIgstPrice(Double igstPrice) {
		this.igstPrice = igstPrice;
	}

	public Double getGstPrice() {
		return gstPrice;
	}

	public void setGstPrice(Double gstPrice) {
		this.gstPrice = gstPrice;
	}

	public List<TemplateHeaderRequest> getTemplateHeaderDetails() {
		return templateHeaderDetails;
	}

	public void setTemplateHeaderDetails(List<TemplateHeaderRequest> templateHeaderDetails) {
		this.templateHeaderDetails = templateHeaderDetails;
	}

	public Boolean getIsDetails() {
		return isDetails;
	}

	public void setIsDetails(Boolean isDetails) {
		this.isDetails = isDetails;
	}

	public ArrayList<Object> getTemplateData() {
		return templateData;
	}

	public void setTemplateData(ArrayList<Object> templateData) {
		this.templateData = templateData;
	}

	public Double getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(Double totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public ArrayList<Object> getOrderColumnData() {
		return orderColumnData;
	}

	public void setOrderColumnData(ArrayList<Object> orderColumnData) {
		this.orderColumnData = orderColumnData;
	}

	public ProductCatagory getCategory() {
		return category;
	}

	public void setCategory(ProductCatagory category) {
		this.category = category;
	}

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	public Integer getMaxNoOfSamples() {
		return maxNoOfSamples;
	}

	public void setMaxNoOfSamples(Integer maxNoOfSamples) {
		this.maxNoOfSamples = maxNoOfSamples;
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

	public Boolean getAllowOrderWithoutStock() {
		return allowOrderWithoutStock;
	}

	public void setAllowOrderWithoutStock(Boolean allowOrderWithoutStock) {
		this.allowOrderWithoutStock = allowOrderWithoutStock;
	}

	public Double getNoOfProductQtyLeftToDispatch() {
		return noOfProductQtyLeftToDispatch;
	}

	public void setNoOfProductQtyLeftToDispatch(Double noOfProductQtyLeftToDispatch) {
		this.noOfProductQtyLeftToDispatch = noOfProductQtyLeftToDispatch;
	}

	public Double getNoOfQtyLeftToPacked() {
		return noOfQtyLeftToPacked;
	}

	public void setNoOfQtyLeftToPacked(Double noOfQtyLeftToPacked) {
		this.noOfQtyLeftToPacked = noOfQtyLeftToPacked;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getBlockStockOnOrderCreate() {
		return blockStockOnOrderCreate;
	}

	public void setBlockStockOnOrderCreate(Boolean blockStockOnOrderCreate) {
		this.blockStockOnOrderCreate = blockStockOnOrderCreate;
	}

	public Long getOpeningQuantity() {
		return openingQuantity;
	}

	public void setOpeningQuantity(Long openingQuantity) {
		this.openingQuantity = openingQuantity;
	}

	public UnitResponseDto getUnit() {
		return unit;
	}

	public void setUnit(UnitResponseDto unit) {
		this.unit = unit;
	}

	public Double getRecentlyPackedQty() {
		return recentlyPackedQty;
	}

	public void setRecentlyPackedQty(Double recentlyPackedQty) {
		this.recentlyPackedQty = recentlyPackedQty;
	}

	public Double getAlreadyPackedQty() {
		return alreadyPackedQty;
	}

	public void setAlreadyPackedQty(Double alreadyPackedQty) {
		this.alreadyPackedQty = alreadyPackedQty;
	}

	public Double getAlreadyDispatchedQty() {
		return alreadyDispatchedQty;
	}

	public void setAlreadyDispatchedQty(Double alreadyDispatchedQty) {
		this.alreadyDispatchedQty = alreadyDispatchedQty;
	}

	public String getPerUnitName() {
		return perUnitName;
	}

	public void setPerUnitName(String perUnitName) {
		this.perUnitName = perUnitName;
	}

	public Boolean getIsUnitChanged() {
		return isUnitChanged;
	}

	public void setIsUnitChanged(Boolean isUnitChanged) {
		this.isUnitChanged = isUnitChanged;
	}

	public Double getPerSubUnitPrice() {
		return perSubUnitPrice;
	}

	public void setPerSubUnitPrice(Double perSubUnitPrice) {
		this.perSubUnitPrice = perSubUnitPrice;
	}

}
