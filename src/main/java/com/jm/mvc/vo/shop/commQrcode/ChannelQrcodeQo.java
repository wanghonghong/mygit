package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品条码Qo")
public class ChannelQrcodeQo {


	@ApiModelProperty(value = "条形码名称")
	private String name;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "渠道商昵称")
	private String channelName;

	@ApiModelProperty(value = "截效日期  开始时间")
	private String startEndTime;

	@ApiModelProperty(value = "截效日期  结束时间")
	private String endEndTime;

	@ApiModelProperty(value = "创建时间  开始时间")
	private String startCreateTime;

	@ApiModelProperty(value = "创建时间  结束时间")
	private String endCreateTime;

	private int fansType;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	@ApiModelProperty(value = "每页显示条数")
	private Integer pageSize;

	public ChannelQrcodeQo(){
		this.curPage = 0;
		this.pageSize = 20;
	}

}
