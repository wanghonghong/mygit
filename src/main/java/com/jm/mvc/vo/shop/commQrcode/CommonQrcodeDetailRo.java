package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "通用商品条码明细Ro")
public class CommonQrcodeDetailRo {

	private Integer commonQrcodeDetailId;

	@ApiModelProperty(value = "条码名称")
	private String name;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "条码类型")
	private int codeType;

	@ApiModelProperty(value = "有效时间  结束时间")
	private Date endTime;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "头像")
	private String headimgurl;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "有效类型 0按有效时间 1永久有效  ")
	private int validType;

	@ApiModelProperty(value = "粉丝数量")
	private int userCount;
}
