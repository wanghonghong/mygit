package com.jm.mvc.vo.wx.group;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxGroupVo {
	/**
	 * 微信上的分组id
	 */
	private int id;
	private String name;
	private int count;
	private int groupid;
	private String appid;
	private Integer userId;
	@ApiModelProperty(value = "微信用户ID 批量用")
	private String userIds;

}
