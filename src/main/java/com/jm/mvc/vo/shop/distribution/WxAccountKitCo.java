package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>提现申请/p>
 */
@Data
@ApiModel(description = "提现申请")
public class WxAccountKitCo {

	@ApiModelProperty(value = "提现金额")
	private int kitMoney;

	@ApiModelProperty(value = "提现类型：1佣金，2积分")
	private int type;
}
