package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "通码授权")
public class CommonAuthUo {

	@ApiModelProperty(value = "通用商品条码明细编号")
	private String commonQrcodeDetailIds;

	@ApiModelProperty(value = "商品编号")
	private Integer goodsId;

	@ApiModelProperty(value = "微信用户编号")
	private Integer userId;

	@ApiModelProperty(value = "类型 0:不修改 1：选择商品 ")
	private int type;


}
