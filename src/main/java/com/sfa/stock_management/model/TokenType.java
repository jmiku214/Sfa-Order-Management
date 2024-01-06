package com.sfa.stock_management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "token_type")
public class TokenType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "token_type")
	private String tokenType;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "created_by")
	private Integer createdBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public TokenType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenType(Long id, String tokenType, Long companyId, Date createdAt, Integer createdBy) {
		super();
		this.id = id;
		this.tokenType = tokenType;
		this.companyId = companyId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	public TokenType(Long id) {
		super();
		this.id = id;
	}

}
