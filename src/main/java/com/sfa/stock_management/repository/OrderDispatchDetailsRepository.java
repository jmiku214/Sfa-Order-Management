package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.OrderDispatchDetails;

@Repository
public interface OrderDispatchDetailsRepository extends JpaRepository<OrderDispatchDetails, Long> {

	List<OrderDispatchDetails> findAllByOrderId(String orderId);

	@Query(value = "SELECT * FROM dispatch_details where order_id=?1 and product_id=?2", nativeQuery = true)
	List<OrderDispatchDetails> findAllByOrderIdAndProductId(String orderId, Long productId);

	@Query(value = "SELECT * FROM dispatch_details where order_prod_map_id=?1", nativeQuery = true)
	List<OrderDispatchDetails> findAllByOrderProdMapId(Long id);

}
