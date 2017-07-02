package com.jm.business.domain.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信退款do
 * @author chenyy
 *
 */
@Data
public class WxRefundDo {
	
	@ApiModelProperty(value = "appid")
	private String appid;
	
	@ApiModelProperty(value = "商户号")
	private String mchId;
	
	@ApiModelProperty(value = "签名")
	private String sign;
	
	@ApiModelProperty(value = "子商户公众账号ID")
	private String subAppid;
	
	@ApiModelProperty(value = "子商户号")
	private String subMchId;
	
	@ApiModelProperty(value = "终端设备号")
	private String deviceInfo;
	
	@ApiModelProperty(value = "随机串")
	private String nonceStr;
	
	@ApiModelProperty(value = "微信订单号 与 商户订单号2选1")
	private String transactionId;
	
	@ApiModelProperty(value = "商户订单号 与 微信订单号 2选1")
	private String outTradeNo;
	
	@ApiModelProperty(value = "商户退款单号")
	private String outRefundNo;
	
	@ApiModelProperty(value = "总金额")
	private Integer totalFee;
	
	@ApiModelProperty(value = "退款金额")
	private Integer refundFee;
}
