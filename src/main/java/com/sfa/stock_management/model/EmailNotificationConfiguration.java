package com.sfa.stock_management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "email_notification_configuration")
public class EmailNotificationConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_id")
	private Long clientId;

	@OneToOne
	@JoinColumn(name = "order_status_id")
	private OrderStatus orderStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public EmailNotificationConfiguration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailNotificationConfiguration(Long id, Long clientId, OrderStatus orderStatus) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.orderStatus = orderStatus;
	}

}
