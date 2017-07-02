package com.jm.repository.po.wx;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
@ApiModel(description = "消息精推记录")
public class WxTemplateMsg {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("标题")
	private String first;
	
	@ApiModelProperty("简介")
	private String description;
	
	@ApiModelProperty("简介")
	private String remark;

	@ApiModelProperty("主图地址")
	private String picUrl;
	
	@ApiModelProperty("点击连击")
	private String url;
	
	@ApiModelProperty("发送人id")
	private Integer userId;
	
	@ApiModelProperty("对应的url超链接名字")
	private String urlName;
	
	@ApiModelProperty("保存时间")
	private Date createTime;

}
