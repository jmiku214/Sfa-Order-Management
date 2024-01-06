package com.sfa.stock_management.constant;

public enum ProductUnit {

	BOX(1L, "Box"), PIECE(2L, "Piece");

	private Long id;
	private String name;

	ProductUnit(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
