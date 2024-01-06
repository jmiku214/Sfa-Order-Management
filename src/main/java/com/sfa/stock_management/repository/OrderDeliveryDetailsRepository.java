package com.sfa.stock_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.OrderDeliveryDetails;

@Repository
public interface OrderDeliveryDetailsRepository extends JpaRepository<OrderDeliveryDetails, Long> {

	Optional<OrderDeliveryDetails> findByOrderId(String orderId);

}
