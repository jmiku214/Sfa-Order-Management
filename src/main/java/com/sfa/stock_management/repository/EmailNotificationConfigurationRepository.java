package com.sfa.stock_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sfa.stock_management.model.EmailNotificationConfiguration;

@Repository
public interface EmailNotificationConfigurationRepository extends JpaRepository<EmailNotificationConfiguration, Long> {

	@Query(value = "select * from email_notification_configuration where client_id=?1 and order_status_id=?2",nativeQuery = true)
	Optional<EmailNotificationConfiguration> findByClientIdAndStatusId(Long clientId,Long statusId);

	List<EmailNotificationConfiguration> findAllByClientId(Long clientId);

}
