package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ProductCatagory;

public interface ProductCatagoryService {

	Response<?> saveProductCatagory(ProductCatagory productCatagory);

	Response<?> getAllProductCategory(Long companyId,Long firmId);

	Response<?> updateProductCatagory(ProductCatagory catagory);

}
