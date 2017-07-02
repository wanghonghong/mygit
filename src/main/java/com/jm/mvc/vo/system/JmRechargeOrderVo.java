package com.jm.mvc.vo.system;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
public class JmRechargeOrderVo {
	
		@ApiModelProperty(value = "充值订单id")
	    private Long orderInfoId;

	    @ApiModelProperty(value = "订单流水")
	    private String orderNum;

	    @ApiModelProperty(value = "充值订单类型，现金充值 0，米币充值 1")
	    private int type;

	    @ApiModelProperty(value = "用户标识")
	    private Integer userId;

	    @ApiModelProperty(value = "充值金额")
	    private int money;

	    @ApiModelProperty(value = "状态 0：待付款; 1:已付款")
	    private int status;

	    @ApiModelProperty(value = "支付ID")
	    private Long payId;

	    @ApiModelProperty(value = "订单日期")
	    private Date createDate;

	    @ApiModelProperty(value = "支付日期")
	    private Date payDate;
	    
	    @ApiModelProperty(value = "支付类型")
	    private int payType;

		@ApiModelProperty(value = "appid")
		private  String appid;

	//----------------------聚客用--------
		private String nickname;//昵称
		private  String upNickname;//上级昵称
		private String headimgurl;//头像
		private String upHeadimgurl;//上级头像
		private String phoneNumber;//电话号码
	

}
