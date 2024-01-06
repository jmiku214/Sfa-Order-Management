package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "select * from transaction where product_id=?1 order by created_at asc",nativeQuery = true)
	List<Transaction> findAllByProductId(Long id);

	
	static Specification<Transaction> search(Long productId) {
		return (root, cq, cb) -> {
			Predicate p1 = cb.conjunction();
			if (productId != null) {
				p1.getExpressions().add(cb.equal(root.get("productId"), productId));
			}

			cq.orderBy(cb.desc(root.get("id")));
			return p1;

		};

	}

	Page<Transaction> findAll(Specification<Transaction> search, Pageable page);

	default Page<Transaction> findAll(Long productId, Pageable page) {
		return findAll(search(productId), page);

	}


	@Query(value = "select * from transaction where token_type=?1 and token_number=?2",nativeQuery = true)
	Transaction findByTokenNumber(Long tokenTypeNumber, String tokenNumber);


	@Query(value="SELECT * FROM transaction where product_id=?1 and columns_object_data like %?2%",nativeQuery = true)
	Optional<Transaction> findByProductIdAndColumnDataLike(Long id, String objectValueName);


	Optional<Transaction> findByOrderId(String orderId);


	@Query(value="SELECT * FROM transaction where order_id=?1 and product_id=?2 and is_sample=?3",nativeQuery = true)
	Optional<Transaction> findByOrderIdAndProductId(String orderId, Long productId, Boolean isSample);

	@Query(value="SELECT * FROM transaction where order_id=?1 and product_id=?2 and is_sample=?3 and columns_ordered_data is not null",nativeQuery = true)
	Optional<Transaction> findByOrderIdAndProductIdV2(String orderId, Long id, Boolean isSample);
}
