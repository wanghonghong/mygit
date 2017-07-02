package com.jm.repository.po.wx;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 微信红包发放流水
 * @author chenyy
 *
 */
@Data
@Entity
@ApiModel(description = "微信红包流水表")
public class WxRedRecord {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer redPayId;
	
	@ApiModelProperty(value = "appid")
	private String appid;
	
	@ApiModelProperty(value = "商户号")
	private String mchId;
	
	@ApiModelProperty(value = "发送对象")
	private String openid;
	
	@ApiModelProperty(value = "订单号，与微信保持一致")
	private String mchBillno;
	
	@ApiModelProperty(value = "使用API发放现金红包时返回的红包单号")
	private String detailId;
	
	@ApiModelProperty(value = "红包金额")
	private Integer amount;
	
	@ApiModelProperty(value = "红包领取时间")
	private Date takeTime;
	
	@ApiModelProperty(value = "红包退回时间")
	private Date refundTime;
	
	@ApiModelProperty(value = "记录创建时间")
	private Date createTime;
	
	@ApiModelProperty(value = "状态 1：发放中    2：已发放待领取   3：发放失败   4：已领取  5：退款中   6：已退款")
	private int status;
	
	@ApiModelProperty(value = "失败原因")
	private String reason;
	
	public WxRedRecord(){
		this.createTime = new Date();
	}

}
