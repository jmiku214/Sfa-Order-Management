package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.UnitConversionMapping;

@Repository
public interface UnitConversionMappingRepository extends JpaRepository<UnitConversionMapping, Long> {

	@Query(value = "SELECT * FROM unit_conversion_mapping where unit_id=?1 and sub_unit_name=?2",nativeQuery = true)
	Optional<UnitConversionMapping> findByUnitIdAndUnitConversionName(Long unitId, String subUnitName);

	@Query(value = "SELECT * FROM unit_conversion_mapping where unit_id=?1 and is_active=1 order by id desc",nativeQuery = true)
	List<UnitConversionMapping> findAllByUnitId(Long unitId);

}
