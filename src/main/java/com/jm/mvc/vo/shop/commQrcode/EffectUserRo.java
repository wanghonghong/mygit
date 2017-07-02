package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品条码 效果用户Ro")
public class EffectUserRo {

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "姓名")
	private String userName;

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "关注时间")
	private Date subscribeTime;

	@ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
	private Integer agentRole ;
	@ApiModelProperty(value = "购买金额")
	private Integer realPrice;

}
