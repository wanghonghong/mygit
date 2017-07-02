package com.jm.mvc.vo.wx.template;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WxTemplateMsgCo {
	
	@ApiModelProperty("标题")
	private String first;
	
	@ApiModelProperty("简介、备注")
	private String description;
	
	@ApiModelProperty("点击连击")
	private String url;
	
	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("主图地址")
	private String picUrl;
	
	@ApiModelProperty("发送对象")
	private List<String>openids;
	
	@ApiModelProperty("是否定时发送  1：是  0：否")
	private int  isTiming;
	
	@ApiModelProperty("定时发送的时间,定时发送的时候字段才有值")
	private Date timingSendTime;
	
	
	@ApiModelProperty("查询关注开始时间")
	private Date subStartDate;
	
	@ApiModelProperty("查询关注结束时间")
	private Date subEndtDate;
	
	@ApiModelProperty("地区    多个用,隔开")
	private List<String> areaIds;
	
	@ApiModelProperty("性别")
	private Integer sex;
	
	@ApiModelProperty("角色")
	private Integer role;
	
	@ApiModelProperty("分组")
	private Integer groupid;
	
	@ApiModelProperty("对应的url超链接名字")
	private String urlName;
	
	public WxTemplateMsgCo(){
		this.sex = -1;
		this.groupid = -1;
		this.areaIds = new ArrayList<>();
		this.openids = new ArrayList<>();
		this.role = -1;
	}

}
