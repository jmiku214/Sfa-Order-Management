package com.sfa.stock_management.dto;

public class GstResponseDto {

	private Double totalPrice;
	private Double cgstPrice;
	private Double sgstPrice;
	private Double igstPrice;
	private Double totalPriceAfterAddedGst;
	private Double perUnitPrice;
	private Double totalPriceWithoutGst;
	private Boolean isSample;
	private Double originalPrice;
	private Double noOfUnits;
	private Integer unitToBeShowOnWeb;
	private String perUnitName;

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getCgstPrice() {
		return cgstPrice;
	}

	public void setCgstPrice(Double cgstPrice) {
		this.cgstPrice = cgstPrice;
	}

	public Double getSgstPrice() {
		return sgstPrice;
	}

	public void setSgstPrice(Double sgstPrice) {
		this.sgstPrice = sgstPrice;
	}

	public Double getIgstPrice() {
		return igstPrice;
	}

	public void setIgstPrice(Double igstPrice) {
		this.igstPrice = igstPrice;
	}

	public Double getTotalPriceAfterAddedGst() {
		return totalPriceAfterAddedGst;
	}

	public void setTotalPriceAfterAddedGst(Double totalPriceAfterAddedGst) {
		this.totalPriceAfterAddedGst = totalPriceAfterAddedGst;
	}

	public Double getPerUnitPrice() {
		return perUnitPrice;
	}

	public void setPerUnitPrice(Double perUnitPrice) {
		this.perUnitPrice = perUnitPrice;
	}

	public Double getTotalPriceWithoutGst() {
		return totalPriceWithoutGst;
	}

	public void setTotalPriceWithoutGst(Double totalPriceWithoutGst) {
		this.totalPriceWithoutGst = totalPriceWithoutGst;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getNoOfUnits() {
		return noOfUnits;
	}

	public void setNoOfUnits(Double noOfUnits) {
		this.noOfUnits = noOfUnits;
	}

	public Integer getUnitToBeShowOnWeb() {
		return unitToBeShowOnWeb;
	}

	public void setUnitToBeShowOnWeb(Integer unitToBeShowOnWeb) {
		this.unitToBeShowOnWeb = unitToBeShowOnWeb;
	}

	public String getPerUnitName() {
		return perUnitName;
	}

	public void setPerUnitName(String perUnitName) {
		this.perUnitName = perUnitName;
	}

}
