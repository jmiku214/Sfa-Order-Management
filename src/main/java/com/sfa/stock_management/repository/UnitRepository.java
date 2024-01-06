package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

	List<Unit> findByCompanyIdAndIsActiveTrue(Long companyId);

	List<Unit> findByCompanyIdIsNullAndIsActiveTrue();

	@Query(value = "select * from unit where name=?1 and company_id=?2 order by id desc", nativeQuery = true)
	Unit findAllByUnitNameAndCompanyId(String unitName, Long companyId);

}
