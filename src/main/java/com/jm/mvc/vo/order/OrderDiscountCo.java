package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderDiscountCo {

    @ApiModelProperty(value = "优惠金额 单位分")
    private int count;

    @ApiModelProperty(value="优惠类型 0:积分 1.卡券")
    private int type;

    @ApiModelProperty(value="订单id")
    private Long orderInfoId;

    @ApiModelProperty(value="用户卡卷ID")
    private Integer userCardId;

}
