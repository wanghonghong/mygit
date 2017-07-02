package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>提现申请/p>
 */
@Data
@ApiModel(description = "提现申请")
public class WxAccountKitVo {

	@ApiModelProperty(value = "id")
	private int id;

	@ApiModelProperty(value = "用户id")
	private int userId;

	@ApiModelProperty(value = "头像")
	private String headimgurl;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "账户总余额")
	private int totalBalance;

	@ApiModelProperty(value = "可提现余额")
	private int kitBalance;

	@ApiModelProperty(value = "账户累计总金额")
	private int totalCount;

	@ApiModelProperty(value = "平台：0微信，1微博")
	private int platForm;

	@ApiModelProperty(value = "申请提现时间")
	private Date kitDate;

	@ApiModelProperty(value = "提现金额")
	private int kitMoney;

	@ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客")
	private int agentRole ;

	@ApiModelProperty(value = "状态0：待审核，1待下次审核，2，审核通过")
	private int status;
}
