package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 微信模板消息模板id
 * @author chenyy
 *
 */
@Data
@Entity
@ApiModel(description = "微信模板消息模板id")
public class WxTemplate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("模板id")
	private String templateId;
	
	@ApiModelProperty("类型  1：发货通知     2：退货通知     3：签收通知     4：退款通知    5：佣金通知   6：下级用户发生通知   7：消息精推  8:商品状态变更通知  9：发送注册通知")
	private int type;
	
	@ApiModelProperty("对应的模板编号")
	private String templateNum;

}
