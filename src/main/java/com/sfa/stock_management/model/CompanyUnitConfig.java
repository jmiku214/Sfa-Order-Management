package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_unit_config")
public class CompanyUnitConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "is_active")
	private Boolean isActive;
	@Column(name = "is_company_wise")
	private Boolean isCompanyWise;
	@Column(name="company_id")
	private Long companyId;
	public CompanyUnitConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompanyUnitConfig(Long id, Boolean isActive, Boolean isCompanyWise, Long companyId) {
		super();
		this.id = id;
		this.isActive = isActive;
		this.isCompanyWise = isCompanyWise;
		this.companyId = companyId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsCompanyWise() {
		return isCompanyWise;
	}
	public void setIsCompanyWise(Boolean isCompanyWise) {
		this.isCompanyWise = isCompanyWise;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
}
