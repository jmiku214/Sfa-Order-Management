package com.sfa.stock_management.util;

import java.util.Date;

public class DateUtil {

	public static Date convertISTtoUTC(Date fromDate) {
		long longDate=fromDate.getTime();
		longDate=longDate-19800000;
		Date updatedDate=new Date(longDate);
		return updatedDate;
	}
	
	public static Date convertUTCTOIST(Date fromDate) {
		long longDate=fromDate.getTime();
		longDate=longDate+19800000;
		Date updatedDate=new Date(longDate);
		return updatedDate;
	}

}
