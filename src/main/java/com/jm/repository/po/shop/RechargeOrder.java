package com.jm.repository.po.shop;

import com.jm.staticcode.util.Toolkit;
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
@Entity
@ApiModel(description = "充值订单信息")
public class RechargeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "充值订单类型，积分充值 1，渠道充值 2")
    private Integer type;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "充值金额")
    private int money;

    @ApiModelProperty(value = "状态 0：待付款; 1:已付款")
    private int status;

    @ApiModelProperty(value = "支付ID")
    private Long payId;

    @ApiModelProperty(value = "渠道类型 1：代理商a，2代理商b，3代理商c，4代理商d，5：分销代理a 6：分销代理b，7分销代理c，8分享客 9我的小店")
    private Integer agentRole;

    @ApiModelProperty(value = "订单日期")
    private Date createDate;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;

    @ApiModelProperty(value = "平台标识 0:微信 1:微博")
    private int platform;

    public RechargeOrder(){
        this.createDate = new Date();
    }
    
}
