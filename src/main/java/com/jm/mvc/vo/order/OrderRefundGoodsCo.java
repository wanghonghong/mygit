package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>退货</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */

@Data
@ApiModel(description = "退货信息")
public class OrderRefundGoodsCo {

    @ApiModelProperty(value = "订单信息标识id")
    private Long orderInfoId;

    @ApiModelProperty(value = "退款金额")
    private Integer refundMoney;

    @ApiModelProperty(value = "1:订单全额退款, 2:订单部分退款, 3:协商一致退款不退货, 4:先退货后退款")
    private Integer refundWay;

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流代码")
    private String transCode;

    @ApiModelProperty(value = "物流编号")
    private String transId;

    @ApiModelProperty(value = "订单金额")
    private Integer totalMoney;

    @ApiModelProperty(value = "商品金额")
    private Integer productMoney;

    @ApiModelProperty(value = "运费金额")
    private Integer sendFee;

    @ApiModelProperty(value = "退货状态: 0:退货中; 1:已入库")
    private int goodStatus;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "拒绝退款原因")
    private String refuseReason;

}
