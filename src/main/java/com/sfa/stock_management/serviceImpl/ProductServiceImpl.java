package com.sfa.stock_management.serviceImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sfa.stock_management.constant.ProductUnit;
import com.sfa.stock_management.dto.ProductDto;
import com.sfa.stock_management.dto.ProductResponseDto;
import com.sfa.stock_management.dto.ProductTemplateDataResponseForOrder;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.dto.TemplateHeaderRequest;
import com.sfa.stock_management.dto.TransactionResponseDto;
import com.sfa.stock_management.dto.UnitResponseDto;
import com.sfa.stock_management.model.CompanyUnitConfig;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.Transaction;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.CompanyUnitConfigRepository;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.TransactionRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UnitRepository unitRepository;
	@Autowired
	private CompanyUnitConfigRepository companyUnitConfigRepository;

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Override
	public Response<?> saveProduct(ProductResponseDto product) throws JsonProcessingException {
		List<Product> productObj = new ArrayList<>();
		if (product.getFirm() != null) {
			productObj = productRepository.findBySkuAndProductNameAndFirmAndCompanyId(product.getSku(),
					product.getName(), product.getFirm().getId(), product.getCompanyId());
		} else {
			productObj = productRepository.findBySkuAndProductNameAndCompanyID(product.getSku(), product.getName(),
					product.getCompanyId());
		}
		if (product.getSku() == null && product.getSku().isEmpty() && product.getHsnCode() == null
				&& product.getHsnCode().isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Product SKU/HSNCode can't be null", null);
		} else if (productObj != null && !productObj.isEmpty()) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Product is already exist with provided SKU/Name",
					null);
		} else {
			product.setCreatedAt(new Date());
			if (product.getIsDetails().booleanValue() == true) {
				product.setAvailableQuantity(0D);
				product.setPhysicalStockQuantity(0D);
				product.setIsDetails(true);
				product.setTotalNoOfProducts(0D);
			}
			if (product.getIsDetails().booleanValue() == false) {
				product.setAvailableQuantity(product.getOpeningQuantity().doubleValue());
				product.setPhysicalStockQuantity(product.getOpeningQuantity().doubleValue());
				product.setIsDetails(false);
				product.setTotalNoOfProducts(product.getOpeningQuantity().doubleValue());
			}
			product.setAvailableQuantity(product.getOpeningQuantity().doubleValue());
			product.setPhysicalStockQuantity(product.getOpeningQuantity().doubleValue());
			Product prodObj = product.convertToEntityV2();
			Boolean isUniqueColumnExist = false;
			if (product.getTemplateHeaderDetails() != null && product.getTemplateHeaderDetails().size() > 0) {
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonString;
				jsonString = objectMapper.writeValueAsString(product.getTemplateHeaderDetails());
				prodObj.setProductColumnDetails(jsonString);
				TemplateHeaderRequest[] responseArray = objectMapper.readValue(prodObj.getProductColumnDetails(),
						TemplateHeaderRequest[].class);
				for (TemplateHeaderRequest req : responseArray) {
					if (req.getIsUnique().booleanValue() == true) {
						isUniqueColumnExist = true;
					}
				}
				if (isUniqueColumnExist) {
					Product products = productRepository.save(prodObj);
					if (product.getIsDetails().booleanValue() == false) {
						Transaction transaction = new Transaction();
						transaction.setCreatedAt(new Date());
						transaction.setProductId(products.getId());
						transaction.setProcuredQuantity(product.getAvailableQuantity());
						if (product.getCreatedBy() != null) {
							transaction.setCreatedBy(product.getCreatedBy());
						}
						transactionRepository.save(transaction);
					}

					return new Response<>(HttpStatus.CREATED.value(), "Product Added Succesfully", products);

				} else {
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please set atleast one column as unique.",
							null);
				}
			}

			else {
				Product products = productRepository.save(prodObj);
				Transaction transaction = new Transaction();
				transaction.setCreatedAt(new Date());
				transaction.setProductId(products.getId());
				transaction.setProcuredQuantity(product.getAvailableQuantity());
				if (product.getCreatedBy() != null) {
					transaction.setCreatedBy(product.getCreatedBy());
				}
				if (prodObj.getMaxNoOfSamples() != null && prodObj.getMaxNoOfSamples() != 0) {
					transaction.setIsSample(true);
				}
				transactionRepository.save(transaction);
				return new Response<>(HttpStatus.CREATED.value(), "Product Added Succesfully", products);
			}

		}

	}

	@Override
	public Response<?> getAllProducts(Long companyId, Long categoryId, Long firmId)
			throws JsonMappingException, JsonProcessingException {
		List<Product> productList = new ArrayList<>();
		if (categoryId == null && firmId == null) {

			productList = productRepository.findAllByCompanyId(companyId);
		}
		if (categoryId != null && companyId != null && firmId == null) {
			productList = productRepository.findAllByCompanyIdAndCatagoryId(companyId, categoryId);
		}
		if (categoryId == null && companyId != null && firmId != null) {
			if (firmId > 0) {
				productList = productRepository.findAllByCompanyIdAndFirmId(companyId, firmId);
			} else {
				productList = productRepository.findAllByCompanyIdAndFirmIdNull(companyId, firmId);
			}
		}
		if (companyId != null && firmId != null && categoryId != null) {
			productList = productRepository.findAllByCompanyIdAndCatagoryIdAndFirmId(companyId, categoryId, firmId);
		}
		List<ProductDto> productDtos = new ArrayList<>();
		List<Unit> unitList = unitRepository.findAll();
		Map<Long, List<Unit>> unitMap = new HashMap<>();
		if (unitList != null && !unitList.isEmpty()) {
			unitMap = unitList.stream().filter(x -> x.getId() != null).collect(Collectors.groupingBy(Unit::getId));
		}
		DecimalFormat formatter=new DecimalFormat("#.##");
		if (productList != null && productList.size() > 0) {
			for (Product prod : productList) {
				ProductDto dto = new ProductDto();
				dto.setId(prod.getId());
				dto.setCurrentQuantity(Double.parseDouble(formatter.format(prod.getAvailableQuantity())));
				dto.setPhysicalAvailableQuantity(Double.parseDouble(formatter.format(prod.getPhysicalStockQuantity())));
				dto.setProductName(prod.getName());
				dto.setSkuCode(prod.getSku());
				dto.setHsnCode(prod.getHsnCode());
				dto.setMinReorderQuantity(prod.getMinReOrderQuantity());
				dto.setCategory(prod.getProductCategory());
				dto.setFirm(prod.getFirm());
				dto.setMaxNoOfSamples(prod.getMaxNoOfSamples());
				dto.setTotalNoOfProducts(prod.getTotalNoOfProducts());
				dto.setAllowOrderWithoutStock(prod.getAllowOrderWithoutStock());
				dto.setMinSellingPrice(prod.getMinSellingPrice());
				dto.setOpeningQuantity(prod.getOpeningQuantity());
				dto.setStandardSellingPrice(prod.getStandardSellingPrice());
				dto.setBlockStockOnOrderCreate(prod.getBlockStockOnOrderCreate());
				if (prod.getIsDetails() != null && prod.getIsDetails().booleanValue() == true) {
					Object ob = prod.getProductColumnDetails();
					ObjectMapper objectMapper = new ObjectMapper();
					String data = ob.toString();
					TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);
					List<TemplateHeaderRequest> request = new ArrayList<>();
					for (TemplateHeaderRequest req : responseArray) {
						TemplateHeaderRequest tmpHeadReq = new TemplateHeaderRequest();
						tmpHeadReq.setHeaderName(req.getHeaderName());
						tmpHeadReq.setIsMandetory(req.getIsMandetory());
						request.add(tmpHeadReq);
					}
					dto.setTemplateHeaderDetails(request);
				}
				dto.setCreatedBy(prod.getCreatedUserName());
				dto.setAllowOrderTillQuantity(prod.getAllowOrderTillQuantity());
				dto.setIsDetails(prod.getIsDetails());
				ArrayList<Object> listdata = new ArrayList<Object>();
				if (prod.getIsDetails() != null && prod.getIsDetails().booleanValue() == true) {
					List<Transaction> transactionList = transactionRepository.findAllByProductId(prod.getId());
					if (transactionList != null && transactionList.size() > 0) {
						for (Transaction trn : transactionList) {
							if (trn.getColumnsObjectData() != null && !trn.getColumnsObjectData().isEmpty()) {
								JSONArray array = new JSONArray(trn.getColumnsObjectData());
								for (Object ob : array) {
									JSONObject jsonObject = new JSONObject(ob.toString());
									if (jsonObject.has("In-Stock")) {
										if (jsonObject.getBoolean("In-Stock") == true) {
											listdata.add(ob.toString());
										}
									}

								}
							}
						}
					}
				}
				dto.setTemplateData(listdata);
//				if (prod.getUnit() == 1) {
//					dto.setUnitName(ProductUnit.BOX.getName());
//				} else {
//					dto.setUnitName(ProductUnit.PIECE.getName());
//				}

//				if (unitMap != null && !unitMap.isEmpty() && unitMap.containsKey(prod.getUnit())
//						&& unitMap.get(prod.getUnit()) != null) {
//					dto.setUnitName(unitMap.get(prod.getUnit()).get(0).getName());
//				} else {
//					dto.setUnitName(null);
//				}
				Optional<Unit> unit=unitRepository.findById(prod.getUnit());
				UnitResponseDto unitResponseDto=new UnitResponseDto();
				unitResponseDto.setId(unit.get().getId());
				unitResponseDto.setUnitName(unit.get().getName());
				dto.setUnit(unitResponseDto);
				dto.setUnitName(unit.get().getName());
				dto.setImageUrl(prod.getImageUrl());
				productDtos.add(dto);
			}
			return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.name(), productDtos);
		} else {
			return new Response<>(HttpStatus.OK.value(), "No value Present", productDtos);
		}

	}

	@Override
	public Response<?> getAllTransactions(String sku) {

		Optional<Product> product = productRepository.findBySku(sku);
		TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
		if (product != null && product.isPresent()) {
			ProductDto dto = new ProductDto();
			dto.setId(product.get().getId());
			dto.setCurrentQuantity(product.get().getAvailableQuantity());
			dto.setMinReorderQuantity(product.get().getMinReOrderQuantity());
			dto.setProductName(product.get().getName());
			dto.setSkuCode(product.get().getSku());
			dto.setHsnCode(product.get().getHsnCode());
			dto.setAllowOrderTillQuantity(product.get().getAllowOrderTillQuantity());
			dto.setStandardSellingPrice(product.get().getStandardSellingPrice());
			dto.setMinSellingPrice(product.get().getMinSellingPrice());
			dto.setPhysicalAvailableQuantity(product.get().getPhysicalStockQuantity());
			dto.setAllowOrderWithoutStock(product.get().getAllowOrderWithoutStock());
			if (product.get().getUnit() == 1) {
				dto.setUnitName(ProductUnit.BOX.getName());
			} else {
				dto.setUnitName(ProductUnit.PIECE.getName());
			}
			transactionResponseDto.setProductDto(dto);
			List<StockUpdateDto> stockUpdateDtos = new ArrayList<>();
			List<Transaction> transactions = transactionRepository.findAllByProductId(product.get().getId());
			if (transactions != null && !transactions.isEmpty()) {
				for (Transaction trn : transactions) {
					StockUpdateDto stockDto = new StockUpdateDto();
					if (trn.getProcuredQuantity() != null) {
						stockDto.setProcuredQuantity(trn.getProcuredQuantity());
					}
					if (trn.getDisbursedQuantity() != null) {
						stockDto.setDisbursedQuantity(trn.getDisbursedQuantity());
					}
					stockDto.setRemarks(trn.getRemarks());
					stockDto.setUpdateDate(trn.getCreatedAt());
					stockUpdateDtos.add(stockDto);
				}

			}
			transactionResponseDto.setStockUpdateDtos(stockUpdateDtos);
		}

		return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.name(), transactionResponseDto);
	}

	@Override
	public Response<?> updateProduct(ProductResponseDto product) {
		Optional<Product> productData = productRepository.findById(product.getId());
		if (productData != null) {
			Product prod = productData.get();
			prod.setMinReOrderQuantity(product.getMinReOrderQuantity());
			prod.setAllowOrderTillQuantity(product.getAllowOrderTillQuantity());
			prod.setBlockStockOnOrderCreate(product.getBlockStockOnOrderCreate());
			prod.setImageUrl(product.getImageUrl());
			prod.setMinSellingPrice(product.getMinSellingPrice());
			prod.setStandardSellingPrice(product.getStandardSellingPrice());
			prod.setMaxNoOfSamples(product.getMaxNoOfSamples());
			prod.setAllowOrderWithoutStock(product.getAllowOrderWithoutStock());
			productRepository.save(prod);
			return new Response<>(HttpStatus.CREATED.value(), "Product data updated successfully..", prod);
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No data found..", null);
		}

	}

	@Override
	public Response<?> getProductBySku(String sku, int pageNo, int pageSize, String searchKey, String activationKey,
			Long firmId, Long companyId) throws JsonMappingException, JsonProcessingException {
		Optional<Product> product = null;
		if (firmId == 0) {
			product = productRepository.findBySkuAndCompanyIdAndFirmNull(sku, companyId);
		} else {
			product = productRepository.findBySkuAndFirmIdAndCompanyId(sku, firmId, companyId);
		}
		ProductResponseDto dto = product.get().convertToDto();
		if (dto.getUnit() != null) {
			Optional<Unit> unitObj = unitRepository.findById(dto.getUnit());
			dto.setUnitEntity(unitObj.get());
		}
		dto.setImageUrl(product.get().getImageUrl());
		if (dto.getIsDetails() != null && dto.getIsDetails().booleanValue() == true) {
			Object ob = product.get().getProductColumnDetails();
			ObjectMapper objectMapper = new ObjectMapper();
			String data = ob.toString();
			TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);
			List<TemplateHeaderRequest> request = new ArrayList<>();
			for (TemplateHeaderRequest req : responseArray) {
				TemplateHeaderRequest tmpHeadReq = new TemplateHeaderRequest();
				tmpHeadReq.setHeaderName(req.getHeaderName());
				tmpHeadReq.setIsMandetory(req.getIsMandetory());
				tmpHeadReq.setIsUnique(req.getIsUnique());
				request.add(tmpHeadReq);
			}
			dto.setTemplateHeaderDetails(request);
			List<Transaction> transactionList = transactionRepository.findAllByProductId(product.get().getId());
			ArrayList<Object> listdata = new ArrayList<Object>();
			if (transactionList != null && transactionList.size() > 0) {
				for (Transaction trn : transactionList) {
					if (trn.getColumnsObjectData() != null && !trn.getColumnsObjectData().isEmpty()) {
						JSONArray array = new JSONArray(trn.getColumnsObjectData());
						for (Object object : array) {
							JSONObject jsonObject = new JSONObject(object.toString());
							if (jsonObject.has("In-Stock")) {
//								if (jsonObject.getBoolean("In-Stock") == true) {
								listdata.add(object.toString());
//								}
							}

						}
					}
				}
			}
			ArrayList<Object> updatedList = new ArrayList<>();
			if (searchKey != null) {
//				searchKey="\""+searchKey+"\"";

				for (int i = 0; i < listdata.size(); i++) {
					for (TemplateHeaderRequest header : request) {
						Object object = listdata.get(i);
						JSONObject jsnOb = new JSONObject(object.toString());
						if (jsnOb.has(header.getHeaderName())) {
							if (jsnOb.getString(header.getHeaderName()).toString().toUpperCase()
									.contains(searchKey.toUpperCase())) {
								updatedList.add(listdata.get(i));
								break;
							}
						}
					}
				}
			} else {
				updatedList = listdata;
			}
			int totalPages = 0;
			if (pageSize > 0 && updatedList.size() > pageSize) {
				totalPages = updatedList.size() / pageSize;
			}
			if (totalPages == 0) {
				if (activationKey != null) {
					ArrayList<Object> activationList = new ArrayList<>();
					if (activationKey.equals("ALL")) {
						dto.setTemplateData(updatedList);
						dto.setTotalNoOfElements(updatedList.size());
					} else if (activationKey.equals("ACTIVE")) {
						for (int i = 0; i < updatedList.size(); i++) {
							Object object = updatedList.get(i);
							JSONObject jsnOb = new JSONObject(object.toString());
							if (jsnOb.has("In-Stock")) {
								Boolean isStock = jsnOb.getBoolean("In-Stock");
								if (isStock) {
									activationList.add(object);
								}
							}
						}
						dto.setTemplateData(activationList);
						dto.setTotalNoOfElements(activationList.size());
					} else {
						for (int i = 0; i < updatedList.size(); i++) {
							Object object = updatedList.get(i);
							JSONObject jsnOb = new JSONObject(object.toString());
							if (jsnOb.has("In-Stock")) {
								Boolean isStock = jsnOb.getBoolean("In-Stock");
								if (!isStock) {
									activationList.add(object);
								}
							}
						}
						dto.setTemplateData(activationList);
						dto.setTotalNoOfElements(listdata.size());
					}
				}

			} else {
				ArrayList<Object> newList = new ArrayList<>();
				ArrayList<Object> updatedActivatedList = new ArrayList<>();
				int pageStart = 0;
				int pageEnd = 0;
				if (pageNo == 0) {
					pageStart = 0;
					pageEnd = pageSize - 1;
				} else {
					pageStart = pageSize * pageNo;
					pageEnd = pageStart + pageSize;
					if (pageEnd > updatedList.size()) {
						pageEnd = updatedList.size();
					}
				}
				if (pageNo == 0) {
					for (int i = pageStart; i <= pageEnd; i++) {
						newList.add(updatedList.get(i));
					}
				} else {
					for (int i = pageStart; i < pageEnd; i++) {
						newList.add(updatedList.get(i));
					}
				}
				if (activationKey != null) {
					ArrayList<Object> activationList = new ArrayList<>();
					if (activationKey.equals("ALL")) {
						dto.setTemplateData(newList);
						dto.setTotalNoOfElements(updatedList.size());
					}

					else if (activationKey.equals("ACTIVE")) {
						for (int i = 0; i < updatedList.size(); i++) {
							Object object = updatedList.get(i);
							JSONObject jsnOb = new JSONObject(object.toString());
							if (jsnOb.has("In-Stock")) {
								Boolean isStock = jsnOb.getBoolean("In-Stock");
								if (isStock) {
									activationList.add(object);
								}
							}
						}
						if (pageEnd > activationList.size()) {
							pageEnd = activationList.size();
						}
						if (pageStart == 0) {
							for (int i = pageStart; i <= pageEnd; i++) {
								updatedActivatedList.add(activationList.get(i));
							}
						} else {
							for (int i = pageStart; i < pageEnd; i++) {
								updatedActivatedList.add(activationList.get(i));
							}
						}
						dto.setTemplateData(updatedActivatedList);
						dto.setTotalNoOfElements(activationList.size());
					} else {
						for (int i = 0; i < updatedList.size(); i++) {
							Object object = updatedList.get(i);
							JSONObject jsnOb = new JSONObject(object.toString());
							if (jsnOb.has("In-Stock")) {
								Boolean isStock = jsnOb.getBoolean("In-Stock");
								if (!isStock) {
									activationList.add(object);
								}
							}
						}
						if (pageEnd > activationList.size()) {
							pageEnd = activationList.size();
						}
						if (pageStart == 0) {
							for (int i = pageStart; i < pageEnd; i++) {
								updatedActivatedList.add(activationList.get(i));
							}
						} else {
							for (int i = pageStart; i < pageEnd; i++) {
								updatedActivatedList.add(activationList.get(i));
							}
						}
						dto.setTemplateData(updatedActivatedList);
						dto.setTotalNoOfElements(activationList.size());
					}
				} else {
					dto.setTemplateData(newList);
					dto.setTotalNoOfElements(updatedList.size());
				}

			}
		}
		return new Response<>(HttpStatus.OK.value(), "OK", dto);
	}

	@Override
	public Response<?> getColumnDetailsForProduct(Long productId) {
		List<Transaction> tranList = transactionRepository.findAllByProductId(productId);
		ArrayList<Object> listdata = new ArrayList<Object>();
		ProductTemplateDataResponseForOrder dto = new ProductTemplateDataResponseForOrder();
		if (tranList != null && !tranList.isEmpty()) {
			for (Transaction trn : tranList) {
				if (trn.getColumnsObjectData() != null && !trn.getColumnsObjectData().isEmpty()) {
					JSONArray array = new JSONArray(trn.getColumnsObjectData());
					for (Object ob : array) {
						JSONObject jsonObject = new JSONObject(ob.toString());
						if (jsonObject.has("In-Stock")) {
							if (jsonObject.getBoolean("In-Stock") == true) {
								listdata.add(ob.toString());
							}
						}

					}
				}
			}

		}
		dto.setTemplateData(listdata);
		return new Response<>(HttpStatus.OK.value(), "Product Template Data.", dto);
	}

	@Override
	public Response<?> addListOfProductByExcel(MultipartFile file, Long createdBy, String createdUserName,
			Long companyId) {

		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			Boolean isDuplicate = false;
			String duplicateProductName = "";
			int numberOfSheets = workbook.getNumberOfSheets();
			List<String> headerList = new ArrayList<>();
			List<String> cellValueString = new ArrayList<>();
			List<JsonObject> objectList = new ArrayList<>();
			Boolean isValidFile = false;
			String[] expectedColumnNames = { "Product Name", "HSN Code", "SKU", "Unit", "Min Reorder qty",
					"Opening Qty", "Allow Order Till Qty", "Min Selling Price", "Standard Selling Price",
					"Block Stock On Order Create" };
			boolean isMismatchedData = false;
			String misMatchedColumnName = "";
			String misMatchedProductName = "";
			List<String> stringList = Arrays.asList(expectedColumnNames);
			Boolean isBlankSheet = false;
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Row topRowData = sheet.getRow(0);
				int noOfTopData = 0;
				for (Cell topCell : topRowData) {
					headerList.add(topCell.toString().trim());
					noOfTopData += 1;
				}

				if (headerList.equals(stringList)) {
					isValidFile = true;
				}
				if (isValidFile) {
					if (!(sheet.getPhysicalNumberOfRows() > 1)) {
						isBlankSheet = true;
					} else {
						for (Row row : sheet) {

							String str = "";
							if (row != null && !isRowEmpty(row)) {
								for (int j = 0; j < noOfTopData; j++) {
									Cell cel = row.getCell(j);

									String cellName = "";
									if (cel == null) {
										if (str == "") {
											str = str + "" + "/n";
										} else {
											str = str + " " + "/n";
										}
									} else {
										cellName = cel.toString();
										if (cel.toString().contains(".") && cel.toString().contains("E")) {
											String[] stringArray = cel.toString().split("E");
											List<String> wordList = Arrays.asList(stringArray);
											String firstString = wordList.get(0);
											String lastString = wordList.get(1);
											if (firstString != null && !firstString.isEmpty() && lastString != null
													&& !lastString.isEmpty()) {
												try {
													cellName = new DecimalFormat("#.##")
															.format(Double.parseDouble(firstString)
																	* Math.pow(10, Double.parseDouble(lastString)));
												} catch (Exception e) {
													// TODO: handle exception
												}
											}
										}
										if (str == "") {
											str = str + cellName + " " + "/n";
										} else {
											str = str + cellName + " " + "/n";
										}

									}

								}
								if (!str.isEmpty() && str != "") {
									cellValueString.add(str);
									str = "";
								} else {
									str = "";
								}
							}

						}
					}
				}

//				System.out.println((rowCount-noOfTopData)/noOfTopData);
			}
			if (isValidFile) {
				if (!isBlankSheet) {
					List<Unit> unitList = unitRepository.findAll();
					Map<String, List<Unit>> unitMap = new HashMap<>();
					if (unitList != null && !unitList.isEmpty()) {
						unitMap = unitList.stream().filter(x -> x.getName() != null && !x.getName().isEmpty())
								.map(x -> new Unit(x.getId(), x.getName().replaceAll("\\s", "")))
								.collect(Collectors.groupingBy(Unit::getName));
					}
					String pattern = "^[^a-zA-Z]*$";
					Pattern regexPattern = Pattern.compile(pattern);
					List<String> updatedList = new ArrayList<>();
					for (int i = 1; i < cellValueString.size(); i++) {
						updatedList.add(cellValueString.get(i));
					}
					for (String str : updatedList) {
						String[] commaSeparatedArray = str.split("/n");
						List<String> wordList = Arrays.asList(commaSeparatedArray);
						JsonObject obj = new JsonObject();

						for (int i = 0; i < headerList.size(); i++) {
							obj.addProperty(headerList.get(i), wordList.get(i));
						}
						objectList.add(obj);

					}
					List<Product> productList = new ArrayList<>();
					for (Object obj : objectList) {
						Product product = new Product();
						JSONObject object = new JSONObject(obj.toString());
						if (object.has("Product Name")) {
							if (object.get("Product Name") == null || object.get("Product Name").equals(" ")
									|| object.get("Product Name").equals("")) {
								product.setName(null);
							} else {
								product.setName(object.getString("Product Name").trim());
							}
						}
						if (object.has("HSN Code")) {
							if (object.get("HSN Code") == null || object.get("HSN Code").equals(" ")
									|| object.get("HSN Code").equals("")) {
								product.setHsnCode(null);
							} else {
								if (object.get("HSN Code").toString().contains(".")) {
									String str = object.get("HSN Code").toString();
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									Long hsnCode = bg.longValue();
									product.setHsnCode(hsnCode.toString());
								} else {
									product.setHsnCode(object.getString("HSN Code").trim());
								}

							}
						}
						if (object.has("SKU")) {
							if (object.get("SKU") == null || object.get("SKU").equals(" ")
									|| object.get("SKU").equals("")) {
								product.setSku(null);
							} else {
								if (object.get("SKU").toString().contains(".")) {
									String str = object.get("SKU").toString();
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									Long hsnCode = bg.longValue();
									product.setSku(hsnCode.toString());
								} else {
									product.setSku(object.getString("SKU").trim());
								}

							}
						}
						if (object.has("Unit")) {
							if (object.get("Unit") == null || object.get("Unit").equals(" ")
									|| object.get("Unit").equals("")) {
								product.setUnit(null);
							} else {
								String str = object.getString("Unit").trim().toUpperCase();
								str = str.replaceAll("\\s", "");
//								if (str.equals("BOX")) {
//									product.setUnit(1L);
//								} else if (str.equals("PIECE")) {
//									product.setUnit(2L);
//								} else {
//									product.setUnit(null);
//								}
								if (unitMap != null && !unitMap.isEmpty() && unitMap.containsKey(str)
										&& unitMap.get(str) != null) {
									product.setUnit(unitMap.get(str).get(0).getId());
								} else {
									product.setUnit(null);
								}
							}
						}
						if (object.has("Min Reorder qty")) {
							if (object.get("Min Reorder qty") == null || object.get("Min Reorder qty").equals(" ")
									|| object.get("Min Reorder qty").equals("")) {
								product.setMinReOrderQuantity(null);
							} else {
								Matcher matcher = regexPattern.matcher(object.get("Min Reorder qty").toString());
								if (matcher.matches()) {
									if (object.get("Min Reorder qty").toString().contains(".")) {

										String str = object.getString("Min Reorder qty");
										str = str.replaceAll("\\s", "");
										BigDecimal bg = new BigDecimal(str);
										product.setMinReOrderQuantity(bg.longValue());
									} else {
										product.setMinReOrderQuantity(
												Long.parseLong(object.getString("Min Reorder qty").toString()));
									}
								} else {
									isMismatchedData = true;
									misMatchedColumnName = "Min Reorder qty";
									misMatchedProductName = product.getName();

								}

							}
						}
						if (object.has("Opening Qty")) {
							Matcher matcher = regexPattern.matcher(object.get("Opening Qty").toString());
							if (matcher.matches()) {
								if (object.get("Opening Qty") == null
										|| object.get("Opening Qty").toString().equals(" ")
										|| object.get("Opening Qty").toString().equals("")) {
									product.setOpeningQuantity(null);
								} else {
									String str = object.getString("Opening Qty");
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									product.setOpeningQuantity(bg.longValue());
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Opening Qty";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Allow Order Till Qty")) {
							Matcher matcher = regexPattern.matcher(object.get("Allow Order Till Qty").toString());
							if (matcher.matches()) {
								if (object.get("Allow Order Till Qty") == null
										|| object.get("Allow Order Till Qty").equals(" ")
										|| object.get("Allow Order Till Qty").equals("")
										|| object.get("Allow Order Till Qty").toString().trim().equals("")) {
									product.setAllowOrderTillQuantity(null);
								} else {
									String str = object.getString("Allow Order Till Qty");
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									product.setAllowOrderTillQuantity(bg.longValue());
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Allow Order Till Qty";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Min Selling Price")) {
							Matcher matcher = regexPattern.matcher(object.get("Min Selling Price").toString());
							if (matcher.matches()) {
								if (object.get("Min Selling Price") == null
										|| object.get("Min Selling Price").equals(" ")
										|| object.get("Min Selling Price").equals("")) {
									product.setMinSellingPrice(null);
								} else {
									product.setMinSellingPrice(object.getDouble("Min Selling Price"));
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Min Selling Price";
								misMatchedProductName = product.getName();

							}
						}
						if (object.has("Standard Selling Price")) {
							Matcher matcher = regexPattern.matcher(object.get("Standard Selling Price").toString());
							if (matcher.matches()) {
								if (object.get("Standard Selling Price") == null
										|| object.get("Standard Selling Price").equals(" ")
										|| object.get("Standard Selling Price").equals("")) {
									product.setStandardSellingPrice(null);
								} else {
									product.setStandardSellingPrice(object.getDouble("Standard Selling Price"));
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Standard Selling Price";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Block Stock On Order Create")) {
							if (object.get("Block Stock On Order Create") == null
									|| object.get("Block Stock On Order Create").equals(" ")
									|| object.get("Block Stock On Order Create").equals("")) {
								product.setBlockStockOnOrderCreate(false);
							} else {
								if (object.getString("Block Stock On Order Create").trim().toUpperCase()
										.equals("YES")) {
									product.setBlockStockOnOrderCreate(true);
								} else if (object.getString("Block Stock On Order Create").trim().toUpperCase()
										.equals("NO")) {
									product.setBlockStockOnOrderCreate(false);
								}

								else {
									product.setBlockStockOnOrderCreate(null);
								}
							}
						}
						product.setCreatedBy(createdBy);
						product.setCreatedUserName(createdUserName);
						product.setCreatedAt(new Date());
						product.setCompanyId(companyId);
						product.setPhysicalStockQuantity(product.getOpeningQuantity().doubleValue());
						product.setAvailableQuantity(product.getOpeningQuantity().doubleValue());
						product.setTotalNoOfProducts(product.getOpeningQuantity().doubleValue());
						product.setIsDetails(false);
						productList.add(product);
					}
					Response<List<String>> response = new Response<>();
					if (productList != null) {
						for (Product prod : productList) {
							if (prod.getName() == null || prod.getName().isEmpty() || prod.getName() == ""
									|| !(prod.getName().length() > 0)) {
								response.setMessage("Product Name can't be blank.");
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (isMismatchedData) {
								response.setMessage("Column " + misMatchedColumnName
										+ " does not support string value for product " + misMatchedProductName);
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getSku() == null || prod.getSku().isEmpty() || prod.getSku() == ""
									|| !(prod.getSku().length() > 0)) {
								response.setMessage("Please Enter the SKU Code For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getHsnCode() == null || prod.getHsnCode().length() == 0
									|| prod.getHsnCode() == "") {
								response.setMessage("Please Enter the HSN Code For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getUnit() == null) {
								response.setMessage("Please Enter the Unit For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinReOrderQuantity() == null) {
								response.setMessage(
										"Please enter minimum reorder quantity value for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinReOrderQuantity() <= 0) {
								response.setMessage(
										"Minimum reorder quantity can't be 0 or -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getOpeningQuantity() == null) {
								response.setMessage("Please enter opening quantity for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getOpeningQuantity() <= -1) {
								response.setMessage("Opening quantity can't -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getStandardSellingPrice() == null) {
								response.setMessage(
										"Standard selling price can't be blank for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							}
//							else if (prod.getAllowOrderTillQuantity() == null) {
//								response.setMessage(
//										"Allow order till quantity can't be blank for product " + prod.getName());
//								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
//								response.setData(null);
//								break;
//							} else if (prod.getAllowOrderTillQuantity() <= 0) {
//								response.setMessage(
//										"Allow order till quantity can't be 0 or -ve for product " + prod.getName());
//								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
//								response.setData(null);
//								break;
//							} 
							else if (prod.getMinSellingPrice() != null && prod.getMinSellingPrice() <= 0) {
								response.setMessage(
										"Minimum selling price can't be 0 or -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getStandardSellingPrice() <= 0) {
								response.setMessage(
										"Standard selling price can't be 0 or -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinSellingPrice() != null
									&& prod.getMinSellingPrice() > prod.getStandardSellingPrice()) {
								response.setMessage(
										"Minimum selling price can't be greater then the standard selling price for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getAllowOrderTillQuantity() != null
									&& prod.getAllowOrderTillQuantity().toString() != ""
									&& prod.getAllowOrderTillQuantity() > prod.getMinReOrderQuantity()) {

								response.setMessage(
										"Allow order till quantity can't be greater than the min reorder quantity for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;

							} else if (prod.getBlockStockOnOrderCreate() == null) {
								response.setMessage(
										"Please provide either Yes/No in block stock on order create column for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else {
								List<String> duplicateDataList = new ArrayList<>();
								for (String str : headerList) {
									for (Object obj : objectList) {
										JSONObject object = new JSONObject(obj.toString());
										JSONArray jsonArray = new JSONArray(objectList.toString());

										if (str.equals("SKU") || str.equals("Product Name")) {
											boolean hasDuplicates = TransactionServiceImpl.containsDuplicate(jsonArray,
													str);
											if (hasDuplicates) {
												String duplicateData = str + ":" + object.getString(str);
												duplicateDataList.add(duplicateData);
											}
										}

									}

								}

								// need to optimized
								List<Product> productObj = productRepository.findBySkuAndProductName(prod.getSku(),
										prod.getName());
								if (duplicateDataList != null && duplicateDataList.size() > 0) {
									response.setResponseCode(HttpStatus.BAD_REQUEST.value());
									response.setData(duplicateDataList);
									response.setMessage("Duplicate data not allow.");
									break;
								} else if (productObj != null && !productObj.isEmpty()) {
									response.setResponseCode(HttpStatus.BAD_REQUEST.value());
									response.setMessage("Product Already Exist With Sku: " + prod.getSku() + "/"
											+ "Product name: " + prod.getName());
									response.setData(null);
									isDuplicate = true;
									duplicateProductName = prod.getName();
									break;
								} else {
									response.setMessage("OK");
									response.setResponseCode(HttpStatus.OK.value());
								}

							}

						}
					}

					if (response.getResponseCode() == HttpStatus.OK.value()) {
						if (!isDuplicate) {
							for (Product product : productList) {
								Product prdObj = productRepository.save(product);
								Transaction transaction = new Transaction();
								transaction.setCreatedAt(new Date());
								transaction.setProductId(prdObj.getId());
								transaction.setProcuredQuantity(prdObj.getAvailableQuantity());
								if (prdObj.getCreatedBy() != null) {
									transaction.setCreatedBy(prdObj.getCreatedBy());
								}
								if (prdObj.getCreatedUserName() != null && !prdObj.getCreatedUserName().isEmpty()) {
									transaction.setCreatedUserName(createdUserName);
								}
								transactionRepository.save(transaction);
							}
							return new Response<>(HttpStatus.OK.value(), "Product List Added Successfully.", null);
						} else {
							return new Response<>(HttpStatus.BAD_REQUEST.value(),
									"Duplicate entry for product " + duplicateProductName, null);
						}

					} else {
						return response;
					}

				} else {
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a valid file.", null);
				}
			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Wrong File.", null);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					null);
		}

	}

	// Function to check if a row is empty (contains only blank cells)
	private static boolean isRowEmpty(Row row) {
		for (Cell cell : row) {
			if (cell.getCellType() != CellType.BLANK) {
				return false; // Row is not empty
			}
		}
		return true; // Row is empty
	}

	@Override
	public Response<?> getAllUnit(Long companyId) {
		// TODO Auto-generated method stub
		List<CompanyUnitConfig> companyUnitConfigList = companyUnitConfigRepository
				.findAllByCompanyIdAndIsActiveTrue(companyId);
		List<Unit> unitList;
		if (companyUnitConfigList != null && !companyUnitConfigList.isEmpty()
				&& companyUnitConfigList.get(0).getIsCompanyWise() != null
				&& companyUnitConfigList.get(0).getIsCompanyWise()) {
			unitList = unitRepository.findByCompanyIdAndIsActiveTrue(companyId);
		} else {
			unitList = unitRepository.findByCompanyIdIsNullAndIsActiveTrue();
		}

		List<UnitResponseDto> unitResponseDtos = new ArrayList<>();
		if (unitList != null && !unitList.isEmpty()) {
			for (Unit unit : unitList) {
				UnitResponseDto dto = new UnitResponseDto();

				dto.setId(unit.getId());
				dto.setUnitName(unit.getName());
				dto.setCreatedAt(unit.getCreatedAt());
				List<UnitConversionMapping> subUnitList = unitConversionMappingRepository.findAllByUnitId(unit.getId());
				List<UnitResponseDto> subUnitLists = new ArrayList<>();
				if (subUnitList != null && subUnitList.size() > 0) {
					for (UnitConversionMapping map : subUnitList) {
						UnitResponseDto unitDto = new UnitResponseDto();
						unitDto.setId(map.getId());
						unitDto.setCreatedAt(new Date());
						unitDto.setUnitName(map.getSubUnitName());
						unitDto.setQuantity(map.getSubUnitQty());
						subUnitLists.add(unitDto);
					}

				}
				dto.setSubUnitList(subUnitLists);
				unitResponseDtos.add(dto);
			}
		}
		Collections.reverse(unitResponseDtos);
		return new Response<>(HttpStatus.OK.value(), "Unit List.", unitResponseDtos);
	}

	@Override
	public Response<?> addListOfProductByExcelV2(MultipartFile file, Long createdBy, String createdUserName,
			Long companyId, Long firm, Long category) {

		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			Boolean isDuplicate = false;
			String duplicateProductName = "";
			int numberOfSheets = workbook.getNumberOfSheets();
			List<String> headerList = new ArrayList<>();
			List<String> cellValueString = new ArrayList<>();
			List<JsonObject> objectList = new ArrayList<>();
			Boolean isValidFile = false;
			String[] expectedColumnNames = { "Product Name", "HSN Code", "SKU", "Unit", "Min Reorder qty",
					"Opening Qty", "Current Qty", "Allow Order Till Qty", "Min Selling Price", "Standard Selling Price",
					"Block Stock On Order Create", "No Of Samples", "Allow Order Without Stock" };
			boolean isMismatchedData = false;
			String misMatchedColumnName = "";
			String misMatchedProductName = "";
			List<String> stringList = Arrays.asList(expectedColumnNames);
			Boolean isBlankSheet = false;
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Row topRowData = sheet.getRow(0);
				int noOfTopData = 0;
				for (Cell topCell : topRowData) {
					headerList.add(topCell.toString().trim());
					noOfTopData += 1;
				}

				if (headerList.equals(stringList)) {
					isValidFile = true;
				}
				if (isValidFile) {
					if (!(sheet.getPhysicalNumberOfRows() > 1)) {
						isBlankSheet = true;
					} else {
						for (Row row : sheet) {

							String str = "";
							if (row != null && !isRowEmpty(row)) {
								for (int j = 0; j < noOfTopData; j++) {
									Cell cel = row.getCell(j);

									String cellName = "";
									if (cel == null) {
										if (str == "") {
											str = str + "" + "/n";
										} else {
											str = str + " " + "/n";
										}
									} else {
										cellName = cel.toString();
										if (cel.toString().contains(".") && cel.toString().contains("E")) {
											String[] stringArray = cel.toString().split("E");
											List<String> wordList = Arrays.asList(stringArray);
											String firstString = wordList.get(0);
											String lastString = wordList.get(1);
											if (firstString != null && !firstString.isEmpty() && lastString != null
													&& !lastString.isEmpty()) {
												try {
													cellName = new DecimalFormat("#.##")
															.format(Double.parseDouble(firstString)
																	* Math.pow(10, Double.parseDouble(lastString)));
												} catch (Exception e) {
													// TODO: handle exception
												}
											}
										}
										if (str == "") {
											str = str + cellName + " " + "/n";
										} else {
											str = str + cellName + " " + "/n";
										}

									}

								}
								if (!str.isEmpty() && str != "") {
									cellValueString.add(str);
									str = "";
								} else {
									str = "";
								}
							}

						}
					}
				}

//				System.out.println((rowCount-noOfTopData)/noOfTopData);
			}
			if (isValidFile) {
				if (!isBlankSheet) {
					List<Unit> unitList = unitRepository.findAll();
					Map<String, List<Unit>> unitMap = new HashMap<>();
					if (unitList != null && !unitList.isEmpty()) {
						unitMap = unitList.stream().filter(x -> x.getName() != null && !x.getName().isEmpty())
								.map(x -> new Unit(x.getId(), x.getName().replaceAll("\\s", "")))
								.collect(Collectors.groupingBy(Unit::getName));
					}
					String pattern = "^[^a-zA-Z]*$";
					Pattern regexPattern = Pattern.compile(pattern);
					List<String> updatedList = new ArrayList<>();
					for (int i = 1; i < cellValueString.size(); i++) {
						updatedList.add(cellValueString.get(i));
					}
					for (String str : updatedList) {
						String[] commaSeparatedArray = str.split("/n");
						List<String> wordList = Arrays.asList(commaSeparatedArray);
						JsonObject obj = new JsonObject();

						for (int i = 0; i < headerList.size(); i++) {
							obj.addProperty(headerList.get(i), wordList.get(i));
						}
						objectList.add(obj);

					}
					List<Product> productList = new ArrayList<>();
					for (Object obj : objectList) {
						Product product = new Product();
						JSONObject object = new JSONObject(obj.toString());
						if (object.has("Product Name")) {
							if (object.get("Product Name") == null || object.get("Product Name").equals(" ")
									|| object.get("Product Name").equals("")) {
								product.setName(null);
							} else {
								product.setName(object.getString("Product Name").trim());
							}
						}
						if (object.has("HSN Code")) {
							if (object.get("HSN Code") == null || object.get("HSN Code").equals(" ")
									|| object.get("HSN Code").equals("")) {
								product.setHsnCode(null);
							} else {
								if (object.get("HSN Code").toString().contains(".")) {
									String str = object.get("HSN Code").toString();
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									Long hsnCode = bg.longValue();
									product.setHsnCode(hsnCode.toString());
								} else {
									product.setHsnCode(object.getString("HSN Code").trim());
								}

							}
						}
						if (object.has("SKU")) {
							if (object.get("SKU") == null || object.get("SKU").equals(" ")
									|| object.get("SKU").equals("")) {
								product.setSku(null);
							} else {
								if (object.get("SKU").toString().contains(".")) {
									String str = object.get("SKU").toString();
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									Long hsnCode = bg.longValue();
									product.setSku(hsnCode.toString().trim());
								} else {
									product.setSku(object.getString("SKU").trim());
								}

							}
						}
						if (object.has("Unit")) {
							if (object.get("Unit") == null || object.get("Unit").equals(" ")
									|| object.get("Unit").equals("")) {
								product.setUnit(null);
							} else {
								String str = object.getString("Unit").trim();
								str = str.replaceAll("\\s", "");
//								if (str.equals("BOX")) {
//									product.setUnit(1L);
//								} else if (str.equals("PIECE")) {
//									product.setUnit(2L);
//								} else {
//									product.setUnit(null);
//								}
								if (unitMap != null && !unitMap.isEmpty() && unitMap.containsKey(str)
										&& unitMap.get(str) != null) {
									product.setUnit(unitMap.get(str).get(0).getId());
								} else {
									product.setUnit(null);
								}
							}
						}
						if (object.has("Min Reorder qty")) {
							if (object.get("Min Reorder qty") == null || object.get("Min Reorder qty").equals(" ")
									|| object.get("Min Reorder qty").equals("")) {
								product.setMinReOrderQuantity(null);
							} else {
								Matcher matcher = regexPattern.matcher(object.get("Min Reorder qty").toString());
								if (matcher.matches()) {
									if (object.get("Min Reorder qty").toString().contains(".")) {

										String str = object.getString("Min Reorder qty");
										str = str.replaceAll("\\s", "");
										BigDecimal bg = new BigDecimal(str);
										product.setMinReOrderQuantity(bg.longValue());
									} else {
										product.setMinReOrderQuantity(
												Long.parseLong(object.getString("Min Reorder qty").toString()));
									}
								} else {
									isMismatchedData = true;
									misMatchedColumnName = "Min Reorder qty";
									misMatchedProductName = product.getName();

								}

							}
						}
						if (object.has("Opening Qty")) {
							Matcher matcher = regexPattern.matcher(object.get("Opening Qty").toString());
							if (matcher.matches()) {
								if (object.get("Opening Qty") == null
										|| object.get("Opening Qty").toString().equals(" ")
										|| object.get("Opening Qty").toString().equals("")) {
									product.setOpeningQuantity(null);
								} else {
									String str = object.getString("Opening Qty");
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									product.setOpeningQuantity(bg.longValue());
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Opening Qty";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Allow Order Till Qty")) {
							Matcher matcher = regexPattern.matcher(object.get("Allow Order Till Qty").toString());
							if (matcher.matches()) {
								if (object.get("Allow Order Till Qty") == null
										|| object.get("Allow Order Till Qty").equals(" ")
										|| object.get("Allow Order Till Qty").equals("")
										|| object.get("Allow Order Till Qty").toString().trim().equals("")) {
									product.setAllowOrderTillQuantity(null);
								} else {
									String str = object.getString("Allow Order Till Qty");
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									product.setAllowOrderTillQuantity(bg.longValue());
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Allow Order Till Qty";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Min Selling Price")) {
							Matcher matcher = regexPattern.matcher(object.get("Min Selling Price").toString());
							if (matcher.matches()) {
								if (object.get("Min Selling Price") == null
										|| object.get("Min Selling Price").equals(" ")
										|| object.get("Min Selling Price").equals("")) {
									product.setMinSellingPrice(null);
								} else {
									product.setMinSellingPrice(object.getDouble("Min Selling Price"));
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Min Selling Price";
								misMatchedProductName = product.getName();

							}
						}
						if (object.has("Standard Selling Price")) {
							Matcher matcher = regexPattern.matcher(object.get("Standard Selling Price").toString());
							if (matcher.matches()) {
								if (object.get("Standard Selling Price") == null
										|| object.get("Standard Selling Price").equals(" ")
										|| object.get("Standard Selling Price").equals("")) {
									product.setStandardSellingPrice(null);
								} else {
									product.setStandardSellingPrice(object.getDouble("Standard Selling Price"));
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Standard Selling Price";
								misMatchedProductName = product.getName();

							}

						}
						if (object.has("Block Stock On Order Create")) {
							if (object.get("Block Stock On Order Create") == null
									|| object.get("Block Stock On Order Create").equals(" ")
									|| object.get("Block Stock On Order Create").equals("")) {
								product.setBlockStockOnOrderCreate(false);
							} else {
								if (object.getString("Block Stock On Order Create").trim().toUpperCase()
										.equals("YES")) {
									product.setBlockStockOnOrderCreate(true);
								} else if (object.getString("Block Stock On Order Create").trim().toUpperCase()
										.equals("NO")) {
									product.setBlockStockOnOrderCreate(false);
								}

								else {
									product.setBlockStockOnOrderCreate(null);
								}
							}
						}
						if (object.has("No Of Samples")) {
							if (object.get("No Of Samples") == null || object.get("No Of Samples").equals(" ")
									|| object.get("No Of Samples").equals("")) {
								product.setMaxNoOfSamples(null);
							} else {
								double doubleValue = Double.parseDouble(object.get("No Of Samples").toString());

								// Convert the double to an integer
								int intValue = (int) doubleValue;
								if (intValue > 0) {
									product.setMaxNoOfSamples(intValue);
								} else {
									product.setMaxNoOfSamples(0);
								}
							}
						}
						if (object.has("Allow Order Without Stock")) {
							if (object.get("Allow Order Without Stock") == null
									|| object.get("Allow Order Without Stock").equals(" ")
									|| object.get("Allow Order Without Stock").equals("")) {
								product.setAllowOrderWithoutStock(false);
							} else {
								if (object.getString("Allow Order Without Stock").trim().toUpperCase().equals("YES")) {
									product.setAllowOrderWithoutStock(true);
								} else if (object.getString("Allow Order Without Stock").trim().toUpperCase()
										.equals("NO")) {
									product.setAllowOrderWithoutStock(false);
								}

								else {
									product.setAllowOrderWithoutStock(null);
								}
							}
						}
						if (object.has("Current Qty")) {
							Matcher matcher = regexPattern.matcher(object.get("Current Qty").toString());
							if (matcher.matches()) {
								if (object.get("Current Qty") == null
										|| object.get("Current Qty").toString().equals(" ")
										|| object.get("Current Qty").toString().equals("")) {
									product.setCurrentQty(null);
								} else {
									String str = object.getString("Current Qty");
									str = str.replaceAll("\\s", "");
									BigDecimal bg = new BigDecimal(str);
									product.setCurrentQty(bg.longValue());
								}
							} else {
								isMismatchedData = true;
								misMatchedColumnName = "Current Qty";
								misMatchedProductName = product.getName();

							}

						}
						product.setCreatedBy(createdBy);
						product.setCreatedUserName(createdUserName);
						product.setCreatedAt(new Date());
						product.setCompanyId(companyId);
						product.setPhysicalStockQuantity(product.getOpeningQuantity().doubleValue());
						product.setAvailableQuantity(product.getOpeningQuantity().doubleValue());
						product.setTotalNoOfProducts(product.getOpeningQuantity().doubleValue());
						product.setIsDetails(false);
						if (firm != null) {
							product.setFirm(new Firm(firm));
						}

						if (category == null) {
							product.setProductCategory(null);
						} else {
							product.setProductCategory(new ProductCatagory(category));
						}
						productList.add(product);
					}
					Response<List<String>> response = new Response<>();
					if (productList != null) {

//						List<String> skuList = productList.stream()
//								.filter(x -> x.getSku().trim() != null && !x.getSku().trim().isEmpty()).map(Product::getSku)
//								.collect(Collectors.toList());
//						List<Product> productObjList = productRepository.findAllBySkuIn(skuList,companyId);
//						Map<String, List<Product>> productMap = new HashMap<>();
//						if (productObjList != null) {
//							productMap = productObjList.stream()
//									.filter(x -> x.getSku() != null && !x.getSku().isEmpty() && x.getName() != null
//											&& !x.getName().isEmpty())
//									.map(x -> new Product(x.getSku() + "" + x.getName()))
//									.collect(Collectors.groupingBy(Product::getName));
//
//						}
						for (Product prod : productList) {

							if (prod.getName() == null || prod.getName().isEmpty() || prod.getName() == ""
									|| !(prod.getName().length() > 0)) {
								response.setMessage("Product Name can't be blank.");
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (isMismatchedData) {
								response.setMessage("Column " + misMatchedColumnName
										+ " does not support string value for product " + misMatchedProductName);
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getSku() == null || prod.getSku().isEmpty() || prod.getSku() == ""
									|| !(prod.getSku().length() > 0)) {
								response.setMessage("Please Enter the SKU Code For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getHsnCode() == null || prod.getHsnCode().length() == 0
									|| prod.getHsnCode() == "") {
								response.setMessage("Please Enter the HSN Code For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getUnit() == null) {
								response.setMessage("Please Enter the Unit For Product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinReOrderQuantity() == null) {
								response.setMessage(
										"Please enter minimum reorder quantity value for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinReOrderQuantity() <= 0) {
								response.setMessage(
										"Minimum reorder quantity can't be 0 or -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getOpeningQuantity() == null) {
								response.setMessage("Please enter opening quantity for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getOpeningQuantity() <= -1) {
								response.setMessage("Opening quantity can't -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getStandardSellingPrice() == null) {
								response.setMessage(
										"Standard selling price can't be blank for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							}
//							else if (prod.getAllowOrderTillQuantity() == null) {
//								response.setMessage(
//										"Allow order till quantity can't be blank for product " + prod.getName());
//								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
//								response.setData(null);
//								break;
//							} else if (prod.getAllowOrderTillQuantity() <= 0) {
//								response.setMessage(
//										"Allow order till quantity can't be 0 or -ve for product " + prod.getName());
//								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
//								response.setData(null);
//								break;
//							} 
//							else if (prod.getMinSellingPrice() != null && prod.getMinSellingPrice() <= 0) {
//								response.setMessage(
//										"Minimum selling price can't be 0 or -ve for product " + prod.getName());
							else if (prod.getMinSellingPrice() != null && prod.getMinSellingPrice() < 0) {
								response.setMessage("Minimum selling price can't be -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getStandardSellingPrice() < 0) {
								response.setMessage(
										"Standard selling price can't be -ve for product " + prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getMinSellingPrice() != null
									&& prod.getMinSellingPrice() > prod.getStandardSellingPrice()) {
								response.setMessage(
										"Minimum selling price can't be greater then the standard selling price for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else if (prod.getAllowOrderTillQuantity() != null
									&& prod.getAllowOrderTillQuantity().toString() != ""
									&& prod.getAllowOrderTillQuantity() > prod.getMinReOrderQuantity()) {

								response.setMessage(
										"Allow order till quantity can't be greater than the min reorder quantity for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;

							} else if (prod.getBlockStockOnOrderCreate() == null) {
								response.setMessage(
										"Please provide either Yes/No in block stock on order create column for product "
												+ prod.getName());
								response.setResponseCode(HttpStatus.BAD_REQUEST.value());
								response.setData(null);
								break;
							} else {
								List<String> duplicateDataList = new ArrayList<>();
								for (String str : headerList) {
									for (Object obj : objectList) {
										JSONObject object = new JSONObject(obj.toString());
										JSONArray jsonArray = new JSONArray(objectList.toString());

										if (str.equals("SKU") || str.equals("Product Name")) {
											boolean hasDuplicates = TransactionServiceImpl.containsDuplicate(jsonArray,
													str);
											if (hasDuplicates) {
												String duplicateData = str + ":" + object.getString(str);
												duplicateDataList.add(duplicateData);
											}
										}

									}

								}

								// need to optimized
//								List<Product> productObj = productRepository.findBySkuAndProductName(prod.getSku(),
//										prod.getName());
//								List<Product> productObj = new ArrayList<>();
//								if (prod.getFirm() != null) {
//									productObj = productRepository.findBySkuAndProductNameAndFirmAndCompanyId(
//											prod.getSku(), prod.getName(), prod.getFirm().getId(), prod.getCompanyId());
//								} else {
//									productObj = productRepository.findBySkuAndProductNameAndCompanyID(prod.getSku(),
//											prod.getName(), prod.getCompanyId());
//								}
								if (duplicateDataList != null && duplicateDataList.size() > 0) {
									response.setResponseCode(HttpStatus.BAD_REQUEST.value());
									response.setData(duplicateDataList);
									response.setMessage("Duplicate data not allow.");
									break;
								} else {
									response.setMessage("OK");
									response.setResponseCode(HttpStatus.OK.value());
								}

							}

						}
					}

					if (response.getResponseCode() == HttpStatus.OK.value()) {
						if (!isDuplicate) {
							for (Product prod : productList) {
//								List<Product> prdObjList = productRepository.saveAll(productList);
								List<Product> productObj = new ArrayList<>();
								if (prod.getFirm() != null) {

									productObj = productRepository.findBySkuAndProductNameAndFirmAndCompanyId(
											prod.getSku(), prod.getName(), prod.getFirm().getId(), prod.getCompanyId());
								} else {
									productObj = productRepository.findBySkuAndProductNameAndCompanyID(prod.getSku(),
											prod.getName(), prod.getCompanyId());
								}
								if (productObj != null && !productObj.isEmpty()) {
									Product existProduct = productObj.get(0);
//									existProduct.setBlockStockOnOrderCreate(prod.getBlockStockOnOrderCreate());
//									existProduct.setHsnCode(prod.getHsnCode());
									existProduct.setAllowOrderWithoutStock(prod.getAllowOrderWithoutStock());
									existProduct.setMaxNoOfSamples(prod.getMaxNoOfSamples());
									existProduct.setMinReOrderQuantity(prod.getMinReOrderQuantity());
									existProduct.setMinSellingPrice(prod.getMinSellingPrice());
									existProduct.setAllowOrderTillQuantity(prod.getAllowOrderTillQuantity());
									existProduct.setMinReOrderQuantity(prod.getMinReOrderQuantity());
//									existProduct.setOpeningQuantity(prod.getOpeningQuantity());
									if (prod.getCurrentQty() != null) {
										if ((existProduct.getAvailableQuantity() > prod.getCurrentQty())
												&& existProduct.getIsDetails().booleanValue() == false) {
											Double totalQty = existProduct.getAvailableQuantity()
													- prod.getCurrentQty().doubleValue();
											existProduct.setAvailableQuantity(
													existProduct.getAvailableQuantity() - totalQty);
											existProduct.setPhysicalStockQuantity(
													existProduct.getPhysicalStockQuantity() - totalQty);
											existProduct.setTotalNoOfProducts(
													existProduct.getTotalNoOfProducts() + totalQty);
//											existProduct.setOpeningQuantity(prod.getOpeningQuantity());
											Transaction trn = new Transaction();
											trn.setProductId(existProduct.getId());
											trn.setDisbursedQuantity(totalQty);
											trn.setRemarks("Disbursed during product update through excel.");
											trn.setCreatedAt(new Date());
											trn.setCreatedBy(prod.getCreatedBy());
											trn.setCreatedUserName(prod.getCreatedUserName());
											trn.setIsOrder(false);
											transactionRepository.save(trn);
										}
									}

									if ((existProduct.getAvailableQuantity() < prod.getCurrentQty())
											&& existProduct.getIsDetails().booleanValue() == false) {
										Double totalQty = prod.getCurrentQty() - existProduct.getAvailableQuantity();
										existProduct
												.setAvailableQuantity(existProduct.getAvailableQuantity() + totalQty);
										existProduct.setPhysicalStockQuantity(
												existProduct.getPhysicalStockQuantity() + totalQty);
										existProduct
												.setTotalNoOfProducts(existProduct.getTotalNoOfProducts() + totalQty);
//										existProduct.setOpeningQuantity(prod.getOpeningQuantity());
										Transaction trn = new Transaction();
										trn.setProcuredQuantity(totalQty);
										trn.setRemarks("Procured during product update through excel.");
										trn.setCreatedAt(new Date());
										trn.setCreatedBy(prod.getCreatedBy());
										trn.setCreatedUserName(prod.getCreatedUserName());
										trn.setProductId(existProduct.getId());
										trn.setIsOrder(false);
										transactionRepository.save(trn);
									}
									existProduct.setStandardSellingPrice(prod.getStandardSellingPrice());
//									existProduct.setUnit(prod.getUnit());
//									existProduct.setName(prod.getName());
									productRepository.save(existProduct);

								} else {
									productRepository.save(prod);
									Transaction transaction = new Transaction();
									transaction.setCreatedAt(new Date());
									transaction.setProductId(prod.getId());
									transaction.setProcuredQuantity(prod.getOpeningQuantity().doubleValue());
									if (prod.getCreatedBy() != null) {
										transaction.setCreatedBy(prod.getCreatedBy());
									}
									if (prod.getCreatedUserName() != null && !prod.getCreatedUserName().isEmpty()) {
										transaction.setCreatedUserName(createdUserName);
									}
									transactionRepository.save(transaction);
								}

							}
							return new Response<>(HttpStatus.OK.value(), "Product List Added Successfully.", null);
//					} 

						} else {
							return new Response<>(HttpStatus.BAD_REQUEST.value(),
									"Duplicate entry for product " + duplicateProductName, null);
						}
					} else {
						return response;
					}

				} else {
					return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a valid file.", null);
				}
			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Wrong File.", null);
			}

		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a valid file.", null);
		}

	}

	@Override
	public Response<?> saveListOfUnits(UnitResponseDto unitResponseDto) {
		List<Unit> unitList = new ArrayList<>();
		String duplicateUnitName = "";
		List<CompanyUnitConfig> compConfig = companyUnitConfigRepository
				.findAllByCompanyIdAndIsActiveTrue(unitResponseDto.getCompanyId());
		if (compConfig == null || compConfig.size() == 0) {
			CompanyUnitConfig unitConf = new CompanyUnitConfig();
			unitConf.setCompanyId(unitResponseDto.getCompanyId());
			unitConf.setIsActive(true);
			unitConf.setIsCompanyWise(true);
			companyUnitConfigRepository.save(unitConf);
		}
		for (String dto : unitResponseDto.getUnitList()) {
			Unit unit = unitRepository.findAllByUnitNameAndCompanyId(dto.trim(), unitResponseDto.getCompanyId());

			if (unit != null) {
				duplicateUnitName = dto;
				break;
			} else {
				Unit unitNew = new Unit();
				unitNew.setCreatedAt(new Date());
				unitNew.setIsActive(true);
				unitNew.setCompanyId(unitResponseDto.getCompanyId());
				unitNew.setName(dto);
				unitList.add(unitNew);
			}
		}
		if (duplicateUnitName.equals("")) {

			unitRepository.saveAll(unitList);
			return new Response<>(HttpStatus.OK.value(), "Unit added successfully.", null);

		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(),
					"Unit already existed with name: " + duplicateUnitName, null);
		}
	}

	@Override
	public Response<?> getAllProductsByIds(List<Long> ids) {
		List<Product> productList = productRepository.findAllById(ids);
		List<ProductDto> prodDto = new ArrayList<>();
		if (productList != null && productList.size() > 0) {
			for (Product prod : productList) {
				ProductDto dto = new ProductDto();
				dto.setProductName(prod.getName());
				dto.setId(prod.getId());
				prodDto.add(dto);
			}
		}
		return new Response<>(HttpStatus.OK.value(), "Product List By Ids.", prodDto);
	}

}
