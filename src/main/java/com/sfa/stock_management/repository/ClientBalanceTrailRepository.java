package com.sfa.stock_management.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.ClientBalanceTrail;
import com.sfa.stock_management.util.DateUtil;

@Repository
public interface ClientBalanceTrailRepository extends JpaRepository<ClientBalanceTrail, Long> {

	static Specification<ClientBalanceTrail> search(Long clientId, Date fromDate, Date toDate, String searchKey) {
		return (root, cq, cb) -> {
			Predicate p1 = cb.conjunction();
			if (clientId != null) {
				p1.getExpressions().add(cb.equal(root.get("clientId"), clientId));
			}
			if (fromDate != null && toDate != null) {

				Date fromDate1 = DateUtil.convertISTtoUTC(fromDate);
				Date toDate1 = DateUtil.convertISTtoUTC(toDate);
				p1.getExpressions().add(cb.or(cb.between(root.get("createdDate"), fromDate1, toDate1)));
			}
			if(fromDate==null && toDate!=null) {
				Date toDate1 = DateUtil.convertISTtoUTC(toDate);
				p1.getExpressions().add(cb.lessThanOrEqualTo(root.get("createdDate"), toDate1));
			}
			if(fromDate!=null && toDate==null) {
				Date fromDate1 = DateUtil.convertISTtoUTC(fromDate);
				p1.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate1));
			}
			if(searchKey!=null || searchKey!="") {
				p1.getExpressions()
				.add(cb.or(cb.like(root.get("transactionId"), "%" + searchKey + "%"),cb.like(root.get("transactionDetails"), "%" + searchKey + "%")));
			}
			cq.orderBy(cb.desc(root.get("id")));
			return p1;

		};

	}

	Page<ClientBalanceTrail> findAll(Specification<ClientBalanceTrail> search, Pageable page);

	default Page<ClientBalanceTrail> findAll(Long clientId, Pageable pageRequest, Date fromDate, Date toDate, String searchKey) {
		return findAll(search(clientId, fromDate, toDate,searchKey), pageRequest);
	}

	List<ClientBalanceTrail> findAllByClientId(Long clientId);

	@Query(value = "SELECT * FROM client_balance_trail where client_id=?1 and transaction_details=?2",nativeQuery = true)
	List<ClientBalanceTrail> findAllByClientIdAndOrderId(Long clientId, String orderId);

}
