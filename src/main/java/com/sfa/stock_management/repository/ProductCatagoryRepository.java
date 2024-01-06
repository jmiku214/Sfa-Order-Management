package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.ProductCatagory;

@Repository
public interface ProductCatagoryRepository extends JpaRepository<ProductCatagory, Long> {

	@Query(value = "SELECT * FROM product_catagory where catagory_name=?1 and company_id=?2", nativeQuery = true)
	ProductCatagory findByCatagoryNameAndCompanyId(String catagoryName, Long companyId);

	@Query(value = "SELECT * FROM product_catagory where company_id=?1 order by id desc", nativeQuery = true)
	List<ProductCatagory> findAllByCompanyId(Long companyId);

	@Query(value = "SELECT * FROM product_catagory where company_id=?1 and firm_id=?2 order by id desc", nativeQuery = true)
	List<ProductCatagory> findAllByCompanyIdAndFirmId(Long companyId, Long firmId);

	@Query(value = "SELECT * FROM product_catagory where company_id=?1 and firm_id is null order by id desc", nativeQuery = true)
	List<ProductCatagory> findAllByCompanyIdAndIsFirmNull(Long companyId);
	
	@Query(value = "SELECT * FROM product_catagory where company_id=?1 and catagory_name=?2 and firm_id is null order by id desc", nativeQuery = true)
	ProductCatagory findAllByCompanyIdAndCategoryNameAndIsFirmNull(Long companyId,String categoryName);

	@Query(value = "SELECT * FROM product_catagory where catagory_name=?1 and company_id=?2 and firm_id=?3", nativeQuery = true)
	ProductCatagory findByCatagoryNameAndCompanyIdAndFirm(String categoryName, Long companyId, Long firmId);

}
