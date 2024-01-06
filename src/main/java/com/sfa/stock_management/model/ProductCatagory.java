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
@Table(name = "product_catagory")
public class ProductCatagory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "catagory_name")
	private String categoryName;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "created_user_name")
	private String createdUserName;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "updated_user_name")
	private String updatedUserName;

	@OneToOne
	@JoinColumn(name = "firm_id")
	private Firm firm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	public ProductCatagory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductCatagory(Long id, Long companyId, String catagoryName, Boolean isActive, Date createdAt,
			Long createdBy, String createdUserName, Date updatedAt, Long updatedBy, String updatedUserName) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.categoryName = catagoryName;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.createdUserName = createdUserName;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
		this.updatedUserName = updatedUserName;
	}

	public ProductCatagory(Long id) {
		super();
		this.id = id;
	}

}
