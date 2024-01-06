package com.sfa.stock_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.TokenType;

@Repository
public interface TokenTypeRepository extends JpaRepository<TokenType, Long> {

	List<TokenType> findAllByCompanyId(Long companyId);

}
