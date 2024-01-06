package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "select * from product where sku=?1",nativeQuery = true)
	Optional<Product> findBySku(String sku);

	@Query(value = "select * from product where available_quanity<min_reorder_qty",nativeQuery = true)
	List<Product> findAllLessAvailableProduct();

	@Query(value = "select * from product where available_quanity<min_reorder_qty",nativeQuery = true)
	List<Product> findAllMinStock(int parseInt);

	@Query(value ="select * from product where company_id=?1 order by id desc",nativeQuery = true)
	List<Product> findAllByCompanyId(Long companyId);

	@Query(value = "select * from product where sku=?1 or name=?2",nativeQuery = true)
	List<Product> findBySkuAndProductName(String sku, String name);

	@Query(value = "select * from product where sku in ?1 and company_id=?2",nativeQuery = true)
	List<Product> findAllBySkuIn(List<String> skuList, Long companyId);

	@Query(value ="select * from product where company_id=?1 and category_id=?2 order by id desc",nativeQuery = true)
	List<Product> findAllByCompanyIdAndCatagoryId(Long companyId, Long categoryId);

	@Query(value ="select * from product where company_id=?1 and category_id=?2 and firm_id=?3 order by id desc",nativeQuery = true)
	List<Product> findAllByCompanyIdAndCatagoryIdAndFirmId(Long companyId, Long categoryId, Long firmId);

	@Query(value ="select * from product where company_id=?1 and firm_id=?2 order by id desc",nativeQuery = true)
	List<Product> findAllByCompanyIdAndFirmId(Long companyId, Long firmId);

	@Query(value ="select * from product where company_id=?1 and firm_id is null order by id desc",nativeQuery = true)
	List<Product> findAllByCompanyIdAndFirmIdNull(Long companyId, Long firmId);

	@Query(value = "select * from product where (sku=?1 or name=?2) and firm_id=?3",nativeQuery = true)
	List<Product> findBySkuAndProductNameAndFirm(String sku, String name, Long firmId);

	@Query(value = "select * from product where (sku=?1 or name=?2) and company_id=?3",nativeQuery = true)
	List<Product> findBySkuAndProductNameAndCompanyID(String sku, String name, Long companyId);

	@Query(value = "select * from product where sku=?1 and firm_id is null",nativeQuery = true)
	Optional<Product> findBySkuAndFirmNull(String sku);
	
	@Query(value = "select * from product where sku=?1 and firm_id=?2",nativeQuery = true)
	Optional<Product> findBySkuAndFirmId(String sku, Long firmId);

	@Query(value = "select * from product where sku=?1 and company_id=?2 and firm_id is null",nativeQuery = true)
	Optional<Product> findBySkuAndCompanyIdAndFirmNull(String sku, Long companyId);

	@Query(value = "select * from product where sku=?1 and firm_id=?2 and company_id=?3",nativeQuery = true)
	Optional<Product> findBySkuAndFirmIdAndCompanyId(String sku, Long firmId,Long companyId);

	@Query(value = "select * from product where (sku=?1 or name=?2) and firm_id=?3 and company_id=?4",nativeQuery = true)
	List<Product> findBySkuAndProductNameAndFirmAndCompanyId(String sku,String name , Long firmId, Long companyId);


	
	
}
