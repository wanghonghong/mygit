package com.jm.mvc.vo.shop.recharge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/2
 */
@Data
@ApiModel(description = "充值订单信息")
public class RechargeOrderQo {

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "充值订单类型，积分充值 1，渠道充值 2")
    private Integer type;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "充值金额")
    private Integer money;

    @ApiModelProperty(value = "状态 0：待付款; 1:已付款")
    private Integer status;

    @ApiModelProperty(value = "支付ID")
    private Long payId;

    @ApiModelProperty(value = "订单日期")
    private Date createDate;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;
    
}
