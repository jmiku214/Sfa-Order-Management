package com.sfa.stock_management.dto;

import java.util.ArrayList;

public class ProductTemplateDataResponseForOrder {

	private ArrayList<Object> templateData;
	private String productName;

	public ArrayList<Object> getTemplateData() {
		return templateData;
	}

	public void setTemplateData(ArrayList<Object> templateData) {
		this.templateData = templateData;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
