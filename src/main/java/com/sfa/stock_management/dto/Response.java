package com.sfa.stock_management.dto;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Component
@JsonInclude(Include.NON_NULL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Response<T> {
	private int responseCode;
	private String message;
	private String requestedURI;
	private T data;
	private com.sfa.stock_management.util.Pagination<List<?>> paginationData;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestedURI() {
		return requestedURI;
	}

	public void setRequestedURI(String requestedURI) {
		this.requestedURI = requestedURI;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	

	public com.sfa.stock_management.util.Pagination<List<?>> getPaginationData() {
		return paginationData;
	}

	public void setPaginationData(com.sfa.stock_management.util.Pagination<List<?>> paginationData) {
		this.paginationData = paginationData;
	}

	public Response(int responseCode, String message, T data) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.data = data;
	}


	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
