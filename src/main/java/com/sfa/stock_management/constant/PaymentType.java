package com.sfa.stock_management.constant;

public enum PaymentType {

	CREDITED(1L, "Credited"), DEBITED(2L, "Debited");
	
	private Long id;
	private String statusName;
	private PaymentType(Long id, String statusName) {
		this.id = id;
		this.statusName = statusName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
}
