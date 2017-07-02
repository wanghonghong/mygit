package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "通用商品条码Qo")
public class CommonQrcodeQo {


	@ApiModelProperty(value = "条形码名称")
	private String name;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "授权者名称")
	private String nickname;

	@ApiModelProperty(value = "有效时间  开始时间")
	private String startTime;

	@ApiModelProperty(value = "有效时间  结束时间")
	private String endTime;

	@ApiModelProperty(value = "创建时间  开始时间")
	private String startCreateTime;

	@ApiModelProperty(value = "创建时间   结束时间")
	private String endCreateTime;

	@ApiModelProperty(value = "是否有粉  0全部  1:无粉 2:有粉")
	private int fansType;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	@ApiModelProperty(value = "每页显示条数")
	private Integer pageSize;



	public CommonQrcodeQo(){
		this.curPage = 0;
		this.pageSize = 20;
	}

}
