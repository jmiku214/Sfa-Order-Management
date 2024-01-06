package com.sfa.stock_management.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.repository.ProductCatagoryRepository;
import com.sfa.stock_management.service.ProductCatagoryService;

@Service
public class ProductCatagoryServiceImpl implements ProductCatagoryService {

	@Autowired
	private ProductCatagoryRepository productCatagoryRepository;

	@Override
	public Response<?> saveProductCatagory(ProductCatagory productCatagory) {
		if (productCatagory.getCategoryName().toUpperCase().equals("OTHER")
				|| productCatagory.getCategoryName().toUpperCase().equals("OTHERS")) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Category already exist.", null);
		} else {
			ProductCatagory catagoryObj = new ProductCatagory();
			if (productCatagory.getFirm().getId() > 0) {
				catagoryObj = productCatagoryRepository.findByCatagoryNameAndCompanyIdAndFirm(
						productCatagory.getCategoryName(), productCatagory.getCompanyId(),
						productCatagory.getFirm().getId());
			} else {
				catagoryObj = productCatagoryRepository.findAllByCompanyIdAndCategoryNameAndIsFirmNull(
						productCatagory.getCompanyId(), productCatagory.getCategoryName());
			}

			if (catagoryObj != null) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Category already exist.", null);
			} else {
				productCatagory.setIsActive(true);
				productCatagory.setCreatedAt(new Date());
				if (productCatagory.getFirm() != null) {
					if (productCatagory.getFirm().getId() > 0) {
						productCatagory.setFirm(productCatagory.getFirm());
					} else {
						productCatagory.setFirm(null);
					}
				}
				productCatagoryRepository.save(productCatagory);
				return new Response<>(HttpStatus.OK.value(), "Category updated successfully.", null);
			}
		}

	}

	@Override
	public Response<?> getAllProductCategory(Long companyId, Long firmId) {
		List<ProductCatagory> productCategoryList = new ArrayList<>();
		if (companyId != null && firmId != null) {
			if (firmId > 0) {
				productCategoryList = productCatagoryRepository.findAllByCompanyIdAndFirmId(companyId, firmId);
			} else {
				productCategoryList = productCatagoryRepository.findAllByCompanyIdAndIsFirmNull(companyId);
				ProductCatagory category = new ProductCatagory();
				category.setCategoryName("Other");
				category.setId(0L);
				productCategoryList.add(category);
			}

		}
		if (companyId != null && firmId == null) {
			productCategoryList = productCatagoryRepository.findAllByCompanyId(companyId);
			ProductCatagory category = new ProductCatagory();
			category.setCategoryName("Other");
			category.setId(0L);
			productCategoryList.add(category);
		}

//		if(firmId==null) {
//			ProductCatagory category=new ProductCatagory();
//			category.setCategoryName("Other");
//			category.setId(0L);
//			productCategoryList.add(category);
//		}

		return new Response<>(HttpStatus.OK.value(), "Product category list.", productCategoryList);
	}

	@Override
	public Response<?> updateProductCatagory(ProductCatagory catagory) {
		if (catagory.getCategoryName().toUpperCase().equals("OTHER")
				|| catagory.getCategoryName().toUpperCase().equals("OTHERS")) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Category name already exist.", null);
		} else {
			Optional<ProductCatagory> catagoryObj = productCatagoryRepository.findById(catagory.getId());
			if (catagoryObj != null && catagoryObj.isPresent()) {
				if (catagory.getCategoryName().trim().equals(catagoryObj.get().getCategoryName().trim())) {
					catagoryObj.get().setCategoryName(catagory.getCategoryName().trim());
					catagoryObj.get().setFirm(catagory.getFirm());
					productCatagoryRepository.save(catagoryObj.get());
					return new Response<>(HttpStatus.OK.value(), "Product category data updated successfully.", null);
				} else {
					ProductCatagory catagoryExistObj = productCatagoryRepository
							.findByCatagoryNameAndCompanyId(catagory.getCategoryName(), catagory.getCompanyId());
					if (catagoryExistObj != null) {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Category name already exist.", null);
					} else {
						if (catagory.getCategoryName().equals(catagoryObj.get().getCategoryName())) {
							catagoryObj.get().setCategoryName(catagory.getCategoryName());
							if (catagory.getFirm() != null) {
								if (catagory.getFirm().getId() > 0L) {
									catagoryObj.get().setFirm(catagory.getFirm());
								} else {
									catagoryObj.get().setFirm(null);
								}
							}
							productCatagoryRepository.save(catagoryObj.get());
							return new Response<>(HttpStatus.OK.value(), "Product category data updated successfully.",
									null);
						} else {
							ProductCatagory catagoryObject = new ProductCatagory();
							if (catagory.getFirm().getId() > 0) {
								catagoryObject = productCatagoryRepository.findByCatagoryNameAndCompanyIdAndFirm(
										catagory.getCategoryName(), catagory.getCompanyId(),
										catagory.getFirm().getId());
							} else {
								catagoryObject = productCatagoryRepository
										.findAllByCompanyIdAndCategoryNameAndIsFirmNull(catagory.getCompanyId(),
												catagory.getCategoryName());
							}
							if (catagoryObject != null) {
								return new Response<>(HttpStatus.BAD_REQUEST.value(), "Category name already exist.",
										null);
							} else {
								catagoryObj.get().setCategoryName(catagory.getCategoryName());
								productCatagoryRepository.save(catagoryObj.get());
								return new Response<>(HttpStatus.OK.value(),
										"Product category data updated successfully.", null);
							}

						}

					}
				}

			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "No value present.", null);
			}
		}

	}

}
