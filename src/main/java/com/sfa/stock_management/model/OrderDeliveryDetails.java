package com.sfa.stock_management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_delivery_details")
public class OrderDeliveryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "image_list")
	private String imageList;

	@Column(name = "description")
	private String description;

	@Column(name = "delivery_attempt_no")
	private Long deliveryAttemptNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getImageList() {
		return imageList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OrderDeliveryDetails(Long id, String orderId, String imageList) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.imageList = imageList;
	}

	public OrderDeliveryDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setImageList(String imageList) {
		this.imageList = imageList;
	}

	public Long getDeliveryAttemptNo() {
		return deliveryAttemptNo;
	}

	public void setDeliveryAttemptNo(Long deliveryAttemptNo) {
		this.deliveryAttemptNo = deliveryAttemptNo;
	}

}
