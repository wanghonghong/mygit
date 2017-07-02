package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "通用商品条码Ro")
public class CommonQrcodeRo {

	private Integer id;

	@ApiModelProperty(value = "条码名称")
	private String name;

	@ApiModelProperty(value = "数量")
	private int count;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "条码类型")
	private int codeType;

	@ApiModelProperty(value = "有效时间  结束时间")
	private Date endTime;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "印刷备注")
	private String printRemark;

	@ApiModelProperty(value = "有效类型 0按有效时间 1永久有效  ")
	private int validType;

	@ApiModelProperty(value = "粉丝数量")
	private int userCount;
}
