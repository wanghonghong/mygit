package com.jm.repository.po.order;

import java.util.Date;

import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@Entity
@ApiModel(description = "订单信息")
public class OrderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "支付Id")
    private Long payId;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;
    
    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "总金额")
    private Integer totalPrice;

    @ApiModelProperty(value="实付金额")
    private Integer realPrice;

    @ApiModelProperty(value = "状态 0：待付款; 1:待发货（已付款）; 2:待收货（已发货）; 3:已完成; 4:已关闭; 5:退货退款中")
    private Integer status;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "地址id")
    private Integer userAddrId;
    
    @ApiModelProperty(value = "卖家备注")
    private String sellerNote;
    
    @ApiModelProperty(value = "物流费")
    private Integer sendFee;

    @ApiModelProperty(value = "优惠方式")
    private Integer discountId;
    
    @ApiModelProperty(value = "是否已查看状态: 0：未查看,1:已查看")
    private int lookStatus;

    @ApiModelProperty(value = "订单日期")
    private Date createDate;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;

    @ApiModelProperty(value = "发货日期")
    private Date sendDate;

    @ApiModelProperty(value = "收货日期")
    private Date takeDate;

    @ApiModelProperty(value = "修改价格日期")
    private Date updatePriceDate;

    @ApiModelProperty(value = "待付款推送状态")
    private int pushStatus;

    @ApiModelProperty(value = "退货状态 0:未退货, 1:退货中")
    private int goodStatus;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

    @ApiModelProperty(value = "买家申请退货日期")
    private Date createGoodDate;

    @ApiModelProperty(value = "卖家同意或拒绝退款日期")
    private Date agreeRefundDate;

    @ApiModelProperty(value="订单类型 0:普通订单 1.拼团 2.一元夺宝 3.秒杀 5.体验装 A 6.礼品")
    private Integer type;

    @ApiModelProperty(value="主单id")
    private Long parentOrderId;

    @ApiModelProperty(value="礼品单已收集个数")
    private int collectCount;

    @ApiModelProperty(value = "微博用户uid")
    private Long uid;

    @ApiModelProperty(value = "平台标识 0:微信 1:微博")
    private int platform;

    public OrderInfo(){
    	this.createDate = new Date();
    	this.orderNum = Toolkit.getOrderNum("");
    }
    
}
