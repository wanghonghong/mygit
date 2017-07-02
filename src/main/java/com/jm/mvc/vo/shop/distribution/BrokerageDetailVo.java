package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "佣金订单详情")
public class BrokerageDetailVo {

	@ApiModelProperty(value = "订单详情标识")
	private Long orderDetailId;

	@ApiModelProperty(value = "商品ID")
	private Integer pid;

	@ApiModelProperty(value = "商品规格ID")
	private Integer productSpecId;

	@ApiModelProperty(value = "商品数量")
	private Integer count;

	@ApiModelProperty(value = "价格")
	private Integer price;

	@ApiModelProperty(value = "规格值")
	private String specValueOne;

	@ApiModelProperty(value = "规格值")
	private String specValueTwo;

	@ApiModelProperty(value = "规格值")
	private String specValueThree;
	@ApiModelProperty(value = "商品名称")
	private String name;

	@ApiModelProperty(value = "商品图片")
	private String picSquare;

	@ApiModelProperty(value = "商品规格图片")
	private String specPic;

}
