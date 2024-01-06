package com.sfa.stock_management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.ClientBalanceDetails;
import com.sfa.stock_management.util.DateUtil;

@Repository
public interface ClientBalanceDetailsRepository extends JpaRepository<ClientBalanceDetails, Long> {

	@Query(value = "select * from client_balance_details where client_id in ?1 order by last_transaction_date desc",nativeQuery = true)
	List<ClientBalanceDetails> findAllByClientIdIn(List<Long> clientIds);

	Optional<ClientBalanceDetails> findByClientId(Long clientId);

	
	static Specification<ClientBalanceDetails> search(List<Long> clientIds,String clientName,Date fromDate,Date toDate) {
		return (root, cq, cb) -> {
			Predicate p1 = cb.conjunction();
			if (clientIds != null) {
//			  for(Long id:clientIds) {
//				  p1.getExpressions().add(cb.equal(root.get("clientId"), id));
//			  }
//			  
			  p1.getExpressions().add(root.get("clientId").in(clientIds));
					
				
				
			}
			
			if(clientName!=null && clientName!="" && !clientName.isEmpty()) {
				p1.getExpressions().add(cb.equal(root.get("clientName"), clientName));
			}
			if(fromDate!=null && toDate!=null) {
				
				Date fromDate1=DateUtil.convertISTtoUTC(fromDate);
				Date toDate1=DateUtil.convertISTtoUTC(toDate);
				p1.getExpressions().add(cb.or(cb.between(root.get("lastTransactionDate"),fromDate1,toDate1)));
			}

//			cq.orderBy(cb.desc(root.get("id")));
			cq.orderBy(cb.asc(root.get("clientName")));
			return p1;

		};

	}

	Page<ClientBalanceDetails> findAll(Specification<ClientBalanceDetails> search, Pageable page);

	default Page<ClientBalanceDetails> findAll(List<Long> clientIds, Pageable page,String clientName,Date fromDate,Date toDate) {
		return findAll(search(clientIds,clientName,fromDate,toDate), page);

	}
}
