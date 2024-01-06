package com.sfa.stock_management.serviceImpl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.GstResponseDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.ClientProductPriceConfig;
import com.sfa.stock_management.model.HsnGstMapping;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.ClientProductPriceConfigRepository;
import com.sfa.stock_management.repository.HsnGstMappingRepository;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.ClientProductPriceConfigService;

@Service
public class ClientProductPriceConfigServiceImpl implements ClientProductPriceConfigService {

	@Autowired
	private ClientProductPriceConfigRepository productPriceConfigRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private HsnGstMappingRepository hsnGstMappingRepository;

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Override
	public Response<?> saveClientProductPrice(ClientProductPriceConfig clientProductPriceConfig) {
		Optional<ClientProductPriceConfig> clientProductExistData = productPriceConfigRepository
				.findByProductIdAndClientId(clientProductPriceConfig.getProductId().getId(),
						clientProductPriceConfig.getClientId());
		if (clientProductExistData != null && clientProductExistData.isPresent()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(),
					"Product price is already exist for the provided client..", null);
		} else {
			Optional<Product> prodObj = productRepository.findById(clientProductPriceConfig.getProductId().getId());
			if (prodObj.get().getMinSellingPrice() != null) {
				if (prodObj != null && prodObj.get().getMinSellingPrice() != null
						&& prodObj.get().getMinSellingPrice() <= clientProductPriceConfig.getPrice()) {
					clientProductPriceConfig.setCreatedAt(new Date());
					ClientProductPriceConfig configObj = productPriceConfigRepository.save(clientProductPriceConfig);
					return new Response<>(HttpStatus.CREATED.value(), "Price set successfully", configObj);
				} else {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"You are not able to set price below " + prodObj.get().getMinSellingPrice(), null);
				}
			} else {
				clientProductPriceConfig.setCreatedAt(new Date());
				ClientProductPriceConfig configObj = productPriceConfigRepository.save(clientProductPriceConfig);
				return new Response<>(HttpStatus.CREATED.value(), "Price set successfully", configObj);
			}

		}

	}

	@Override
	public Double getClientWiseProductPrice(Long productId, Long clientId) {
		Double price = 0D;
		Optional<ClientProductPriceConfig> clientPriceFromConfigTable = productPriceConfigRepository
				.findByProductIdAndClientId(productId, clientId);
		if (clientPriceFromConfigTable != null && clientPriceFromConfigTable.isPresent()) {
			price = clientPriceFromConfigTable.get().getPrice();
		} else {
			Optional<Product> productData = productRepository.findById(productId);
			if (productData != null && productData.isPresent()) {
				price = productData.get().getStandardSellingPrice();
			}
		}
		return price;
	}

	@Override
	public Response<?> updateClientWiseProductPrice(ClientProductPriceConfig clientProductPriceConfig) {

		Optional<ClientProductPriceConfig> clientData = productPriceConfigRepository.findByProductIdAndClientId(
				clientProductPriceConfig.getProductId().getId(), clientProductPriceConfig.getClientId());
		Optional<Product> productObj = productRepository.findById(clientProductPriceConfig.getProductId().getId());
		if (clientData != null && clientData.isPresent()) {
			if (productObj.get().getMinSellingPrice() != null
					&& clientProductPriceConfig.getPrice() >= productObj.get().getMinSellingPrice()) {
				clientData.get().setPrice(clientProductPriceConfig.getPrice());
				clientData.get().setUpdateAt(new Date());
				ClientProductPriceConfig configObj = productPriceConfigRepository.save(clientData.get());
				return new Response<>(HttpStatus.CREATED.value(), "Client price updated succesfully", configObj);
			} else if (productObj.get().getMinSellingPrice() == null) {
				clientData.get().setPrice(clientProductPriceConfig.getPrice());
				clientData.get().setUpdateAt(new Date());
				ClientProductPriceConfig configObj = productPriceConfigRepository.save(clientData.get());
				return new Response<>(HttpStatus.CREATED.value(), "Client price updated succesfully", configObj);
			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(),
						"You are not able to set price below " + productObj.get().getMinSellingPrice(), null);
			}

		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No Data Available", null);
		}

	}

	@Override
	public Response<?> getAllClientProductsPrice(Long clientId) {
		List<ClientProductPriceConfig> clientProductListData = productPriceConfigRepository.findAllByClientId(clientId);
		return new Response<>(HttpStatus.OK.value(), "OK", clientProductListData);
	}

	@Override
	public Response<?> deleteClientWiseProductPrice(ClientProductPriceConfig clientProductPriceConfig) {
		Optional<ClientProductPriceConfig> clientData = productPriceConfigRepository.findByProductIdAndClientId(
				clientProductPriceConfig.getProductId().getId(), clientProductPriceConfig.getClientId());
		if (clientData != null && clientData.isPresent()) {
			productPriceConfigRepository.delete(clientData.get());
			return new Response<>(HttpStatus.OK.value(), "Client data deleted successfully!!", null);
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No Data Found!!", null);
		}
	}

	@Override
	public Response<?> getClientWiseProductPriceV2(Long productId, Long clientId, Long clientStateId, Long userStateId,
			Integer totalQuantity, Boolean isSample) {
		Double totalPrice = 0D;
		DecimalFormat df = new DecimalFormat("#.##");
		Optional<ClientProductPriceConfig> clientPriceFromConfigTable = productPriceConfigRepository
				.findByProductIdAndClientId(productId, clientId);
		GstResponseDto responseDto = new GstResponseDto();
		if (clientPriceFromConfigTable != null && clientPriceFromConfigTable.isPresent()) {
			totalPrice = clientPriceFromConfigTable.get().getPrice();
			responseDto.setPerUnitPrice(totalPrice);
		} else {
			Optional<Product> productData = productRepository.findById(productId);
			if (productData != null && productData.isPresent()) {
				totalPrice = productData.get().getStandardSellingPrice();
				responseDto.setPerUnitPrice(totalPrice);
			}
		}
		if (isSample.booleanValue() == true) {
			totalPrice = 0D;
			responseDto.setIsSample(true);
			responseDto.setPerUnitPrice(0D);
		} else {
			responseDto.setIsSample(false);
		}
		responseDto.setOriginalPrice(totalPrice);
		totalPrice = totalPrice * totalQuantity;
		responseDto.setTotalPriceWithoutGst(totalPrice);
		Long gstPercent = 0L;
		Long cgstPercent = 0L;
		Long igstPercent = 0L;
		Long sgstPercent = 0L;
		Optional<Product> productObj = productRepository.findById(productId);
		Optional<HsnGstMapping> hsnGstData = hsnGstMappingRepository.findByHsnCode(productObj.get().getHsnCode());
		if (hsnGstData != null && hsnGstData.isPresent()) {
			gstPercent = Long.parseLong(hsnGstData.get().getGstPercentage().toString());
		} else {
			gstPercent = 16L;
		}
		if (clientStateId == userStateId) {
			cgstPercent = gstPercent / 2;
			sgstPercent = gstPercent / 2;
		}
		if (clientStateId != userStateId) {
			igstPercent = gstPercent;
		}
		if (cgstPercent != 0) {
			Double result = 0D;
			result = (cgstPercent / 100D) * totalPrice;
			responseDto.setCgstPrice(Double.parseDouble(df.format(result)));
		}
		if (sgstPercent != 0) {
			Double result = 0D;
			result = (sgstPercent / 100D) * totalPrice;
			responseDto.setSgstPrice(Double.parseDouble(df.format(result)));
		}
		if (igstPercent != 0) {
			Double result = 0D;
			result = (igstPercent / 100D) * totalPrice;
			responseDto.setIgstPrice(Double.parseDouble(df.format(result)));
		}
		if (responseDto.getCgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getCgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getCgstPrice())));
			}

		}
		if (responseDto.getSgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getSgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getSgstPrice())));
			}
		}
		if (responseDto.getIgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getIgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getIgstPrice())));
			}
		}
		return new Response<>(HttpStatus.OK.value(), "Gst Percent Response.", responseDto);
	}

	@Override
	public Response<?> getClientWiseProductPriceV3(Long productId, Long clientId, Long clientStateId, Long userStateId,
			Integer totalQuantity, Boolean isSample, Long unitId) {

		Double totalPrice = 0D;
		Optional<Product> productData = productRepository.findById(productId);
		DecimalFormat df = new DecimalFormat("#.##");
		Optional<ClientProductPriceConfig> clientPriceFromConfigTable = productPriceConfigRepository
				.findByProductIdAndClientId(productId, clientId);
		GstResponseDto responseDto = new GstResponseDto();
		if (clientPriceFromConfigTable != null && clientPriceFromConfigTable.isPresent()) {
			totalPrice = clientPriceFromConfigTable.get().getPrice();
//			responseDto.setPerUnitPrice(totalPrice);
		} else {

			if (productData != null && productData.isPresent()) {
				totalPrice = productData.get().getStandardSellingPrice();
//				responseDto.setPerUnitPrice(totalPrice);
			}
		}
		if (isSample.booleanValue() == true) {
			totalPrice = 0D;
			responseDto.setIsSample(true);
			responseDto.setPerUnitPrice(0D);
		} else {
			responseDto.setIsSample(false);
		}
		Optional<Unit> unit = unitRepository.findById(productData.get().getUnit());
		responseDto.setOriginalPrice(totalPrice);
		if (unitId == null || productData.get().getUnit() == unitId) {
			responseDto.setPerUnitPrice(totalPrice);
			responseDto.setNoOfUnits(totalQuantity.doubleValue());
			responseDto.setUnitToBeShowOnWeb(totalQuantity);
			responseDto.setPerUnitName("Per " + unit.get().getName());
			totalPrice = totalPrice * totalQuantity;
		} else {
			Optional<UnitConversionMapping> unitConversionMapData = unitConversionMappingRepository.findById(unitId);
			totalPrice = totalPrice / unitConversionMapData.get().getSubUnitQty();
			responseDto.setPerUnitPrice(totalPrice);
//			Double perSubunitQty = 1 / unitConversionMapData.get().getSubUnitQty().doubleValue();
//			Double totalSubQty = perSubunitQty * totalQuantity;
//			responseDto.setNoOfUnits(totalSubQty);
			responseDto.setUnitToBeShowOnWeb(totalQuantity);
			responseDto.setPerUnitName("Per " + unit.get().getName());
			totalPrice = totalPrice * totalQuantity;
		}
//		DecimalFormat format=new DecimalFormat("#.00");
//		responseDto.setTotalPriceWithoutGst(Double.parseDouble(format.format(totalPrice)));
		responseDto.setTotalPriceWithoutGst(totalPrice);
		Long gstPercent = 0L;
		Long cgstPercent = 0L;
		Long igstPercent = 0L;
		Long sgstPercent = 0L;
		Optional<Product> productObj = productRepository.findById(productId);
		Optional<HsnGstMapping> hsnGstData = hsnGstMappingRepository.findByHsnCode(productObj.get().getHsnCode());
		if (hsnGstData != null && hsnGstData.isPresent()) {
			gstPercent = Long.parseLong(hsnGstData.get().getGstPercentage().toString());
		} else {
			gstPercent = 18L;
		}
		if (clientStateId == userStateId) {
			cgstPercent = gstPercent / 2;
			sgstPercent = gstPercent / 2;
		}
		if (clientStateId != userStateId) {
			igstPercent = gstPercent;
		}
		if (cgstPercent != 0) {
			Double result = 0D;
			result = (cgstPercent / 100D) * totalPrice;
			responseDto.setCgstPrice(Double.parseDouble(df.format(result)));
		}
		if (sgstPercent != 0) {
			Double result = 0D;
			result = (sgstPercent / 100D) * totalPrice;
			responseDto.setSgstPrice(Double.parseDouble(df.format(result)));
		}
		if (igstPercent != 0) {
			Double result = 0D;
			result = (igstPercent / 100D) * totalPrice;
			responseDto.setIgstPrice(Double.parseDouble(df.format(result)));
		}
		if (responseDto.getCgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getCgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getCgstPrice())));
			}

		}
		if (responseDto.getSgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getSgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getSgstPrice())));
			}
		}
		if (responseDto.getIgstPrice() != null) {
			if (responseDto.getTotalPriceAfterAddedGst() != null) {
				responseDto.setTotalPriceAfterAddedGst(Double
						.parseDouble(df.format(responseDto.getTotalPriceAfterAddedGst() + responseDto.getIgstPrice())));
			} else {
				responseDto.setTotalPriceAfterAddedGst(
						Double.parseDouble(df.format(totalPrice + responseDto.getIgstPrice())));
			}
		}
		return new Response<>(HttpStatus.OK.value(), "Gst Percent Response.", responseDto);

	}

}
