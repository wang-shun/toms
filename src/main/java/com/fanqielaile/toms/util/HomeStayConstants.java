package com.fanqielaile.toms.util;

public interface HomeStayConstants {
	int minBookingDays = 1;
	int maxBookingDays = 999;
	String checkOutTime = "12:00";
	String checkInTime = "14:00";
	String receptionHours = "14:00 - 23：00";
	int depositAmount = 0;
	String houseModel = "标准间";
	String avatarUrl ="http://img.fanqiele.com/imgFtpFolder/1474337511842/yflx.jpg";
	String depositType = "1";
	
	int invoiceType = 0;
	int hasLandlord = 0;
	int refundDays = 1;
	String tradingRules = "入住前1天 14:00前如果取消订单，预付定金将全部退还";
	int instantBook = 1;
	/**
	 * 线上支付比例
	 */
	int OnlinePayRatio = 100;
	String bedType = "床位";
	
	int DEFAULE_PAGE_INDEX = 0;
	int DEFAULE_PAGE_SIZE = 50;
	Integer BOOKING_STATUS_YES = 1;
	Integer BOOKING_STATUS_NO = 0;
	int maxGuests = 999;
	int houseSize = 10;
}

