package com.sfa.stock_management.dto;

import java.util.Date;
import java.util.List;

public class UnitResponseDto {

	private Long id;
	private String unitName;
	private Long companyId;
	private List<String> unitList;
	private Date createdAt;
	private Long quantity;
	private List<UnitResponseDto> subUnitList;
	private Boolean isDefault;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public List<String> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<String> unitList) {
		this.unitList = unitList;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<UnitResponseDto> getSubUnitList() {
		return subUnitList;
	}

	public void setSubUnitList(List<UnitResponseDto> subUnitList) {
		this.subUnitList = subUnitList;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}
