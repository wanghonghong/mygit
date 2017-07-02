package com.jm.repository.po.wx;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 已发送的群发消息
 * @author chenyy
 *
 */
@Data
@Entity
@ApiModel(description = "已发送消息")
public class WxContentSent {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("类型 0：群发消息   1：精推消息")
	private  int type;
	
	@ApiModelProperty("所关联的素材id")
	private Integer contentId;
	
	@ApiModelProperty("发送时间")
	private Date sendTime;
	
	@ApiModelProperty("发送状态   0:未发送  1：发送成功   2：发送失败  99：发送中")
	private int status;
	
	@ApiModelProperty("错误原因，发送成功本字段忽略")
	private String errorMsg;
	
	@ApiModelProperty("地区    多个用,隔开")
	private String areaIds;
	
	@ApiModelProperty("性别")
	private Integer sex;
	
	@ApiModelProperty("角色")
	private Integer role;
	
	@ApiModelProperty("角色")
	private Integer groupid;
	
	@ApiModelProperty("查询关注开始时间")
	private Date subStartDate;
	
	@ApiModelProperty("查询关注结束时间")
	private Date subEndtDate;
	
	@ApiModelProperty("接收总人数")
	private int count;
	
	public WxContentSent(){
		this.sex=-1;
		this.role=-1;
		this.groupid=-1;
	}

}
