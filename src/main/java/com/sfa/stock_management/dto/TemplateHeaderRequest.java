package com.sfa.stock_management.dto;

public class TemplateHeaderRequest {

	private String headerName;
	private Boolean isMandetory;
	private Boolean isUnique;

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public Boolean getIsMandetory() {
		return isMandetory;
	}

	public void setIsMandetory(Boolean isMandetory) {
		this.isMandetory = isMandetory;
	}

	public Boolean getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}

}
