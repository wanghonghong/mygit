package com.jm.mvc.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>提现申请/p>
 */
@Data
@ApiModel(description = "提现申请")
public class BrokerageKitForCreateVo {

	@ApiModelProperty(value = "提现金额")
	private double kitMoney;

}
