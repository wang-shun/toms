package com.fanqielaile.toms.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestParam;

public class PmsCancelOrderParam {
	/* oms订单号 */
	@NotNull(message = "oms订单号不能为空！")
	String omsOrderCode;
	/* 扣款状态 true为扣款，false不扣款 */
	boolean refundStatus = true;
	/*toms订单id*/
	String id;

	public String getOmsOrderCode() {
		return omsOrderCode;
	}

	public void setOmsOrderCode(String omsOrderCode) {
		this.omsOrderCode = omsOrderCode;
	}

	public boolean isRefundStatus() {
		return refundStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRefundStatus(boolean refundStatus) {
		this.refundStatus = refundStatus;
	}

}
