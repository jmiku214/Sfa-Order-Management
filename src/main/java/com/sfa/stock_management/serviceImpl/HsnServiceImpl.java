package com.sfa.stock_management.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.HsnGstMapping;
import com.sfa.stock_management.repository.HsnGstMappingRepository;
import com.sfa.stock_management.service.HsnService;

@Service
public class HsnServiceImpl implements HsnService {

	@Autowired
	private HsnGstMappingRepository hsnGstMappingRepository;

	@Override
	public Response<?> getAllHsn() {
		List<HsnGstMapping> hsnGstAll = hsnGstMappingRepository.findAll();
		return new Response<>(HttpStatus.OK.value(), "Hsn with gst list", hsnGstAll);
	}

	@Override
	public Response<?> saveHsnWiseGst(HsnGstMapping hsnGstMapping) {
		Optional<HsnGstMapping> hsnGstObj = hsnGstMappingRepository.findByHsnCode(hsnGstMapping.getHsnCode());
		if (hsnGstObj != null && hsnGstObj.isPresent()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Gst percent is already exist for the provided hsn",
					null);
		} else {
			hsnGstMappingRepository.save(hsnGstMapping);
			return new Response<>(HttpStatus.CREATED.value(), "Gst percent is saved", null);
		}
	}

	@Override
	public Response<?> updateHsnWiseGst(HsnGstMapping hsnGstMapping) {
		Optional<HsnGstMapping> hsnObj = hsnGstMappingRepository.findByHsnCode(hsnGstMapping.getHsnCode());
		if (hsnObj != null && hsnObj.isPresent()) {
			hsnObj.get().setGstPercentage(hsnGstMapping.getGstPercentage());
			hsnObj.get().setHsnCode(hsnGstMapping.getHsnCode());
			hsnGstMappingRepository.save(hsnObj.get());
			return new Response<>(HttpStatus.CREATED.value(), "Gst percent updated successfully", null);
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No value Present", null);
		}
	}

}
