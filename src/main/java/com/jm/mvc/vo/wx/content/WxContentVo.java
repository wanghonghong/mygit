package com.jm.mvc.vo.wx.
content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxContentVo {
	
	public WxContentVo(){
		this.sex = -1;
		this.groupid = -1;
		this.areaIds = new ArrayList<>();
		this.openids = new ArrayList<>();
		this.role = -1;
	}
	@ApiModelProperty("分组")
	private Integer groupid;
	
	@ApiModelProperty("地区")
	private List<String> areaIds;
	
	@ApiModelProperty("性别")
	private Integer sex;
	
	@ApiModelProperty("角色")
	private Integer role;
	
	@ApiModelProperty("内容id")
	private Integer contentId;
	
	@ApiModelProperty("发送对象")
	private List<String>openids;
	
	@ApiModelProperty("预览接收对象微信号")
	private String towxname;
	
	private WxContentCo wxContentCo;
	
	@ApiModelProperty("查询关注开始时间")
	private Date subStartDate;
	
	@ApiModelProperty("查询关注结束时间")
	private Date subEndtDate;
	
	@ApiModelProperty("群发的类型 1：全部群发  2：根据分组群发  3：根据openid群发")
	private int type;
	
	@ApiModelProperty("是否定时发送  1：是  0：否")
	private int  isTiming;
	
	@ApiModelProperty("定时发送的时间,定时发送的时候字段才有值")
	private Date timingSendTime;

}
