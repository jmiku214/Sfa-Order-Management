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
import javax.persistence.Transient;

import com.sfa.stock_management.dto.ProductResponseDto;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sku")
	private String sku;

	@Column(name = "name")
	private String name;

	@Column(name = "unit")
	private Long unit;

	@Column(name = "opening_qty")
	private Long openingQuantity;

	@Column(name = "hsn_code")
	private String hsnCode;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_user_name")
	private String createdUserName;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_user_name")
	private String updatedUserName;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "min_reorder_qty")
	private Long minReOrderQuantity;

	@Column(name = "allow_order_till_qty")
	private Long allowOrderTillQuantity;

	@Column(name = "image")
	private String imageUrl;

	@Column(name = "block_stock_in_order")
	private Boolean blockStockOnOrderCreate;

	@Column(name = "available_quanity")
	private Double availableQuantity;

	@Column(name = "physical_quantity")
	private Double physicalStockQuantity;

	@Column(name = "standard_selling_price")
	private Double standardSellingPrice;

	@Column(name = "min_selling_price")
	private Double minSellingPrice;

	@Column(name = "is_details")
	private Boolean isDetails;

	@Column(name = "details_column_data")
	private String productColumnDetails;

	@Column(name = "total_no_products")
	private Double totalNoOfProducts;

	@OneToOne
	@JoinColumn(name = "category_id")
	private ProductCatagory productCategory;

	@OneToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;

	@Column(name = "max_samples")
	private Integer maxNoOfSamples;

	@Column(name = "allow_order_without_stock")
	private Boolean allowOrderWithoutStock;

	@Transient
	private Long currentQty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	

	public Boolean getBlockStockOnOrderCreate() {
		return blockStockOnOrderCreate;
	}

	public void setBlockStockOnOrderCreate(Boolean blockStockOnOrderCreate) {
		this.blockStockOnOrderCreate = blockStockOnOrderCreate;
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

	

	public Boolean getIsDetails() {
		return isDetails;
	}

	public void setIsDetails(Boolean isDetails) {
		this.isDetails = isDetails;
	}

	public String getProductColumnDetails() {
		return productColumnDetails;
	}

	public void setProductColumnDetails(String productColumnDetails) {
		this.productColumnDetails = productColumnDetails;
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

	public Double getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(Double totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public ProductCatagory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCatagory productCategory) {
		this.productCategory = productCategory;
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

	public Long getCurrentQty() {
		return currentQty;
	}

	public void setCurrentQty(Long currentQty) {
		this.currentQty = currentQty;
	}

	public Product(String sku, String name, Long unit, Long openingQuantity, String hsnCode, Long companyId,
			Long createdBy, String createdUserName, Date createdAt, Long minReOrderQuantity,
			Long allowOrderTillQuantity, String imageUrl, Boolean blockStockOnOrderCreate, Double standardSellingPrice,
			Double minSellingPrice, Boolean isDetails, String productColumnDetails) {
		super();

		this.sku = sku;
		this.name = name;
		this.unit = unit;
		this.openingQuantity = openingQuantity;
		this.hsnCode = hsnCode;
		this.companyId = companyId;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.createdAt = createdAt;
		this.minReOrderQuantity = minReOrderQuantity;
		this.allowOrderTillQuantity = allowOrderTillQuantity;
		this.imageUrl = imageUrl;
		this.blockStockOnOrderCreate = blockStockOnOrderCreate;
		this.standardSellingPrice = standardSellingPrice;
		this.minSellingPrice = minSellingPrice;
		this.isDetails = isDetails;
		this.productColumnDetails = productColumnDetails;
	}

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductResponseDto convertToDto() {

		return new ProductResponseDto(this.id != null ? this.id : null, this.sku != null ? this.sku : null,
				this.name != null ? this.name : null, this.unit != null ? this.unit : null,
				this.openingQuantity != null ? this.openingQuantity : null, this.hsnCode != null ? this.hsnCode : null,
				this.companyId != null ? this.companyId : null, this.createdBy != null ? this.createdBy : null,
				this.createdUserName != null ? this.createdUserName : null,
				this.updatedBy != null ? this.updatedBy : null,
				this.updatedUserName != null ? this.updatedUserName : null,
				this.createdAt != null ? this.createdAt : null,
				this.minReOrderQuantity != null ? this.minReOrderQuantity : null,
				this.allowOrderTillQuantity != null ? this.allowOrderTillQuantity : null,
				this.blockStockOnOrderCreate != null ? this.blockStockOnOrderCreate : null,
				this.availableQuantity != null ? this.availableQuantity : null,
				this.physicalStockQuantity != null ? this.physicalStockQuantity : null,
				this.standardSellingPrice != null ? this.standardSellingPrice : null,
				this.minSellingPrice != null ? this.minSellingPrice : null,
				this.imageUrl != null ? this.imageUrl : null, this.isDetails != null ? this.isDetails : null,
				this.productCategory != null ? this.productCategory : null, this.firm != null ? this.firm : null,
				this.maxNoOfSamples != null ? this.maxNoOfSamples : null,
				this.allowOrderWithoutStock != null ? this.allowOrderWithoutStock : null);
	}

	public Product(String sku, String name, Long unit, Long openingQuantity, String hsnCode, Long companyId,
			Long createdBy, String createdUserName, Date createdAt, Long minReOrderQuantity,
			Long allowOrderTillQuantity, String imageUrl, Boolean blockStockOnOrderCreate, Double availableQuantity,
			Double physicalStockQuantity, Double standardSellingPrice, Double minSellingPrice, Boolean isDetails,
			Double totalNoOfProducts, ProductCatagory productCatagory, Firm firm, Integer maxNoOfSamples,
			Boolean allowOrderWithoutStock) {
		super();
		this.sku = sku;
		this.name = name;
		this.unit = unit;
		this.openingQuantity = openingQuantity;
		this.hsnCode = hsnCode;
		this.companyId = companyId;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.createdAt = createdAt;
		this.minReOrderQuantity = minReOrderQuantity;
		this.allowOrderTillQuantity = allowOrderTillQuantity;
		this.imageUrl = imageUrl;
		this.blockStockOnOrderCreate = blockStockOnOrderCreate;
		this.availableQuantity = availableQuantity;
		this.physicalStockQuantity = physicalStockQuantity;
		this.standardSellingPrice = standardSellingPrice;
		this.minSellingPrice = minSellingPrice;
		this.isDetails = isDetails;
		this.totalNoOfProducts = totalNoOfProducts;
		this.productCategory = productCatagory;
		this.firm = firm;
		this.maxNoOfSamples = maxNoOfSamples;
		this.allowOrderWithoutStock = allowOrderWithoutStock;
	}

	public Product(String name) {
		super();
		this.name = name;
	}

}
