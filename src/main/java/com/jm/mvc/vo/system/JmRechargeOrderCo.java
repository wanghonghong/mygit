package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账户充值
 * @author chenyy
 *
 */
@Data
public class JmRechargeOrderCo {
	
	
	  @ApiModelProperty(value = "充值金额")
	    private int money;
	  
	    @ApiModelProperty(value = "充值订单类型，现金充值 0，米币充值 1")
	    private int type;
	    
	    @ApiModelProperty(value = "用户标识")
	    private Integer userId;

		@ApiModelProperty(value = "充值方式 0：商家平台充值 1：聚客平台充值")
		private int rechargeType;

}
