package com.jm.mvc.vo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "微信用户总表查询Qo")
public class CustomerQo {

	@ApiModelProperty(value = "用户编号")
	private Integer userId;

	 @ApiModelProperty(value = "昵称")
	 private String nikename;

	 @ApiModelProperty(value = "姓名")
	 private String name;

	 @ApiModelProperty(value = "手机号")
	 private String phoneNum;

	 @ApiModelProperty(value = "地区")
	 private String area;

	 @ApiModelProperty(value = "性别")
	 private Integer sex;

	@ApiModelProperty(value = "是否关注")
	private Integer isSubscribe;

	@ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8：分享客 ")
	private Integer agentRole;

	/**
	 * 关注开始时间
	 */
	private String starSubscribeTime;

	/**
	 * 关注结束时间
	 */
	private String endSubscribeTime;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	@ApiModelProperty(value = "每页显示条数")
	private Integer pageSize;

	public CustomerQo(){
		this.curPage = 0;
		this.pageSize = 20;
	}

}
