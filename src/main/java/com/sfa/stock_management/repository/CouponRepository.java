package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	@Query(value="SELECT * FROM coupon where coupon_code=?1 and is_active=true",nativeQuery = true)
	Optional<Coupon> findByCouponCode(String couponCode);

	@Query(value="SELECT * FROM coupon where company_id=?1 order by id desc",nativeQuery = true)
	List<Coupon> findAllByCompanyId(Long companyId);
	
	@Query(value="SELECT * FROM coupon where coupon_code=?1",nativeQuery = true)
	Optional<Coupon> getCouponDataByCouponCode(String couponCode);

	@Query(value="SELECT * FROM coupon where coupon_code=?1",nativeQuery = true)
	Optional<Coupon> findByCouponCodeForStatuschange(String couponCode);

	@Query(value = "SELECT * FROM coupon where expiry_date<?1",nativeQuery = true)
	List<Coupon> findAllExpiredCoupon(String date);

	@Query(value = "SELECT * FROM coupon where min_order_value<?1 and is_active=true",nativeQuery = true)
	List<Coupon> findAllCouponUnderPrice(Double price);

}
