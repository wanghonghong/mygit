package com.jm.mvc.vo.wx.template;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxTemplateMsgVo {
	@ApiModelProperty("标题")
	private String first;
	
	@ApiModelProperty("简介、备注")
	private String description;
	
	@ApiModelProperty("点击连击")
	private String url;
	
	@ApiModelProperty("备注")
	private String remark;
	
	@ApiModelProperty("发送时间")
	private Date sendTime;
	
	@ApiModelProperty("发送人")
	private String userName;
	
	@ApiModelProperty("发送状态  0等待发送 、 1发送成功 、2发送失败")
	private int status;
	
	@ApiModelProperty("接收总人数")
	private int count;
	
	@ApiModelProperty("分组名称")
	private String groupName;
	
	@ApiModelProperty("地区名称")
	private String areaName;
	
	@ApiModelProperty("性别")
	private Integer sex;
	
	@ApiModelProperty("角色名称")
	private Integer role;
	
	@ApiModelProperty("地区id")
	private String areaIds;
	
	@ApiModelProperty("对应的url超链接名字")
	private String urlName;

}
