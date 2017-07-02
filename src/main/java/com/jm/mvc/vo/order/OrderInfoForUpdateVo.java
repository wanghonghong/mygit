package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderInfoForUpdateVo {
	
    @ApiModelProperty(value = "订单名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "状态 0：待付款; 1:待发货（已付款）; 2:待收货（已发货）; 3:已完成; 4:已关闭; 5:退款中")
    private Integer status;
    
    @ApiModelProperty(value = "卖家备注")
    private String sellerNote;
    
    @ApiModelProperty(value = "物流费")
    private Integer sendFee;
    
    @ApiModelProperty(value = "优惠方式")
    private Integer discountWay;
    
    @ApiModelProperty(value = "优惠金额")
    private Integer discountAmount;
    
    @ApiModelProperty(value = "总金额")
    private Integer totalPrice;

    @ApiModelProperty(value = "退款金额")
    private Integer refundMoney;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value="实付金额")
    private Integer realPrice;

}
