package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 微信会员等级
 * @author chenyy
 *
 */
@Data
@Entity
@ApiModel(description = "微信会员等级")
public class WxUserLevel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "主键")
	private Integer id;
	@ApiModelProperty(value = "等级名称")
	private String levelName;
	@ApiModelProperty(value = "appid")
	private String appid;
	

}
