package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/23
 */

@Data
public class SendsVo {

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流公司代码（code）")
    private String transCode;

    @ApiModelProperty(value = "物流单号")
    private String transNumber;

    @ApiModelProperty(value = "发货备注")
    private String deliveryNote;

    @ApiModelProperty(value = "状态，0 未发货，1 已发货")
    private int status;

    @ApiModelProperty(value = "订单详情标识")
    private String[] orderDetailId;
}
