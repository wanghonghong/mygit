package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/28
 */

@Data
public class OrderDeliveryVo {

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流公司代码（code）")
    private String transCode;

    @ApiModelProperty(value = "物流单号")
    private String transNumber;

    @ApiModelProperty(value = "物流信息")
    private String transMsg;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetailVo> orderDetails = new ArrayList<>();

}
