package com.sfa.stock_management.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.Unit;

public class ProductResponseDto {

	private Long id;

	private String sku;

	private String name;

	private Long unit;

	private Long openingQuantity;

	private String hsnCode;

	private Long companyId;

	private Long createdBy;

	private String createdUserName;

	private Long updatedBy;

	private String updatedUserName;

	private Date createdAt;

	private Long minReOrderQuantity;

	private Long allowOrderTillQuantity;

	private String imageUrl;

	private Boolean blockStockOnOrderCreate;

	private Double availableQuantity;

	private Double physicalStockQuantity;

	private Double standardSellingPrice;

	private Double minSellingPrice;

	private String mobileApkImageUrl;

	private Boolean isDetails;

	private List<TemplateHeaderRequest> templateHeaderDetails;

	private Double totalNoOfProducts;

	private ArrayList<Object> templateData;

	private Integer totalNoOfElements;

	private Unit unitEntity;

	private ProductCatagory productCatagory;

	private Firm firm;

	private Integer maxNoOfSamples;

	private Boolean allowOrderWithoutStock;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}

	public Long getOpeningQuantity() {
		return openingQuantity;
	}

	public void setOpeningQuantity(Long openingQuantity) {
		this.openingQuantity = openingQuantity;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getMinReOrderQuantity() {
		return minReOrderQuantity;
	}

	public void setMinReOrderQuantity(Long minReOrderQuantity) {
		this.minReOrderQuantity = minReOrderQuantity;
	}

	public Long getAllowOrderTillQuantity() {
		return allowOrderTillQuantity;
	}

	public void setAllowOrderTillQuantity(Long allowOrderTillQuantity) {
		this.allowOrderTillQuantity = allowOrderTillQuantity;
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

	public Double getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Double getPhysicalStockQuantity() {
		return physicalStockQuantity;
	}

	public void setPhysicalStockQuantity(Double physicalStockQuantity) {
		this.physicalStockQuantity = physicalStockQuantity;
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

	public String getMobileApkImageUrl() {
		return mobileApkImageUrl;
	}

	public void setMobileApkImageUrl(String mobileApkImageUrl) {
		this.mobileApkImageUrl = mobileApkImageUrl;
	}

	public Boolean getIsDetails() {
		return isDetails;
	}

	public void setIsDetails(Boolean isDetails) {
		this.isDetails = isDetails;
	}

	public List<TemplateHeaderRequest> getTemplateHeaderDetails() {
		return templateHeaderDetails;
	}

	public void setTemplateHeaderDetails(List<TemplateHeaderRequest> templateHeaderDetails) {
		this.templateHeaderDetails = templateHeaderDetails;
	}

	public Double getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(Double totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public ArrayList<Object> getTemplateData() {
		return templateData;
	}

	public void setTemplateData(ArrayList<Object> templateData) {
		this.templateData = templateData;
	}

	public Integer getTotalNoOfElements() {
		return totalNoOfElements;
	}

	public void setTotalNoOfElements(Integer totalNoOfElements) {
		this.totalNoOfElements = totalNoOfElements;
	}

	public Unit getUnitEntity() {
		return unitEntity;
	}

	public void setUnitEntity(Unit unitEntity) {
		this.unitEntity = unitEntity;
	}

	public ProductCatagory getProductCatagory() {
		return productCatagory;
	}

	public void setProductCatagory(ProductCatagory productCatagory) {
		this.productCatagory = productCatagory;
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

	public Boolean getAllowOrderWithoutStock() {
		return allowOrderWithoutStock;
	}

	public void setAllowOrderWithoutStock(Boolean allowOrderWithoutStock) {
		this.allowOrderWithoutStock = allowOrderWithoutStock;
	}

	public ProductResponseDto(Long id, String sku, String name, Long unit, Long openingQuantity, String hsnCode,
			Long companyId, Long createdBy, String createdUserName, Long updatedBy, String updatedUserName,
			Date createdAt, Long minReOrderQuantity, Long allowOrderTillQuantity, Boolean blockStockOnOrderCreate,
			Double availableQuantity, Double physicalStockQuantity, Double standardSellingPrice, Double minSellingPrice,
			String mobileApkImageUrl, Boolean isDetails, ProductCatagory productCatagory, Firm firm,
			Integer maxNoOfSamples, Boolean allowOrderWithoutStock) {
		super();
		this.id = id;
		this.sku = sku;
		this.name = name;
		this.unit = unit;
		this.openingQuantity = openingQuantity;
		this.hsnCode = hsnCode;
		this.companyId = companyId;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.updatedBy = updatedBy;
		this.updatedUserName = updatedUserName;
		this.createdAt = createdAt;
		this.minReOrderQuantity = minReOrderQuantity;
		this.allowOrderTillQuantity = allowOrderTillQuantity;
		this.blockStockOnOrderCreate = blockStockOnOrderCreate;
		this.availableQuantity = availableQuantity;
		this.physicalStockQuantity = physicalStockQuantity;
		this.standardSellingPrice = standardSellingPrice;
		this.minSellingPrice = minSellingPrice;
		this.mobileApkImageUrl = mobileApkImageUrl;
		this.isDetails = isDetails;
		this.productCatagory = productCatagory;
		this.firm = firm;
		this.maxNoOfSamples = maxNoOfSamples;
		this.allowOrderWithoutStock = allowOrderWithoutStock;
	}

	public ProductResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product convertToEntity() {

		return new Product(this.sku != null ? this.sku : null, this.name != null ? this.name : null,
				this.unit != null ? this.unit : null, this.openingQuantity != null ? this.openingQuantity : null,
				this.hsnCode != null ? this.hsnCode : null, this.companyId != null ? this.companyId : null,
				this.createdBy != null ? this.createdBy : null,
				this.createdUserName != null ? this.createdUserName : null,
				this.createdAt != null ? this.createdAt : null,
				this.minReOrderQuantity != null ? this.minReOrderQuantity : null,
				this.allowOrderTillQuantity != null ? this.allowOrderTillQuantity : null,
				this.imageUrl != null ? this.imageUrl : null,
				this.blockStockOnOrderCreate != null ? this.blockStockOnOrderCreate : null,
				this.standardSellingPrice != null ? this.standardSellingPrice : null,
				this.minSellingPrice != null ? this.minSellingPrice : null,
				this.isDetails != null ? this.isDetails : null, null);
	}

	public Product convertToEntityV2() {
		// TODO Auto-generated method stub
		return new Product(this.sku != null ? this.sku : null, this.name != null ? this.name : null,
				this.unit != null ? this.unit : null, this.openingQuantity != null ? this.openingQuantity : null,
				this.hsnCode != null ? this.hsnCode : null, this.companyId != null ? this.companyId : null,
				this.createdBy != null ? this.createdBy : null,
				this.createdUserName != null ? this.createdUserName : null,
				this.createdAt != null ? this.createdAt : null,
				this.minReOrderQuantity != null ? this.minReOrderQuantity : null,
				this.allowOrderTillQuantity != null ? this.allowOrderTillQuantity : null,
				this.imageUrl != null ? this.imageUrl : null,
				this.blockStockOnOrderCreate != null ? this.blockStockOnOrderCreate : null,
				this.availableQuantity != null ? this.availableQuantity : null,
				this.physicalStockQuantity != null ? this.physicalStockQuantity : null,
				this.standardSellingPrice != null ? this.standardSellingPrice : null,
				this.minSellingPrice != null ? this.minSellingPrice : null,
				this.isDetails != null ? this.isDetails : false,
				this.totalNoOfProducts != null ? this.totalNoOfProducts : null,
				this.productCatagory != null ? this.productCatagory : null, this.firm != null ? this.firm : null,
				this.maxNoOfSamples != null ? this.maxNoOfSamples : null,
				this.allowOrderWithoutStock != null ? this.allowOrderWithoutStock : null);
	}

}
