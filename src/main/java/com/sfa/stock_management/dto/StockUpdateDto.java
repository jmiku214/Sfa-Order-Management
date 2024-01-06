package com.sfa.stock_management.dto;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.TokenType;

public class StockUpdateDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;

	private Double procuredQuantity;
	private Double disbursedQuantity;
	private String remarks;
	private Long createdBy;
	private String createdUserName;
	private String tokenNumber;
	private Boolean isOrder;
	private Boolean isProcured;
	private Boolean isDisbursed;
	private MultipartFile file;
	private ArrayList<Object> templateData;
	private String orderObjectData;
	private ArrayList<Object> orderedData;
	private Long companyId;
	private TokenType tokenType;
	private Long tokenTypeId;
	private Long firm;
	private Long category;
	private String unitName;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Double getProcuredQuantity() {
		return procuredQuantity;
	}

	public void setProcuredQuantity(Double procuredQuantity) {
		this.procuredQuantity = procuredQuantity;
	}

	public Double getDisbursedQuantity() {
		return disbursedQuantity;
	}

	public void setDisbursedQuantity(Double disbursedQuantity) {
		this.disbursedQuantity = disbursedQuantity;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Boolean getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(Boolean isOrder) {
		this.isOrder = isOrder;
	}

	public Boolean getIsProcured() {
		return isProcured;
	}

	public void setIsProcured(Boolean isProcured) {
		this.isProcured = isProcured;
	}

	public Boolean getIsDisbursed() {
		return isDisbursed;
	}

	public void setIsDisbursed(Boolean isDisbursed) {
		this.isDisbursed = isDisbursed;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public ArrayList<Object> getTemplateData() {
		return templateData;
	}

	public void setTemplateData(ArrayList<Object> templateData) {
		this.templateData = templateData;
	}

	public String getOrderObjectData() {
		return orderObjectData;
	}

	public void setOrderObjectData(String orderObjectData) {
		this.orderObjectData = orderObjectData;
	}

	public ArrayList<Object> getOrderedData() {
		return orderedData;
	}

	public void setOrderedData(ArrayList<Object> orderedData) {
		this.orderedData = orderedData;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public Long getTokenTypeId() {
		return tokenTypeId;
	}

	public void setTokenTypeId(Long tokenTypeId) {
		this.tokenTypeId = tokenTypeId;
	}

	public Long getFirm() {
		return firm;
	}

	public void setFirm(Long firm) {
		this.firm = firm;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
