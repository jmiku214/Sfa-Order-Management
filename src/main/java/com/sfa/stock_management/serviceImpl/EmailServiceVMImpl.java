package com.sfa.stock_management.serviceImpl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sfa.stock_management.constant.Constant;
import com.sfa.stock_management.dto.CreateNewTransactionDto;
import com.sfa.stock_management.dto.OrderDto;
import com.sfa.stock_management.dto.OrderSaveRequestDto;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.Order;
import com.sfa.stock_management.model.OrderProductMapping;
import com.sfa.stock_management.model.Product;
import com.sfa.stock_management.model.Unit;
import com.sfa.stock_management.model.UnitConversionMapping;
import com.sfa.stock_management.repository.OrderProductMappingRepository;
import com.sfa.stock_management.repository.OrderRepository;
import com.sfa.stock_management.repository.ProductRepository;
import com.sfa.stock_management.repository.UnitConversionMappingRepository;
import com.sfa.stock_management.repository.UnitRepository;
import com.sfa.stock_management.service.EmailServiceVM;

@Service
public class EmailServiceVMImpl implements EmailServiceVM {

	@Autowired
	private JavaMailSender javaMailService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderProductMappingRepository orderProductMappingRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Value("${sfa.backend.url}")
	private String sfaBackendUrl;

	@Autowired
	private UnitConversionMappingRepository unitConversionMappingRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Override
	public void sendEmail() {
		try {

			List<Product> products = productRepository.findAllLessAvailableProduct();
			int i = 1;
			if (products != null && !products.isEmpty()) {
				Map<Long, List<Product>> prodMap = products.stream()
						.collect(Collectors.groupingBy(Product::getCreatedBy));
				for (Map.Entry<Long, List<Product>> entry : prodMap.entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append("<html><body>");
					sb.append("<p>The below product have less available stock.</p>");
					sb.append("<table style='border: 2px solid black;'>");
					sb.append(
							"<tr><th style='padding:10px; text-align:center; border: 2px solid black; '>SL No.</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Product Name</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Available Quantity</th></tr>");
					for (Product prod : entry.getValue()) {
						Optional<Unit> unitData = unitRepository.findById(prod.getUnit());
						sb.append("<tr>");
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + i
								+ "</td>");
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ prod.getName() + "</td>");
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ prod.getAvailableQuantity() + " " + unitData.get().getName() + "</td>");

						i++;
					}
					MimeMessage mimeMsg = javaMailService.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
					RestTemplate restTemplate = new RestTemplate();
					String url = sfaBackendUrl + Constant.USER_INFO_BY_ID;
					String requestBody = "{\"userIds\":" + "[" + entry.getKey() + "]" + "}";
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
					ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, entity,
							Response.class);
					JSONObject jsnobject = new JSONObject(response.getBody());
					JSONArray dataArray = jsnobject.getJSONArray("data");
					JSONObject obj = dataArray.getJSONObject(0);
					if (obj.has("email")) {
						String[] mails = obj.getString("email").split(",");
						helper.setTo(mails);
						helper.setSubject("Less Available Stock For The Below Products!!");
						helper.setText(sb.toString(), true);
						EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
						Thread parallelThread = new Thread(sendEmail);
						parallelThread.setPriority(Thread.MAX_PRIORITY);
						parallelThread.start();

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendOrderCreatedEmail(OrderSaveRequestDto orderSaveRequestDto) {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body>");
			sb.append("<p>Your Order </p>" + orderSaveRequestDto.getOrderId() + "<p>has been placed succesfully.</p>");
			sb.append("<table style='border: 2px solid black;'>");
			sb.append(
					"<tr><th style='padding:10px; text-align:center; border: 2px solid black; '>SL No.</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Order Id.</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Product Name</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Quantity</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Price</th></tr>");
			List<OrderDto> orders = orderSaveRequestDto.getOrder();
			int i = 1;
			if (orders != null && !orders.isEmpty()) {
				for (OrderDto ord : orders) {
					sb.append("<tr>");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + i + "</td>");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ orderSaveRequestDto.getOrderId() + "</td>");
					Optional<Product> proOptional = productRepository.findById(ord.getProduct().getId());
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ proOptional.get().getName() + "</td>");
					Optional<Unit> unit = unitRepository.findById(proOptional.get().getUnit());
					if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() == proOptional.get().getUnit().longValue()) {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					} else if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() != proOptional.get().getUnit().longValue()) {
						Optional<UnitConversionMapping> unitObj = unitConversionMappingRepository
								.findById(ord.getUnitId());
						ord.setQuantity(ord.getQuantity() * unitObj.get().getSubUnitQty());
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unitObj.get().getSubUnitName() + "</td>");
					} else {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					}

					Double totalPrice = 0D;
					if (ord.getTotalPrice() != null) {
						totalPrice = ord.getTotalPrice();
						if (ord.getTotalCgstPrice() != null) {
							totalPrice = totalPrice + ord.getTotalCgstPrice();
						}
						if (ord.getTotalSgstPrice() != null) {
							totalPrice = totalPrice + ord.getTotalSgstPrice();
						}
						if (ord.getTotalIgstPrice() != null) {
							totalPrice = totalPrice + ord.getTotalIgstPrice();
						}
						DecimalFormat decimalFormatter = new DecimalFormat("#,###");

						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ decimalFormatter.format(totalPrice) + "</td>");
					}

					i++;
				}
				MimeMessage mimeMsg = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
				helper.setTo(orderSaveRequestDto.getClientEmailAddress());
				if (orderSaveRequestDto.getCcMails() != null) {
					helper.setCc(orderSaveRequestDto.getCcMails());
				}

				helper.setSubject("OrderID " + orderSaveRequestDto.getOrderId() + " Has Been Placed Successfully");
				helper.setText(sb.toString(), true);
				EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
				Thread parallelThread = new Thread(sendEmail);
				parallelThread.setPriority(Thread.MAX_PRIORITY);
				parallelThread.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendOrderUpdateStatusMail(String name, String orderId, String mailId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body>");
			sb.append("<p>Your Order with Id " + orderId + " has been " + name + "..</p>");
			MimeMessage mimeMsg = javaMailService.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
			helper.setTo(mailId);
			helper.setSubject("Your Order " + orderId + " Status Has Been Changed..");
			sb.append(
					"<tr><th style='padding:10px; text-align:center; border: 2px solid black; '>SL No.</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Order Id.</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Product Name</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Quantity</th><th style='padding:10px; text-align:center; border: 2px solid black; '>Price</th></tr>");
			List<OrderProductMapping> orderProOptional = orderProductMappingRepository.findAllByOrderId(orderId);
			Double totalPrice = 0D;
			if (orderProOptional != null) {
				int i = 1;
				for (OrderProductMapping ord : orderProOptional) {
					sb.append("<tr>");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + i + "</td>");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ ord.getOrderId() + "</td>");
					Optional<Product> proOptional = productRepository.findById(ord.getProduct().getId());
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ proOptional.get().getName() + "</td>");
					Optional<Unit> unit = unitRepository.findById(proOptional.get().getUnit());
					if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() == proOptional.get().getUnit().longValue()) {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					} else if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() != proOptional.get().getUnit().longValue()) {
						Optional<UnitConversionMapping> unitObj = unitConversionMappingRepository
								.findById(ord.getUnitId());
						ord.setQuantity(ord.getQuantity() * unitObj.get().getSubUnitQty());
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unitObj.get().getSubUnitName() + "</td>");
					} else {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					}
					totalPrice = totalPrice + ord.getAfterAddGstTotalPrice();
					DecimalFormat decimalFormatter = new DecimalFormat("#,###");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ decimalFormatter.format(totalPrice) + "</td>");
				}
			}
			helper.setText(sb.toString(), true);
			EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
			Thread parallelThread = new Thread(sendEmail);
			parallelThread.setPriority(Thread.MAX_PRIORITY);
			parallelThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendPaymentReceivedMail(CreateNewTransactionDto createNewTransactionDto) throws MessagingException {
		Optional<Order> order = orderRepository.findByOrderId(createNewTransactionDto.getOrderId());
		if (order.get().getClientEmailId() != null && !order.get().getClientEmailId().isEmpty()
				&& order.get().getClientEmailId() != "") {
			if (createNewTransactionDto.getTransactionTypeId() == 1) {
				StringBuilder sb = new StringBuilder();
				MimeMessage mimeMsg = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
				helper.setTo(order.get().getClientEmailId());
				helper.setSubject("Payment Received.");
				sb.append("<html><div>");
				sb.append("<h3>Hi " + order.get().getClientName() + ",</h3>");
				sb.append("<h4>Your payment of Rs. " + createNewTransactionDto.getAmount()
						+ " , is received against Order Id " + createNewTransactionDto.getOrderId() + "."
						+ " Your transaction Id is " + createNewTransactionDto.getTansactionId() + ".</h4>");
				sb.append("<h4>Thank you</h4>");
				sb.append(" </div></html>");
				helper.setText(sb.toString(), true);
				EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
				Thread parallelThread = new Thread(sendEmail);
				parallelThread.setPriority(Thread.MAX_PRIORITY);
				parallelThread.start();
			} else {
				StringBuilder sb = new StringBuilder();
				MimeMessage mimeMsg = javaMailService.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
				helper.setTo(order.get().getClientEmailId());
				helper.setSubject("Amount Debited.");
				sb.append("<html><div>");
				sb.append("<h3>Hi " + order.get().getClientName() + ",</h3>");
				sb.append("<h4>Your payment of Rs. " + createNewTransactionDto.getAmount()
						+ " , is debited against Order Id " + createNewTransactionDto.getOrderId() + "."
						+ " Your transaction Id is " + createNewTransactionDto.getTansactionId() + ".</h4>");
				sb.append("<h4>Thank you</h4>");
				sb.append(" </div></html>");
				helper.setText(sb.toString(), true);
				EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
				Thread parallelThread = new Thread(sendEmail);
				parallelThread.setPriority(Thread.MAX_PRIORITY);
				parallelThread.start();
			}

		}

	}

	@Override
	public void sendDispatchProductsMail(String name, String orderId, String clientEmailId,
			List<OrderDto> orderDispatchList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body>");
			sb.append("<p>Your Order with Id " + orderId + " has been " + name + "..</p>");
			MimeMessage mimeMsg = javaMailService.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true);
			helper.setTo(clientEmailId);
			helper.setSubject("Your Order " + orderId + " Status Has Been Changed..");
			sb.append("<tr>" + "<th style='padding:10px; text-align:center; border: 2px solid black; '>SL No.</th>"
					+ "<th style='padding:10px; text-align:center; border: 2px solid black; '>Product Name</th>"
					+ "<th style='padding:10px; text-align:center; border: 2px solid black; '>Quantity</th>"
					+ "<th style='padding:10px; text-align:center; border: 2px solid black; '>Is Sample</th></tr>");
			List<OrderProductMapping> orderProOptional = orderProductMappingRepository.findAllByOrderId(orderId);
			if (orderProOptional != null) {
				int i = 1;
				for (OrderDto ord : orderDispatchList) {
					sb.append("<tr>");
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + i + "</td>");
					Optional<Product> proOptional = productRepository.findById(ord.getProduct().getId());
					sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
							+ proOptional.get().getName() + "</td>");
					Optional<Unit> unit = unitRepository.findById(proOptional.get().getUnit());
					if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() == proOptional.get().getUnit().longValue()) {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					} else if (ord.getUnitId() != null
							&& ord.getUnitId().longValue() != proOptional.get().getUnit().longValue()) {
						Optional<UnitConversionMapping> unitObj = unitConversionMappingRepository
								.findById(ord.getUnitId());
						ord.setQuantity(ord.getQuantity() * unitObj.get().getSubUnitQty());
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unitObj.get().getSubUnitName() + "</td>");
					} else {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>"
								+ ord.getQuantity() + " " + unit.get().getName() + "</td>");
					}
					if (ord.getIsSample() != null && ord.getIsSample().booleanValue() == true) {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + "Yes"
								+ "</td>");
					}
					if (ord.getIsSample() != null && ord.getIsSample().booleanValue() == false) {
						sb.append("<td style='padding:10px; text-align:center; border: 2px solid black; '>" + "No"
								+ "</td>");
					}

				}
			}
			helper.setText(sb.toString(), true);
			EmailThread sendEmail = new EmailThread(javaMailService, mimeMsg);
			Thread parallelThread = new Thread(sendEmail);
			parallelThread.setPriority(Thread.MAX_PRIORITY);
			parallelThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
