package com.jm.mvc.vo.wx;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

@Data
public class RefundRecodCo {
	
	 @ApiModelProperty("订单id")
	 private Long orderId;
	    
	  @ApiModelProperty("退款金额")
	  private Integer refundFee;

	  @ApiModelProperty("订单总金额")
	  private Integer totalFee;
	  
	  @ApiModelProperty("退款流水号")
	  private String reFunNo;


	    
	  @ApiModelProperty("退款类型   1：原路退款  2：红包退款  3：其他渠道")
	  private String reFunType;
	    
	  @ApiModelProperty("退款时间")
	  private Date reFunTime;
	    
	  @ApiModelProperty("操作人")
	  private Integer opUserId;
	  
	  private String clientIp;
	  
	  private String appid;
	  
	  public RefundRecodCo(Long orderId,Integer refundFee,Integer opUserId,String clientIp,String appid){
		  this.orderId=orderId;
		  this.refundFee=refundFee;
		  this.opUserId=opUserId;
		  this.clientIp=clientIp;
		  this.appid=appid;
	  }
	  

}
