package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.OrderPackedDetails;

@Repository
public interface OrderPackedDetailsRepository extends JpaRepository<OrderPackedDetails, Long> {

	@Query(value = "SELECT * FROM order_packed_mapping where order_id=?1 and product_id=?2 and is_sample=?3", nativeQuery = true)
	List<OrderPackedDetails> findAllByOrderIdAndProductIdAndIsSample(String orderId, Long productId, Boolean isSample);

	@Query(value = "SELECT * FROM order_packed_mapping where order_prod_map_id=?1 order by id desc", nativeQuery = true)
	List<OrderPackedDetails> findAllByOrderProdMapId(Long id);

}
