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
@Table(name = "unit_conversion_mapping")
public class UnitConversionMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sub_unit_name")
	private String subUnitName;

	@OneToOne
	@JoinColumn(name = "unit_id")
	private Unit unit;

	@Column(name = "sub_unit_qty")
	private Long subUnitQty;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_at")
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubUnitName() {
		return subUnitName;
	}

	public void setSubUnitName(String subUnitName) {
		this.subUnitName = subUnitName;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Long getSubUnitQty() {
		return subUnitQty;
	}

	public void setSubUnitQty(Long subUnitQty) {
		this.subUnitQty = subUnitQty;
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

	public UnitConversionMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnitConversionMapping(Long id, String subUnitName, Unit unit, Long subUnitQty) {
		super();
		this.id = id;
		this.subUnitName = subUnitName;
		this.unit = unit;
		this.subUnitQty = subUnitQty;
	}

}
