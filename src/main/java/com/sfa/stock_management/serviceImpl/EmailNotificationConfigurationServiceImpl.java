package com.sfa.stock_management.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sfa.stock_management.dto.EmailNotificationResponse;
import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.EmailNotificationConfiguration;
import com.sfa.stock_management.model.OrderStatus;
import com.sfa.stock_management.repository.EmailNotificationConfigurationRepository;
import com.sfa.stock_management.repository.OrderStatusRepository;
import com.sfa.stock_management.service.EmailNotificationConfigurationService;

@Service
public class EmailNotificationConfigurationServiceImpl implements EmailNotificationConfigurationService {

	@Autowired
	private EmailNotificationConfigurationRepository emailNotificationConfigurationRepository;

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	@Override
	public Response<?> saveEmailNotification(EmailNotificationConfiguration emailNotificationConfiguration) {
		Optional<EmailNotificationConfiguration> emailConfigData = emailNotificationConfigurationRepository
				.findByClientIdAndStatusId(emailNotificationConfiguration.getClientId(),
						emailNotificationConfiguration.getOrderStatus().getId());
		if (emailConfigData != null && emailConfigData.isPresent()) {
			emailNotificationConfigurationRepository.delete(emailConfigData.get());
			return new Response<>(HttpStatus.CREATED.value(), "Email Configuration updated successfully!", null);
		} else {
			emailNotificationConfigurationRepository.save(emailNotificationConfiguration);
			return new Response<>(HttpStatus.CREATED.value(), "Email notification updated successfully!!", null);
		}
	}

	@Override
	public Response<?> getAllEmailConfigDataForClient(Long clientId) {
		List<EmailNotificationConfiguration> emailConfigData = emailNotificationConfigurationRepository
				.findAllByClientId(clientId);
		if (emailConfigData != null) {
			return new Response<>(HttpStatus.OK.value(), "OK", emailConfigData);
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No data found!!", null);
		}
	}

	@Override
	public Response<?> deleteEmailNotification(EmailNotificationConfiguration emailNotificationConfiguration) {
		Optional<EmailNotificationConfiguration> emailConfigData = emailNotificationConfigurationRepository
				.findByClientIdAndStatusId(emailNotificationConfiguration.getClientId(),
						emailNotificationConfiguration.getOrderStatus().getId());
		if (emailConfigData != null && emailConfigData.isPresent()) {
			emailNotificationConfigurationRepository.delete(emailConfigData.get());
			return new Response<>(HttpStatus.OK.value(), "Notificatio Deleted Successfully", null);
		} else {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "No Data Found..", null);
		}

	}

	@Override
	public Response<?> getClientEmailNotificationData(Long clientId) {
		List<EmailNotificationResponse> emailNotoficationResponse = new ArrayList<>();
		List<OrderStatus> orderStatusList = orderStatusRepository.findAll();
		List<EmailNotificationConfiguration> activeEmailList = emailNotificationConfigurationRepository
				.findAllByClientId(clientId);
		Map<Long, List<OrderStatus>> orderStatusMap = orderStatusList.stream()
				.collect(Collectors.groupingBy(OrderStatus::getId));
		if (activeEmailList != null && !activeEmailList.isEmpty()) {
			for (OrderStatus os : orderStatusList) {
				for (EmailNotificationConfiguration eng : activeEmailList) {
					if (eng.getOrderStatus().getId() == os.getId()) {
						EmailNotificationResponse response = new EmailNotificationResponse();
						response.setIsActive(true);
						response.setStatus(os);
						emailNotoficationResponse.add(response);
					}
				}
			}
			Map<Long, List<EmailNotificationResponse>> activeNotificationResponseMap = emailNotoficationResponse
					.stream().collect(Collectors.groupingBy(e -> e.getStatus().getId()));
			Set<Long> activeIds = activeNotificationResponseMap.keySet();
			for (Long id : activeIds) {
				orderStatusMap.remove(id);
			}
			for (Map.Entry<Long, List<OrderStatus>> entry : orderStatusMap.entrySet()) {
				for (OrderStatus status : entry.getValue()) {
					EmailNotificationResponse response = new EmailNotificationResponse();
					response.setIsActive(false);
					response.setStatus(status);
					emailNotoficationResponse.add(response);
				}

			}
		} else {
			for (OrderStatus status : orderStatusList) {
				EmailNotificationResponse response = new EmailNotificationResponse();
				response.setIsActive(false);
				response.setStatus(status);
				emailNotoficationResponse.add(response);
			}
		}
		return new Response<>(HttpStatus.OK.value(), "OK", emailNotoficationResponse);
	}

}
