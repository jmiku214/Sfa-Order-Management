package com.sfa.stock_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.HsnGstMapping;

@Repository
public interface HsnGstMappingRepository extends JpaRepository<HsnGstMapping, Long> {

	Optional<HsnGstMapping> findByHsnCode(String hsnCode);

}
