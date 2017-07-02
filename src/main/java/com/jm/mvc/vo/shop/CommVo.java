package com.jm.mvc.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "佣金明细  （App使用）")
public class CommVo {

	private String headimgurl;

	private String nickname;

	private Integer totalPrice;

	private Integer commissionPrice;

	private Integer brokerage;

	@ApiModelProperty(value = "佣金日期")
	private java.sql.Date orderDate;

	@ApiModelProperty(value = "订单状态 0：待付款; 1:待发货（已付款）; 2:待收货（已发货）; 3:已完成; 4:已关闭; 5:退货退款中")
	private Integer status;

	@ApiModelProperty(value = "退款状态: 0:申请退款; 1:已退款(同意退款); 2:拒绝退款; ")
	private int refundStatus;

}
