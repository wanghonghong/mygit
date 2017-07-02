package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "通用查看下载Ro")
public class CommonCheckDownLoadRo {

	@ApiModelProperty(value = "条码名称")
	private String name;

	@ApiModelProperty(value = "条码明细批号")
	private String detailNum;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "商品图片")
	private String productImg;

	@ApiModelProperty(value = "通码id")
	private Integer commonQrcodeId;

	@ApiModelProperty(value = "通码明细id")
	private Integer commonQrcodeDetailId;

	@ApiModelProperty(value = "压缩包地址")
	private String zipUrl;

	@ApiModelProperty(value = "条码数量")
	private int count;

}
