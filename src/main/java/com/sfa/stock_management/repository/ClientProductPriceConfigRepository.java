package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.ClientProductPriceConfig;

@Repository
public interface ClientProductPriceConfigRepository extends JpaRepository<ClientProductPriceConfig, Long> {

//	@Query(value = "select * from client_product_price_configuration where product_id=?1", nativeQuery = true)
//	Optional<ClientProductPriceConfig> findByProductId(Long id);

	@Query(value = "select * from client_product_price_configuration where product_id=?1 and client_id=?2", nativeQuery = true)
	Optional<ClientProductPriceConfig> findByProductIdAndClientId(Long productId, Long clientId);

	List<ClientProductPriceConfig> findAllByClientId(Long clientId);

}
