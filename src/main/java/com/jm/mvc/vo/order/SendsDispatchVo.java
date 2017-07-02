package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/23
 */

@Data
public class SendsDispatchVo {

    @ApiModelProperty( value = "送礼子订单Id标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "收货人")
    private String userName;

    @ApiModelProperty("收货人手机")
    private String phoneNumber;

    @ApiModelProperty(value = "收货地址")
    private String detailAddress;

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

}
