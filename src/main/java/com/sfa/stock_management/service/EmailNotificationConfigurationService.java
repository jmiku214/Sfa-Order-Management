package com.sfa.stock_management.service;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.model.EmailNotificationConfiguration;

public interface EmailNotificationConfigurationService {

	Response<?> saveEmailNotification(EmailNotificationConfiguration emailNotificationConfiguration);

	Response<?> getAllEmailConfigDataForClient(Long clientId);

	Response<?> deleteEmailNotification(EmailNotificationConfiguration emailNotificationConfiguration);

	Response<?> getClientEmailNotificationData(Long clientId);


}
