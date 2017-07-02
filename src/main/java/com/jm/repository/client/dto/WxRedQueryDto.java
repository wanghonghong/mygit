package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

/**
 * 微信红包发送记录查询返回参数
 * @author chenyy
 *
 */
@Data
public class WxRedQueryDto {
	
	@ApiModelProperty(value = "返回状态码")
	private String returnCode;
	
	@ApiModelProperty(value = "返回信息")
	private String returnMsg;
	
	@ApiModelProperty(value = "业务结果")
	private String resultCode;
	
	@ApiModelProperty(value = "错误代码")
	private String errCode;
	
	@ApiModelProperty(value = "错误代码描述")
	private String errCodeDes;
	
	@ApiModelProperty(value = "商户订单号")
	private String mchBillno;
	
	@ApiModelProperty(value = "商户号")
	private String mchId;
	
	@ApiModelProperty(value = "红包单号")
	private String detailId;
	
	@ApiModelProperty(value = "红包状态")
	private String status;
	
	@ApiModelProperty(value = "发放类型")
	private String sendType;
	
	@ApiModelProperty(value = "红包金额")
	private Integer totalAmount;
	
	@ApiModelProperty(value = "失败原因")
	private String reason;
	
	@ApiModelProperty(value = "红包发送时间")
	private String sendTime;
	
	@ApiModelProperty(value = "领取人")
	private String openid;
	
	@ApiModelProperty(value = "红包退款时间")
	private String refundTime;

	@ApiModelProperty(value = "红包退款金额")
	private Integer refundAmount;
	
	@ApiModelProperty(value = "领取红包的时间")
	private String rcvTime;
	
	@ApiModelProperty(value = "领取红包金额")
	private Integer amount;
	
	private int statusType;
	
	public int getStatusType(){
		if("SENDING".equals(status)){
			this.statusType = 1;
		}else if("SENT".equals(status)){
			this.statusType = 2;
		}else if("FAILED".equals(status)){
			this.statusType = 3;
		}else if("RECEIVED".equals(status)){
			this.statusType = 4;
		}else if("RFUND_ING".equals(status)){
			this.statusType = 5;
		}else if("REFUND".equals(status)){
			this.statusType = 6;
		}
		return this.statusType;	
	}


}
