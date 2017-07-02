package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/10
 */

@Data
@ApiModel(description = "退款")
public class OrderRefundUpdateVo {

    @ApiModelProperty(value = "退款信息标识")
    private Integer id;

    @ApiModelProperty(value = "退款金额")
    private Integer refundMoney;

    @ApiModelProperty(value = "退款状态: 0:申请退款; 1:已退款; 2:拒绝退款; 3:退款中 ")
    private int refundStatus;

    @ApiModelProperty(value = "退款具体方式：1、按卖家申请退款 2：协议后申请退款")
    private Integer operateWay;

    @ApiModelProperty(value = "拒绝退款原因")
    private String refuseReason;

}
