package com.sfa.stock_management.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.UnitResponseDto;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.UnitConversionService;

@Service
public class UnitConversionServiceImpl implements UnitConversionService {

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Override
	public Response<?> saveSubUnit(UnitConversionMapping unitConversionMapping) {
		Optional<UnitConversionMapping> unitConversionMapData = unitConversionMappingRepository
				.findByUnitIdAndUnitConversionName(unitConversionMapping.getUnit().getId(),
						unitConversionMapping.getSubUnitName());
		if (unitConversionMapData != null && unitConversionMapData.isPresent()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Sub unit already existed for the provided unit.",
					null);
		} else {
			unitConversionMapping.setCreatedAt(new Date());
			unitConversionMapping.setIsActive(true);
			unitConversionMappingRepository.save(unitConversionMapping);
			return new Response<>(HttpStatus.OK.value(), "Sub unit added successfully.", null);
		}
	}

	@Override
	public Response<?> getAllSubunitByUnitId(Long unitId) {
		List<UnitConversionMapping> unitConversionMapping = unitConversionMappingRepository.findAllByUnitId(unitId);
		return new Response<>(HttpStatus.OK.value(), "Sub unit List.", unitConversionMapping);
	}

	@Override
	public Response<?> getAllSubunitById(Long subunitId) {
		Optional<UnitConversionMapping> subUnitData = unitConversionMappingRepository.findById(subunitId);
		if (subUnitData != null && subUnitData.isPresent()) {
			return new Response<>(HttpStatus.OK.value(), "Sub unit data.", subUnitData.get());
		} else {
			return new Response<>(HttpStatus.NOT_FOUND.value(), "No data found.", null);
		}
	}

	@Override
	public Response<?> updateSubunitData(Long subUnitId, UnitConversionMapping unitConversionMapping) {
		Optional<UnitConversionMapping> subUnitData = unitConversionMappingRepository.findById(subUnitId);
		if (subUnitData != null && subUnitData.isPresent()) {
			subUnitData.get().setSubUnitQty(unitConversionMapping.getSubUnitQty());
//			subUnitData.get().setSubUnitName(unitConversionMapping.getSubUnitName());
			unitConversionMappingRepository.save(subUnitData.get());
			return new Response<>(HttpStatus.OK.value(), "Subunit data updated successfully.", null);
		} else {
			return new Response<>(HttpStatus.NOT_FOUND.value(), "No data found.", null);
		}
	}

	@Override
	public Response<?> updateSubUnitStatus(Long subUnitId) {
		Optional<UnitConversionMapping> subUnitData = unitConversionMappingRepository.findById(subUnitId);
		if (subUnitData != null && subUnitData.isPresent()) {
			if (subUnitData.get().getIsActive() != null && subUnitData.get().getIsActive().booleanValue() == true) {
				subUnitData.get().setIsActive(false);
			} else {
				subUnitData.get().setIsActive(true);
			}
			unitConversionMappingRepository.save(subUnitData.get());
			return new Response<>(HttpStatus.OK.value(), "Status updated successfully.", null);
		} else {
			return new Response<>(HttpStatus.NOT_FOUND.value(), "No data found.", null);
		}
	}

	@Override
	public Response<?> getAllUnitByProductId(Long productId) {
		Optional<Product> product = productRepository.findById(productId);
		if (product != null && product.isPresent()) {
			List<UnitResponseDto> unitResponseDto = new ArrayList<>();
			Optional<Unit> unitObj = unitRepository.findById(product.get().getUnit());
			UnitResponseDto dto = new UnitResponseDto();
			dto.setId(unitObj.get().getId());
			dto.setUnitName(unitObj.get().getName());
			dto.setQuantity(1L);
			dto.setIsDefault(true);
			unitResponseDto.add(dto);
			List<UnitConversionMapping> unitConversionData = unitConversionMappingRepository
					.findAllByUnitId(product.get().getUnit());
			if (unitConversionData != null && unitConversionData.size() > 0) {
				for (UnitConversionMapping map : unitConversionData) {
					UnitResponseDto dtos = new UnitResponseDto();
					dtos.setId(map.getId());
					dtos.setUnitName(map.getSubUnitName());
					dtos.setQuantity(map.getSubUnitQty());
					dtos.setIsDefault(false);
					unitResponseDto.add(dtos);
				}
			}
			return new Response<>(HttpStatus.OK.value(), "Unit List.", unitResponseDto);
		} else {
			return new Response<>(HttpStatus.NOT_FOUND.value(), "No data found.", null);
		}
	}
}
