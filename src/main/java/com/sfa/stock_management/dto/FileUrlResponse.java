package com.sfa.stock_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class FileUrlResponse {
	public Integer responseCode;
	public String message;
	public FileUrlResponseDetails data;

	public FileUrlResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileUrlResponse(Integer responseCode, String message, FileUrlResponseDetails data) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.data = data;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FileUrlResponseDetails getData() {
		return data;
	}

	public void setData(FileUrlResponseDetails data) {
		this.data = data;
	}

}
