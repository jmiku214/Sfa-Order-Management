package com.sfa.stock_management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hsn_gst_mapping")
public class HsnGstMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "hsn_code")
	private String hsnCode;

	@Column(name = "gst_percent")
	private Integer gstPercentage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(Integer gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public HsnGstMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HsnGstMapping(Long id, String hsnCode, Integer gstPercentage) {
		super();
		this.id = id;
		this.hsnCode = hsnCode;
		this.gstPercentage = gstPercentage;
	}

}
