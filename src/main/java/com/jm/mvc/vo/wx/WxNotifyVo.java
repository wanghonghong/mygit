package com.jm.mvc.vo.wx;

import lombok.Data;

/**
 * 微信支付回调Vo
 * @author chenyy
 *
 */
@Data
public class WxNotifyVo {
	
	private String returnCode;
	private String returnMsg;
	private String appid;
	private String mchId;
	private String subAppid;
	private String subMchId;
	private String deviceInfo;
	private String nonceStr;
	private String sign;
	private String resultCode;
	private String errCode;
	private String errCodeDes;
	private String openid;
	private String isSubscribe;
	private String subOpenid;
	private String subIsSubscribe;
	private String tradeType;
	private String bankType;
	private Integer totalFee;
	private String feeType;
	private Integer  cashFee;
	private String cashFeeType;
	private Integer couponFee;
	private Integer couponCount;
	private String couponId_$n;
	private Integer couponFee_$n;
	private String transactionId;
	private String outTradeNo;
	private String attach;
	private String timeEnd;

}
