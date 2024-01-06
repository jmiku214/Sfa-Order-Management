package com.sfa.stock_management.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.repository.FirmRepository;
import com.sfa.stock_management.service.FirmService;

@Service
public class FirmServiceImpl implements FirmService {

	@Autowired
	private FirmRepository firmRepository;

	@Override
	public Response<?> saveFirm(Firm firm) {
		if(firm.getFirmName().toUpperCase().equals("OTHER") || firm.getFirmName().toUpperCase().equals("OTHERS")) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Firm already exist with provided name.", null);
		}
		else {
			Firm firmObj = firmRepository.findByFirmNameAndCompanyId(firm.getFirmName().trim(), firm.getCompanyId());
			//Optional<Firm> firmObjByPanandGst = firmRepository.findByPanAndGstNo(firm.getPanNumber(), firm.getGstNumber());
			List<Firm> firmObjByGst = firmRepository.findByGstNo(firm.getGstNumber());

			if (firmObj != null) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Firm already exist with provided name.", null);
			} 
			
		    for (Firm existingGst : firmObjByGst) {

			if (!existingGst.getPanNumber().equals(firm.getPanNumber())) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), " GSTIN no is already linked to a PAN no.", null);
			}
		    }
			//else {
				firm.setIsActive(true);
				firm.setCreatedAt(new Date());
				
				firmRepository.save(firm);
				return new Response<>(HttpStatus.OK.value(), "Firm created successfully.", null);
			//}
		}
		
	}

	@Override
	public Response<?> updateFirm(Firm firm) {
		List<Firm> firmObjByGst = firmRepository.findByGstNo(firm.getGstNumber());
		for (Firm existingGst : firmObjByGst) {

			if (!existingGst.getPanNumber().equals(firm.getPanNumber())) {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), " GSTIN no is already linked to a PAN no.", null);
			}
		    }
		firm.setIsActive(true);
		firmRepository.save(firm);
		return new Response<>(HttpStatus.OK.value(), "Firm data updated successfully.", null);
	}

	@Override
	public Response<?> getAllFirmByCompanyId(Long companyId) {
		List<Firm> firmList = firmRepository.findAllByCompanyId(companyId);
		if(firmList!=null && firmList.size()>0) {
			Firm firm=new Firm();
			firm.setId(-1L);
			firm.setFirmName("Other");
			firmList.add(firm);
			return new Response<>(HttpStatus.OK.value(),"Firm List.",firmList);
		}
		else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Firm List.", new ArrayList<>());
		}
		
	}

	@Override
	public Response<?> getFirmById(Long firmId) {
		Optional<Firm> firm = firmRepository.findById(firmId);
		if (firm != null && firm.isPresent()) {
			return new Response<>(HttpStatus.OK.value(), "Firm data.", firm.get());
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Not found.", null);
		}

	}

}
