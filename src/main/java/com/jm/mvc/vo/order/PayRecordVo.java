package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

@Data
@ApiModel(description = "支付流水")
public class PayRecordVo {
	
	@ApiModelProperty("流水id")
    private Long payId;
    
    @ApiModelProperty("支付类型：1：微信支付")
    private Integer payType;

    @ApiModelProperty("第三方接口支付状态：0=调用成功 1=失败")
    private Integer payStatus;

    @ApiModelProperty("支付编号,自己系统生成的流水号")
    private String payNo;
    
    @ApiModelProperty("微信支付订单号")
    private String transactionId;

    @ApiModelProperty("支付金额")
    private Integer payMoney;

    @ApiModelProperty("支付时间")
    private Date payDate;
    
    @ApiModelProperty("微信支付结束时间")
    private String timeEnd;
    
    @ApiModelProperty("类型 ： 0、普通购买商品支付  1、积分充值支付  2、代理商充值支付")
    private int type;

}
