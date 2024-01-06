package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.CompanyUnitConfig;

@Repository
public interface CompanyUnitConfigRepository extends JpaRepository<CompanyUnitConfig, Long> {

	List<CompanyUnitConfig> findAllByCompanyIdAndIsActiveTrue(Long companyId);

}
