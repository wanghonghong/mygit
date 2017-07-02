package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查看下载Ro")
public class CheckDownLoadRo {

	@ApiModelProperty(value = "条码编号")
	private Integer goodsQrcodeId;

	@ApiModelProperty(value = "条码名称")
	private String name;

	@ApiModelProperty(value = "条码编号")
	private String number;

	@ApiModelProperty(value = "条码图片地址")
	private String qrcode;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "商品图片")
	private String productImg;

}
