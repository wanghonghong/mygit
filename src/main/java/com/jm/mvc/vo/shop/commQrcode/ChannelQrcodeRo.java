package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品条码Ro")
public class ChannelQrcodeRo {

	@ApiModelProperty(value = "商品条码id")
	private String goodsQrcodeId;

	@ApiModelProperty(value = "头像")
	private String headimgurl;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "姓名")
	private String userName;

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "条码名称")
	private String name;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "条码类型")
	private int codeType;

	@ApiModelProperty(value = "有效时间  结束时间")
	private Date endTime;

	@ApiModelProperty(value = "有效时间  结束时间")
	private Date createTime;

	@ApiModelProperty(value = "加粉数量")
	private int count;

	@ApiModelProperty(value = "印刷备注")
	private String printRemark;

	@ApiModelProperty(value = "有效类型 0按有效时间 1永久有效  ")
	private int validType;

	@ApiModelProperty(value = "是否有粉  0无  1有")
	private int fansType;

}
