package com.sfa.stock_management.serviceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sfa.stock_management.constant.Constant;
import com.sfa.stock_management.dto.GstResponseDto;
import com.sfa.stock_management.dto.LocalityResponseDto;
import com.sfa.stock_management.dto.OrderDispatchDetailsDto;
import com.sfa.stock_management.dto.OrderDto;
import com.sfa.stock_management.dto.OrderPageResponseList;
import com.sfa.stock_management.dto.OrderResponseDto;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.ProductDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.dto.StateResponseDto;
import com.sfa.stock_management.dto.TemplateHeaderRequest;
import com.sfa.stock_management.dto.UnitResponseDto;
import com.sfa.stock_management.model.ClientBalanceDetails;
import com.sfa.stock_management.model.ClientBalanceTrail;
import com.sfa.stock_management.model.ClientProductPriceConfig;
import com.sfa.stock_management.model.Configuration;
import com.sfa.stock_management.model.Coupon;
import com.sfa.stock_management.model.EmailNotificationConfiguration;
import com.sfa.stock_management.model.Firm;
import com.sfa.stock_management.model.HsnGstMapping;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.model.OrderDeliveryDetails;
import com.sfa.stock_management.model.OrderDispatchDetails;
import com.sfa.stock_management.model.OrderPackedDetails;
import com.sfa.stock_management.model.OrderProductMapping;
import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.model.OrderTrail;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.ProductCatagory;
import com.sfa.stock_management.model.Transaction;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.ClientBalanceDetailsRepository;
import com.sfa.stock_management.repository.ClientBalanceTrailRepository;
import com.sfa.stock_management.repository.ConfigurationRepository;
import com.sfa.stock_management.repository.CouponRepository;
import com.sfa.stock_management.repository.EmailNotificationConfigurationRepository;
import com.sfa.stock_management.repository.FirmRepository;
import com.sfa.stock_management.repository.HsnGstMappingRepository;
import com.sfa.stock_management.repository.OrderDeliveryDetailsRepository;
import com.sfa.stock_management.repository.OrderDispatchDetailsRepository;
import com.sfa.stock_management.repository.OrderPackedDetailsRepository;
import com.sfa.stock_management.repository.OrderProductMappingRepository;
import com.sfa.stock_management.repository.OrderRepository;
import com.sfa.stock_management.repository.OrderStatusRepository;
import com.sfa.stock_management.repository.OrderTrailRepository;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.TransactionRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.EmailServiceVM;
import com.sfa.stock_management.service.OrderService;
import com.sfa.stock_management.util.DateUtil;
import com.sfa.stock_management.util.Pagination;

@Service
public class OrderServiceImpl implements OrderService {

	@Value("${sfa.backend.url}")
	private String sfaBackendUrl;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private OrderTrailRepository orderTrailRepository;

	@Autowired
	private OrderDeliveryDetailsRepository orderDeliveryDetailsRepository;

	@Autowired
	private OrderProductMappingRepository orderProductMappingRepository;

	@Autowired
	private EmailServiceVM emailServiceVM;

	@Autowired
	private ClientBalanceDetailsRepository clientBalanceDetailsRepository;

	@Autowired
	private ClientBalanceTrailRepository clientBalanceTrailRepository;

	@Autowired
	private EmailNotificationConfigurationRepository emailNotificationConfigurationRepository;

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	@Autowired
	private HsnGstMappingRepository hsnGstMappingRepository;

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private FirmRepository firmRepository;

	@Autowired
	private OrderDispatchDetailsRepository orderDispatchDetailsRepository;

	@Autowired
	private OrderPackedDetailsRepository orderPackedDetailsRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Override
	public Response<?> saveOrder(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {
		String transactionId = generateTransactionId(orderSaveRequestDto.getClientId());
		String orderId = generateOrderId(orderSaveRequestDto.getClientId());
		DecimalFormat df = new DecimalFormat("#.##");
		Order orderObj = new Order();
		String userEmail = "";
		String companyLogoUrl = "";
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.USER_INFO_BY_ID;
		JsonObject request = new JsonObject();
		request.addProperty("userIds", "[" + orderSaveRequestDto.getCreatedByUserId() + "]");
		Gson gson = new Gson();
//		String requestBody = gson.toJson(request);
		String requestBody = "{" + "\"userIds\"" + ":" + "[" + orderSaveRequestDto.getCreatedByUserId() + "]" + "}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		JSONObject jsnobject = new JSONObject(response.getBody());
		JSONArray jsonArray = jsnobject.getJSONArray("data");
		JSONObject obj = jsonArray.getJSONObject(0);
		if (obj.has("email")) {
			userEmail = obj.getString("email");
		}
		if (obj.has("companyLogoUrl")) {
			companyLogoUrl = obj.getString("companyLogoUrl");
			orderObj.setCompanyLogoUrl(companyLogoUrl);
		}
		if (orderSaveRequestDto.getCouponCode() != null && orderSaveRequestDto.getCouponCode() != "") {
			Optional<Coupon> couponObj = couponRepository.findByCouponCode(orderSaveRequestDto.getCouponCode());
			if (couponObj != null) {
				orderObj.setCoupon(couponObj.get());
			}
		}

		orderObj.setCreatedAt(new Date());
		orderObj.setClientId(orderSaveRequestDto.getClientId());
		orderObj.setStatus(new OrderStatus(1L));
		orderObj.setClientName(orderSaveRequestDto.getClientName());
		orderObj.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
		orderObj.setClientEmailId(orderSaveRequestDto.getClientEmailAddress());
		orderObj.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
		orderObj.setRemarks(orderSaveRequestDto.getOrderRemarks());
		Double cgstPrice = 0D;
		Double sgstPrice = 0D;
		Double igstPrice = 0D;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 6); // Adding 5 days
		String deliveryDate = sdf.format(c.getTime());
		orderObj.setDeliveryDate(deliveryDate);
		orderObj.setOrderId(orderId);
		orderObj.setDeliveryAddress(orderSaveRequestDto.getClientAddress());

		OrderTrail trailOrder = new OrderTrail();
		trailOrder.setOrderId(orderId);
		trailOrder.setOrderStatus(new OrderStatus(1L));
		trailOrder.setCreatedDate(new Date());
		orderTrailRepository.save(trailOrder);

		Long gstPercent = 0L;
		Double totalPriceAfterAddedGst = 0D;

		for (OrderDto ord : orderSaveRequestDto.getOrder()) {

			OrderProductMapping orderProductMapping = new OrderProductMapping();
			orderProductMapping.setOrderId(orderId);
			orderProductMapping.setProduct(ord.getProduct());
			orderProductMapping.setQuantity(ord.getQuantity());
			orderProductMapping.setTotalPrice(ord.getTotalPrice());
			orderProductMapping.setIsPriceEdited(ord.getIsPriceEdited());
			Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());

			Optional<HsnGstMapping> hsnGstData = hsnGstMappingRepository.findByHsnCode(productObj.get().getHsnCode());
			if (hsnGstData != null && hsnGstData.isPresent()) {
				gstPercent = Long.parseLong(hsnGstData.get().getGstPercentage().toString());
			} else {
				gstPercent = 16L;
			}
			if (orderSaveRequestDto.getClientStateId() == orderSaveRequestDto.getUserStateId()) {
				orderProductMapping.setCgst(gstPercent / 2);
				orderProductMapping.setSgst(gstPercent / 2);
			}
			if (orderSaveRequestDto.getClientStateId() != orderSaveRequestDto.getUserStateId()) {
				orderProductMapping.setIgst(gstPercent);
			}

			if (orderProductMapping.getCgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getCgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(ord.getTotalPrice() + result);
				orderProductMapping.setGstPrice(result);
				cgstPrice = cgstPrice + result;
				ord.setTotalCgstPrice(result);

			}
			if (orderProductMapping.getSgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getSgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(orderProductMapping.getAfterAddGstTotalPrice() + result);
				orderProductMapping.setGstPrice(orderProductMapping.getGstPrice() + result);
				sgstPrice = sgstPrice + result;
				ord.setTotalSgstPrice(result);

			}
			if (orderProductMapping.getIgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getIgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(ord.getTotalPrice() + result);
				orderProductMapping.setGstPrice(result);
				igstPrice = igstPrice + result;
				ord.setTotalIgstPrice(result);

			}
			totalPriceAfterAddedGst = totalPriceAfterAddedGst + orderProductMapping.getAfterAddGstTotalPrice();
			orderProductMappingRepository.save(orderProductMapping);

			if (productObj.get().getBlockStockOnOrderCreate() != null
					&& productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {
				Product product = productObj.get();
				product.setAvailableQuantity(product.getAvailableQuantity() - ord.getQuantity());
				productRepository.save(product);
			}
//			if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {
//				JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
//				JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
//				Object ob = productObj.get().getProductColumnDetails();
//				ObjectMapper objectMapper = new ObjectMapper();
//				String data = ob.toString();
//				List<String> orderColumnDataSet = new ArrayList<>();
//				Set<String> transactionObjectList = new HashSet<>();
//				TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);
//				for (TemplateHeaderRequest req : responseArray) {
//					for (Object obj : columnData) {
//						JSONObject object = new JSONObject(obj.toString());
//						if (object.has(req.getHeaderName()) && req.getIsUnique() != null
//								&& req.getIsUnique().booleanValue() == true) {
//							String objectValueName = "\"" + req.getHeaderName() + "\":\""
//									+ object.getString(req.getHeaderName()) + "\"";
//							Optional<Transaction> transactionExist = transactionRepository
//									.findByProductIdAndColumnDataLike(productObj.get().getId(), objectValueName);
//							if (transactionExist != null && transactionExist.isPresent()) {
//								JSONArray transactionColumn = new JSONArray(
//										transactionExist.get().getColumnsObjectData());
//
//								for (Object str : transactionColumn) {
////									for(int i=0;i<transactionColumn.length();i++) {
//
//									JSONObject jsnObject = new JSONObject(str.toString());
//									if (obj.toString().equals(jsnObject.toString())
//											&& jsnObject.getBoolean("In-Stock") == true) {
////											ObjectMapper mapper=new ObjectMapper();
//										jsnObject.remove("In-Stock");
//										Boolean stockStatus = false;
//										jsnObject.accumulate("In-Stock", stockStatus);
//										jsnObject.accumulate("orderId", orderId);
//										orderColumnDataSet.add(jsnObject.toString());
//										if (transactionObjectList.contains(jsnObject.toString())) {
//
//										} else {
//											transactionObjectList.add(jsnObject.toString());
//										}
//									} else {
//										transactionObjectList.add(str.toString());
//									}
//
//								}
//								transactionExist.get().setColumnsObjectData(transactionObjectList.toString());
//								transactionObjectList = new HashSet<>();
//								transactionRepository.save(transactionExist.get());
//							}
//
//						}
//					}
//				}
//				Transaction trn = new Transaction();
//				trn.setOrderColumnData(orderColumnDataSet.toString());
//				trn.setCreatedAt(new Date());
//				trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
//				trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
//				Integer disbursedQuantity = columnData.length();
//				trn.setDisbursedQuantity(Long.parseLong(disbursedQuantity.toString()));
//				trn.setIsOrder(true);
//				trn.setProductId(productObj.get().getId());
//				trn.setRemarks(orderId);
//				trn.setOrderId(orderId);
//				transactionRepository.save(trn);
//			} 
//			else {
			Transaction trn = new Transaction();
			trn.setProductId(ord.getProduct().getId());
			trn.setDisbursedQuantity(Double.parseDouble(ord.getQuantity().toString()));
			trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
			trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
			trn.setRemarks(orderId);
			trn.setOrderId(orderId);
			trn.setCreatedAt(new Date());
			trn.setIsOrder(true);
			transactionRepository.save(trn);
//			}

		}

		orderObj.setTotalCgstPrice(cgstPrice);
		orderObj.setTotalSgstPrice(sgstPrice);
		orderObj.setTotalIgstPrice(igstPrice);
		if (orderSaveRequestDto.getCouponPrice() != null && orderSaveRequestDto.getCouponPrice().toString() != "") {
			orderObj.setCouponPrice(orderSaveRequestDto.getCouponPrice());
			totalPriceAfterAddedGst = totalPriceAfterAddedGst - orderSaveRequestDto.getCouponPrice();
		}
		orderObj.setTotalPrice(totalPriceAfterAddedGst);
		orderRepository.save(orderObj);
		Optional<ClientBalanceDetails> clientBalanceObj = clientBalanceDetailsRepository
				.findByClientId(orderSaveRequestDto.getClientId());
		if (clientBalanceObj != null && clientBalanceObj.isPresent()) {
			clientBalanceObj.get().setLastTransactionAmount(Double.parseDouble(df.format(-totalPriceAfterAddedGst)));
			clientBalanceObj.get().setCurrentBalance(Double
					.parseDouble(df.format(clientBalanceObj.get().getCurrentBalance() - totalPriceAfterAddedGst)));
			clientBalanceObj.get().setLastTransactionDate(new Date());
			clientBalanceObj.get().setLastTransactionRemark(orderId);
			clientBalanceObj.get().setLastTransactionId(transactionId);
			clientBalanceDetailsRepository.save(clientBalanceObj.get());
			ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
			clientBalanceTrail.setClientId(clientBalanceObj.get().getClientId());
			clientBalanceTrail.setCreatedDate(new Date());
			clientBalanceTrail.setDebitAmount(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			clientBalanceTrail.setRemark(null);
			clientBalanceTrail.setTransactionDetails(orderId);
			clientBalanceTrail.setTransactionId(transactionId);
			clientBalanceTrailRepository.save(clientBalanceTrail);
		} else {
			ClientBalanceDetails clientBalanceDetails = new ClientBalanceDetails();
			clientBalanceDetails.setClientId(orderSaveRequestDto.getClientId());
			clientBalanceDetails.setLastTransactionDate(new Date());
			clientBalanceDetails.setLastTransactionAmount(Double.parseDouble(df.format(-totalPriceAfterAddedGst)));
			if (clientBalanceDetails.getCurrentBalance() != null) {
				clientBalanceDetails.setCurrentBalance(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			} else {
				clientBalanceDetails.setCurrentBalance(Double.parseDouble(df.format(0 - totalPriceAfterAddedGst)));
			}
			clientBalanceDetails.setLastTransactionRemark(orderId);
			clientBalanceDetails.setClientName(orderSaveRequestDto.getClientName());
			clientBalanceDetails.setLastTransactionId(transactionId);
			clientBalanceDetailsRepository.save(clientBalanceDetails);
			ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
			clientBalanceTrail.setClientId(orderSaveRequestDto.getClientId());
			clientBalanceTrail.setCreatedDate(new Date());
			clientBalanceTrail.setDebitAmount(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			clientBalanceTrail.setRemark(null);
			clientBalanceTrail.setTransactionDetails(orderId);
			clientBalanceTrail.setTransactionId(transactionId);
			clientBalanceTrailRepository.save(clientBalanceTrail);
		}

		if (orderSaveRequestDto.getClientEmailAddress() != null && orderSaveRequestDto.getClientEmailAddress() != ""
				&& !orderSaveRequestDto.getClientEmailAddress().isEmpty()) {
			orderSaveRequestDto.setOrderId(orderId);
			Configuration configMail = configurationRepository.findByConfigurationKey("ADMIN_EMAIL");
			String[] ccMails = null;
			String[] adminMails = null;
			if (configMail != null && configMail.getConfigurationValue() != null
					&& !configMail.getConfigurationValue().isEmpty()) {
				adminMails = configMail.getConfigurationValue().split(",");

			}
			if (userEmail != null || !userEmail.isEmpty() || userEmail != "") {
				if (adminMails != null) {
					ccMails = Arrays.copyOf(adminMails, adminMails.length + 1);
					ccMails[adminMails.length] = userEmail;
				} else {
					ArrayList<String> list = new ArrayList<>();
					list.add(userEmail);
					ccMails = list.toArray(new String[0]);
				}
			}
			if (ccMails != null) {
				orderSaveRequestDto.setCcMails(ccMails);
			}
			emailServiceVM.sendOrderCreatedEmail(orderSaveRequestDto);

		}
		return new Response<>(HttpStatus.CREATED.value(), "Order Created Succesfully", null);
	}

	public String generateOrderId(Long clientId) {
		List<Order> orderList = orderRepository.findAllByClientId(clientId);
		String orderId = "";
		LocalDate localDate = LocalDate.now();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		String monthString = "";
		if (month < 10) {
			monthString = "0" + month;
		}
		if (orderList != null && !orderList.isEmpty()) {
			orderId = "ORD" + "-" + monthString + "-" + year + "-" + clientId + "-" + (orderList.size() + 1);
		} else {
			orderId = "ORD" + "-" + monthString + "-" + year + "-" + clientId + "-" + 1;
		}
		return orderId;
	}

	@Override
	public Response<?> getAllOrdersByUserId(Long userId) {
		List<Order> orderList = orderRepository.findAllByUserId(userId);
		if (orderList != null) {
			return new Response<>(HttpStatus.OK.value(), "OK", orderList);
		} else {
			return new Response<>(HttpStatus.NO_CONTENT.value(), "No Data Available!!", null);
		}

	}

	@Override
	public Response<?> getAllOrdersByUserIdV2(Long hrmsUserId, Long userId) {
		List<OrderResponseDto> orderResponse = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.OUTWORK_HRMSUSER_LIST + hrmsUserId + "/" + userId;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, null, Response.class);
		JSONObject jsnobject = new JSONObject(response.getBody());
		List<Long> userIds = new ArrayList<>();
		if (jsnobject.has("data")) {
			JSONArray userArray = jsnobject.getJSONArray("data");
			for (Object obj : userArray) {
				JSONObject ob = new JSONObject(obj.toString());
				Long id = ob.getLong("userId");
				userIds.add(id);
			}
		}

		List<Order> orderData = orderRepository.findAllByUserIdIn(userIds);

		for (Order order : orderData) {
			OrderResponseDto orderResponseDto = new OrderResponseDto();
			orderResponseDto.setId(order.getId());
			orderResponseDto.setOrderId(order.getOrderId());
			List<ProductDto> productList = new ArrayList<>();
			List<OrderProductMapping> orderProductMappedData = orderProductMappingRepository
					.findAllByOrderId(order.getOrderId());
			Double totalProductPrice = 0D;
			Double totalProductPriceAfterAddedGst = 0D;
			Integer listSize = orderProductMappedData.size();
			orderResponseDto.setTotalNumberOfProducts(listSize.doubleValue());
			for (OrderProductMapping orderProductMap : orderProductMappedData) {
				ProductDto prodDto = new ProductDto();
				prodDto.setProductName(orderProductMap.getProduct().getName());
				prodDto.setTotalPrice(orderProductMap.getTotalPrice());
				prodDto.setNumberOfQuantity(orderProductMap.getQuantity().doubleValue());
				orderResponseDto.setClientName(order.getClientName());
				orderResponseDto.setCreatedAt(order.getCreatedAt());
				totalProductPrice = totalProductPrice + orderProductMap.getTotalPrice();
				totalProductPriceAfterAddedGst = totalProductPriceAfterAddedGst
						+ orderProductMap.getAfterAddGstTotalPrice();
				orderResponseDto.setDeliveryDate(order.getDeliveryDate());
				/// Oct 30 ------////////
				orderResponseDto.setMrp(orderProductMap.getMrp());
				orderResponseDto.setOrderStatus(order.getStatus());
				orderResponseDto.setCreatedBy(order.getCreatedUserName());
				if (order.getUpdatedAt() != null) {
					orderResponseDto.setLastStatusUpdatedOn(order.getUpdatedAt());
				} else {
					orderResponseDto.setLastStatusUpdatedOn(order.getCreatedAt());
				}
				productList.add(prodDto);
				orderResponseDto.setTotalPrice(totalProductPrice);
				DecimalFormat df = new DecimalFormat("#.##");
				String addedGstPriceString = df.format(totalProductPriceAfterAddedGst);

				orderResponseDto.setTotalProductPriceWithGst(Double.parseDouble(addedGstPriceString));
				orderResponseDto.setProductDto(productList);

			}
			orderResponse.add(orderResponseDto);

		}
		orderResponse.sort(Comparator.comparing(OrderResponseDto::getId).reversed());
		return new Response<>(HttpStatus.OK.value(), "Ok", orderResponse);
	}

	@Override
	public Response<?> getOrderByOrderId(String orderId) {
		List<Order> orderData = orderRepository.findAllByOrderId(orderId);
		return new Response<>(HttpStatus.OK.value(), "OK", orderData);
	}

	@Override
	public Response<?> updateOrderStatus(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {
		List<Order> orderList = orderRepository.findAllByOrderId(orderSaveRequestDto.getOrderId());
		DecimalFormat df = new DecimalFormat("#.##");
		Response<?> response = new Response<>();
		for (Order order : orderList) {
			if (orderSaveRequestDto.getOrderStatus().getId().longValue() < order.getStatus().getId().longValue()
					|| orderSaveRequestDto.getOrderStatus().getId().longValue() == order.getStatus().getId()
							.longValue()) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (order.getStatus().getId().longValue() + 1 != orderSaveRequestDto.getOrderStatus().getId()
					&& orderSaveRequestDto.getOrderStatus().getId() != 7
					&& orderSaveRequestDto.getOrderStatus().getId() != 6) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (order.getStatus().getId().longValue() == 7 || order.getStatus().getId().longValue() == 6) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Status updation is failed as the order is already cancelled/rejected...");
			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 4) {

				order.setStatus(orderSaveRequestDto.getOrderStatus());
				order.setUpdatedAt(new Date());
//				orderRepository.save(order);
				OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
				orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
				orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
				orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
//				orderDeliveryDetailsRepository.save(orderDeliveryDetails);
				List<OrderProductMapping> orderProdMapData = orderProductMappingRepository
						.findAllByOrderId(orderSaveRequestDto.getOrderId());
				Boolean isOrderDispatch = true;
				String productName = "";
				for (OrderProductMapping mapdata : orderProdMapData) {
					Optional<Product> productObj = productRepository.findById(mapdata.getProduct().getId());
					if (productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {
						productObj.get().setPhysicalStockQuantity(
								productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
//						productRepository.save(productObj.get());
						if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
							isOrderDispatch = false;
							productName = productObj.get().getName();
						}

					} else {

						if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
							isOrderDispatch = false;
							productName = productObj.get().getName();
						} else {
							productObj.get().setPhysicalStockQuantity(
									productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
							productObj.get().setAvailableQuantity(
									productObj.get().getAvailableQuantity() - mapdata.getQuantity());
						}
//   						productRepository.save(productObj.get());

					}

					if (isOrderDispatch) {
						productRepository.save(productObj.get());
					}

				}
				if (isOrderDispatch) {
					orderRepository.save(order);
					orderDeliveryDetailsRepository.save(orderDeliveryDetails);
					Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
							.findByClientIdAndStatusId(order.getClientId(),
									orderSaveRequestDto.getOrderStatus().getId());
					if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
						Optional<OrderStatus> orderStatus = orderStatusRepository
								.findById(orderSaveRequestDto.getOrderStatus().getId());
						emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
								orderSaveRequestDto.getOrderId(), order.getClientEmailId());
					}
					response.setMessage("Status updated successfully");
					response.setResponseCode(HttpStatus.CREATED.value());
				} else {
					return new Response<>(HttpStatus.BAD_REQUEST.value(),
							"You can't dispatch the order due to less physical availability of " + productName, null);
				}

			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 3) {
				List<Transaction> transactionList = new ArrayList<>();
				for (OrderDto ord : orderSaveRequestDto.getOrder()) {
					Optional<Transaction> transaction = transactionRepository
							.findByOrderIdAndProductId(order.getOrderId(), ord.getProduct().getId(), ord.getIsSample());
					if (transaction != null && transaction.isPresent()) {
						if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {
							Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());
							JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
							JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
							Object ob = productObj.get().getProductColumnDetails();
							ObjectMapper objectMapper = new ObjectMapper();
							String data = ob.toString();
							List<String> orderColumnDataSet = new ArrayList<>();
							Set<String> transactionObjectList = new HashSet<>();
							TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
									TemplateHeaderRequest[].class);
							for (TemplateHeaderRequest req : responseArray) {
								for (Object obj : columnData) {
									JSONObject object = new JSONObject(obj.toString());
									if (object.has(req.getHeaderName()) && req.getIsUnique() != null
											&& req.getIsUnique().booleanValue() == true) {
										String objectValueName = "\"" + req.getHeaderName() + "\":\""
												+ object.getString(req.getHeaderName()) + "\"";
										Optional<Transaction> transactionExist = transactionRepository
												.findByProductIdAndColumnDataLike(productObj.get().getId(),
														objectValueName);
										if (transactionExist != null && transactionExist.isPresent()) {
											JSONArray transactionColumn = new JSONArray(
													transactionExist.get().getColumnsObjectData());

											for (Object str : transactionColumn) {
//												for(int i=0;i<transactionColumn.length();i++) {

												JSONObject jsnObject = new JSONObject(str.toString());
												if (obj.toString().equals(jsnObject.toString())
														&& jsnObject.getBoolean("In-Stock") == true) {
//														ObjectMapper mapper=new ObjectMapper();
													jsnObject.remove("In-Stock");
													Boolean stockStatus = false;
													jsnObject.accumulate("In-Stock", stockStatus);
													jsnObject.accumulate("orderId", order.getOrderId());
													orderColumnDataSet.add(jsnObject.toString());
													if (transactionObjectList.contains(jsnObject.toString())) {

													} else {
														transactionObjectList.add(jsnObject.toString());
													}
												} else {
													transactionObjectList.add(str.toString());
												}

											}
											transactionExist.get()
													.setColumnsObjectData(transactionObjectList.toString());
											transactionObjectList = new HashSet<>();
											transactionRepository.save(transactionExist.get());
										}

									}
								}
							}
							transaction.get().setOrderColumnData(orderColumnDataSet.toString());
							transactionList.add(transaction.get());
						}
					}
				}
				transactionRepository.saveAll(transactionList);
				order.setStatus(orderSaveRequestDto.getOrderStatus());
				orderRepository.save(order);
				Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
						.findByClientIdAndStatusId(order.getClientId(), orderSaveRequestDto.getOrderStatus().getId());
				if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
					Optional<OrderStatus> orderStatus = orderStatusRepository
							.findById(orderSaveRequestDto.getOrderStatus().getId());
					emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
							orderSaveRequestDto.getOrderId(), order.getClientEmailId());
				}
				response.setResponseCode(HttpStatus.CREATED.value());
				response.setMessage("Status updated successfully");

			} else if (order.getStatus().getId().longValue() == 4
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 7
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 6)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order dispatched, Cannot cancel/reject the Order!!");
			} else if (order.getStatus().getId().longValue() == 5
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 7
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 6)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order delivered, Cannot cancel/reject the Order!!");
			} else {
				order.setStatus(orderSaveRequestDto.getOrderStatus());
				order.setUpdatedAt(new Date());
				if (orderSaveRequestDto.getOrderStatus().getId() == 5) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
					Date convertDate = DateUtil.convertUTCTOIST(new Date());
					String date = formatter.format(convertDate);
					order.setDeliveryDate(date);
				}
				orderRepository.save(order);
				if (order.getStatus().getId() == 6 || order.getStatus().getId() == 7) {
					List<OrderProductMapping> orderProductList = orderProductMappingRepository
							.findAllByOrderId(order.getOrderId());
					List<Product> productList = new ArrayList<>();
					Double addedGstPrice = 0D;
					for (OrderProductMapping orderProductMapping : orderProductList) {
						Optional<Product> product = productRepository
								.findById(orderProductMapping.getProduct().getId());
						if (product.get().getBlockStockOnOrderCreate().booleanValue() == true) {
							product.get().setAvailableQuantity(
									product.get().getAvailableQuantity() + orderProductMapping.getQuantity());
						}

						addedGstPrice = addedGstPrice + orderProductMapping.getAfterAddGstTotalPrice();
						productList.add(product.get());
						Optional<Transaction> transactionObj = transactionRepository.findByOrderIdAndProductId(
								order.getOrderId(), product.get().getId(), orderProductMapping.getIsSample());
						if (transactionObj != null && transactionObj.get().getOrderColumnData() != null
								&& transactionObj.get().getOrderColumnData() != "") {
							JSONArray columnData = new JSONArray(transactionObj.get().getOrderColumnData().toString());
							Object ob = product.get().getProductColumnDetails();
							ObjectMapper objectMapper = new ObjectMapper();
							String data = ob.toString();
							Set<String> transactionObjectList = new HashSet<>();
							TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
									TemplateHeaderRequest[].class);
							for (TemplateHeaderRequest req : responseArray) {
								for (Object obj : columnData) {
									JSONObject object = new JSONObject(obj.toString());
									if (object.has(req.getHeaderName()) && req.getIsUnique() != null
											&& req.getIsUnique().booleanValue() == true) {
										String objectValueName = "\"" + req.getHeaderName() + "\":\""
												+ object.getString(req.getHeaderName()) + "\"";
										Optional<Transaction> transactionExist = transactionRepository
												.findByProductIdAndColumnDataLike(product.get().getId(),
														objectValueName);
										if (transactionExist != null && transactionExist.isPresent()) {
											JSONArray transactionColumn = new JSONArray(
													transactionExist.get().getColumnsObjectData());

											for (Object str : transactionColumn) {
												JSONObject jsnObject = new JSONObject(str.toString());
												if (obj.toString().equals(jsnObject.toString())
														&& jsnObject.getBoolean("In-Stock") == false) {
													jsnObject.remove("In-Stock");
													jsnObject.remove("orderId");
													Boolean stockStatus = true;
													jsnObject.accumulate("In-Stock", stockStatus);
													if (transactionObjectList.contains(jsnObject.toString())) {

													} else {
														transactionObjectList.add(jsnObject.toString());
													}
												} else {
													transactionObjectList.add(str.toString());
												}

											}
											transactionExist.get()
													.setColumnsObjectData(transactionObjectList.toString());
											transactionObjectList = new HashSet<>();
											transactionRepository.save(transactionExist.get());
										}

									}
								}
							}
							Transaction trn = new Transaction();
							trn.setOrderColumnData(columnData.toString());
							trn.setCreatedAt(new Date());
							trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
							trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
							Integer procuredQuantity = columnData.length();
							trn.setProcuredQuantity(Double.parseDouble(procuredQuantity.toString()));
							trn.setIsOrder(true);
							trn.setProductId(orderProductMapping.getProduct().getId());
							trn.setRemarks(order.getOrderId());
//							trn.setOrderId(orderProductMapping.getOrderId());
							transactionRepository.save(trn);
						} else {
							Transaction trn = new Transaction();
							trn.setCreatedAt(new Date());
							trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
							trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
							trn.setProductId(product.get().getId());
							trn.setRemarks(order.getOrderId());
							trn.setIsOrder(true);
							trn.setProcuredQuantity(orderProductMapping.getQuantity().doubleValue());
							transactionRepository.save(trn);
						}

					}
					order.setDeliveryDate(null);
					orderRepository.save(order);
					productRepository.saveAll(productList);
					Optional<ClientBalanceDetails> clientBalanceDetails = clientBalanceDetailsRepository
							.findByClientId(order.getClientId());
					clientBalanceDetails.get().setCurrentBalance(Double
							.parseDouble(df.format(clientBalanceDetails.get().getCurrentBalance() + addedGstPrice)));
					clientBalanceDetails.get().setClientName(order.getClientName());
					clientBalanceDetails.get().setLastTransactionAmount(Double.parseDouble(df.format(addedGstPrice)));
					clientBalanceDetails.get().setLastTransactionDate(new Date());
					clientBalanceDetails.get().setLastTransactionRemark(order.getOrderId());
					clientBalanceDetailsRepository.save(clientBalanceDetails.get());
					ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
					clientBalanceTrail.setClientId(order.getClientId());
					clientBalanceTrail.setCreatedDate(new Date());
					clientBalanceTrail.setCreditAmount(Double.parseDouble(df.format(addedGstPrice)));
					clientBalanceTrail.setTransactionDetails(order.getOrderId());
					clientBalanceTrail.setRemark("Cancelled/Rejected");
					clientBalanceTrailRepository.save(clientBalanceTrail);
				}
				Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
						.findByClientIdAndStatusId(order.getClientId(), orderSaveRequestDto.getOrderStatus().getId());
				if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
					Optional<OrderStatus> orderStatus = orderStatusRepository
							.findById(orderSaveRequestDto.getOrderStatus().getId());
					emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
							orderSaveRequestDto.getOrderId(), order.getClientEmailId());
				}
				response.setResponseCode(HttpStatus.CREATED.value());
				response.setMessage("Status updated successfully");
			}
		}
		if (response.getResponseCode() == HttpStatus.CREATED.value()) {
			OrderTrail orderTrail = new OrderTrail();
			orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
			orderTrail.setOrderStatus(orderSaveRequestDto.getOrderStatus());
			orderTrail.setCreatedDate(new Date());
			orderTrailRepository.save(orderTrail);
		}
		return response;
	}

	@Override
	public Response<?> getOrderById(String orderId) throws JsonMappingException, JsonProcessingException {
		Optional<Order> order = orderRepository.findByOrderId(orderId);
		OrderResponseDto orderResponse = new OrderResponseDto();
		DecimalFormat df = new DecimalFormat("#.##");
		orderResponse.setTotalCgstPrice(Double.parseDouble(df.format(order.get().getTotalCgstPrice())));
		orderResponse.setTotalSgstPrice(Double.parseDouble(df.format(order.get().getTotalSgstPrice())));
		orderResponse.setTotalIgstPrice(Double.parseDouble(df.format(order.get().getTotalIgstPrice())));
		orderResponse.setCompanyLogoUrl(order.get().getCompanyLogoUrl());
		orderResponse.setFirm(order.get().getFirm());
		List<OrderProductMapping> orderProdData = orderProductMappingRepository.findAllByOrderId(orderId);
		Double totalProductPrice = 0D;
		Double totalProductPriceWithGst = 0D;
		Long totalNumberOfProducts = 0L;
		List<ProductDto> productList = new ArrayList<>();
		orderResponse.setOrderId(orderId);
		for (OrderProductMapping orderProdMap : orderProdData) {
			ProductDto prodDto = new ProductDto();
			Optional<Transaction> transactionData = transactionRepository.findByOrderIdAndProductId(
					orderProdMap.getOrderId(), orderProdMap.getProduct().getId(), orderProdMap.getIsSample());
			if (transactionData != null && transactionData.isPresent()) {
				if (transactionData.get().getOrderColumnData() != null) {
					JSONArray array = new JSONArray(transactionData.get().getOrderColumnData());
					ArrayList<Object> listdata = new ArrayList<Object>();
					for (Object ob : array) {
						listdata.add(ob.toString());
					}
					prodDto.setOrderColumnData(listdata);

				}

			}
			if (orderProdMap.getProduct().getIsDetails() != null
					&& orderProdMap.getProduct().getIsDetails().booleanValue() == true) {
				Object ob = orderProdMap.getProduct().getProductColumnDetails();
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
				prodDto.setTemplateHeaderDetails(request);
				List<Transaction> tranList = transactionRepository
						.findAllByProductId(orderProdMap.getProduct().getId());
				ArrayList<Object> listdata = new ArrayList<Object>();
//				ProductTemplateDataResponseForOrder dto = new ProductTemplateDataResponseForOrder();
				if (tranList != null && !tranList.isEmpty()) {
					for (Transaction trn : tranList) {
						if (trn.getColumnsObjectData() != null && !trn.getColumnsObjectData().isEmpty()) {
							JSONArray array = new JSONArray(trn.getColumnsObjectData());
							for (Object obj : array) {
								JSONObject jsonObject = new JSONObject(obj.toString());
								if (jsonObject.has("In-Stock")) {
									if (jsonObject.getBoolean("In-Stock") == true) {
										listdata.add(obj.toString());
									}
								}

							}
						}
					}

				}
//				dto.setTemplateData(listdata);
				prodDto.setTemplateData(listdata);
			}
			totalProductPrice = orderProdMap.getTotalPrice() + totalProductPrice;
			totalProductPriceWithGst = orderProdMap.getAfterAddGstTotalPrice() + totalProductPriceWithGst;
			prodDto.setProductName(orderProdMap.getProduct().getName());
			prodDto.setTotalPrice(Double.parseDouble(df.format(orderProdMap.getTotalPrice())));
			prodDto.setAfterAddedGstTotalPrice(Double.parseDouble(df.format(orderProdMap.getAfterAddGstTotalPrice())));
			prodDto.setCgst(orderProdMap.getCgst());
			prodDto.setIsSample(orderProdMap.getIsSample());
			prodDto.setSgst(orderProdMap.getSgst());
			prodDto.setId(orderProdMap.getProduct().getId());
			prodDto.setIgst(orderProdMap.getIgst());
			prodDto.setIsDetails(orderProdMap.getProduct().getIsDetails());
			prodDto.setGstPrice(Double.parseDouble(df.format(orderProdMap.getGstPrice())));
			prodDto.setNumberOfQuantity(orderProdMap.getQuantity().doubleValue());
			prodDto.setPerUnitPrice(orderProdMap.getTotalPrice() / orderProdMap.getQuantity().doubleValue());
			prodDto.setSkuCode(orderProdMap.getProduct().getSku());
			prodDto.setHsnCode(orderProdMap.getProduct().getHsnCode());
			prodDto.setCreatedBy(order.get().getCreatedUserName());
			prodDto.setMrp(orderProdMap.getMrp());
			prodDto.setPhysicalAvailableQuantity(orderProdMap.getProduct().getPhysicalStockQuantity());
			orderResponse.setClientName(order.get().getClientName());
			orderResponse.setCreatedAt(order.get().getCreatedAt());
			orderResponse.setDeliveryDate(order.get().getDeliveryDate());
			orderResponse.setOrderStatus(order.get().getStatus());
			orderResponse.setTotalNumberOfProducts(totalNumberOfProducts + orderProdMap.getQuantity());
			orderResponse.setId(order.get().getId());
			orderResponse.setCreatedBy(order.get().getCreatedUserName());
			orderResponse.setOrderRemarks(order.get().getRemarks());
			if (order.get().getUpdatedAt() != null) {
				orderResponse.setLastStatusUpdatedOn(order.get().getUpdatedAt());
			} else {
				orderResponse.setLastStatusUpdatedOn(order.get().getCreatedAt());
			}

			orderResponse.setClientAddress(order.get().getDeliveryAddress());
			List<OrderDispatchDetails> orderDispatchList = orderDispatchDetailsRepository
					.findAllByOrderProdMapId(orderProdMap.getId());
			List<OrderPackedDetails> packedOrderPackedDetails = orderPackedDetailsRepository
					.findAllByOrderProdMapId(orderProdMap.getId());
			Double recentlyPackedQty = 0D;
			if (packedOrderPackedDetails != null && packedOrderPackedDetails.size() > 0) {
				recentlyPackedQty = packedOrderPackedDetails.get(0).getQty();
			}
			Optional<UnitConversionMapping> unitConvObj = null;
			if (orderProdMap.getUnitId() != null) {
				unitConvObj = unitConversionMappingRepository.findById(orderProdMap.getUnitId());
			}
			if (orderProdMap.getUnitId() != null
					&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit()) {
				prodDto.setRecentlyPackedQty(recentlyPackedQty * unitConvObj.get().getSubUnitQty());
			} else {
				prodDto.setRecentlyPackedQty(recentlyPackedQty);
			}

			Double alreadyPAckedQty = 0D;
			for (OrderPackedDetails dtls : packedOrderPackedDetails) {
				alreadyPAckedQty = alreadyPAckedQty + dtls.getQty();
			}
			Double noOfProductQtyLeftToDispatch = 0D;
			Double alreadyDispatchedQty = 0D;

			if (orderDispatchList != null && orderDispatchList.size() > 0) {
				for (OrderDispatchDetails details : orderDispatchList) {
					noOfProductQtyLeftToDispatch = details.getQuantity() + noOfProductQtyLeftToDispatch;
					alreadyDispatchedQty = alreadyDispatchedQty + details.getQuantity();
				}
				if (order.get().getIsPartiallyPacked() != null
						&& order.get().getIsPartiallyPacked().booleanValue() == true) {
//					if (orderProdMap.getUnitId() != null
//							&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit()) {
//						noOfProductQtyLeftToDispatch = (alreadyPAckedQty * unitConvObj.get().getSubUnitQty())
//								- (noOfProductQtyLeftToDispatch * unitConvObj.get().getSubUnitQty());
//					} 
//					else {
					noOfProductQtyLeftToDispatch = alreadyPAckedQty - noOfProductQtyLeftToDispatch;
//					}

				} else {

					noOfProductQtyLeftToDispatch = orderProdMap.getQuantity() - noOfProductQtyLeftToDispatch;
				}

			} else {
				if (order.get().getIsPartiallyPacked() != null
						&& order.get().getIsPartiallyPacked().booleanValue() == true) {
					noOfProductQtyLeftToDispatch = alreadyPAckedQty;
				} else {
					noOfProductQtyLeftToDispatch = orderProdMap.getQuantity();
				}

			}
//			if (order.get().getStatus().getId() < 6) {
			DecimalFormat formatter = new DecimalFormat("#");
			if (orderProdMap.getUnitId() != null
					&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit().longValue()) {
				prodDto.setNoOfProductQtyLeftToDispatch(Double.parseDouble(
						formatter.format(noOfProductQtyLeftToDispatch * unitConvObj.get().getSubUnitQty())));
				prodDto.setAlreadyDispatchedQty(alreadyDispatchedQty * unitConvObj.get().getSubUnitQty());
			} else {
				prodDto.setNoOfProductQtyLeftToDispatch(
						Double.parseDouble(formatter.format(noOfProductQtyLeftToDispatch)));
				prodDto.setAlreadyDispatchedQty(alreadyDispatchedQty);
			}
//			prodDto.setNoOfProductQtyLeftToDispatch(noOfProductQtyLeftToDispatch);

//			String str=prodDto.getNoOfProductQtyLeftToDispatch().toString();
//			if(str.contains(".")) {
//				prodDto.setNoOfProductQtyLeftToDispatch(Double.parseDouble(formatter.format(prodDto.getNoOfProductQtyLeftToDispatch())));
//			}
//			if(orderProdMap.getUnitId() != null
//					&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit()) {
//				alreadyDispatchedQty=alreadyDispatchedQty*unitConvObj.get().getSubUnitQty();
//			}
//			prodDto.setAlreadyDispatchedQty(alreadyDispatchedQty);
//			} else {
//				prodDto.setNoOfProductQtyLeftToDispatch(0);
//			}

			Double noOfQtyLeftToPacked = 0D;
			Double alreadyPackedQty = 0D;
			if (packedOrderPackedDetails != null && packedOrderPackedDetails.size() > 0) {

				for (OrderPackedDetails packedDetails : packedOrderPackedDetails) {
					if (orderProdMap.getUnitId() != null
							&& orderProdMap.getUnitId().longValue() == orderProdMap.getProduct().getUnit()) {
						noOfQtyLeftToPacked = packedDetails.getQty() + noOfQtyLeftToPacked;
						alreadyPackedQty = alreadyPackedQty + packedDetails.getQty();
					} else if (orderProdMap.getUnitId() != null
							&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit()) {

						noOfQtyLeftToPacked = (packedDetails.getQty() * unitConvObj.get().getSubUnitQty())
								+ noOfQtyLeftToPacked;
						alreadyPackedQty = alreadyPackedQty
								+ (packedDetails.getQty() * unitConvObj.get().getSubUnitQty());
					} else {
						noOfQtyLeftToPacked = packedDetails.getQty() + noOfQtyLeftToPacked;
						alreadyPackedQty = alreadyPackedQty + packedDetails.getQty();
					}
				}
				if (orderProdMap.getUnitId() != null
						&& orderProdMap.getUnitId().longValue() != orderProdMap.getProduct().getUnit()) {
					noOfQtyLeftToPacked = (orderProdMap.getQuantity() * unitConvObj.get().getSubUnitQty())
							- noOfQtyLeftToPacked;
				} else {
					noOfQtyLeftToPacked = orderProdMap.getQuantity() - noOfQtyLeftToPacked;
				}

			} else {
				noOfQtyLeftToPacked = orderProdMap.getQuantity();
			}
			prodDto.setNoOfQtyLeftToPacked(noOfQtyLeftToPacked);
			prodDto.setAlreadyPackedQty(alreadyPackedQty);
			Optional<Unit> parentUnitObj = unitRepository.findById(orderProdMap.getProduct().getUnit());
			if (orderProdMap.getUnitId() == null) {
				Optional<Unit> unitObj = unitRepository.findById(orderProdMap.getProduct().getUnit());
				UnitResponseDto unitResponse = new UnitResponseDto();
				unitResponse.setId(unitObj.get().getId());
				unitResponse.setUnitName(unitObj.get().getName());
				unitResponse.setQuantity(1L);
				prodDto.setUnit(unitResponse);
				prodDto.setPerUnitName("Per " + parentUnitObj.get().getName());
			} else {
				if (orderProdMap.getProduct().getUnit().longValue() == orderProdMap.getUnitId().longValue()) {
					Optional<Unit> unitObj = unitRepository.findById(orderProdMap.getProduct().getUnit());
					UnitResponseDto unitResponse = new UnitResponseDto();
					unitResponse.setId(unitObj.get().getId());
					unitResponse.setUnitName(unitObj.get().getName());
					unitResponse.setQuantity(1L);
					prodDto.setUnit(unitResponse);
				} else {
					Optional<UnitConversionMapping> subUnitData = unitConversionMappingRepository
							.findById(orderProdMap.getUnitId());
					UnitResponseDto unitResponse = new UnitResponseDto();
					unitResponse.setId(subUnitData.get().getId());
					unitResponse.setQuantity(subUnitData.get().getSubUnitQty());
					unitResponse.setUnitName(subUnitData.get().getSubUnitName());
					if (order.get().getStatus().getId() < 3) {
						prodDto.setPhysicalAvailableQuantity(
								prodDto.getPhysicalAvailableQuantity() * subUnitData.get().getSubUnitQty());
						prodDto.setNoOfQtyLeftToPacked(
								prodDto.getNoOfQtyLeftToPacked() * subUnitData.get().getSubUnitQty());
						prodDto.setNumberOfQuantity(prodDto.getNumberOfQuantity() * subUnitData.get().getSubUnitQty());
						prodDto.setIsUnitChanged(true);
						prodDto.setPerSubUnitPrice(prodDto.getMrp() / subUnitData.get().getSubUnitQty());
						prodDto.setUnit(unitResponse);
					} else {
						prodDto.setNumberOfQuantity(prodDto.getNumberOfQuantity() * subUnitData.get().getSubUnitQty());
						prodDto.setPhysicalAvailableQuantity(
								prodDto.getPhysicalAvailableQuantity() * subUnitData.get().getSubUnitQty());
//						prodDto.setPerUnitPrice(prodDto.getPerUnitPrice()*subUnitData.get().getSubUnitQty());
						prodDto.setPerSubUnitPrice(prodDto.getMrp() / subUnitData.get().getSubUnitQty());
//						prodDto.setRecentlyPackedQty(
//								prodDto.getRecentlyPackedQty() * subUnitData.get().getSubUnitQty());
//						prodDto.setNoOfProductQtyLeftToDispatch(
//								prodDto.getNoOfProductQtyLeftToDispatch() * subUnitData.get().getSubUnitQty());
//						prodDto.setAlreadyDispatchedQty(
//								prodDto.getAlreadyDispatchedQty() * subUnitData.get().getSubUnitQty());
//						prodDto.setAlreadyPackedQty(prodDto.getAlreadyPackedQty() * subUnitData.get().getSubUnitQty());
//						prodDto.setNoOfQtyLeftToPacked(
//								prodDto.getNoOfQtyLeftToPacked() * subUnitData.get().getSubUnitQty());

//						prodDto.setNumberOfQuantity(prodDto.getNumberOfQuantity() * subUnitData.get().getSubUnitQty());
						prodDto.setPerSubUnitPrice(prodDto.getMrp() / subUnitData.get().getSubUnitQty());
						prodDto.setIsUnitChanged(true);
						prodDto.setUnit(unitResponse);
					}

				}
				prodDto.setPerUnitName("Per " + parentUnitObj.get().getName());
			}
			productList.add(prodDto);
		}
		orderResponse.setTotalPrice(Double.parseDouble(df.format(totalProductPrice)));
		orderResponse.setTotalProductPriceWithGst(Double.parseDouble(df.format(totalProductPriceWithGst)));
		if (order.get().getCouponPrice() != null) {
			orderResponse.setCouponPrice(order.get().getCouponPrice());
			orderResponse.setTotalProductPriceWithGst(Double.parseDouble(
					df.format(orderResponse.getTotalProductPriceWithGst() - orderResponse.getCouponPrice())));
		}
		if (order.get().getStatus().getId() > 1) {
			List<OrderDispatchDetails> orderDispatchList = orderDispatchDetailsRepository
					.findAllByOrderId(order.get().getOrderId());

			Map<Long, List<OrderDispatchDetailsDto>> orderDispatchMap = new HashMap<>();
			if (orderDispatchList != null && orderDispatchList.size() > 0) {

				for (OrderDispatchDetails details : orderDispatchList) {
					OrderDispatchDetailsDto detailsDto = details.convertToDto();
					Optional<Product> productObj = productRepository.findById(detailsDto.getProduct().getId());
					if (details.getOrderProductMapping() != null
							&& detailsDto.getOrderProductMapping().getUnitId() != null
							&& detailsDto.getOrderProductMapping().getUnitId().longValue() != productObj.get().getUnit()
									.longValue()) {
						Optional<UnitConversionMapping> unitConObj = unitConversionMappingRepository
								.findById(detailsDto.getOrderProductMapping().getUnitId());
						detailsDto.setQuantity(detailsDto.getQuantity() * unitConObj.get().getSubUnitQty());
						UnitResponseDto unitResponse = new UnitResponseDto();
						unitResponse.setUnitName(unitConObj.get().getSubUnitName());
						detailsDto.setUnit(unitResponse);
					} else {
						Optional<Unit> unit = unitRepository.findById(productObj.get().getUnit());
						UnitResponseDto unitResponse = new UnitResponseDto();
						unitResponse.setUnitName(unit.get().getName());
						detailsDto.setUnit(unitResponse);
					}
					if (orderDispatchMap.containsKey(detailsDto.getDispatchAttemptNo())) {
						orderDispatchMap.get(details.getDispatchAttemptNo()).add(detailsDto);
					} else {
						List<OrderDispatchDetailsDto> newList = new ArrayList<>();
						newList.add(detailsDto);
						orderDispatchMap.put(details.getDispatchAttemptNo(), newList);
					}
				}
				orderResponse.setOrderDispatchMap(orderDispatchMap);
			}

			orderResponse.setOrderDispatchDetails(orderDispatchList);
		}
		orderResponse.setProductDto(productList);
		List<OrderTrail> orderTrailData = orderTrailRepository.findAllByOrderId(orderId);
		orderResponse.setTrailDate(orderTrailData);
		if (order.get().getCoupon() != null) {
			orderResponse.setCoupon(order.get().getCoupon());
		}
		return new Response<>(HttpStatus.OK.value(), "OK", orderResponse);
	}

	@Override
	public Response<?> saveDeliveryDetails(OrderDeliveryDetails orderDeliveryDetails) {
		OrderDeliveryDetails details = orderDeliveryDetailsRepository.save(orderDeliveryDetails);
		return new Response<>(HttpStatus.CREATED.value(), "Delivery details saved successfully", details);
	}

	@Override
	public Response<?> getDispatchDetails(String orderId) {
		Optional<OrderDeliveryDetails> orderDeliveryDetails = orderDeliveryDetailsRepository.findByOrderId(orderId);
		if (orderDeliveryDetails != null && orderDeliveryDetails.isPresent()) {
			return new Response<>(HttpStatus.OK.value(), "OK", orderDeliveryDetails.get());
		} else {
			return new Response<>(HttpStatus.NO_CONTENT.value(), "No data available..", null);
		}

	}

	@Override
	public Response<?> getAllOrdersByUserIdV3(Long hrmsUserId, Long userId, SearchDto searchDto, int pageNo,
			int pageSize) throws JsonMappingException, JsonProcessingException {
		Pagination<List<?>> pagination = new Pagination<>();
		Pageable pageRequest = Pageable.unpaged();
		if (pageSize > 0) {
			pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		}
		List<OrderResponseDto> orderResponse = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.OUTWORK_HRMSUSER_LIST + hrmsUserId + "/" + userId;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, null, Response.class);
		JSONObject jsnobject = new JSONObject(response.getBody());
		List<Long> userIds = new ArrayList<>();
		if (jsnobject.has("data")) {
			JSONArray userArray = jsnobject.getJSONArray("data");
			for (Object obj : userArray) {
				JSONObject ob = new JSONObject(obj.toString());
				Long id = ob.getLong("userId");
				userIds.add(id);
			}
		}

		Page<Order> orderData = orderRepository.findAll(userIds, searchDto, pageRequest);
		for (Order order : orderData) {
			OrderResponseDto orderResponseDto = new OrderResponseDto();
			orderResponseDto.setId(order.getId());
			orderResponseDto.setOrderId(order.getOrderId());
			List<ProductDto> productList = new ArrayList<>();
			List<OrderProductMapping> orderProductMappedData = orderProductMappingRepository
					.findAllByOrderId(order.getOrderId());
			Double totalProductPrice = 0D;
			Double totalProductPriceAfterAddedGst = 0D;
			Integer listSize = orderProductMappedData.size();
			orderResponseDto.setTotalNumberOfProducts(listSize.doubleValue());
			orderResponseDto.setOrderRemarks(order.getRemarks());
			orderResponseDto.setFirm(order.getFirm());
			orderResponseDto.setProductCatagory(order.getProductCatagory());
			orderResponseDto.setLocalityId(order.getLocalityId());
			orderResponseDto.setLocalityName(order.getLocalityName());
			if (order.getStatus().getId() > 6 && order.getNoOfDeliveryAttempts() == null
					&& (order.getStatus().getId() != 8 || order.getStatus().getId() != 9)) {
				orderResponseDto.setNoOfDeliveryAttempts(0L);
			} else {
				orderResponseDto.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts());
			}
			for (OrderProductMapping orderProductMap : orderProductMappedData) {
				Optional<Order> orderObj = orderRepository.findByOrderId(orderProductMap.getOrderId());
				ProductDto prodDto = new ProductDto();
				Optional<Transaction> transactionData = transactionRepository.findByOrderIdAndProductIdV2(
						orderProductMap.getOrderId(), orderProductMap.getProduct().getId(),
						orderProductMap.getIsSample());
				if (transactionData != null && transactionData.isPresent()) {
					if (transactionData.get().getOrderColumnData() != null) {
						JSONArray array = new JSONArray(transactionData.get().getOrderColumnData());
						ArrayList<Object> listdata = new ArrayList<Object>();
						for (Object ob : array) {
							listdata.add(ob.toString());
						}
						prodDto.setTemplateData(listdata);
						prodDto.setIsDetails(true);
					}

				}
				if (orderProductMap.getProduct().getIsDetails() != null
						&& orderProductMap.getProduct().getIsDetails().booleanValue() == true) {
					Object ob = orderProductMap.getProduct().getProductColumnDetails();
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
					prodDto.setTemplateHeaderDetails(request);
				}
				prodDto.setProductName(orderProductMap.getProduct().getName());
				prodDto.setTotalPrice(orderProductMap.getTotalPrice());
				prodDto.setNumberOfQuantity(orderProductMap.getQuantity().doubleValue());
				List<OrderDispatchDetails> orderDispatchList = orderDispatchDetailsRepository
						.findAllByOrderProdMapId(orderProductMap.getId());
				List<OrderPackedDetails> packedOrderPackedDetails = orderPackedDetailsRepository
						.findAllByOrderProdMapId(orderProductMap.getId());
				Double recentlyPackedQty = 0D;
				if (packedOrderPackedDetails != null && packedOrderPackedDetails.size() > 0) {
					recentlyPackedQty = packedOrderPackedDetails.get(0).getQty();
				}
				prodDto.setRecentlyPackedQty(recentlyPackedQty);
				Double alreadyPAckedQty = 0D;
				for (OrderPackedDetails dtls : packedOrderPackedDetails) {
					alreadyPAckedQty = alreadyPAckedQty + dtls.getQty();
				}
				Double noOfProductQtyLeftToDispatch = 0D;
				Double alreadyDispatchedQty = 0D;
				if (orderDispatchList != null && orderDispatchList.size() > 0) {
					for (OrderDispatchDetails details : orderDispatchList) {
						noOfProductQtyLeftToDispatch = details.getQuantity() + noOfProductQtyLeftToDispatch;
						alreadyDispatchedQty = alreadyDispatchedQty + details.getQuantity();
					}
					if (orderObj.get().getIsPartiallyPacked() != null
							&& orderObj.get().getIsPartiallyPacked().booleanValue() == true) {
						noOfProductQtyLeftToDispatch = alreadyPAckedQty - noOfProductQtyLeftToDispatch;
					} else {
						noOfProductQtyLeftToDispatch = orderProductMap.getQuantity() - noOfProductQtyLeftToDispatch;
					}

				} else {
					if (orderObj.get().getIsPartiallyPacked() != null
							&& orderObj.get().getIsPartiallyPacked().booleanValue() == true) {
						noOfProductQtyLeftToDispatch = alreadyPAckedQty;
					} else {
						noOfProductQtyLeftToDispatch = orderProductMap.getQuantity();
					}

				}
//				if (order.get().getStatus().getId() < 6) {
				prodDto.setNoOfProductQtyLeftToDispatch(noOfProductQtyLeftToDispatch);
				if (orderProductMap.getUnitId() != null && orderProductMap.getUnitId().longValue() != orderProductMap
						.getProduct().getUnit().longValue()) {
					Optional<UnitConversionMapping> unitConvObj = unitConversionMappingRepository
							.findById(orderProductMap.getUnitId());
					prodDto.setAlreadyDispatchedQty(alreadyDispatchedQty * unitConvObj.get().getSubUnitQty());
					prodDto.setNumberOfQuantity(prodDto.getNumberOfQuantity() * unitConvObj.get().getSubUnitQty());
//					prodDto.setNumberOfQuantity(prodDto.getNumberOfQuantity()*unitConvObj.get().getSubUnitQty());
					prodDto.setUnitName(unitConvObj.get().getSubUnitName());
				} else {
					prodDto.setAlreadyDispatchedQty(alreadyDispatchedQty);
					Optional<Unit> unitObj = unitRepository.findById(orderProductMap.getProduct().getUnit());
					prodDto.setUnitName(unitObj.get().getName());
				}

//				} else {
//					prodDto.setNoOfProductQtyLeftToDispatch(0);
//				}

				Double noOfQtyLeftToPacked = 0D;
				Double alreadyPackedQty = 0D;
				if (packedOrderPackedDetails != null && packedOrderPackedDetails.size() > 0) {
					for (OrderPackedDetails packedDetails : packedOrderPackedDetails) {
						noOfQtyLeftToPacked = packedDetails.getQty() + noOfQtyLeftToPacked;
						alreadyPackedQty = alreadyPackedQty + packedDetails.getQty();
					}
					noOfQtyLeftToPacked = orderProductMap.getQuantity() - noOfQtyLeftToPacked;
				} else {
					noOfQtyLeftToPacked = orderProductMap.getQuantity();
				}
				prodDto.setNoOfQtyLeftToPacked(noOfQtyLeftToPacked);
				prodDto.setAlreadyPackedQty(alreadyPackedQty);
				orderResponseDto.setClientName(order.getClientName());

				orderResponseDto.setCreatedAt(order.getCreatedAt());
				orderResponseDto.setClientAddress(order.getDeliveryAddress());
				totalProductPrice = totalProductPrice + orderProductMap.getTotalPrice();
				totalProductPriceAfterAddedGst = totalProductPriceAfterAddedGst
						+ orderProductMap.getAfterAddGstTotalPrice();
				orderResponseDto.setDeliveryDate(order.getDeliveryDate());
				orderResponseDto.setOrderStatus(order.getStatus());
				orderResponseDto.setCreatedBy(order.getCreatedUserName());
				if (order.getUpdatedAt() != null) {
					orderResponseDto.setLastStatusUpdatedOn(order.getUpdatedAt());
				} else {
					orderResponseDto.setLastStatusUpdatedOn(order.getCreatedAt());
				}
				productList.add(prodDto);
				orderResponseDto.setTotalPrice(totalProductPrice);
				DecimalFormat df = new DecimalFormat("#.##");
//				if(order.getCouponPrice()!=null) {
//					totalProductPriceAfterAddedGst=totalProductPriceAfterAddedGst-order.getCouponPrice();
//				}
//				System.out.println(orderProductMap.getOrderId());
				String addedGstPriceString = "";
				if (order.getTotalPrice() != null) {
					addedGstPriceString = df.format(order.getTotalPrice());
				} else {
					addedGstPriceString = "0";
				}

				orderResponseDto.setTotalProductPriceWithGst(Double.parseDouble(addedGstPriceString));
				List<ClientBalanceTrail> clientBalanceTrailData = clientBalanceTrailRepository
						.findAllByClientIdAndOrderId(order.getClientId(), order.getOrderId());
				if (clientBalanceTrailData != null) {
					Double amount = 0D;
					for (ClientBalanceTrail trailData : clientBalanceTrailData) {
						if (trailData.getCreditAmount() != null) {
							amount = amount + trailData.getCreditAmount();
						}
						if (trailData.getDebitAmount() != null) {
							amount = amount - trailData.getDebitAmount();
						}
					}
					Double pendingAmount = (amount - orderResponseDto.getTotalProductPriceWithGst())
							+ orderResponseDto.getTotalProductPriceWithGst();
					String pendingAmountInString = df.format(pendingAmount);
					orderResponseDto.setPendingAmount(Double.parseDouble(pendingAmountInString));
				}
				orderResponseDto.setProductDto(productList);

			}
			orderResponse.add(orderResponseDto);

		}
		pagination.setData(orderResponse);
		pagination.setNumberOfElements(orderData.getNumberOfElements());
		pagination.setTotalElements(orderData.getTotalElements());
		pagination.setTotalPages(orderData.getTotalPages());
		Response responsedata = new Response<>();
		responsedata.setPaginationData(pagination);
		responsedata.setResponseCode(HttpStatus.OK.value());
		return responsedata;
	}

	@Override
	public Response<?> getOrderPagedataList(Long hrmsUserId, Long userId) {
		OrderPageResponseList orderPageResponseList = new OrderPageResponseList();
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.FETCH_VENDOR_LIST_FOR_HRMS;
		JsonObject request = new JsonObject();
		request.addProperty("userId", userId);
		request.addProperty("hrmsUserId", hrmsUserId);
		Gson gson = new Gson();
		String requestBody = gson.toJson(request);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		JSONObject jsnobject = new JSONObject(response.getBody());
		List<Long> userIds = new ArrayList<>();
		List<String> userName = new ArrayList<>();
		if (jsnobject.has("data")) {
			JSONArray userArray = jsnobject.getJSONArray("data");
			for (Object obj : userArray) {
				JSONObject ob = new JSONObject(obj.toString());
				Long id = ob.getLong("id");
				String name = "";
				name = ob.getString("name");
				if (ob.has("emailId")) {
					name = name + "(" + ob.getString("emailId") + ")";
				} else {
					name = name + "(" + ")";
				}
				userName.add(name);
				userIds.add(id);
			}
		}
		List<Order> orderData = orderRepository.findAllByUserIdIn(userIds);
		List<String> clientNameList = userName;
		List<String> orderIdList = new ArrayList<>();
		List<String> cretedUserNameList = new ArrayList<>();
		if (orderData != null && !orderData.isEmpty()) {
			for (Order ord : orderData) {
				String orderId = ord.getOrderId();
				String createdBy = ord.getCreatedUserName();
				orderIdList.add(orderId);
				cretedUserNameList.add(createdBy);
			}
		}
		orderPageResponseList.setClientNameList(clientNameList);
		orderPageResponseList.setCreatedByNameList(cretedUserNameList);
		orderPageResponseList.setOrderIdList(orderIdList);
		return new Response<>(HttpStatus.OK.value(), "OK", orderPageResponseList);
	}

	@Override
	public Response<?> getAllState() {
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.STATE_LIST;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, null, Response.class);
		List<StateResponseDto> stateResponseList = new ArrayList<>();
		JSONObject jsnobject = new JSONObject(response.getBody());
		if (jsnobject.has("data")) {
			JSONArray userArray = jsnobject.getJSONArray("data");
			for (Object obj : userArray) {
				JSONObject ob = new JSONObject(obj.toString());
				Long stateId = ob.getLong("stateCode");
				String stateName = ob.getString("name");
				stateResponseList.add(new StateResponseDto(stateId, stateName));
			}
		}
		return new Response<>(HttpStatus.OK.value(), "State List!!", stateResponseList);
	}

	public String generateTransactionId(Long clientId) {
		List<ClientBalanceTrail> orderList = clientBalanceTrailRepository.findAllByClientId(clientId);
		String orderId = "";
		LocalDate localDate = LocalDate.now();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		String monthString = "";
		if (month < 10) {
			monthString = "0" + month;
		}
		if (orderList != null && !orderList.isEmpty()) {
			orderId = "TRN" + "-" + monthString + "-" + year + "-" + clientId + "-" + (orderList.size() + 1);
		} else {
			orderId = "TRN" + "-" + monthString + "-" + year + "-" + clientId + "-" + 1;
		}
		return orderId;
	}

	@Override
	public Response<?> checkForBlockStockNoProduct(Long productId, Long quantity) {
		List<OrderProductMapping> orderProductMapping = orderProductMappingRepository.findAllByProductId(productId);
		Optional<Product> productData = productRepository.findById(productId);
		Double numberOfProductOrdered = 0D;
		if (orderProductMapping != null && orderProductMapping.size() > 0) {
			Set<String> orderIds = new HashSet<>();
			for (OrderProductMapping ord : orderProductMapping) {
				orderIds.add(ord.getOrderId());
			}
//			System.out.println(orderIds);
			List<Order> orderList = orderRepository.findAllByOrderIdIn(orderIds);
			orderList = orderList
					.stream().filter(e -> e.getStatus().getId().longValue() != 5
							|| e.getStatus().getId().longValue() != 6 || e.getStatus().getId().longValue() != 7)
					.collect(Collectors.toList());
			List<String> updatedOrderIds = orderList.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
			List<OrderProductMapping> orderProductMapUpdatedData = orderProductMappingRepository
					.findAllByOrderIdIn(updatedOrderIds);
			for (OrderProductMapping orderMap : orderProductMapUpdatedData) {
				numberOfProductOrdered = numberOfProductOrdered + orderMap.getQuantity();
			}
			if (numberOfProductOrdered + quantity > productData.get().getAvailableQuantity()) {
				return new Response<>(
						HttpStatus.BAD_REQUEST.value(), "You can order upto "
								+ (productData.get().getAvailableQuantity() - numberOfProductOrdered) + " quantity",
						null);
			} else {
				return new Response<>(HttpStatus.OK.value(), "OK", null);
			}
		} else {
			return new Response<>(HttpStatus.OK.value(), "OK", null);
		}

	}

	@Override
	public Response<?> getClientWiseProductPriceAfterPriceChange(Long productId, Long clientId, Long clientStateId,
			Long userStateId, Double totalQuantity, Double totalPrice, Double originalPrice, Long unitId) {

		GstResponseDto responseDto = new GstResponseDto();
		responseDto.setPerUnitPrice(totalPrice);
		DecimalFormat df = new DecimalFormat("#.##");
		Optional<Product> productObj = productRepository.findById(productId);
//		if (unitId == null || productObj.get().getUnit().longValue() == unitId.longValue()) {
//			totalQuantity = totalQuantity;
//		} else {
//			Optional<UnitConversionMapping> unitData = unitConversionMappingRepository.findById(unitId);
//			totalQuantity = totalQuantity / unitData.get().getSubUnitQty();
//		}
		totalPrice = totalPrice * totalQuantity;
		responseDto.setTotalPriceWithoutGst(Double.parseDouble(df.format(totalPrice)));
		Long gstPercent = 0L;
		Long cgstPercent = 0L;
		Long igstPercent = 0L;
		Long sgstPercent = 0L;
		responseDto.setOriginalPrice(originalPrice);

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

	@Override
	public Response<?> getAllOrderByClientId(String clientName) {
		List<Order> orderList = orderRepository.findAllByClientName(clientName);
		List<String> orderIdsList = orderList.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
		return new Response<>(HttpStatus.OK.value(), "OrderIds List..", orderIdsList);
	}

	// oct 30 ---

	public Response<?> saveOrderV2(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {
		String transactionId = generateTransactionId(orderSaveRequestDto.getClientId());
		String orderId = generateOrderId(orderSaveRequestDto.getClientId());
		DecimalFormat df = new DecimalFormat("#.##");
		Order orderObj = new Order();
		String userEmail = "";
		String companyLogoUrl = "";
		RestTemplate restTemplate = new RestTemplate();
		String url = sfaBackendUrl + Constant.USER_INFO_BY_ID;
		JsonObject request = new JsonObject();
		request.addProperty("userIds", "[" + orderSaveRequestDto.getCreatedByUserId() + "]");
		String requestBody = "{" + "\"userIds\"" + ":" + "[" + orderSaveRequestDto.getCreatedByUserId() + "]" + "}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		JSONObject jsnobject = new JSONObject(response.getBody());
		JSONArray jsonArray = jsnobject.getJSONArray("data");
		JSONObject obj = jsonArray.getJSONObject(0);
		if (obj.has("email")) {
			userEmail = obj.getString("email");
		}
		if (obj.has("companyLogoUrl")) {
			companyLogoUrl = obj.getString("companyLogoUrl");
			orderObj.setCompanyLogoUrl(companyLogoUrl);
		}
		if (orderSaveRequestDto.getCouponCode() != null && orderSaveRequestDto.getCouponCode() != "") {
			Optional<Coupon> couponObj = couponRepository.findByCouponCode(orderSaveRequestDto.getCouponCode());
			if (couponObj != null) {
				orderObj.setCoupon(couponObj.get());
			}
		}

		if (orderSaveRequestDto.getFirmId() != null) {
			if (orderSaveRequestDto.getFirmId() > 0) {
				Optional<Firm> firmId = firmRepository.findByFirmId(orderSaveRequestDto.getFirmId());
				if (firmId != null) {
					orderObj.setFirm(firmId.get());
				}
			} else {
				orderObj.setFirm(null);
			}

		}
		if (orderSaveRequestDto.getCategoryId() != null) {
			if (orderSaveRequestDto.getCategoryId() > 0) {

				orderObj.setProductCatagory(new ProductCatagory(orderSaveRequestDto.getCategoryId()));

			} else {
				orderObj.setProductCatagory(null);
			}

		}
		if (orderSaveRequestDto.getLocalityId() != null) {
			orderObj.setLocalityId(orderSaveRequestDto.getLocalityId());
			orderObj.setLocalityName(orderSaveRequestDto.getLocalityName());
		}
		orderObj.setCreatedAt(new Date());
		orderObj.setClientId(orderSaveRequestDto.getClientId());
		orderObj.setStatus(new OrderStatus(1L));
		orderObj.setClientName(orderSaveRequestDto.getClientName());
		orderObj.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
		orderObj.setClientEmailId(orderSaveRequestDto.getClientEmailAddress());
		orderObj.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
		orderObj.setRemarks(orderSaveRequestDto.getOrderRemarks());
		Double cgstPrice = 0D;
		Double sgstPrice = 0D;
		Double igstPrice = 0D;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 6); // Adding 5 days
		String deliveryDate = sdf.format(c.getTime());
		orderObj.setDeliveryDate(deliveryDate);
		orderObj.setOrderId(orderId);
		orderObj.setDeliveryAddress(orderSaveRequestDto.getClientAddress());

		OrderTrail trailOrder = new OrderTrail();
		trailOrder.setOrderId(orderId);
		trailOrder.setOrderStatus(new OrderStatus(1L));
		trailOrder.setCreatedDate(new Date());
		orderTrailRepository.save(trailOrder);

		Long gstPercent = 0L;
		Double totalPriceAfterAddedGst = 0D;

		for (OrderDto ord : orderSaveRequestDto.getOrder()) {
			Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());
			OrderProductMapping orderProductMapping = new OrderProductMapping();
			orderProductMapping.setOrderId(orderId);
			orderProductMapping.setProduct(ord.getProduct());
			if (ord.getUnitId() != null && ord.getUnitId().longValue() == productObj.get().getUnit()) {
				ord.setQuantity(ord.getQuantity());
				orderProductMapping.setQuantity(ord.getQuantity());
			} else if (ord.getUnitId() != null
					&& ord.getUnitId().longValue() != productObj.get().getUnit().longValue()) {
				Optional<UnitConversionMapping> unitConvData = unitConversionMappingRepository
						.findById(ord.getUnitId());
				ord.setQuantity(ord.getQuantity() / unitConvData.get().getSubUnitQty());
				orderProductMapping.setQuantity(ord.getQuantity());
			} else {
				orderProductMapping.setQuantity(ord.getQuantity());
			}
			orderProductMapping.setTotalPrice(ord.getTotalPrice());
			orderProductMapping.setIsPriceEdited(ord.getIsPriceEdited());
			orderProductMapping.setMrp(ord.getMrp());
			orderProductMapping.setUnitId(ord.getUnitId());

			Optional<HsnGstMapping> hsnGstData = hsnGstMappingRepository.findByHsnCode(productObj.get().getHsnCode());
			if (hsnGstData != null && hsnGstData.isPresent()) {
				gstPercent = Long.parseLong(hsnGstData.get().getGstPercentage().toString());
			} else {
				gstPercent = 18L;
			}
			if (orderSaveRequestDto.getClientStateId() == orderSaveRequestDto.getUserStateId()) {
				orderProductMapping.setCgst(gstPercent / 2);
				orderProductMapping.setSgst(gstPercent / 2);
			}
			if (orderSaveRequestDto.getClientStateId() != orderSaveRequestDto.getUserStateId()) {
				orderProductMapping.setIgst(gstPercent);
			}

			if (orderProductMapping.getCgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getCgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(ord.getTotalPrice() + result);
				orderProductMapping.setGstPrice(result);
				cgstPrice = cgstPrice + result;
				ord.setTotalCgstPrice(result);

			}
			if (orderProductMapping.getSgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getSgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(orderProductMapping.getAfterAddGstTotalPrice() + result);
				orderProductMapping.setGstPrice(orderProductMapping.getGstPrice() + result);
				sgstPrice = sgstPrice + result;
				ord.setTotalSgstPrice(result);

			}
			if (orderProductMapping.getIgst() != null) {
				Double result = 0D;
				result = (orderProductMapping.getIgst().doubleValue() / 100D) * ord.getTotalPrice().doubleValue();
				orderProductMapping.setAfterAddGstTotalPrice(ord.getTotalPrice() + result);
				orderProductMapping.setGstPrice(result);
				igstPrice = igstPrice + result;
				ord.setTotalIgstPrice(result);

			}
			totalPriceAfterAddedGst = totalPriceAfterAddedGst + orderProductMapping.getAfterAddGstTotalPrice();
			orderProductMapping.setIsSample(ord.getIsSample());
			orderProductMappingRepository.save(orderProductMapping);

			if (productObj.get().getBlockStockOnOrderCreate() != null
					&& productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {
				Product product = productObj.get();
				product.setAvailableQuantity(product.getAvailableQuantity() - ord.getQuantity());
				productRepository.save(product);
			}
//			if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {
//				JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
//				JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
//				Object ob = productObj.get().getProductColumnDetails();
//				ObjectMapper objectMapper = new ObjectMapper();
//				String data = ob.toString();
//				List<String> orderColumnDataSet = new ArrayList<>();
//				Set<String> transactionObjectList = new HashSet<>();
//				TemplateHeaderRequest[] responseArray = objectMapper.readValue(data, TemplateHeaderRequest[].class);
//				for (TemplateHeaderRequest req : responseArray) {
//					for (Object obj : columnData) {
//						JSONObject object = new JSONObject(obj.toString());
//						if (object.has(req.getHeaderName()) && req.getIsUnique() != null
//								&& req.getIsUnique().booleanValue() == true) {
//							String objectValueName = "\"" + req.getHeaderName() + "\":\""
//									+ object.getString(req.getHeaderName()) + "\"";
//							Optional<Transaction> transactionExist = transactionRepository
//									.findByProductIdAndColumnDataLike(productObj.get().getId(), objectValueName);
//							if (transactionExist != null && transactionExist.isPresent()) {
//								JSONArray transactionColumn = new JSONArray(
//										transactionExist.get().getColumnsObjectData());
//
//								for (Object str : transactionColumn) {
////									for(int i=0;i<transactionColumn.length();i++) {
//
//									JSONObject jsnObject = new JSONObject(str.toString());
//									if (obj.toString().equals(jsnObject.toString())
//											&& jsnObject.getBoolean("In-Stock") == true) {
////											ObjectMapper mapper=new ObjectMapper();
//										jsnObject.remove("In-Stock");
//										Boolean stockStatus = false;
//										jsnObject.accumulate("In-Stock", stockStatus);
//										jsnObject.accumulate("orderId", orderId);
//										orderColumnDataSet.add(jsnObject.toString());
//										if (transactionObjectList.contains(jsnObject.toString())) {
//
//										} else {
//											transactionObjectList.add(jsnObject.toString());
//										}
//									} else {
//										transactionObjectList.add(str.toString());
//									}
//
//								}
//								transactionExist.get().setColumnsObjectData(transactionObjectList.toString());
//								transactionObjectList = new HashSet<>();
//								transactionRepository.save(transactionExist.get());
//							}
//
//						}
//					}
//				}
//				Transaction trn = new Transaction();
//				trn.setOrderColumnData(orderColumnDataSet.toString());
//				trn.setCreatedAt(new Date());
//				trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
//				trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
//				Integer disbursedQuantity = columnData.length();
//				trn.setDisbursedQuantity(Long.parseLong(disbursedQuantity.toString()));
//				trn.setIsOrder(true);
//				trn.setProductId(productObj.get().getId());
//				trn.setRemarks(orderId);
//				trn.setOrderId(orderId);
//				transactionRepository.save(trn);
//			} 
//			else {
			Transaction trn = new Transaction();
			trn.setProductId(ord.getProduct().getId());
			trn.setDisbursedQuantity(Double.parseDouble(ord.getQuantity().toString()));
			trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
			trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
			trn.setRemarks(orderId);
			trn.setOrderId(orderId);
			trn.setCreatedAt(new Date());
			trn.setIsOrder(true);
			trn.setIsSample(ord.getIsSample());
			transactionRepository.save(trn);
//			}

		}

		orderObj.setTotalCgstPrice(cgstPrice);
		orderObj.setTotalSgstPrice(sgstPrice);
		orderObj.setTotalIgstPrice(igstPrice);
		if (orderSaveRequestDto.getCouponPrice() != null && orderSaveRequestDto.getCouponPrice().toString() != "") {
			orderObj.setCouponPrice(orderSaveRequestDto.getCouponPrice());
			totalPriceAfterAddedGst = totalPriceAfterAddedGst - orderSaveRequestDto.getCouponPrice();
		}
		orderObj.setTotalPrice(totalPriceAfterAddedGst);
		orderRepository.save(orderObj);
		Optional<ClientBalanceDetails> clientBalanceObj = clientBalanceDetailsRepository
				.findByClientId(orderSaveRequestDto.getClientId());
		if (clientBalanceObj != null && clientBalanceObj.isPresent()) {
			clientBalanceObj.get().setLastTransactionAmount(Double.parseDouble(df.format(-totalPriceAfterAddedGst)));
			clientBalanceObj.get().setCurrentBalance(Double
					.parseDouble(df.format(clientBalanceObj.get().getCurrentBalance() - totalPriceAfterAddedGst)));
			clientBalanceObj.get().setLastTransactionDate(new Date());
			clientBalanceObj.get().setLastTransactionRemark(orderId);
			clientBalanceObj.get().setLastTransactionId(transactionId);
			clientBalanceDetailsRepository.save(clientBalanceObj.get());
			ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
			clientBalanceTrail.setClientId(clientBalanceObj.get().getClientId());
			clientBalanceTrail.setCreatedDate(new Date());
			clientBalanceTrail.setDebitAmount(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			clientBalanceTrail.setRemark(null);
			clientBalanceTrail.setTransactionDetails(orderId);
			clientBalanceTrail.setTransactionId(transactionId);
			clientBalanceTrailRepository.save(clientBalanceTrail);
		} else {
			ClientBalanceDetails clientBalanceDetails = new ClientBalanceDetails();
			clientBalanceDetails.setClientId(orderSaveRequestDto.getClientId());
			clientBalanceDetails.setLastTransactionDate(new Date());
			clientBalanceDetails.setLastTransactionAmount(Double.parseDouble(df.format(-totalPriceAfterAddedGst)));
			if (clientBalanceDetails.getCurrentBalance() != null) {
				clientBalanceDetails.setCurrentBalance(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			} else {
				clientBalanceDetails.setCurrentBalance(Double.parseDouble(df.format(0 - totalPriceAfterAddedGst)));
			}
			clientBalanceDetails.setLastTransactionRemark(orderId);
			clientBalanceDetails.setClientName(orderSaveRequestDto.getClientName());
			clientBalanceDetails.setLastTransactionId(transactionId);
			clientBalanceDetailsRepository.save(clientBalanceDetails);
			ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
			clientBalanceTrail.setClientId(orderSaveRequestDto.getClientId());
			clientBalanceTrail.setCreatedDate(new Date());
			clientBalanceTrail.setDebitAmount(Double.parseDouble(df.format(totalPriceAfterAddedGst)));
			clientBalanceTrail.setRemark(null);
			clientBalanceTrail.setTransactionDetails(orderId);
			clientBalanceTrail.setTransactionId(transactionId);
			clientBalanceTrailRepository.save(clientBalanceTrail);
		}

		if (orderSaveRequestDto.getClientEmailAddress() != null && orderSaveRequestDto.getClientEmailAddress() != ""
				&& !orderSaveRequestDto.getClientEmailAddress().isEmpty()) {
			orderSaveRequestDto.setOrderId(orderId);
			Configuration configMail = configurationRepository.findByConfigurationKey("ADMIN_EMAIL");
			String[] ccMails = null;
			String[] adminMails = null;
			if (configMail != null && configMail.getConfigurationValue() != null
					&& !configMail.getConfigurationValue().isEmpty()) {
				adminMails = configMail.getConfigurationValue().split(",");

			}
			if (userEmail != null || !userEmail.isEmpty() || userEmail != "") {
				if (adminMails != null) {
					ccMails = Arrays.copyOf(adminMails, adminMails.length + 1);
					ccMails[adminMails.length] = userEmail;
				} else {
					ArrayList<String> list = new ArrayList<>();
					list.add(userEmail);
					ccMails = list.toArray(new String[0]);
				}
			}
			if (ccMails != null) {
				orderSaveRequestDto.setCcMails(ccMails);
			}
			emailServiceVM.sendOrderCreatedEmail(orderSaveRequestDto);

		}
		return new Response<>(HttpStatus.CREATED.value(), "Order Created Succesfully", null);
	}

	@Override
	public Response<?> getAllLocalityByCompanyId(Long companyId) {
		String url = sfaBackendUrl + Constant.GET_ALL_LOCALITY_BY_COMPANY_ID;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject request = new JsonObject();
		request.addProperty("companyId", companyId);
		Gson gson = new Gson();
		String requestBody = gson.toJson(request);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
		List<LocalityResponseDto> localityResponseList = new ArrayList<>();
		if (response != null) {
			JSONObject jsnobject = new JSONObject(response.getBody());
			if (jsnobject.has("data")) {
				JSONArray clientArray = jsnobject.getJSONArray("data");
				for (Object obj : clientArray) {
					LocalityResponseDto dto = new LocalityResponseDto();
					JSONObject ob = new JSONObject(obj.toString());
					if (ob.has("id")) {
						dto.setId(Long.parseLong(ob.get("id").toString()));
					}
					if (ob.has("name")) {
						dto.setLocalityName(ob.getString("name"));
					}
					if (ob.has("city")) {
						dto.setCityName(ob.getString("city"));
					}
					localityResponseList.add(dto);
				}
			}
		}
		return new Response<>(HttpStatus.OK.value(), "Locality List.", localityResponseList);
	}

	@Override
	public Response<?> updateOrderStatusV2(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {
		List<Order> orderList = orderRepository.findAllByOrderId(orderSaveRequestDto.getOrderId());
		DecimalFormat df = new DecimalFormat("#.##");
		Response<?> response = new Response<>();
		for (Order order : orderList) {
			if ((order.getStatus().getId() != 5 && orderSaveRequestDto.getOrderStatus().getId() >= 4)
					&& orderSaveRequestDto.getOrderStatus().getId().longValue() < order.getStatus().getId().longValue()
					|| orderSaveRequestDto.getOrderStatus().getId().longValue() == order.getStatus().getId()
							.longValue()) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 6) {
				if (order.getStatus().getId() == 3 || order.getStatus().getId() == 5) {
					order.setStatus(orderSaveRequestDto.getOrderStatus());
					order.setUpdatedAt(new Date());
					if (order.getNoOfDeliveryAttempts() != null) {

						order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
					} else {
						order.setNoOfDeliveryAttempts(1L);
					}
//    				orderRepository.save(order);
					OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
					orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
					orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
					orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
					orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
//    				orderDeliveryDetailsRepository.save(orderDeliveryDetails);
					List<OrderProductMapping> orderProdMapData = orderProductMappingRepository
							.findAllByOrderId(orderSaveRequestDto.getOrderId());
					Boolean isOrderDispatch = true;
					String productName = "";
					List<OrderDispatchDetails> dispatchList = new ArrayList<>();
					for (OrderProductMapping mapdata : orderProdMapData) {
						List<OrderDispatchDetails> orderDispatchExistList = orderDispatchDetailsRepository
								.findAllByOrderProdMapId(mapdata.getId());
						if (orderDispatchExistList != null && orderDispatchExistList.size() > 0) {
							Map<Long, Double> dispatchedProductMap = new HashMap<>();

							for (OrderDispatchDetails dispatch : orderDispatchExistList) {
								if (dispatchedProductMap.containsKey(dispatch.getProduct().getId())) {
									Double currentQuantity = dispatchedProductMap.get(dispatch.getProduct().getId());
									dispatchedProductMap.put(dispatch.getProduct().getId(),
											currentQuantity + dispatch.getQuantity());
								} else {
									dispatchedProductMap.put(dispatch.getProduct().getId(), dispatch.getQuantity());
								}
							}

							OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
							dispatchDetails.setCreatedAt(new Date());
							dispatchDetails.setIsSample(mapdata.getIsSample());
							dispatchDetails.setOrderId(mapdata.getOrderId());
							dispatchDetails.setProduct(mapdata.getProduct());
							dispatchDetails.setQuantity(
									mapdata.getQuantity() - dispatchedProductMap.get(mapdata.getProduct().getId()));
							dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
							dispatchDetails.setOrderProductMapping(mapdata);
							dispatchList.add(dispatchDetails);
						} else {
							OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
							dispatchDetails.setCreatedAt(new Date());
							dispatchDetails.setIsSample(mapdata.getIsSample());
							dispatchDetails.setOrderId(mapdata.getOrderId());
							dispatchDetails.setProduct(mapdata.getProduct());
							dispatchDetails.setQuantity(mapdata.getQuantity());
							dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
							dispatchList.add(dispatchDetails);
						}

						Optional<Product> productObj = productRepository.findById(mapdata.getProduct().getId());
						if (productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {
							productObj.get().setPhysicalStockQuantity(
									productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
//    						productRepository.save(productObj.get());
							if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
								isOrderDispatch = false;
								productName = productObj.get().getName();
								break;
							}

						} else {

							if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
								isOrderDispatch = false;

								productName = productObj.get().getName();
								break;
							} else {
								productObj.get().setPhysicalStockQuantity(
										productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
								productObj.get().setAvailableQuantity(
										productObj.get().getAvailableQuantity() - mapdata.getQuantity());
							}
//       						productRepository.save(productObj.get());

						}

						if (isOrderDispatch) {
							productRepository.save(productObj.get());
						}

					}

					if (isOrderDispatch.booleanValue() == true) {
						orderDispatchDetailsRepository.saveAll(dispatchList);
						orderRepository.save(order);
						orderDeliveryDetailsRepository.save(orderDeliveryDetails);
						Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
								.findByClientIdAndStatusId(order.getClientId(),
										orderSaveRequestDto.getOrderStatus().getId());
						if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
							Optional<OrderStatus> orderStatus = orderStatusRepository
									.findById(orderSaveRequestDto.getOrderStatus().getId());
							emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
									orderSaveRequestDto.getOrderId(), order.getClientEmailId());
						}
						response.setMessage("Status updated successfully");
						response.setResponseCode(HttpStatus.CREATED.value());
					} else {
						return new Response<>(HttpStatus.BAD_REQUEST.value(),
								"You can't dispatch the order due to less physical availability of " + productName,
								null);
					}
				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}

			} else if (orderSaveRequestDto.getOrderStatus().getId() == 4) {
				if (order.getStatus().getId() == 3 || order.getStatus().getId() == 5) {
					List<OrderProductMapping> orderProductMapData = orderProductMappingRepository
							.findAllByOrderId(order.getOrderId());
					Map<Long, Double> orderProdMap = new HashMap<>();
					for (OrderProductMapping mapping : orderProductMapData) {
						if (orderProdMap.containsKey(mapping.getProduct().getId())) {
							Double currentQuantity = orderProdMap.get(mapping.getProduct().getId());
							orderProdMap.put(mapping.getProduct().getId(), currentQuantity + mapping.getQuantity());
						} else {
							orderProdMap.put(mapping.getProduct().getId(), mapping.getQuantity());
						}
					}
					List<OrderDispatchDetails> orderDispatchList = orderDispatchDetailsRepository
							.findAllByOrderId(order.getOrderId());
					Map<Long, Double> requestProdMapData = new HashMap<>();
					for (OrderDto ordDto : orderSaveRequestDto.getOrderDispatchList()) {
						if (requestProdMapData.containsKey(ordDto.getProduct().getId())) {
							Double currentQuantity = requestProdMapData.get(ordDto.getProduct().getId());
							requestProdMapData.put(ordDto.getProduct().getId(), currentQuantity + ordDto.getQuantity());
						} else {
							requestProdMapData.put(ordDto.getProduct().getId(), ordDto.getQuantity());
						}
					}
					Map<Long, Double> dispatchedProductMap = new HashMap<>();
					Map<Long, Double> orderedMap = new HashMap<>();
					if (orderDispatchList != null && orderDispatchList.size() > 0) {
						for (OrderDispatchDetails dispatch : orderDispatchList) {
							if (dispatchedProductMap.containsKey(dispatch.getProduct().getId())) {
								Double currentQuantity = dispatchedProductMap.get(dispatch.getProduct().getId());
								dispatchedProductMap.put(dispatch.getProduct().getId(),
										currentQuantity + dispatch.getQuantity());
							} else {
								dispatchedProductMap.put(dispatch.getProduct().getId(), dispatch.getQuantity());
							}
						}
					} else {
						for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
							if (orderedMap.containsKey(dto.getProduct().getId())) {
								Double currentQuantity = orderedMap.get(dto.getProduct().getId());
								orderedMap.put(dto.getProduct().getId(), currentQuantity + dto.getQuantity());
							} else {
								orderedMap.put(dto.getProduct().getId(), dto.getQuantity());
							}
						}
					}

					Boolean isAbleToDispatch = true;
					Set<Long> orderProdKeys = orderProdMap.keySet();
					Set<Long> dispatchedKeys = null;
					if (!dispatchedProductMap.isEmpty()) {
						dispatchedKeys = dispatchedProductMap.keySet();
					} else {
						dispatchedKeys = orderedMap.keySet();
					}
					if (!orderProdKeys.equals(dispatchedKeys)) {
						isAbleToDispatch = false;
					} else {
						for (Long key : orderProdKeys) {
							Double orderQuantity = orderProdMap.get(key);
							Double dispatchedQuantity = 0D;
							if (!dispatchedProductMap.isEmpty()) {
								if (requestProdMapData.containsKey(key)) {
									dispatchedQuantity = dispatchedProductMap.get(key) + requestProdMapData.get(key);
								} else {
									dispatchedQuantity = dispatchedProductMap.get(key);
								}
							} else {
								if (orderedMap.containsKey(key)) {
									dispatchedQuantity = orderedMap.get(key);
								} else {
									dispatchedQuantity = orderedMap.get(key);
								}
							}

							if (orderQuantity != dispatchedQuantity) {
								isAbleToDispatch = false;
								break;
							}
						}
					}
					Boolean isOrderDispatch = true;
					String productName = "";
					List<Product> productList = new ArrayList<>();
					for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
						Optional<Product> productObj = productRepository.findById(dto.getProduct().getId());
						if (productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {
							productObj.get().setPhysicalStockQuantity(
									productObj.get().getPhysicalStockQuantity() - dto.getQuantity());
//    						productRepository.save(productObj.get());
							if (productObj.get().getPhysicalStockQuantity() < dto.getQuantity()) {
								isOrderDispatch = false;
								productName = productObj.get().getName();
								break;
							}

						} else {

							if (productObj.get().getPhysicalStockQuantity() < dto.getQuantity()) {
								isOrderDispatch = false;
								productName = productObj.get().getName();
								break;
							} else {
								productObj.get().setPhysicalStockQuantity(
										productObj.get().getPhysicalStockQuantity() - dto.getQuantity());
								productObj.get().setAvailableQuantity(
										productObj.get().getAvailableQuantity() - dto.getQuantity());
							}
//       						productRepository.save(productObj.get());

						}

						if (isOrderDispatch) {
							productList.add(productObj.get());
//							productRepository.save(productObj.get());
						}
					}

					if (isOrderDispatch.booleanValue() == true) {
						if (isAbleToDispatch.booleanValue() == true) {
							productRepository.saveAll(productList);
							order.setStatus(new OrderStatus(6L));
							if (order.getNoOfDeliveryAttempts() != null) {
								order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
							} else {
								order.setNoOfDeliveryAttempts(1L);
							}
							OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
							orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
							orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
							orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
							orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
							orderDeliveryDetailsRepository.save(orderDeliveryDetails);
							orderRepository.save(order);
							List<OrderDispatchDetails> orderDispatchLists = new ArrayList<>();
							for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
								OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
								dispatchDetails.setOrderId(order.getOrderId());
								dispatchDetails.setProduct(dto.getProduct());
								dispatchDetails.setQuantity(dto.getQuantity());
								dispatchDetails.setCreatedAt(new Date());
								dispatchDetails.setIsSample(dto.getIsSample());
								dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
								Optional<OrderProductMapping> orderProdMapObj = orderProductMappingRepository
										.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(),
												dto.getProduct().getId(), dto.getIsSample());
								dispatchDetails.setOrderProductMapping(orderProdMapObj.get());
								orderDispatchLists.add(dispatchDetails);
							}
							orderDispatchDetailsRepository.saveAll(orderDispatchLists);
							OrderTrail orderTrail = new OrderTrail();
							orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
							orderTrail.setOrderStatus(new OrderStatus(6L));
							orderTrail.setCreatedDate(new Date());
							orderTrailRepository.save(orderTrail);
							Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
									.findByClientIdAndStatusId(order.getClientId(), 6L);
							if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
								Optional<OrderStatus> orderStatus = orderStatusRepository
										.findById(orderSaveRequestDto.getOrderStatus().getId());
								emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
										orderSaveRequestDto.getOrderId(), order.getClientEmailId());

							}
							response.setResponseCode(HttpStatus.OK.value());
							response.setMessage("Status updated successfully");

						} else {
							order.setStatus(orderSaveRequestDto.getOrderStatus());
							if (order.getNoOfDeliveryAttempts() != null) {
								order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
							} else {

								order.setStatus(orderSaveRequestDto.getOrderStatus());
								if (order.getNoOfDeliveryAttempts() != null) {
									order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
								} else {
									order.setNoOfDeliveryAttempts(1L);
								}
								OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
								orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
								orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
								orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
								orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
								orderDeliveryDetailsRepository.save(orderDeliveryDetails);
								orderRepository.save(order);
								List<OrderDispatchDetails> orderDispatchLists = new ArrayList<>();
								for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
									OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
									dispatchDetails.setOrderId(order.getOrderId());
									dispatchDetails.setProduct(dto.getProduct());
									dispatchDetails.setQuantity(dto.getQuantity());
									dispatchDetails.setCreatedAt(new Date());
									dispatchDetails.setIsSample(dto.getIsSample());
									dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
									Optional<OrderProductMapping> orderProdMapObj = orderProductMappingRepository
											.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(),
													dto.getProduct().getId(), dto.getIsSample());
									dispatchDetails.setOrderProductMapping(orderProdMapObj.get());
									orderDispatchLists.add(dispatchDetails);
								}
								orderDispatchDetailsRepository.saveAll(orderDispatchLists);
								Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
										.findByClientIdAndStatusId(order.getClientId(), 6L);
								if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
									Optional<OrderStatus> orderStatus = orderStatusRepository
											.findById(orderSaveRequestDto.getOrderStatus().getId());

									emailServiceVM.sendDispatchProductsMail(orderStatus.get().getName(),
											orderSaveRequestDto.getOrderId(), order.getClientEmailId(),
											orderSaveRequestDto.getOrderDispatchList());
								}
								response.setResponseCode(HttpStatus.CREATED.value());
								response.setMessage("Status updated successfully");
								order.setNoOfDeliveryAttempts(1L);
							}
						}

					} else {
						return new Response<>(HttpStatus.BAD_REQUEST.value(),
								"You can't dispatch the order due to less physical availability of " + productName,
								null);
					}
				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}
			} else if ((order.getStatus().getId().longValue() + 1 != orderSaveRequestDto.getOrderStatus().getId())
					&& orderSaveRequestDto.getOrderStatus().getId() != 9
					&& orderSaveRequestDto.getOrderStatus().getId() != 8) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (order.getStatus().getId().longValue() == 9 || order.getStatus().getId().longValue() == 8) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Status updation is failed as the order is already cancelled/rejected...");
			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 3) {
				List<Transaction> transactionList = new ArrayList<>();
				for (OrderDto ord : orderSaveRequestDto.getOrder()) {
					Optional<Transaction> transaction = transactionRepository
							.findByOrderIdAndProductId(order.getOrderId(), ord.getProduct().getId(), ord.getIsSample());
					if (transaction != null && transaction.isPresent()) {
						if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {
							Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());
							JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
							JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
							Object ob = productObj.get().getProductColumnDetails();
							ObjectMapper objectMapper = new ObjectMapper();
							String data = ob.toString();
							List<String> orderColumnDataSet = new ArrayList<>();
							Set<String> transactionObjectList = new HashSet<>();
							TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
									TemplateHeaderRequest[].class);
							for (TemplateHeaderRequest req : responseArray) {
								for (Object obj : columnData) {
									JSONObject object = new JSONObject(obj.toString());
									if (object.has(req.getHeaderName()) && req.getIsUnique() != null
											&& req.getIsUnique().booleanValue() == true) {
										String objectValueName = "\"" + req.getHeaderName() + "\":\""
												+ object.getString(req.getHeaderName()) + "\"";
										Optional<Transaction> transactionExist = transactionRepository
												.findByProductIdAndColumnDataLike(productObj.get().getId(),
														objectValueName);
										if (transactionExist != null && transactionExist.isPresent()) {
											JSONArray transactionColumn = new JSONArray(
													transactionExist.get().getColumnsObjectData());

											for (Object str : transactionColumn) {
//												for(int i=0;i<transactionColumn.length();i++) {

												JSONObject jsnObject = new JSONObject(str.toString());
												if (obj.toString().equals(jsnObject.toString())
														&& jsnObject.getBoolean("In-Stock") == true) {
//														ObjectMapper mapper=new ObjectMapper();
													jsnObject.remove("In-Stock");
													Boolean stockStatus = false;
													jsnObject.accumulate("In-Stock", stockStatus);
													jsnObject.accumulate("orderId", order.getOrderId());
													orderColumnDataSet.add(jsnObject.toString());
													if (transactionObjectList.contains(jsnObject.toString())) {

													} else {
														transactionObjectList.add(jsnObject.toString());
													}
												} else {
													transactionObjectList.add(str.toString());
												}

											}
											transactionExist.get()
													.setColumnsObjectData(transactionObjectList.toString());
											transactionObjectList = new HashSet<>();
											transactionRepository.save(transactionExist.get());
										}

									}
								}
							}
							transaction.get().setOrderColumnData(orderColumnDataSet.toString());
							transactionList.add(transaction.get());
						}
					}
				}
				transactionRepository.saveAll(transactionList);
				order.setStatus(orderSaveRequestDto.getOrderStatus());
				orderRepository.save(order);
				Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
						.findByClientIdAndStatusId(order.getClientId(), orderSaveRequestDto.getOrderStatus().getId());
				if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
					Optional<OrderStatus> orderStatus = orderStatusRepository
							.findById(orderSaveRequestDto.getOrderStatus().getId());
					emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
							orderSaveRequestDto.getOrderId(), order.getClientEmailId());
				}
				response.setResponseCode(HttpStatus.CREATED.value());
				response.setMessage("Status updated successfully");

			} else if ((order.getStatus().getId().longValue() == 6 || order.getStatus().getId().longValue() == 4)
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 9
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 8)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order dispatched, Cannot cancel/reject the Order!!");
			} else if ((order.getStatus().getId().longValue() == 7 || order.getStatus().getId().longValue() == 5)
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 9
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 8)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order delivered, Cannot cancel/reject the Order!!");
			}

			else {
				order.setStatus(orderSaveRequestDto.getOrderStatus());
				order.setUpdatedAt(new Date());
				if (orderSaveRequestDto.getOrderStatus().getId() == 7) {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
					Date convertDate = DateUtil.convertUTCTOIST(new Date());
					String date = formatter.format(convertDate);
					order.setDeliveryDate(date);
				}
				orderRepository.save(order);
				if (order.getStatus().getId() == 8 || order.getStatus().getId() == 9) {
					List<OrderProductMapping> orderProductList = orderProductMappingRepository
							.findAllByOrderId(order.getOrderId());
					List<Product> productList = new ArrayList<>();
					Double addedGstPrice = 0D;
					for (OrderProductMapping orderProductMapping : orderProductList) {
						Optional<Product> product = productRepository
								.findById(orderProductMapping.getProduct().getId());
						if (product.get().getBlockStockOnOrderCreate().booleanValue() == true) {
							product.get().setAvailableQuantity(
									product.get().getAvailableQuantity() + orderProductMapping.getQuantity());
						}

						addedGstPrice = addedGstPrice + orderProductMapping.getAfterAddGstTotalPrice();
						productList.add(product.get());
						Optional<Transaction> transactionObj = transactionRepository.findByOrderIdAndProductId(
								order.getOrderId(), product.get().getId(), orderProductMapping.getIsSample());
						if (transactionObj != null && transactionObj.get().getOrderColumnData() != null
								&& transactionObj.get().getOrderColumnData() != "") {
							JSONArray columnData = new JSONArray(transactionObj.get().getOrderColumnData().toString());
							Object ob = product.get().getProductColumnDetails();
							ObjectMapper objectMapper = new ObjectMapper();
							String data = ob.toString();
							Set<String> transactionObjectList = new HashSet<>();
							TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
									TemplateHeaderRequest[].class);
							for (TemplateHeaderRequest req : responseArray) {
								for (Object obj : columnData) {
									JSONObject object = new JSONObject(obj.toString());
									if (object.has(req.getHeaderName()) && req.getIsUnique() != null
											&& req.getIsUnique().booleanValue() == true) {
										String objectValueName = "\"" + req.getHeaderName() + "\":\""
												+ object.getString(req.getHeaderName()) + "\"";
										Optional<Transaction> transactionExist = transactionRepository
												.findByProductIdAndColumnDataLike(product.get().getId(),
														objectValueName);
										if (transactionExist != null && transactionExist.isPresent()) {
											JSONArray transactionColumn = new JSONArray(
													transactionExist.get().getColumnsObjectData());

											for (Object str : transactionColumn) {
												JSONObject jsnObject = new JSONObject(str.toString());
												if (obj.toString().equals(jsnObject.toString())
														&& jsnObject.getBoolean("In-Stock") == false) {
													jsnObject.remove("In-Stock");
													jsnObject.remove("orderId");
													Boolean stockStatus = true;
													jsnObject.accumulate("In-Stock", stockStatus);
													if (transactionObjectList.contains(jsnObject.toString())) {

													} else {
														transactionObjectList.add(jsnObject.toString());
													}
												} else {
													transactionObjectList.add(str.toString());
												}

											}
											transactionExist.get()
													.setColumnsObjectData(transactionObjectList.toString());
											transactionObjectList = new HashSet<>();
											transactionRepository.save(transactionExist.get());
										}

									}
								}
							}
							Transaction trn = new Transaction();
							trn.setOrderColumnData(columnData.toString());
							trn.setCreatedAt(new Date());
							trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
							trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
							Integer procuredQuantity = columnData.length();
							trn.setProcuredQuantity(Double.parseDouble(procuredQuantity.toString()));
							trn.setIsOrder(true);
							trn.setProductId(orderProductMapping.getProduct().getId());
							trn.setRemarks(order.getOrderId());
//							trn.setOrderId(orderProductMapping.getOrderId());
							transactionRepository.save(trn);
						} else {
							Transaction trn = new Transaction();
							trn.setCreatedAt(new Date());
							trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
							trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
							trn.setProductId(product.get().getId());
							trn.setRemarks(order.getOrderId());
							trn.setIsOrder(true);
							trn.setProcuredQuantity(orderProductMapping.getQuantity().doubleValue());
							transactionRepository.save(trn);
						}

					}
					order.setDeliveryDate(null);
					orderRepository.save(order);
					productRepository.saveAll(productList);
					Optional<ClientBalanceDetails> clientBalanceDetails = clientBalanceDetailsRepository
							.findByClientId(order.getClientId());
					clientBalanceDetails.get().setCurrentBalance(Double
							.parseDouble(df.format(clientBalanceDetails.get().getCurrentBalance() + addedGstPrice)));
					clientBalanceDetails.get().setClientName(order.getClientName());
					clientBalanceDetails.get().setLastTransactionAmount(Double.parseDouble(df.format(addedGstPrice)));
					clientBalanceDetails.get().setLastTransactionDate(new Date());
					clientBalanceDetails.get().setLastTransactionRemark(order.getOrderId());
					clientBalanceDetailsRepository.save(clientBalanceDetails.get());
					ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
					clientBalanceTrail.setClientId(order.getClientId());
					clientBalanceTrail.setCreatedDate(new Date());
					clientBalanceTrail.setCreditAmount(Double.parseDouble(df.format(addedGstPrice)));
					clientBalanceTrail.setTransactionDetails(order.getOrderId());
					clientBalanceTrail.setRemark("Cancelled/Rejected");
					clientBalanceTrailRepository.save(clientBalanceTrail);
				}
				Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
						.findByClientIdAndStatusId(order.getClientId(), orderSaveRequestDto.getOrderStatus().getId());
				if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
					Optional<OrderStatus> orderStatus = orderStatusRepository
							.findById(orderSaveRequestDto.getOrderStatus().getId());
					emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
							orderSaveRequestDto.getOrderId(), order.getClientEmailId());
				}
				response.setResponseCode(HttpStatus.CREATED.value());
				response.setMessage("Status updated successfully");
			}
		}
		if (response.getResponseCode() == HttpStatus.CREATED.value())

		{
			OrderTrail orderTrail = new OrderTrail();
			orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
			orderTrail.setOrderStatus(orderSaveRequestDto.getOrderStatus());
			orderTrail.setCreatedDate(new Date());
			orderTrailRepository.save(orderTrail);
		}
		return response;
	}

	@Override
	public Response<?> updateOrderStatusV3(OrderSaveRequestDto orderSaveRequestDto)
			throws JsonMappingException, JsonProcessingException {

		List<Order> orderList = orderRepository.findAllByOrderId(orderSaveRequestDto.getOrderId());
		DecimalFormat df = new DecimalFormat("#.##");
		Response<?> response = new Response<>();
		for (Order order : orderList) {
			if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 3) {
				if (order.getStatus().getId() == 2 || order.getStatus().getId() == 6) {
					List<Transaction> transactionList = new ArrayList<>();
					Boolean isAbleToPacked = true;
					List<OrderDto> ordersList = orderSaveRequestDto.getOrder();
					for (OrderDto ord : ordersList) {
						Optional<Transaction> transaction = transactionRepository.findByOrderIdAndProductId(
								order.getOrderId(), ord.getProduct().getId(), ord.getIsSample());
						Optional<OrderProductMapping> prodMapData = orderProductMappingRepository
								.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(), ord.getProduct().getId(),
										ord.getIsSample());
						if (prodMapData.get().getUnitId() != null && prodMapData.get().getProduct().getUnit()
								.longValue() != prodMapData.get().getUnitId()) {
							Optional<UnitConversionMapping> unitMapData = unitConversionMappingRepository
									.findById(prodMapData.get().getUnitId());
							if (ord.getQuantity() != null) {
								ord.setQuantity(ord.getQuantity() / unitMapData.get().getSubUnitQty());
							} else {
								ord.setQuantity(0D);
							}
						}
						if (transaction != null && transaction.isPresent()) {

							if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {

								Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());
								JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
								JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
								Object ob = productObj.get().getProductColumnDetails();
								ObjectMapper objectMapper = new ObjectMapper();
								String data = ob.toString();
								List<String> orderColumnDataSet = new ArrayList<>();
								Set<String> transactionObjectList = new HashSet<>();
								TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
										TemplateHeaderRequest[].class);
								for (TemplateHeaderRequest req : responseArray) {
									for (Object obj : columnData) {
										JSONObject object = new JSONObject(obj.toString());
										if (object.has(req.getHeaderName()) && req.getIsUnique() != null
												&& req.getIsUnique().booleanValue() == true) {
											String objectValueName = "\"" + req.getHeaderName() + "\":\""
													+ object.getString(req.getHeaderName()) + "\"";
											Optional<Transaction> transactionExist = transactionRepository
													.findByProductIdAndColumnDataLike(productObj.get().getId(),
															objectValueName);
											if (transactionExist != null && transactionExist.isPresent()) {
												JSONArray transactionColumn = new JSONArray(
														transactionExist.get().getColumnsObjectData());

												for (Object str : transactionColumn) {
//														for(int i=0;i<transactionColumn.length();i++) {

													JSONObject jsnObject = new JSONObject(str.toString());
													if (obj.toString().equals(jsnObject.toString())
															&& jsnObject.getBoolean("In-Stock") == true) {
//																ObjectMapper mapper=new ObjectMapper();
														jsnObject.remove("In-Stock");
														Boolean stockStatus = false;
														jsnObject.accumulate("In-Stock", stockStatus);
														jsnObject.accumulate("orderId", order.getOrderId());
														orderColumnDataSet.add(jsnObject.toString());
														if (transactionObjectList.contains(jsnObject.toString())) {

														} else {
															transactionObjectList.add(jsnObject.toString());
														}
													} else {
														transactionObjectList.add(str.toString());
													}

												}
												transactionExist.get()
														.setColumnsObjectData(transactionObjectList.toString());
												transactionObjectList = new HashSet<>();
												transactionRepository.save(transactionExist.get());
											}

										}
									}
								}
								OrderPackedDetails packedDetails = new OrderPackedDetails();
								packedDetails.setProductId(ord.getProduct());

								packedDetails.setOrderProdMapId(prodMapData.get());
								packedDetails.setIsSample(ord.getIsSample());
								packedDetails.setOrderId(order.getOrderId());
								packedDetails.setQty(ord.getQuantity());
								packedDetails.setCreatedAt(new Date());
								orderPackedDetailsRepository.save(packedDetails);
								if (transaction.get().getOrderColumnData() != null
										&& transaction.get().getOrderColumnData() != ""
										&& !transaction.get().getOrderColumnData().equals("[]")) {
									JSONArray jsonArray1 = new JSONArray(transaction.get().getOrderColumnData());
									JSONArray jsonArray2 = new JSONArray(orderColumnDataSet.toString());
									JSONArray resultArray = new JSONArray();
									for (int i = 0; i < jsonArray1.length(); i++) {
										resultArray.put(jsonArray1.get(i));
									}
									for (int i = 0; i < jsonArray2.length(); i++) {
										resultArray.put(jsonArray2.get(i));
									}
									transaction.get().setOrderColumnData(resultArray.toString());
								} else {
									transaction.get().setOrderColumnData(orderColumnDataSet.toString());
								}

								transactionList.add(transaction.get());
							} else {
								OrderPackedDetails packedDetails = new OrderPackedDetails();
								packedDetails.setProductId(ord.getProduct());

								packedDetails.setOrderProdMapId(prodMapData.get());
								packedDetails.setIsSample(ord.getIsSample());
								packedDetails.setOrderId(order.getOrderId());
								if (ord.getQuantity() != null) {
									packedDetails.setQty(ord.getQuantity());
								} else {
									packedDetails.setQty(0D);
								}
								packedDetails.setCreatedAt(new Date());
								orderPackedDetailsRepository.save(packedDetails);

							}
						}
						List<OrderPackedDetails> orderExistedData = orderPackedDetailsRepository
								.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(), ord.getProduct().getId(),
										ord.getIsSample());
						Double alreadyPackedQty = 0D;
						if (orderExistedData != null && orderExistedData.size() > 0) {

							for (OrderPackedDetails details : orderExistedData) {
								alreadyPackedQty = alreadyPackedQty + details.getQty();
							}
						}
						if (prodMapData.get().getQuantity().doubleValue() == alreadyPackedQty.doubleValue()) {
							if (isAbleToPacked.booleanValue() == true) {
								isAbleToPacked = true;
							}
						} else {
							isAbleToPacked = false;
						}

					}
					transactionRepository.saveAll(transactionList);
					if (isAbleToPacked.booleanValue() == true) {
						order.setStatus(new OrderStatus(4L));
						order.setIsPartiallyPacked(false);
						OrderTrail orderTrail = new OrderTrail();
						orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
						orderTrail.setOrderStatus(new OrderStatus(4L));
						orderTrail.setCreatedDate(new Date());
						orderTrailRepository.save(orderTrail);
						response.setResponseCode(HttpStatus.OK.value());
						response.setMessage("Status updated successfully");
					} else {
						order.setStatus(orderSaveRequestDto.getOrderStatus());
						order.setIsPartiallyPacked(true);
						response.setResponseCode(HttpStatus.CREATED.value());
						response.setMessage("Status updated successfully");
					}

					orderRepository.save(order);
					Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
							.findByClientIdAndStatusId(order.getClientId(), order.getStatus().getId());
					if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
						Optional<OrderStatus> orderStatus = orderStatusRepository.findById(order.getStatus().getId());

						emailServiceVM.sendDispatchProductsMail(orderStatus.get().getName(), order.getOrderId(),
								order.getClientEmailId(), orderSaveRequestDto.getOrder());
					}

				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}

			} else if ((order.getStatus().getId() != 6 && orderSaveRequestDto.getOrderStatus().getId() >= 5)
					&& orderSaveRequestDto.getOrderStatus().getId().longValue() < order.getStatus().getId().longValue()
					|| orderSaveRequestDto.getOrderStatus().getId().longValue() == order.getStatus().getId()
							.longValue()) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 7) {
				if (order.getStatus().getId() == 4 || order.getStatus().getId() == 6) {
					List<OrderTrail> orderTrailData = orderTrailRepository.findAllByOrderId(order.getOrderId());
					Boolean isAbleToCancel = true;
					for (OrderTrail trail : orderTrailData) {
						if (trail.getOrderStatus().getName().equals("Partially Dispatched")
								|| trail.getOrderStatus().getName().equals("Partially Delivered")) {
							isAbleToCancel = false;
							break;
						}
					}
					if (isAbleToCancel.booleanValue() == true) {
						order.setStatus(orderSaveRequestDto.getOrderStatus());
						order.setUpdatedAt(new Date());
						if (order.getNoOfDeliveryAttempts() != null) {

							order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
						} else {
							order.setNoOfDeliveryAttempts(1L);
						}
//	    				orderRepository.save(order);
						OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
						orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
						orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
						orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
						orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
//	    				orderDeliveryDetailsRepository.save(orderDeliveryDetails);
						List<OrderProductMapping> orderProdMapData = orderProductMappingRepository
								.findAllByOrderId(orderSaveRequestDto.getOrderId());
						Boolean isOrderDispatch = true;
						String productName = "";
						List<OrderDispatchDetails> dispatchList = new ArrayList<>();
						for (OrderProductMapping mapdata : orderProdMapData) {
							List<OrderDispatchDetails> orderDispatchExistList = orderDispatchDetailsRepository
									.findAllByOrderProdMapId(mapdata.getId());
							if (orderDispatchExistList != null && orderDispatchExistList.size() > 0) {
								Map<Long, Double> dispatchedProductMap = new HashMap<>();

								for (OrderDispatchDetails dispatch : orderDispatchExistList) {
									if (dispatchedProductMap.containsKey(dispatch.getProduct().getId())) {
										Double currentQuantity = dispatchedProductMap
												.get(dispatch.getProduct().getId());
										dispatchedProductMap.put(dispatch.getProduct().getId(),
												currentQuantity + dispatch.getQuantity());
									} else {
										dispatchedProductMap.put(dispatch.getProduct().getId(), dispatch.getQuantity());
									}
								}

								OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
								dispatchDetails.setCreatedAt(new Date());
								dispatchDetails.setIsSample(mapdata.getIsSample());
								dispatchDetails.setOrderId(mapdata.getOrderId());
								dispatchDetails.setProduct(mapdata.getProduct());
								dispatchDetails.setQuantity(
										mapdata.getQuantity() - dispatchedProductMap.get(mapdata.getProduct().getId()));
								dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
								dispatchDetails.setOrderProductMapping(mapdata);
								dispatchList.add(dispatchDetails);
							} else {
								OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
								dispatchDetails.setCreatedAt(new Date());
								dispatchDetails.setIsSample(mapdata.getIsSample());
								dispatchDetails.setOrderId(mapdata.getOrderId());
								dispatchDetails.setProduct(mapdata.getProduct());
								dispatchDetails.setQuantity(mapdata.getQuantity());
								dispatchDetails.setOrderProductMapping(mapdata);
								dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
								dispatchList.add(dispatchDetails);
							}

							Optional<Product> productObj = productRepository.findById(mapdata.getProduct().getId());
							if (productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {

//	    						productRepository.save(productObj.get());
								if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
									isOrderDispatch = false;
									productName = productObj.get().getName();
									break;
								} else {
									productObj.get().setPhysicalStockQuantity(
											productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
								}

							} else {

								if (productObj.get().getPhysicalStockQuantity() < mapdata.getQuantity()) {
									isOrderDispatch = false;

									productName = productObj.get().getName();
									break;
								} else {
									productObj.get().setPhysicalStockQuantity(
											productObj.get().getPhysicalStockQuantity() - mapdata.getQuantity());
									productObj.get().setAvailableQuantity(
											productObj.get().getAvailableQuantity() - mapdata.getQuantity());
								}
//	       						productRepository.save(productObj.get());

							}

							if (isOrderDispatch) {
								productRepository.save(productObj.get());
							}

						}

						if (isOrderDispatch.booleanValue() == true) {
							orderDispatchDetailsRepository.saveAll(dispatchList);
							orderRepository.save(order);
							orderDeliveryDetailsRepository.save(orderDeliveryDetails);
							Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
									.findByClientIdAndStatusId(order.getClientId(),
											orderSaveRequestDto.getOrderStatus().getId());
							if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
								Optional<OrderStatus> orderStatus = orderStatusRepository
										.findById(orderSaveRequestDto.getOrderStatus().getId());
								emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
										orderSaveRequestDto.getOrderId(), order.getClientEmailId());
							}
							response.setMessage("Status updated successfully");
							response.setResponseCode(HttpStatus.CREATED.value());
						} else {
							return new Response<>(HttpStatus.BAD_REQUEST.value(),
									"You can't dispatch the order due to less physical availability of " + productName,
									null);
						}
					} else {
						response.setResponseCode(HttpStatus.BAD_REQUEST.value());
						response.setMessage("Please provide a valid status!!");
						return response;
					}

				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}

			} else if (orderSaveRequestDto.getOrderStatus().getId() == 5) {
				if (order.getStatus().getId() == 3 || order.getStatus().getId() == 4
						|| order.getStatus().getId() == 6 && order.getStatus().getId() != 5) {
					List<OrderProductMapping> orderProductMapData = orderProductMappingRepository
							.findAllByOrderId(order.getOrderId());
					Map<Long, Double> orderProdMap = new HashMap<>();
					for (OrderProductMapping mapping : orderProductMapData) {
						if (orderProdMap.containsKey(mapping.getProduct().getId())) {
							Double currentQuantity = orderProdMap.get(mapping.getProduct().getId());
							orderProdMap.put(mapping.getProduct().getId(), currentQuantity + mapping.getQuantity());
						} else {
							orderProdMap.put(mapping.getProduct().getId(), mapping.getQuantity());
						}
					}
					List<OrderDispatchDetails> orderDispatchList = orderDispatchDetailsRepository
							.findAllByOrderId(order.getOrderId());
					Map<Long, Double> requestProdMapData = new HashMap<>();
					for (OrderDto ordDto : orderSaveRequestDto.getOrderDispatchList()) {
						Optional<Product> productObj = productRepository.findById(ordDto.getProduct().getId());
						if (ordDto.getUnitId() != null
								&& productObj.get().getUnit().longValue() != ordDto.getUnitId().longValue()) {
							Optional<UnitConversionMapping> unitMapObj = unitConversionMappingRepository
									.findById(ordDto.getUnitId());
							ordDto.setQuantity(ordDto.getQuantity() / unitMapObj.get().getSubUnitQty());
						}
						if (requestProdMapData.containsKey(ordDto.getProduct().getId())) {
							Double currentQuantity = requestProdMapData.get(ordDto.getProduct().getId());
							requestProdMapData.put(ordDto.getProduct().getId(), currentQuantity + ordDto.getQuantity());
						} else {
							requestProdMapData.put(ordDto.getProduct().getId(), ordDto.getQuantity());
						}
					}
					Map<Long, Double> dispatchedProductMap = new HashMap<>();
					Map<Long, Double> orderedMap = new HashMap<>();
					if (orderDispatchList != null && orderDispatchList.size() > 0) {
						for (OrderDispatchDetails dispatch : orderDispatchList) {
							if (dispatchedProductMap.containsKey(dispatch.getProduct().getId())) {
								Double currentQuantity = dispatchedProductMap.get(dispatch.getProduct().getId());
								dispatchedProductMap.put(dispatch.getProduct().getId(),
										currentQuantity + dispatch.getQuantity());
							} else {
								dispatchedProductMap.put(dispatch.getProduct().getId(), dispatch.getQuantity());
							}
						}
					} else {
						for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
							if (orderedMap.containsKey(dto.getProduct().getId())) {
								Double currentQuantity = orderedMap.get(dto.getProduct().getId());
								orderedMap.put(dto.getProduct().getId(), currentQuantity + dto.getQuantity());
							} else {
								orderedMap.put(dto.getProduct().getId(), dto.getQuantity());
							}
						}
					}

					Boolean isAbleToDispatch = true;
					Set<Long> orderProdKeys = orderProdMap.keySet();
					Set<Long> dispatchedKeys = null;
					if (!dispatchedProductMap.isEmpty()) {
						dispatchedKeys = dispatchedProductMap.keySet();
					} else {
						dispatchedKeys = orderedMap.keySet();
					}
					if (!orderProdKeys.equals(dispatchedKeys)) {
						isAbleToDispatch = false;
					} else {
						for (Long key : orderProdKeys) {
							Double orderQuantity = orderProdMap.get(key);
							Double dispatchedQuantity = 0D;
							if (!dispatchedProductMap.isEmpty()) {
								if (requestProdMapData.containsKey(key)) {
									dispatchedQuantity = dispatchedProductMap.get(key) + requestProdMapData.get(key);
								} else {
									dispatchedQuantity = dispatchedProductMap.get(key);
								}
							} else {
								if (orderedMap.containsKey(key)) {
									dispatchedQuantity = orderedMap.get(key);
								} else {
									dispatchedQuantity = orderedMap.get(key);
								}
							}

							if (orderQuantity.doubleValue() != dispatchedQuantity.doubleValue()) {
								isAbleToDispatch = false;
								break;
							}
						}
					}
					Boolean isOrderDispatch = true;
					String productName = "";
					List<Product> productList = new ArrayList<>();
					for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
						Optional<Product> productObj = productRepository.findById(dto.getProduct().getId());
//						if(productObj.get().getUnit().longValue()!=dto.getUnitId().longValue()) {
//							Optional<UnitConversionMapping> unitMapObj=unitConversionMappingRepository.findById(dto.getUnitId());
//							dto.setQuantity(dto.getQuantity()/unitMapObj.get().getSubUnitQty());
//						}
						if (dto.getUnitId() != null
								&& productObj.get().getUnit().longValue() != dto.getUnitId().longValue()) {
//							Optional<UnitConversionMapping> unitMapObj = unitConversionMappingRepository
//									.findById(dto.getUnitId());
							dto.setQuantity(dto.getQuantity());
						}
						if (productObj.get().getBlockStockOnOrderCreate().booleanValue() == true) {

//    						productRepository.save(productObj.get());
							if (productObj.get().getPhysicalStockQuantity() < dto.getQuantity()) {
								isOrderDispatch = false;
								productName = productObj.get().getName();
								break;
							} else {
								productObj.get().setPhysicalStockQuantity(
										productObj.get().getPhysicalStockQuantity() - dto.getQuantity());
							}

						} else {

							if (productObj.get().getPhysicalStockQuantity() < dto.getQuantity()) {
								isOrderDispatch = false;
								productName = productObj.get().getName();
								break;
							} else {
								productObj.get().setPhysicalStockQuantity(
										productObj.get().getPhysicalStockQuantity() - dto.getQuantity());
								productObj.get().setAvailableQuantity(
										productObj.get().getAvailableQuantity() - dto.getQuantity());
							}
//       						productRepository.save(productObj.get());

						}

						if (isOrderDispatch) {
							productList.add(productObj.get());
//							productRepository.save(productObj.get());
						}
					}

					if (isOrderDispatch.booleanValue() == true) {
						if (isAbleToDispatch.booleanValue() == true) {
							productRepository.saveAll(productList);
							order.setStatus(new OrderStatus(7L));

							if (order.getNoOfDeliveryAttempts() != null) {
								order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
							} else {
								order.setNoOfDeliveryAttempts(1L);
							}
							OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
							orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
							orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
							orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
							orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
							orderDeliveryDetailsRepository.save(orderDeliveryDetails);
							orderRepository.save(order);
							List<OrderDispatchDetails> orderDispatchLists = new ArrayList<>();
							for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
								OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
								dispatchDetails.setOrderId(order.getOrderId());
								dispatchDetails.setProduct(dto.getProduct());
								if (dto.getQuantity() != null) {
									dispatchDetails.setQuantity(dto.getQuantity());
								} else {
									dispatchDetails.setQuantity(0D);
								}
								dispatchDetails.setCreatedAt(new Date());
								dispatchDetails.setIsSample(dto.getIsSample());
								dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
								Optional<OrderProductMapping> orderProdMapObj = orderProductMappingRepository
										.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(),
												dto.getProduct().getId(), dto.getIsSample());
								dispatchDetails.setOrderProductMapping(orderProdMapObj.get());
								orderDispatchLists.add(dispatchDetails);
							}
							orderDispatchDetailsRepository.saveAll(orderDispatchLists);
							OrderTrail orderTrail = new OrderTrail();
							orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
							orderTrail.setOrderStatus(new OrderStatus(7L));
							orderTrail.setCreatedDate(new Date());
							orderTrailRepository.save(orderTrail);
							Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
									.findByClientIdAndStatusId(order.getClientId(), order.getStatus().getId());
							if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
								Optional<OrderStatus> orderStatus = orderStatusRepository
										.findById(orderSaveRequestDto.getOrderStatus().getId());
								emailServiceVM.sendDispatchProductsMail(orderStatus.get().getName(), order.getOrderId(),
										order.getClientEmailId(), orderSaveRequestDto.getOrderDispatchList());

							}
							response.setResponseCode(HttpStatus.OK.value());
							response.setMessage("Status updated successfully");

						} else {
							order.setStatus(orderSaveRequestDto.getOrderStatus());
							if (order.getNoOfDeliveryAttempts() != null) {
								order.setNoOfDeliveryAttempts(order.getNoOfDeliveryAttempts() + 1);
							} else {
								order.setNoOfDeliveryAttempts(1L);
							}
							order.setStatus(orderSaveRequestDto.getOrderStatus());
							OrderDeliveryDetails orderDeliveryDetails = new OrderDeliveryDetails();
							orderDeliveryDetails.setOrderId(orderSaveRequestDto.getOrderId());
							orderDeliveryDetails.setImageList(orderSaveRequestDto.getImages());
							orderDeliveryDetails.setDescription(orderSaveRequestDto.getRemarks());
							orderDeliveryDetails.setDeliveryAttemptNo(order.getNoOfDeliveryAttempts());
							orderDeliveryDetailsRepository.save(orderDeliveryDetails);
							orderRepository.save(order);
							List<OrderDispatchDetails> orderDispatchLists = new ArrayList<>();
							for (OrderDto dto : orderSaveRequestDto.getOrderDispatchList()) {
								OrderDispatchDetails dispatchDetails = new OrderDispatchDetails();
								dispatchDetails.setOrderId(order.getOrderId());
								dispatchDetails.setProduct(dto.getProduct());
								if (dto.getQuantity() != null) {
									dispatchDetails.setQuantity(dto.getQuantity());
								} else {
									dispatchDetails.setQuantity(0D);
								}
								dispatchDetails.setCreatedAt(new Date());
								dispatchDetails.setIsSample(dto.getIsSample());
								dispatchDetails.setDispatchAttemptNo(order.getNoOfDeliveryAttempts());
								Optional<OrderProductMapping> orderProdMapObj = orderProductMappingRepository
										.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(),
												dto.getProduct().getId(), dto.getIsSample());
								dispatchDetails.setOrderProductMapping(orderProdMapObj.get());
								orderDispatchLists.add(dispatchDetails);
							}
							orderDispatchDetailsRepository.saveAll(orderDispatchLists);
							Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
									.findByClientIdAndStatusId(order.getClientId(),
											orderSaveRequestDto.getOrderStatus().getId());
							if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
								Optional<OrderStatus> orderStatus = orderStatusRepository
										.findById(orderSaveRequestDto.getOrderStatus().getId());

								emailServiceVM.sendDispatchProductsMail(orderStatus.get().getName(), order.getOrderId(),
										order.getClientEmailId(), orderSaveRequestDto.getOrderDispatchList());
							}
							response.setResponseCode(HttpStatus.CREATED.value());
							response.setMessage("Status updated successfully");

						}

					} else {
						return new Response<>(HttpStatus.BAD_REQUEST.value(),
								"You can't dispatch the order due to less physical availability of " + productName,
								null);
					}
				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}
			} else if (orderSaveRequestDto.getOrderStatus().getId().longValue() == 4) {
				if (order.getStatus().getId() == 2 || order.getStatus().getId() == 6) {
					List<OrderTrail> orderTrailData = orderTrailRepository.findAllByOrderId(order.getOrderId());
					Boolean isAbleToCancel = true;
					for (OrderTrail trail : orderTrailData) {
						if (trail.getOrderStatus().getName().equals("Partially Dispatched")
								|| trail.getOrderStatus().getName().equals("Partially Delivered")) {
							isAbleToCancel = false;
							break;
						}
					}
					if (isAbleToCancel.booleanValue() == true) {
						List<Transaction> transactionList = new ArrayList<>();
						for (OrderDto ord : orderSaveRequestDto.getOrder()) {
							Optional<Transaction> transaction = transactionRepository.findByOrderIdAndProductId(
									order.getOrderId(), ord.getProduct().getId(), ord.getIsSample());
							Optional<OrderProductMapping> prodMapData = orderProductMappingRepository
									.findAllByOrderIdAndProductIdAndIsSample(order.getOrderId(),
											ord.getProduct().getId(), ord.getIsSample());
							if (transaction != null && transaction.isPresent()) {
								if (ord.getOrderObjectData() != null && ord.getOrderObjectData() != "") {
									Optional<Product> productObj = productRepository.findById(ord.getProduct().getId());
									JSONObject fromFrontEndObj = new JSONObject(ord.getOrderObjectData());
									JSONArray columnData = new JSONArray(fromFrontEndObj.get("orderObject").toString());
									Object ob = productObj.get().getProductColumnDetails();
									ObjectMapper objectMapper = new ObjectMapper();
									String data = ob.toString();
									List<String> orderColumnDataSet = new ArrayList<>();
									Set<String> transactionObjectList = new HashSet<>();
									TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
											TemplateHeaderRequest[].class);
									for (TemplateHeaderRequest req : responseArray) {
										for (Object obj : columnData) {
											JSONObject object = new JSONObject(obj.toString());
											if (object.has(req.getHeaderName()) && req.getIsUnique() != null
													&& req.getIsUnique().booleanValue() == true) {
												String objectValueName = "\"" + req.getHeaderName() + "\":\""
														+ object.getString(req.getHeaderName()) + "\"";
												Optional<Transaction> transactionExist = transactionRepository
														.findByProductIdAndColumnDataLike(productObj.get().getId(),
																objectValueName);
												if (transactionExist != null && transactionExist.isPresent()) {
													JSONArray transactionColumn = new JSONArray(
															transactionExist.get().getColumnsObjectData());

													for (Object str : transactionColumn) {
//														for(int i=0;i<transactionColumn.length();i++) {

														JSONObject jsnObject = new JSONObject(str.toString());
														if (obj.toString().equals(jsnObject.toString())
																&& jsnObject.getBoolean("In-Stock") == true) {
//																ObjectMapper mapper=new ObjectMapper();
															jsnObject.remove("In-Stock");
															Boolean stockStatus = false;
															jsnObject.accumulate("In-Stock", stockStatus);
															jsnObject.accumulate("orderId", order.getOrderId());
															orderColumnDataSet.add(jsnObject.toString());
															if (transactionObjectList.contains(jsnObject.toString())) {

															} else {
																transactionObjectList.add(jsnObject.toString());
															}
														} else {
															transactionObjectList.add(str.toString());
														}

													}
													transactionExist.get()
															.setColumnsObjectData(transactionObjectList.toString());
													transactionObjectList = new HashSet<>();
													transactionRepository.save(transactionExist.get());
												}

											}
										}
									}
									OrderPackedDetails packedDetails = new OrderPackedDetails();
									packedDetails.setProductId(ord.getProduct());

									packedDetails.setOrderProdMapId(prodMapData.get());
									packedDetails.setIsSample(ord.getIsSample());
									packedDetails.setOrderId(order.getOrderId());
									packedDetails.setQty(ord.getQuantity());
									packedDetails.setCreatedAt(new Date());
									orderPackedDetailsRepository.save(packedDetails);
									if (transaction.get().getOrderColumnData() != null
											&& transaction.get().getOrderColumnData() != "") {
										JSONArray jsonArray1 = new JSONArray(transaction.get().getOrderColumnData());
										JSONArray jsonArray2 = new JSONArray(orderColumnDataSet.toString());
										JSONArray resultArray = new JSONArray();
										for (int i = 0; i < jsonArray1.length(); i++) {
											resultArray.put(jsonArray1.get(i));
										}
										for (int i = 0; i < jsonArray2.length(); i++) {
											resultArray.put(jsonArray2.get(i));
										}
										transaction.get().setOrderColumnData(resultArray.toString());
									} else {
										transaction.get().setOrderColumnData(orderColumnDataSet.toString());
									}
									transactionList.add(transaction.get());
								}
							}
						}
						transactionRepository.saveAll(transactionList);
						order.setStatus(orderSaveRequestDto.getOrderStatus());
						orderRepository.save(order);
						Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
								.findByClientIdAndStatusId(order.getClientId(),
										orderSaveRequestDto.getOrderStatus().getId());
						if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
							Optional<OrderStatus> orderStatus = orderStatusRepository
									.findById(orderSaveRequestDto.getOrderStatus().getId());
							emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
									orderSaveRequestDto.getOrderId(), order.getClientEmailId());
						}
						response.setResponseCode(HttpStatus.CREATED.value());
						response.setMessage("Status updated successfully");
					} else {
						response.setResponseCode(HttpStatus.BAD_REQUEST.value());
						response.setMessage("Please provide a valid status!!");
						return response;
					}

				} else {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Please provide a valid status!!");
					return response;
				}

			} else if ((order.getStatus().getId().longValue() + 1 != orderSaveRequestDto.getOrderStatus().getId())
					&& orderSaveRequestDto.getOrderStatus().getId() != 10
					&& orderSaveRequestDto.getOrderStatus().getId() != 9) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Please provide a valid status!!");
			} else if (order.getStatus().getId().longValue() == 10 || order.getStatus().getId().longValue() == 9) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Status updation is failed as the order is already cancelled/rejected...");
			} else if ((order.getStatus().getId().longValue() == 7 || order.getStatus().getId().longValue() == 5)
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 10
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 9)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order dispatched, Cannot cancel/reject the Order!!");
			} else if ((order.getStatus().getId().longValue() == 8 || order.getStatus().getId().longValue() == 6)
					&& (orderSaveRequestDto.getOrderStatus().getId().longValue() == 10
							|| orderSaveRequestDto.getOrderStatus().getId().longValue() == 9)) {
				response.setResponseCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Order delivered, Cannot cancel/reject the Order!!");
			}

			else {
				List<OrderTrail> orderTrailData = orderTrailRepository.findAllByOrderId(order.getOrderId());
				Boolean isAbleToCancel = true;
				for (OrderTrail trail : orderTrailData) {
					if (trail.getOrderStatus().getName().equals("Partially Dispatched")
							|| trail.getOrderStatus().getName().equals("Partially Delivered")) {
						isAbleToCancel = false;
						break;
					}
				}
				if ((orderSaveRequestDto.getOrderStatus().getId() == 9
						|| orderSaveRequestDto.getOrderStatus().getId() == 10)
						&& isAbleToCancel.booleanValue() == false) {
					response.setResponseCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Order partially dispatched/delivered, Cannot cancel/reject the Order!! ");
				} else {

					order.setStatus(orderSaveRequestDto.getOrderStatus());
					order.setUpdatedAt(new Date());
					if (orderSaveRequestDto.getOrderStatus().getId() == 8) {
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
						Date convertDate = DateUtil.convertUTCTOIST(new Date());
						String date = formatter.format(convertDate);
						order.setDeliveryDate(date);
					}
					orderRepository.save(order);
					if (order.getStatus().getId() == 9 || order.getStatus().getId() == 10) {
						List<OrderProductMapping> orderProductList = orderProductMappingRepository
								.findAllByOrderId(order.getOrderId());
						List<Product> productList = new ArrayList<>();
						Double addedGstPrice = 0D;
						for (OrderProductMapping orderProductMapping : orderProductList) {
							Double orderQty = 0D;
							Optional<Product> product = productRepository
									.findById(orderProductMapping.getProduct().getId());
							if (product.get().getBlockStockOnOrderCreate().booleanValue() == true) {
								if (orderProductMapping.getUnitId() != null && orderProductMapping.getUnitId()
										.longValue() != orderProductMapping.getProduct().getUnit().longValue()) {
									Optional<UnitConversionMapping> unitCovData = unitConversionMappingRepository
											.findById(orderProductMapping.getUnitId());
									orderQty = orderProductMapping.getQuantity() / unitCovData.get().getSubUnitQty();
									product.get().setAvailableQuantity(product.get().getAvailableQuantity()
											+ (orderProductMapping.getQuantity() / unitCovData.get().getSubUnitQty()));
								} else {
									product.get().setAvailableQuantity(
											product.get().getAvailableQuantity() + orderProductMapping.getQuantity());
								}

							}

							addedGstPrice = addedGstPrice + orderProductMapping.getAfterAddGstTotalPrice();
							productList.add(product.get());
							Optional<Transaction> transactionObj = transactionRepository.findByOrderIdAndProductId(
									order.getOrderId(), product.get().getId(), orderProductMapping.getIsSample());
							if (transactionObj != null && transactionObj.get().getOrderColumnData() != null
									&& transactionObj.get().getOrderColumnData() != "") {
								JSONArray columnData = new JSONArray(
										transactionObj.get().getOrderColumnData().toString());
								Object ob = product.get().getProductColumnDetails();
								ObjectMapper objectMapper = new ObjectMapper();
								String data = ob.toString();
								Set<String> transactionObjectList = new HashSet<>();
								TemplateHeaderRequest[] responseArray = objectMapper.readValue(data,
										TemplateHeaderRequest[].class);
								for (TemplateHeaderRequest req : responseArray) {
									for (Object obj : columnData) {
										JSONObject object = new JSONObject(obj.toString());
										if (object.has(req.getHeaderName()) && req.getIsUnique() != null
												&& req.getIsUnique().booleanValue() == true) {
											String objectValueName = "\"" + req.getHeaderName() + "\":\""
													+ object.getString(req.getHeaderName()) + "\"";
											Optional<Transaction> transactionExist = transactionRepository
													.findByProductIdAndColumnDataLike(product.get().getId(),
															objectValueName);
											if (transactionExist != null && transactionExist.isPresent()) {
												JSONArray transactionColumn = new JSONArray(
														transactionExist.get().getColumnsObjectData());

												for (Object str : transactionColumn) {
													JSONObject jsnObject = new JSONObject(str.toString());
													if (obj.toString().equals(jsnObject.toString())
															&& jsnObject.getBoolean("In-Stock") == false) {
														jsnObject.remove("In-Stock");
														jsnObject.remove("orderId");
														Boolean stockStatus = true;
														jsnObject.accumulate("In-Stock", stockStatus);
														if (transactionObjectList.contains(jsnObject.toString())) {

														} else {
															transactionObjectList.add(jsnObject.toString());
														}
													} else {
														transactionObjectList.add(str.toString());
													}

												}
												transactionExist.get()
														.setColumnsObjectData(transactionObjectList.toString());
												transactionExist.get().setOrderColumnData(null);
												transactionObjectList = new HashSet<>();
												transactionRepository.save(transactionExist.get());
											}

										}
									}
								}
								Transaction trn = new Transaction();
//								trn.setOrderColumnData(columnData.toString());
								trn.setCreatedAt(new Date());
								trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
								trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
								Integer procuredQuantity = columnData.length();
								trn.setProcuredQuantity(Double.parseDouble(procuredQuantity.toString()));
								trn.setIsOrder(true);
								trn.setProductId(orderProductMapping.getProduct().getId());
								trn.setRemarks(order.getOrderId());
//								trn.setOrderId(orderProductMapping.getOrderId());
								transactionRepository.save(trn);
							} else {
								Transaction trn = new Transaction();
								trn.setCreatedAt(new Date());
								trn.setCreatedBy(orderSaveRequestDto.getCreatedByUserId());
								trn.setCreatedUserName(orderSaveRequestDto.getCreatedByUserName());
								trn.setProductId(product.get().getId());
								trn.setRemarks(order.getOrderId());
								trn.setIsOrder(true);
								trn.setProcuredQuantity(orderQty);
								transactionRepository.save(trn);
							}

						}
						order.setDeliveryDate(null);
						orderRepository.save(order);
						productRepository.saveAll(productList);
						Optional<ClientBalanceDetails> clientBalanceDetails = clientBalanceDetailsRepository
								.findByClientId(order.getClientId());
						clientBalanceDetails.get().setCurrentBalance(Double.parseDouble(
								df.format(clientBalanceDetails.get().getCurrentBalance() + addedGstPrice)));
						clientBalanceDetails.get().setClientName(order.getClientName());
						clientBalanceDetails.get()
								.setLastTransactionAmount(Double.parseDouble(df.format(addedGstPrice)));
						clientBalanceDetails.get().setLastTransactionDate(new Date());
						clientBalanceDetails.get().setLastTransactionRemark(order.getOrderId());
						clientBalanceDetailsRepository.save(clientBalanceDetails.get());
						ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
						clientBalanceTrail.setClientId(order.getClientId());
						clientBalanceTrail.setCreatedDate(new Date());
						clientBalanceTrail.setCreditAmount(Double.parseDouble(df.format(addedGstPrice)));
						clientBalanceTrail.setTransactionDetails(order.getOrderId());
						clientBalanceTrail.setRemark("Cancelled/Rejected");
						clientBalanceTrailRepository.save(clientBalanceTrail);
					}
					Optional<EmailNotificationConfiguration> emailNotificationConfigData = emailNotificationConfigurationRepository
							.findByClientIdAndStatusId(order.getClientId(),
									orderSaveRequestDto.getOrderStatus().getId());
					if (emailNotificationConfigData != null && emailNotificationConfigData.isPresent()) {
						Optional<OrderStatus> orderStatus = orderStatusRepository
								.findById(orderSaveRequestDto.getOrderStatus().getId());
						emailServiceVM.sendOrderUpdateStatusMail(orderStatus.get().getName(),
								orderSaveRequestDto.getOrderId(), order.getClientEmailId());
					}
					response.setResponseCode(HttpStatus.CREATED.value());
					response.setMessage("Status updated successfully");
				}
			}

		}
		if (response.getResponseCode() == HttpStatus.CREATED.value())

		{
			OrderTrail orderTrail = new OrderTrail();
			orderTrail.setOrderId(orderSaveRequestDto.getOrderId());
			orderTrail.setOrderStatus(orderSaveRequestDto.getOrderStatus());
			orderTrail.setCreatedDate(new Date());
			orderTrailRepository.save(orderTrail);
		}
		return response;

	}
}
