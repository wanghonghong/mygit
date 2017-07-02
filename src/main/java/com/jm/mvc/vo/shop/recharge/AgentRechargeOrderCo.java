package com.jm.mvc.vo.shop.recharge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/2
 */
@Data
@ApiModel(description = "充值订单信息")
public class AgentRechargeOrderCo {

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "充值金额")
    private Integer money;

    @ApiModelProperty(value = "渠道类型 1：代理商a，2代理商b，3代理商c，4代理商d，5：分销代理a 6：分销代理b，7分销代理c,9我的小店")
    private Integer agentRole;

    @ApiModelProperty(value = "充值订单类型，积分充值 1，渠道充值 2")
    private Integer type;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;
}
