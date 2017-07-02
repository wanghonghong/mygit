package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>支付记录表</p>
 *
 * @author chenyy
 * @version latest
 */
@Data
@Entity
@ApiModel(description = "支付记录")
public class PayRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;
    
    @ApiModelProperty("支付类型：1：微信支付 2:微博支付")
    private Integer payType;

    @ApiModelProperty("第三方接口支付状态：1=调用成功 0=失败")
    private Integer payStatus;

    @ApiModelProperty("支付编号,自己系统生成的流水号")
    private String payNo;
    
    @ApiModelProperty("微信/微博支付订单号")
    private String transactionId;

    @ApiModelProperty("支付金额")
    private Integer payMoney;

    @ApiModelProperty("支付时间")
    private Date payDate;
    
    @ApiModelProperty("微信支付结束时间")
    private Date payEndDate;

}
