package com.sfa.stock_management.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sfa.stock_management.service.CouponService;
import com.sfa.stock_management.service.EmailServiceVM;

@Component
public class Scheduler {

	@Autowired
	private EmailServiceVM emailServiceVM;
	
	@Autowired
	private CouponService couponService;
	
	@Scheduled(cron = "00 00 18 * * *",zone = "UTC")
	public void sendEmailOnLessAvailStock() {
		emailServiceVM.sendEmail();
	}
	
	@Scheduled(cron = "00 00 18 * * *",zone = "UTC")
	public void setExpiredCouponAsDeactivate() {
		couponService.setExpiredCouponAsDeactivate();
	}
	
	
}
