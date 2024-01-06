package com.sfa.stock_management.dto;

import java.util.List;

public class ClientDataGetRequestBody {

	private Long userId;
	private Long hrmsUserId;
	private String name;
	private Long id;
	private String address;
	private String email;
	private Long stateId;
	private List<String> clientBranches;
	private String cityName;
	private String localityName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getHrmsUserId() {
		return hrmsUserId;
	}

	public void setHrmsUserId(Long hrmsUserId) {
		this.hrmsUserId = hrmsUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public List<String> getClientBranches() {
		return clientBranches;
	}

	public void setClientBranches(List<String> clientBranches) {
		this.clientBranches = clientBranches;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

}
