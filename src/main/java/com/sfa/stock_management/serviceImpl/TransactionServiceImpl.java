package com.sfa.stock_management.serviceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sfa.stock_management.dto.ProductDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.StockUpdateDto;
import com.sfa.stock_management.dto.TemplateHeaderRequest;
import com.sfa.stock_management.dto.TransactionResponseDto;
import com.sfa.stock_management.model.ClientBalanceTrail;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.model.OrderProductMapping;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.TokenType;
import com.sfa.stock_management.model.Transaction;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.ClientBalanceTrailRepository;
import com.sfa.stock_management.repository.OrderProductMappingRepository;
import com.sfa.stock_management.repository.OrderRepository;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.TransactionRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.TransactionService;
import com.sfa.stock_management.util.Pagination;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ClientBalanceTrailRepository clientBalanceTrailRepository;

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private OrderProductMappingRepository orderProductMappingRepository;

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Override
	public Response<?> getAllTransactionByProductId(String sku, int pageNo, int pageSize, Long firmId, Long companyId)
			throws JsonMappingException, JsonProcessingException {

		Pagination<List<?>> pagination = new Pagination<>();
		Pageable pageRequest = Pageable.unpaged();
		if (pageSize > 0) {
			pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		}
		Optional<Product> product = null;
		if (firmId == 0) {
			product = productRepository.findBySkuAndCompanyIdAndFirmNull(sku, companyId);
		} else {
			product = productRepository.findBySkuAndFirmIdAndCompanyId(sku, firmId, companyId);
		}
		TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
		if (product != null && product.isPresent()) {
			DecimalFormat formatter = new DecimalFormat("#.##");
			ProductDto dto = new ProductDto();
			dto.setId(product.get().getId());
			dto.setCurrentQuantity(Double.parseDouble(formatter.format(product.get().getAvailableQuantity())));
			dto.setProductName(product.get().getName());
			dto.setSkuCode(product.get().getSku());
			dto.setHsnCode(product.get().getHsnCode());
			dto.setAllowOrderTillQuantity(product.get().getAllowOrderTillQuantity());
			dto.setMinReorderQuantity(product.get().getMinReOrderQuantity());
			dto.setStandardSellingPrice(product.get().getStandardSellingPrice());
			dto.setMinSellingPrice(product.get().getMinSellingPrice());
			dto.setPhysicalAvailableQuantity(
					Double.parseDouble(formatter.format(product.get().getPhysicalStockQuantity())));
			dto.setFirm(product.get().getFirm());
			dto.setCategory(product.get().getProductCategory());
			dto.setBlockStockOnOrderCreate(product.get().getBlockStockOnOrderCreate());
			dto.setAllowOrderWithoutStock(product.get().getAllowOrderWithoutStock());
			dto.setImageUrl(product.get().getImageUrl());
			dto.setOpeningQuantity(product.get().getOpeningQuantity());
			dto.setMaxNoOfSamples(product.get().getMaxNoOfSamples());
			if (product.get().getIsDetails() != null && product.get().getIsDetails().booleanValue() == true) {
				Object ob = product.get().getProductColumnDetails();
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
				dto.setIsDetails(product.get().getIsDetails());
				dto.setTemplateHeaderDetails(request);
			}

//			if (product.get().getUnit() == 1) {
//				dto.setUnitName(ProductUnit.BOX.getName());
//			} else {
//				dto.setUnitName(ProductUnit.PIECE.getName());
//			}

			if (product.get().getUnit() != null) {
				Optional<Unit> unitOptional = unitRepository.findById(product.get().getUnit());
				if (unitOptional != null && unitOptional.isPresent()) {
					dto.setUnitName(unitOptional.get().getName());
				}
			}
			transactionResponseDto.setProductDto(dto);
		}
		Page<Transaction> transactionList = transactionRepository.findAll(product.get().getId(), pageRequest);
		List<StockUpdateDto> stockDtos = new ArrayList<>();
		for (Transaction trn : transactionList) {
			StockUpdateDto stockDto = new StockUpdateDto();
			Optional<OrderProductMapping> orderProdMapData = orderProductMappingRepository
					.findAllByOrderIdAndProductIdAndIsSample(trn.getOrderId(), trn.getProductId(), trn.getIsSample());
			if (trn.getProcuredQuantity() != null) {

				if ((orderProdMapData != null && orderProdMapData.isPresent())
						&& orderProdMapData.get().getUnitId() != null && orderProdMapData.get().getUnitId()
								.longValue() != orderProdMapData.get().getProduct().getUnit().longValue()) {
					Optional<UnitConversionMapping> unitObj = unitConversionMappingRepository
							.findById(orderProdMapData.get().getUnitId());
					if (orderProdMapData.get().getUnitId() != null) {
						stockDto.setProcuredQuantity(trn.getProcuredQuantity() * unitObj.get().getSubUnitQty());
						stockDto.setUnitName(unitObj.get().getSubUnitName());
					} else {
						Optional<UnitConversionMapping> unitsObj = unitConversionMappingRepository
								.findById(orderProdMapData.get().getProduct().getUnit());
						stockDto.setUnitName(unitsObj.get().getSubUnitName());
						stockDto.setProcuredQuantity(trn.getProcuredQuantity());
					}

				} else {
					Optional<Product> prodObj=productRepository.findById(trn.getProductId());
					Optional<Unit> unitObj = unitRepository.findById(prodObj.get().getUnit());
					stockDto.setUnitName(unitObj.get().getName());
					stockDto.setProcuredQuantity(trn.getProcuredQuantity());
				}

			}
			if (trn.getDisbursedQuantity() != null) {
				if ((orderProdMapData != null && orderProdMapData.isPresent())
						&& orderProdMapData.get().getUnitId() != null && orderProdMapData.get().getUnitId()
								.longValue() != orderProdMapData.get().getProduct().getUnit().longValue()) {
					if (orderProdMapData.get().getUnitId() != null) {
						Optional<UnitConversionMapping> unitObj = unitConversionMappingRepository
								.findById(orderProdMapData.get().getUnitId());
						stockDto.setDisbursedQuantity(trn.getDisbursedQuantity() * unitObj.get().getSubUnitQty());
						stockDto.setUnitName(unitObj.get().getSubUnitName());
					} else {
						Optional<Unit> unitObj = unitRepository.findById(orderProdMapData.get().getProduct().getUnit());
						stockDto.setUnitName(unitObj.get().getName());
						stockDto.setDisbursedQuantity(trn.getDisbursedQuantity());
					}
				} else {
					Optional<Product> prodObj=productRepository.findById(trn.getProductId());
					Optional<Unit> unitObj = unitRepository.findById(prodObj.get().getUnit());
					stockDto.setUnitName(unitObj.get().getName());
					stockDto.setDisbursedQuantity(trn.getDisbursedQuantity());
				}
			}
			stockDto.setRemarks(trn.getRemarks());
			stockDto.setUpdateDate(trn.getCreatedAt());
			stockDto.setCreatedBy(trn.getCreatedBy());
			stockDto.setCreatedUserName(trn.getCreatedUserName());
			if (trn.getTokenType() != null) {
				stockDto.setTokenNumber(trn.getTokenType().getTokenType() + "(" + trn.getTokenNumber() + ")");
			}
//			stockDto.setTokenNumber(trn.getTokenNumber());
			if (trn.getColumnsObjectData() != null) {
				JSONArray array = new JSONArray(trn.getColumnsObjectData());
				ArrayList<Object> listdata = new ArrayList<Object>();
				for (Object ob : array) {
					listdata.add(ob.toString());
				}
				stockDto.setTemplateData(listdata);
			}
			if (trn.getOrderColumnData() != null) {
				JSONArray array = new JSONArray(trn.getOrderColumnData());
				ArrayList<Object> listdata = new ArrayList<Object>();
				for (Object ob : array) {
					listdata.add(ob.toString());
				}
				stockDto.setOrderedData(listdata);
			}

			if (trn.getIsOrder() != null) {
				stockDto.setIsOrder(trn.getIsOrder());
			}
			stockDtos.add(stockDto);
		}
		pagination.setData(stockDtos);
		pagination.setNumberOfElements(transactionList.getNumberOfElements());
		pagination.setTotalElements(transactionList.getTotalElements());
		pagination.setTotalPages(transactionList.getTotalPages());
		Response response = new Response<>();
		response.setData(transactionResponseDto.getProductDto());
		response.setPaginationData(pagination);
		response.setResponseCode(HttpStatus.OK.value());
		return response;
	}

	@Override
	public Response<?> updateStock(String sku, StockUpdateDto stockUpdateDto, Long firmId) {
		try {
			Optional<Product> product = null;
			if (firmId == 0) {
				product = productRepository.findBySkuAndFirmNull(sku);
			} else {
				product = productRepository.findBySkuAndFirmId(sku, firmId);
			}
			Transaction transaction = new Transaction();
			Transaction transactionObj = null;
			List<String> duplicateDataList = new ArrayList<>();
			List<String> mandetoryFieldList = new ArrayList<>();
			List<String> uniqueFieldList = new ArrayList<>();
			List<String> uniqueFieldDuplicateList = new ArrayList<>();
			if (stockUpdateDto.getTokenNumber() != null && !stockUpdateDto.getTokenNumber().isEmpty()
					&& stockUpdateDto.getTokenNumber() != "") {
				transactionObj = transactionRepository.findByTokenNumber(stockUpdateDto.getTokenTypeId(),
						stockUpdateDto.getTokenNumber());
			}
			List<TemplateHeaderRequest> headerColumns = new ArrayList<>();
			if (product.get().getProductColumnDetails() != null) {
				Object ob = product.get().getProductColumnDetails();
				ObjectMapper objectMapper = new ObjectMapper();
				String data = ob.toString();
				TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);

				for (TemplateHeaderRequest req : responseArray) {
					TemplateHeaderRequest tmpHeadReq = new TemplateHeaderRequest();
					tmpHeadReq.setHeaderName(req.getHeaderName());
					tmpHeadReq.setIsMandetory(req.getIsMandetory());
					headerColumns.add(tmpHeadReq);
				}
			}
			List<JsonObject> objectList = new ArrayList<>();
			if (transactionObj == null && stockUpdateDto.getIsProcured() != null
					&& stockUpdateDto.getIsProcured().booleanValue() == true) {
				if (sku != null && !sku.isEmpty()) {
					if (stockUpdateDto.getFile() != null) {
						XSSFWorkbook workbook = new XSSFWorkbook(stockUpdateDto.getFile().getInputStream());
						int numberOfSheets = workbook.getNumberOfSheets();
						List<String> headerList = new ArrayList<>();
						List<String> cellValueString = new ArrayList<>();
						for (int i = 0; i < numberOfSheets; i++) {
							Sheet sheet = workbook.getSheetAt(i);
							Row topRowData = sheet.getRow(0);
							int noOfTopData = 0;
							for (Cell topCell : topRowData) {
								headerList.add(topCell.toString());
								noOfTopData += 1;
							}
							int rowCount = 0;
							for (Row row : sheet) {

								String str = "";

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
										if (cellName.length() > 0) {
											char topAlphabet = cellName.charAt(0);
											Boolean isTopElementString = false;
											if ((topAlphabet >= 'A' && topAlphabet <= 'Z')
													|| (topAlphabet >= 'a' && topAlphabet <= 'z')) {
												isTopElementString = true;
											}

											if (!isTopElementString) {
												if (cel.toString().contains(".") && cel.toString().contains("E")) {
													String[] stringArray = cel.toString().split("E");
													List<String> wordList = Arrays.asList(stringArray);
													String firstString = wordList.get(0);
													String lastString = wordList.get(1);
													cellName = new DecimalFormat("#.##")
															.format(Double.parseDouble(firstString)
																	* Math.pow(10, Double.parseDouble(lastString)));
												}
												if (str == "") {
													str = str + cellName;
												} else {
													str = str + "/n" + cellName;
												}

												rowCount += 1;
											} else {
												if (str == "") {
													str = str + cellName;
												} else {
													str = str + "/n" + cellName;
												}

												rowCount += 1;
											}
										} else {
											if (str == "") {
												str = str + cellName;
											} else {
												str = str + "/n" + cellName;
											}

											rowCount += 1;
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

//							System.out.println((rowCount-noOfTopData)/noOfTopData);
						}

						List<String> updatedList = new ArrayList<>();
						for (int i = 1; i < cellValueString.size(); i++) {
							updatedList.add(cellValueString.get(i));
						}

						headerList.add("In-Stock");
						headerList.add("Date");
						for (String str : updatedList) {
							String[] commaSeparatedArray = str.split("/n");
							List<String> wordList = Arrays.asList(commaSeparatedArray);
							JsonObject obj = new JsonObject();

							for (int i = 0; i < headerList.size(); i++) {

								if (headerList.get(i).equals("In-Stock")) {
									if (stockUpdateDto.getIsProcured().booleanValue() == true) {
										obj.addProperty(headerList.get(i), true);
									} else {
										obj.addProperty(headerList.get(i), false);
									}
								} else {
									if (wordList.size() <= i) {
										obj.addProperty(headerList.get(i), "");
									} else {
										obj.addProperty(headerList.get(i), wordList.get(i));
									}

								}
								if (headerList.get(i).equals("Date")) {
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									if (stockUpdateDto.getUpdateDate() != null) {
										obj.addProperty(headerList.get(i),
												formatter.format(stockUpdateDto.getUpdateDate()));
									} else {
										obj.addProperty(headerList.get(i), formatter.format(new Date()));
									}

								}
							}
							objectList.add(obj);

						}
						if (stockUpdateDto.getIsProcured() != null
								&& stockUpdateDto.getIsProcured().booleanValue() == true) {
							product.get()
									.setAvailableQuantity(product.get().getAvailableQuantity() + objectList.size());
							product.get()
									.setTotalNoOfProducts(product.get().getTotalNoOfProducts() + objectList.size());
							product.get().setPhysicalStockQuantity(
									product.get().getPhysicalStockQuantity() + objectList.size());
							transaction.setProcuredQuantity((double) objectList.size());
						}
						if (stockUpdateDto.getIsDisbursed() != null
								&& stockUpdateDto.getIsDisbursed().booleanValue() == true) {
							product.get()
									.setAvailableQuantity(product.get().getAvailableQuantity() - objectList.size());
							product.get().setPhysicalStockQuantity(
									product.get().getPhysicalStockQuantity() - objectList.size());
							transaction.setDisbursedQuantity((double) objectList.size());
						}

						transaction.setColumnsObjectData(objectList.toString());

					} else {
						if (stockUpdateDto.getProcuredQuantity() != null) {
							product.get().setAvailableQuantity(
									product.get().getAvailableQuantity() + stockUpdateDto.getProcuredQuantity());
							product.get().setPhysicalStockQuantity(
									product.get().getPhysicalStockQuantity() + stockUpdateDto.getProcuredQuantity());
							transaction.setProcuredQuantity(stockUpdateDto.getProcuredQuantity());
							product.get().setTotalNoOfProducts(
									product.get().getTotalNoOfProducts() + stockUpdateDto.getProcuredQuantity());
						}
						if (stockUpdateDto.getDisbursedQuantity() != null) {
							product.get().setAvailableQuantity(
									product.get().getAvailableQuantity() - stockUpdateDto.getDisbursedQuantity());
							product.get().setPhysicalStockQuantity(
									product.get().getPhysicalStockQuantity() - stockUpdateDto.getDisbursedQuantity());
							transaction.setDisbursedQuantity(stockUpdateDto.getDisbursedQuantity());
						}
					}

				}
				if (product.get().getAvailableQuantity().longValue() > 9000000) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"You can't able to add more than 90000000 quantity in stock..", null);
				} else if (product.get().getAvailableQuantity().longValue() < 0) {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"Disbursed quantity should be less than the current stock quantity!!", null);
				} else {
					Boolean isValidFile = true;
					Boolean isDuplicate = false;
					Boolean isUniqueNull = false;
					Boolean isUniqueDuplicate = false;
					Boolean isMandetoryNull = false;
					List<TemplateHeaderRequest> columnsListFromDb = new ArrayList<>();
					if (product.get().getProductColumnDetails() != null) {
						Object ob = product.get().getProductColumnDetails();
						ObjectMapper objectMapper = new ObjectMapper();
						String data = ob.toString();
						TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
								TemplateHeaderRequest[].class);
						for (TemplateHeaderRequest req : responseArray) {
							TemplateHeaderRequest tmpHeadReq = new TemplateHeaderRequest();
							tmpHeadReq.setHeaderName(req.getHeaderName());
							tmpHeadReq.setIsMandetory(req.getIsMandetory());
							tmpHeadReq.setIsUnique(req.getIsUnique());
							columnsListFromDb.add(tmpHeadReq);
						}
						for (TemplateHeaderRequest req : columnsListFromDb) {
							for (Object obj : objectList) {
								JSONObject object = new JSONObject(obj.toString());
								if (object.has(req.getHeaderName())) {
									if (req.getIsMandetory().booleanValue() == true
											&& (object.get(req.getHeaderName()).equals("")
													|| object.get(req.getHeaderName()).equals(" "))) {
										String headerName = req.getHeaderName();
										mandetoryFieldList.add(headerName);
										isValidFile = false;
										isMandetoryNull = true;
									}
									if (req.getIsUnique() != null && req.getIsUnique().booleanValue() == true
											&& (object.get(req.getHeaderName()).equals("")
													|| object.get(req.getHeaderName()).equals(" "))) {
										String headerName = req.getHeaderName();
										uniqueFieldList.add(headerName);
										JSONArray jsonArray = new JSONArray(objectList.toString());
										boolean hasDuplicates = containsDuplicate(jsonArray, req.getHeaderName());
										if (hasDuplicates) {
											isUniqueDuplicate = true;
											String duplicateData = req.getHeaderName() + ":"
													+ object.getString(req.getHeaderName());
											uniqueFieldDuplicateList.add(duplicateData);
										}
										isValidFile = false;
										isUniqueNull = true;
									}
									if (req.getIsUnique() != null && req.getIsUnique().booleanValue() == true) {
//										String headerName=req.getHeaderName();
//										uniqueFieldList.add(headerName);
										JSONArray jsonArray = new JSONArray(objectList.toString());
										boolean hasDuplicates = containsDuplicate(jsonArray, req.getHeaderName());
										if (hasDuplicates) {
											isUniqueDuplicate = true;
											String duplicateData = req.getHeaderName() + ":"
													+ object.getString(req.getHeaderName());
											uniqueFieldDuplicateList.add(duplicateData);
											isValidFile = false;
										}

//										isUniqueNull=true;
									}
									if (req.getIsUnique() != null && req.getIsUnique().booleanValue() == true
											&& (!object.get(req.getHeaderName()).equals("")
													|| !object.get(req.getHeaderName()).equals(" "))
											&& stockUpdateDto.getIsProcured().booleanValue() == true) {

										String objectValueName = "\"" + req.getHeaderName() + "\":\""
												+ object.getString(req.getHeaderName()) + "\"";
										Optional<Transaction> transactionExist = transactionRepository
												.findByProductIdAndColumnDataLike(product.get().getId(),
														objectValueName);
										if (transactionExist != null && transactionExist.isPresent()) {
											String duplicateData = req.getHeaderName() + ":"
													+ object.getString(req.getHeaderName());
											duplicateDataList.add(duplicateData);
											isDuplicate = true;
											isValidFile = false;
										}
									}
//									if(req.getIsMandetory().booleanValue() == true
//											&& !object.get(req.getHeaderName()).equals("") && stockUpdateDto.getIsDisbursed().booleanValue()==true) {
//										String objectValueName=object.getString(req.getHeaderName());
//										Optional<Transaction> transactionExist=transactionRepository.findByProductIdAndColumnDataLike(product.get().getId(),objectValueName);
//										if(transactionExist!=null && transactionExist.isPresent()) {
//											JSONArray objectArray=new JSONArray(transactionExist.get().getColumnsObjectData());
//											for(Object objArr:objectArray) {
//												JSONObject jsnObj=new JSONObject(objArr.toString());
//												if(jsnObj.has(req.getHeaderName())) {
//													jsnObj.remove("In-Stock");
//													jsnObj.append("In-Stock", false);
//													
//												}
//											}
//										}
//									}
								} else {
									isValidFile = false;
								}
							}
						}
					}
					List<Transaction> trasaction = transactionRepository.findAllByProductId(product.get().getId());
					if (isDuplicate) {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Duplicate data not allow.",
								duplicateDataList);
					} else if (isUniqueDuplicate) {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Unique fields can't be blank/duplicate",
								uniqueFieldDuplicateList);
					} else if (isUniqueNull) {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Unique Field can't be blank.",
								uniqueFieldList);
					}
//					else if (trasaction.isEmpty() && trasaction.size() <= 0
//							&& objectList.size() < product.get().getAllowOrderTillQuantity()) {
//						return new Response<>(HttpStatus.BAD_REQUEST.value(), "In the first stock please provide above "
//								+ product.get().getAllowOrderTillQuantity() + " Quantity.", null);
//					}
					else if (isMandetoryNull) {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Mandatory Field can't be blank.",
								mandetoryFieldList);
					} else if (isValidFile.booleanValue() == true) {
						productRepository.save(product.get());
						transaction.setProductId(product.get().getId());
						transaction.setCreatedBy(stockUpdateDto.getCreatedBy());
						transaction.setCreatedUserName(stockUpdateDto.getCreatedUserName());
						transaction.setRemarks(stockUpdateDto.getRemarks());
						if (stockUpdateDto.getTokenTypeId() != null) {
							transaction.setTokenType(new TokenType(stockUpdateDto.getTokenTypeId()));
							transaction.setTokenNumber(stockUpdateDto.getTokenNumber());
						}

						transaction.setIsOrder(false);
						if (stockUpdateDto.getCreatedBy() != null) {
							transaction.setCreatedBy(stockUpdateDto.getCreatedBy());
						}
						if (stockUpdateDto.getCreatedUserName() != null) {
							transaction.setCreatedUserName(stockUpdateDto.getCreatedUserName());
						}
						if (stockUpdateDto.getUpdateDate() == null) {
							transaction.setCreatedAt(new Date());
						} else {
							transaction.setCreatedAt(stockUpdateDto.getUpdateDate());
						}

						transactionRepository.save(transaction);
						return new Response<>(HttpStatus.CREATED.value(), "Stock updated successfully!!", null);
					}

					else {
						return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a valid file.",
								mandetoryFieldList);
					}
				}
			} else if (transactionObj == null && stockUpdateDto.getIsDisbursed().booleanValue() == true) {
				JSONObject fromFrontEndObj = new JSONObject(stockUpdateDto.getOrderObjectData());
				JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
//				JSONArray columnData=new JSONArray(fromFrontEndObj.toString());
				Object ob = product.get().getProductColumnDetails();
				ObjectMapper objectMapper = new ObjectMapper();
				String data = ob.toString();
				Set<String> transactionObjectList = new HashSet<>();
				TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);
//				List<Transaction> transactionList=new ArrayList<>();
				for (TemplateHeaderRequest req : responseArray) {
					for (Object obj : columnData) {
						JSONObject object = new JSONObject(obj.toString());
						if (object.has(req.getHeaderName()) && req.getIsUnique() != null
								&& req.getIsUnique().booleanValue() == true) {
							String objectValueName = "\"" + req.getHeaderName() + "\":\""
									+ object.getString(req.getHeaderName()) + "\"";
							Optional<Transaction> transactionExist = transactionRepository
									.findByProductIdAndColumnDataLike(product.get().getId(), objectValueName);
//							ArrayList<Object> updatedObjectList=new ArrayList<>();
							if (transactionExist != null && transactionExist.isPresent()) {
//								List<String> transactionColumn=Arrays.asList(transactionExist.get().getColumnsObjectData());
								JSONArray transactionColumn = new JSONArray(
										transactionExist.get().getColumnsObjectData());

								for (Object str : transactionColumn) {
//									for(int i=0;i<transactionColumn.length();i++) {

									JSONObject jsnObject = new JSONObject(str.toString());
									if (obj.toString().equals(jsnObject.toString())
											&& jsnObject.getBoolean("In-Stock") == true) {
//											ObjectMapper mapper=new ObjectMapper();
										jsnObject.remove("In-Stock");
										Boolean stockStatus = false;
										jsnObject.accumulate("In-Stock", stockStatus);
										if (transactionObjectList.contains(jsnObject.toString())) {

										} else {
											transactionObjectList.add(jsnObject.toString());
										}
									} else {
										transactionObjectList.add(str.toString());
									}

								}

								transactionExist.get().setColumnsObjectData(transactionObjectList.toString());
								transactionObjectList = new HashSet<>();
								transactionRepository.save(transactionExist.get());

							}

						}
					}
				}
				Transaction trn = new Transaction();
				trn.setOrderColumnData(columnData.toString());
				if (stockUpdateDto.getUpdateDate() != null) {
					trn.setCreatedAt(stockUpdateDto.getUpdateDate());
				} else {
					trn.setCreatedAt(new Date());
				}
				trn.setCreatedBy(stockUpdateDto.getCreatedBy());
				trn.setCreatedUserName(stockUpdateDto.getCreatedUserName());
				Integer disbursedQuantity = columnData.length();
				trn.setDisbursedQuantity(Double.parseDouble(disbursedQuantity.toString()));
				trn.setIsOrder(false);
				trn.setProductId(product.get().getId());
				trn.setRemarks(stockUpdateDto.getRemarks());
				if (stockUpdateDto.getTokenTypeId() != null) {
					trn.setTokenNumber(stockUpdateDto.getTokenNumber());
					trn.setTokenType(new TokenType(stockUpdateDto.getTokenTypeId()));
				}

				transactionRepository.save(trn);
				product.get().setAvailableQuantity(product.get().getAvailableQuantity() - disbursedQuantity);
				product.get().setPhysicalStockQuantity(product.get().getPhysicalStockQuantity() - disbursedQuantity);
				productRepository.save(product.get());
				return new Response<>(HttpStatus.OK.value(), "Stock updated successfully.", null);

			} else if (transactionObj == null && (stockUpdateDto.getProcuredQuantity() != null
					|| stockUpdateDto.getDisbursedQuantity() != null)) {

				if (stockUpdateDto.getProcuredQuantity() != null) {
					product.get().setAvailableQuantity(
							product.get().getAvailableQuantity() + stockUpdateDto.getProcuredQuantity());
					if (product.get().getTotalNoOfProducts() != null) {
						product.get().setTotalNoOfProducts(
								product.get().getTotalNoOfProducts() + stockUpdateDto.getProcuredQuantity());
					} else {
						product.get().setTotalNoOfProducts(stockUpdateDto.getProcuredQuantity());
					}
					product.get().setPhysicalStockQuantity(
							product.get().getPhysicalStockQuantity() + stockUpdateDto.getProcuredQuantity());
					transaction.setProcuredQuantity(stockUpdateDto.getProcuredQuantity());
				}
				if (stockUpdateDto.getDisbursedQuantity() != null) {
					product.get().setAvailableQuantity(
							product.get().getAvailableQuantity() - stockUpdateDto.getDisbursedQuantity());
					product.get().setPhysicalStockQuantity(
							product.get().getPhysicalStockQuantity() - stockUpdateDto.getDisbursedQuantity());
					transaction.setDisbursedQuantity(stockUpdateDto.getDisbursedQuantity());
				}
				productRepository.save(product.get());
				transaction.setProductId(product.get().getId());
				transaction.setRemarks(stockUpdateDto.getRemarks());
				if (stockUpdateDto.getTokenTypeId() != null) {
					transaction.setTokenNumber(stockUpdateDto.getTokenNumber());
					transaction.setTokenType(new TokenType(stockUpdateDto.getTokenTypeId()));
				}
//				transaction.setTokenNumber(stockUpdateDto.getTokenNumber());
				transaction.setIsOrder(false);
				if (stockUpdateDto.getCreatedBy() != null) {
					transaction.setCreatedBy(stockUpdateDto.getCreatedBy());
				}
				if (stockUpdateDto.getCreatedUserName() != null) {
					transaction.setCreatedUserName(stockUpdateDto.getCreatedUserName());
				}
				if (stockUpdateDto.getUpdateDate() == null) {
					transaction.setCreatedAt(new Date());
				} else {
					transaction.setCreatedAt(stockUpdateDto.getUpdateDate());
				}

				transactionRepository.save(transaction);
				return new Response<>(HttpStatus.CREATED.value(), "Stock updated successfully!!", null);
			} else {
				return new Response<>(HttpStatus.BAD_REQUEST.value(), "Duplicate token number is not allowed!!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please provide a valid file.", null);
		}

	}

	public static boolean containsDuplicate(JSONArray jsonArray, String key) {
		Set<Object> uniqueValues = new HashSet();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Object value = jsonObject.opt(key); // Use opt() to handle cases where the key may not exist in the object
			if (value != null) {
				if (uniqueValues.contains(value)) {
					return true; // Found a duplicate
				}
				uniqueValues.add(value);
			}
		}

		return false; // No duplicates found
	}

	@Override
	public Double getOutStandingPriceForClient(Long clientId, String orderId) {
		List<ClientBalanceTrail> clientBalanceTrailData = clientBalanceTrailRepository
				.findAllByClientIdAndOrderId(clientId, orderId);
		Optional<Order> orderObj = orderRepository.findByOrderId(orderId);
		Double amount = 0D;
		DecimalFormat df = new DecimalFormat("#.##");
		if (clientBalanceTrailData != null) {
			for (ClientBalanceTrail trailData : clientBalanceTrailData) {
				if (trailData.getCreditAmount() != null) {
					amount = amount + trailData.getCreditAmount();
				}
				if (trailData.getDebitAmount() != null) {
					amount = amount - trailData.getDebitAmount();
				}
			}
			Double pendingAmount = (amount - orderObj.get().getTotalPrice()) + orderObj.get().getTotalPrice();
			String pendingAmountInString = df.format(pendingAmount);
			amount = Double.parseDouble(pendingAmountInString);
			amount = Math.floor(amount);
		}
		if (amount > 0) {
			amount = 0D;
		}
		return amount;
	}
}
