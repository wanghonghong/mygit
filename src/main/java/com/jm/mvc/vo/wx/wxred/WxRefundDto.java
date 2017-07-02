package com.jm.mvc.vo.wx.wxred;

import lombok.Data;

/**
 * 退款返回参数
 * @author chenyy
 *
 */
@Data
public class WxRefundDto {
	
	private String returnCode;
	
	private String returnMsg;
	
	//以下字段在return_code为SUCCESS的时候有返回
	private String resultCode;
	
	private String errCode;
	
	private String errCodeDes;
	
	private String appid;
	
	private String mchId;
	
	private String subAppid;
	
	private String subMchId;
	
	private String deviceInfo;
	
	private String nonceStr;
	
	private String sign;
	
	private String transactionId;
	
	private String outTradeNo;
	
	private String outRefundNo;
	
	private String refundId;
	
	private String refundChannel;
	
	private Integer refundFee;
	
	private Integer settlementRefundFee;
	
	private Integer totalFee;
	
	private Integer settlementTotalFee;
	
	private String feeType;
	
	private String cashFee;
}
