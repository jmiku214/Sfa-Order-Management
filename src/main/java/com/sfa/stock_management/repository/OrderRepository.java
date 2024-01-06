package com.sfa.stock_management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.util.DateUtil;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "select * from orders where created_by=?1 order by id desc", nativeQuery = true)
	List<Order> findAllByUserId(Long userId);

	List<Order> findAllByOrderId(String orderId);

	List<Order> findAllByClientId(Long clientId);

	Optional<Order> findByOrderId(String orderId);

	@Query(value = "select * from orders where client_id IN (:userIds) order by id desc", nativeQuery = true)
	List<Order> findAllByUserIdIn(List<Long> userIds);

//	@Query(value = "select * from orders where created_by IN (:userIds) order by id desc",nativeQuery = true)
	Page<Order> findAll(Specification<Order> search, Pageable page);

	default Page<Order> findAll(List<Long> userIds, SearchDto searchDto, Pageable page) {
		return findAll(search(userIds, searchDto), page);

	}

	static Specification<Order> search(List<Long> userIds, SearchDto searchDto) {
		return (root, cq, cb) -> {
			Predicate p1 = cb.conjunction();
			if (userIds != null) {
				p1.getExpressions().add(root.get("createdBy").in(userIds));
			}
			if (searchDto.getCreatedUserName() != null) {
				p1.getExpressions()
						.add(cb.or(cb.like(root.get("createdUserName"), "%" + searchDto.getCreatedUserName() + "%")));
			}
			if (searchDto.getOrderStatus() != null && searchDto.getOrderStatus().getId() != null) {
				p1.getExpressions().add(cb.equal(root.get("status"), searchDto.getOrderStatus()));
			}
			if (searchDto.getClientName() != null) {
				p1.getExpressions().add(cb.or(cb.like(root.get("clientName"), "%" + searchDto.getClientName() + "%")));
			}
			if (searchDto.getOrderId() != null) {
				p1.getExpressions().add(cb.or(cb.equal(root.get("orderId"), searchDto.getOrderId())));
			}
			if (searchDto.getFromDate() != null && searchDto.getToDate() != null) {
				Date fromDate = DateUtil.convertISTtoUTC(searchDto.getFromDate());
				Date toDate = DateUtil.convertISTtoUTC(searchDto.getToDate());
				p1.getExpressions().add(cb.or(cb.between(root.get("createdAt"), fromDate, toDate)));
			}
			if(searchDto.getFirmId()!=null) {
				if(searchDto.getFirmId().getId()>0) {
					p1.getExpressions().add(cb.equal(root.get("firm"), searchDto.getFirmId()));
				}
				else {
					 p1.getExpressions().add(cb.isNull(root.get("firm")));
				}
			}
			if(searchDto.getCategoryId()!=null) {
				if(searchDto.getFirmId().getId()>0) {
					p1.getExpressions().add(cb.equal(root.get("productCatagory"), searchDto.getCategoryId()));
				}
				else {
					 p1.getExpressions().add(cb.isNull(root.get("productCatagory")));
				}
			}
			if(searchDto.getLocalityId()!=null) {
				p1.getExpressions().add(cb.or(cb.equal(root.get("localityId"), searchDto.getLocalityId())));
			}
			cq.orderBy(cb.desc(root.get("id")));
			return p1;

		};

	}

	@Query(value = "select * from orders where order_id IN (:orderIds)", nativeQuery = true)
	List<Order> findAllByOrderIdIn(Set<String> orderIds);

	List<Order> findAllByClientName(String clientName);

}
