package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "送礼子订单")
public class OrderDispatchVo {

    @ApiModelProperty(value = "订单标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @ApiModelProperty(value = "收货人姓名")
    private String phoneNumber;

    @ApiModelProperty(value = "收货人地址")
    private String detailAddress;

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流公司代码（code）")
    private String transCode;

    @ApiModelProperty(value = "物流单号")
    private String transNumber;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

}
