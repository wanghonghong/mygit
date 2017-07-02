package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>佣金设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "订单佣金清单")
public class BrokerageOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "订单生成时间")
    private Date orderDate;

    @ApiModelProperty(value = "收货时间")
    private Date takeDate;

    @ApiModelProperty(value = "佣金比例")
    private int brokerage;

    @ApiModelProperty(value = "商品总金额")
    private int totalPrice;

    @ApiModelProperty(value = "佣金金额")
    private int commissionPrice;

    @ApiModelProperty(value = "订单类型：1佣金，2积分")
    private int type;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

    @ApiModelProperty(value = "状态")
    private int status;//0未收货，1已收货未满15天，2有效佣金 3退款退货后佣金无效

}
