package com.sfa.stock_management.dto;

import java.util.List;

public class TransactionResponseDto {

	private ProductDto productDto;
	private List<StockUpdateDto> stockUpdateDtos;

	public ProductDto getProductDto() {
		return productDto;
	}

	public void setProductDto(ProductDto productDto) {
		this.productDto = productDto;
	}

	public List<StockUpdateDto> getStockUpdateDtos() {
		return stockUpdateDtos;
	}

	public void setStockUpdateDtos(List<StockUpdateDto> stockUpdateDtos) {
		this.stockUpdateDtos = stockUpdateDtos;
	}

}
