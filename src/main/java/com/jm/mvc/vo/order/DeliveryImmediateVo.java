package com.jm.mvc.vo.order;

import com.jm.repository.po.order.SendCompanysKd100;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>快递公司和订单详情信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/22
 */

@Data
public class DeliveryImmediateVo {

    @ApiModelProperty( value = "快递公司信息")
    private List<SendCompanysKd100> sendCompanysKd100s;

    @ApiModelProperty( value = "订单详情信息" )
    private List<OrderDetailVo> orderDetails;

    @ApiModelProperty( value = "送礼订单信息")
    private List<OrderDispatchVo> orderInfoDispatchVos;

    @ApiModelProperty( value = "订单详情数量")
    private int count;
}
