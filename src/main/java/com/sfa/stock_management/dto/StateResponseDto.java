package com.sfa.stock_management.dto;

public class StateResponseDto {

	private Long stateId;
	private String stateName;

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public StateResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StateResponseDto(Long stateId, String stateName) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
	}

}
