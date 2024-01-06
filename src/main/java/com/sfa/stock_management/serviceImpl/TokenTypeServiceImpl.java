package com.sfa.stock_management.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.TokenType;
import com.sfa.stock_management.repository.TokenTypeRepository;
import com.sfa.stock_management.service.TokenTypeService;

@Service
public class TokenTypeServiceImpl implements TokenTypeService {

	@Autowired
	private TokenTypeRepository tokenTypeRepository;
	
	@Override
	public Response<?> getAllTokenType(Long companyId) {
		List<TokenType> tokenTypeList=tokenTypeRepository.findAllByCompanyId(companyId);
		return new Response<>(HttpStatus.OK.value(),"Token Number Type List.",tokenTypeList);
	}

}
