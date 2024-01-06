package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.OrderProductMapping;

@Repository
public interface OrderProductMappingRepository extends JpaRepository<OrderProductMapping, Long> {

	List<OrderProductMapping> findAllByOrderId(String orderId);

	@Query(value = "SELECT * FROM order_product_mapping where product_id=?1", nativeQuery = true)
	List<OrderProductMapping> findAllByProductId(Long productId);

	@Query(value = "select * from order_product_mapping where order_id IN (:updatedOrderIds)", nativeQuery = true)
	List<OrderProductMapping> findAllByOrderIdIn(List<String> updatedOrderIds);

	@Query(value = "SELECT * FROM order_product_mapping where order_id=?1 and product_id=?2 and is_sample=?3", nativeQuery = true)
	Optional<OrderProductMapping> findAllByOrderIdAndProductIdAndIsSample(String orderId, Long productId,
			Boolean isSample);

}
