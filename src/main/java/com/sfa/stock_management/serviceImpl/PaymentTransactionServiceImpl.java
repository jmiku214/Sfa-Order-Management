package com.sfa.stock_management.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sfa.stock_management.constant.Constant;
import com.sfa.stock_management.constant.PaymentType;
import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.dto.SearchDto;
import com.sfa.stock_management.model.ClientBalanceDetails;
import com.sfa.stock_management.model.ClientBalanceTrail;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.repository.ClientBalanceDetailsRepository;
import com.sfa.stock_management.repository.ClientBalanceTrailRepository;
import com.sfa.stock_management.repository.OrderRepository;
import com.sfa.stock_management.service.EmailServiceVM;
import com.sfa.stock_management.service.PaymentTransactionService;
import com.sfa.stock_management.util.Pagination;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

	@Autowired
	private ClientBalanceDetailsRepository clientBalanceDetailsRepository;

	@Autowired
	private ClientBalanceTrailRepository clientBalanceTrailRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private EmailServiceVM emailServiceVM;

	@Value("${sfa.backend.url}")
	private String sfaBackendUrl;

	@Override
	public Response<?> getAllClientAccountsDetails(Long userId, Long hrmsUserId, int pageNo, int pageSize,
			String clientName, Date fromDate, Date toDate) {
		String url = sfaBackendUrl + Constant.FETCH_VENDOR_LIST_FOR_HRMS;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject request = new JsonObject();
		request.addProperty("userId", userId);
		request.addProperty("hrmsUserId", hrmsUserId);
		Gson gson = new Gson();
		String requestBody = gson.toJson(request);
		HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);
//		Map<Long, ClientAccountDetailsResponse> clientListMap = new HashMap<>();
		List<Long> clientIds = new ArrayList<>();
		Pagination<List<?>> pagination = new Pagination<>();
		Pageable pageRequest = Pageable.unpaged();
		if (pageSize > 0) {
			pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		}
		if (response != null) {
			JSONObject jsnobject = new JSONObject(response.getBody());
			if (jsnobject.has("data")) {
				JSONArray clientArray = jsnobject.getJSONArray("data");
				for (Object obj : clientArray) {
					JSONObject ob = new JSONObject(obj.toString());
					Long clientId = ob.getLong("id");
//					ClientAccountDetailsResponse accountDetailsResponse = new ClientAccountDetailsResponse();
					clientIds.add(clientId);
//					clientListMap.put(clientId, accountDetailsResponse);
				}
			}
		}
		Page<ClientBalanceDetails> clientBalanceList = clientBalanceDetailsRepository.findAll(clientIds, pageRequest,
				clientName, fromDate, toDate);
		List<ClientBalanceDetails> detailsList = clientBalanceList.toList();
		pagination.setData(detailsList.stream().filter(e->e.getLastTransactionDate()!=null).sorted(Comparator.comparing(ClientBalanceDetails::getLastTransactionDate).reversed()).collect(Collectors.toList()));
		pagination.setNumberOfElements(clientBalanceList.getNumberOfElements());
		pagination.setTotalElements(clientBalanceList.getTotalElements());
		pagination.setTotalPages(clientBalanceList.getTotalPages());

		return new Response<>(HttpStatus.OK.value(), "OK", pagination);
	}

	@Override
	public Response<?> getClientTransactionDetails(Long clientId, int pageNo, int pageSize,Date fromDate, Date toDate,String searchKey) {
		Pagination<List<?>> pagination = new Pagination<>();
		Response response = new Response<>();
		Pageable pageRequest = Pageable.unpaged();
		if (pageSize > 0) {
			pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
		}
		Optional<ClientBalanceDetails> clientBalanceDetailsObj = clientBalanceDetailsRepository
				.findByClientId(clientId);
		if (clientBalanceDetailsObj != null) {
			response.setData(clientBalanceDetailsObj);
			Page<ClientBalanceTrail> transactionList = clientBalanceTrailRepository.findAll(clientId, pageRequest,fromDate,toDate,searchKey);
			List<ClientBalanceTrail> trailList = transactionList.getContent();
			pagination.setData(trailList);
			pagination.setNumberOfElements(transactionList.getNumberOfElements());
			pagination.setTotalElements(transactionList.getTotalElements());
			pagination.setTotalPages(transactionList.getTotalPages());
			response.setPaginationData(pagination);
			response.setResponseCode(HttpStatus.OK.value());
		}
		return response;
	}

	@Override
	public Response<?> getAllOrderIdForClient(Long clientId) {

		List<Order> orderList = orderRepository.findAllByClientId(clientId);
		List<String> orderIds = new ArrayList<>();
		if (orderList != null) {
			orderIds = orderList.stream().map(e -> e.getOrderId()).collect(Collectors.toList());
		}
		return new Response<>(HttpStatus.OK.value(), "OK", orderIds);
	}

	@Override
	public Response<?> createNewPayment(CreateNewTransactionDto createNewTransactionDto) throws MessagingException {
		String transactionId = generateTransactionId(createNewTransactionDto.getClientId());
		if (createNewTransactionDto.getCreationDate() == null) {
			createNewTransactionDto.setCreationDate(new Date());
		}
		ClientBalanceTrail clientBalanceTrail = new ClientBalanceTrail();
		clientBalanceTrail.setClientId(createNewTransactionDto.getClientId());
		if (createNewTransactionDto.getTransactionTypeId() == PaymentType.CREDITED.getId()) {
			clientBalanceTrail.setCreditAmount(createNewTransactionDto.getAmount());
		}
		if (createNewTransactionDto.getTransactionTypeId() == PaymentType.DEBITED.getId()) {
			clientBalanceTrail.setDebitAmount(createNewTransactionDto.getAmount());
		}
		clientBalanceTrail.setCreatedDate(createNewTransactionDto.getCreationDate());
		clientBalanceTrail.setRemark(createNewTransactionDto.getRemark());
		clientBalanceTrail.setTransactionDetails(createNewTransactionDto.getOrderId());
		clientBalanceTrail.setTransactionId(transactionId);
		clientBalanceTrailRepository.save(clientBalanceTrail);
		Optional<ClientBalanceDetails> clientBalanceDetailsObj = clientBalanceDetailsRepository
				.findByClientId(createNewTransactionDto.getClientId());
		if (clientBalanceDetailsObj != null) {
			if (createNewTransactionDto.getTransactionTypeId() == PaymentType.CREDITED.getId()) {
				clientBalanceDetailsObj.get().setCurrentBalance(
						clientBalanceDetailsObj.get().getCurrentBalance() + createNewTransactionDto.getAmount());
				clientBalanceDetailsObj.get().setLastTransactionAmount(createNewTransactionDto.getAmount());
			}
			if (createNewTransactionDto.getTransactionTypeId() == PaymentType.DEBITED.getId()) {
				clientBalanceDetailsObj.get().setCurrentBalance(
						clientBalanceDetailsObj.get().getCurrentBalance() - createNewTransactionDto.getAmount());
				clientBalanceDetailsObj.get().setLastTransactionAmount(-createNewTransactionDto.getAmount());
			}

			clientBalanceDetailsObj.get().setLastTransactionDate(clientBalanceTrail.getCreatedDate());
			clientBalanceDetailsObj.get().setLastTransactionRemark(createNewTransactionDto.getOrderId());
			clientBalanceDetailsObj.get().setLastTransactionId(transactionId);
			clientBalanceDetailsRepository.save(clientBalanceDetailsObj.get());
		}
		createNewTransactionDto.setTansactionId(transactionId);
		emailServiceVM.sendPaymentReceivedMail(createNewTransactionDto);
		return new Response<>(HttpStatus.CREATED.value(), "Transaction created successfully!!", null);
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
}
