package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 退款流水表
 * @author chenyy
 * @date 2016-11-1
 */
@Data
@Entity
@ApiModel(description = "退款流水表")
public class RefundRecod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    @ApiModelProperty("订单id")
    private Long orderId;
    
    @ApiModelProperty("微信退款单号(微信生成的退款单号)")
    private String refundId;
    
    @ApiModelProperty("退款金额")
    private Integer refundFee;
    
    @ApiModelProperty("退款流水号")
    private String reFunNo;
    
    @ApiModelProperty("商户退款单号(内部生成的单号传到微信的)")
    private String outRefundNo;
    
    @ApiModelProperty("退款类型   1：原路退款  2：红包退款  3：其他渠道")
    private Integer reFunType;
    
    @ApiModelProperty("退款时间")
    private Date reFunTime;
    
    @ApiModelProperty("操作人")
    private Integer opUserId;

}
