package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Firm;

@Repository
public interface FirmRepository extends JpaRepository<Firm, Long> {

	Firm findByFirmNameAndCompanyId(String trim, Long companyId);

	@Query(value = "select * from firm where company_id=?1 order by id desc",nativeQuery = true)
	List<Firm> findAllByCompanyId(Long companyId);

	@Query(value = "select * from firm where id=?1",nativeQuery = true)
	Optional<Firm> findByFirmId(Long firmId);

	@Query(value = "select * from firm where pan_number=?1 and gst_number=?2",nativeQuery = true)
	Optional<Firm>  findByPanAndGstNo(String panNumber, String gstNumber);

	@Query(value = "select * from firm where gst_number=?1",nativeQuery = true)
	List<Firm> findByGstNo(String panNumber);

}
