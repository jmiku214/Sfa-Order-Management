package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.OrderTrail;

@Repository
public interface OrderTrailRepository extends JpaRepository<OrderTrail, Long> {

	List<OrderTrail> findAllByOrderId(String orderId);

}
