package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "渠道用户Qo")
public class ChannelQo {

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	@ApiModelProperty(value = "每页显示条数")
	private Integer pageSize;

	public ChannelQo(){
		this.curPage = 0;
		this.pageSize = 20;
	}


}
