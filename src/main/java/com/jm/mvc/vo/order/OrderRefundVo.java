package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */

@Data
@ApiModel(description = "退款信息")
public class OrderRefundVo {

    @ApiModelProperty(value = "退款信息标识")
    private Integer orderRefundId;

    @ApiModelProperty(value = "订单信息标识id")
    private Long orderInfoId;

    @ApiModelProperty(value = "退款类型,1:退单, 2:售后 ")
    private int refundType;

    @ApiModelProperty(value = "退款金额")
    private Integer refundMoney;

    @ApiModelProperty(value = "1:订单全额退款, 2:订单部分退款, 3:协商一致退款不退货, 4:先退货后退款")
    private Integer refundWay;

    @ApiModelProperty(value = "退款具体方式：1、按卖家申请退款 2：协议后申请退款")
    private Integer operateWay;

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流编号")
    private String transId;

    @ApiModelProperty(value = "订单金额")
    private Integer totalMoney;

    @ApiModelProperty(value = "商品金额")
    private Integer productMoney;

    @ApiModelProperty(value = "运费金额")
    private Integer sendFee;

    @ApiModelProperty(value = "退款状态: 0:申请退款; 1:已退款(同意退款); 2:拒绝退款; 3:退款中 ")
    private int refundStatus;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "拒绝退款原因")
    private String refuseReason;

    @ApiModelProperty(value="实付金额")
    private Integer realPrice;
}
