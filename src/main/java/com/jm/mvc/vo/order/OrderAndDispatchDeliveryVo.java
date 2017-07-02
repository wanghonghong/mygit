package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单与发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/2/24
 */

@Data
@ApiModel(description = "订单")
public class OrderAndDispatchDeliveryVo {

    @ApiModelProperty(value = "订单详情列表")
    private List<OrderAndDeliveryVo> orderAndDeliveryVos = new ArrayList<>();

    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetailVo> orderDetails = new ArrayList<>();

}
