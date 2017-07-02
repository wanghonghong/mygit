package com.jm.repository.po.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jm.staticcode.util.Toolkit;

import java.util.Date;

/**
 * <p>聚米充值订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
@Data
@Entity
@ApiModel(description = "聚米充值订单信息")
public class JmRechargeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单信息标识")
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

    @ApiModelProperty(value = "appid")
    private String appid;

    @ApiModelProperty(value = "充值方式 0：商家平台充值 1：聚客平台充值")
    private int rechargeType;

    public JmRechargeOrder(){
    	this.createDate = new Date();
    	this.orderNum = Toolkit.getOrderNum("JM");
        this.rechargeType=0;
    }
    
}
