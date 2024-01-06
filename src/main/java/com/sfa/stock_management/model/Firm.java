package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "firm")
public class Firm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "firm_name")
	private String firmName;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "pan_number")
	private String panNumber;

	@Column(name = "gst_number")
	private String gstNumber;

	@Column(name = "address")
	private String address;

	@Column(name = "is_active")
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Firm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Firm(Long id, String firmName, Long companyId, Date createdAt, Long createdBy, String panNumber,
			String gstNumber, String address, Boolean isActive) {
		super();
		this.id = id;
		this.firmName = firmName;
		this.companyId = companyId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.panNumber = panNumber;
		this.gstNumber = gstNumber;
		this.address = address;
		this.isActive = isActive;
	}

	public Firm(Long id) {
		super();
		this.id = id;
	}

}
