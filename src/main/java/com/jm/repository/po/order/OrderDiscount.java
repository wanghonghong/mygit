package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ApiModel(description = "订单优惠表")
public class OrderDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "优惠id")
    private Integer id;

    @ApiModelProperty(value = "优惠金额 单位分 / 折扣值为 整数 例如 85折")
    private int count;

    @ApiModelProperty(value="优惠类型 0:积分 1.卡券 2.分销商折扣")
    private int type;

    @ApiModelProperty(value="订单id")
    private Long orderInfoId;

    @ApiModelProperty(value="用户卡卷ID")
    private Integer userCardId;

}
