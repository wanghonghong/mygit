/**
 * 
 */
package com.jm.repository.po.order;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * <p>订单发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */
@Data
@Entity
@ApiModel(description = "订单发货信息")
public class OrderDelivery {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @ApiModelProperty(value = "订单发货信息标识")
   private Integer orderDeliveryId;
   
   @ApiModelProperty(value = "订单信息标识")
   private Long orderInfoId;
   
   @ApiModelProperty(value = "发货时间")
   private Date createTime;
   
   @ApiModelProperty(value = "物流公司")
   private String transCompany;

   @ApiModelProperty(value = "物流公司代码（code）")
   private String transCode;
   
   @ApiModelProperty(value = "物流单号")
   private String transNumber;
   
   @ApiModelProperty(value = "发货备注")
   private String deliveryNote;

   @ApiModelProperty(value = "状态，0 未发货，1 已发货")
   private int status;

   public OrderDelivery(){
	   this.createTime = new Date();
   }
	
}
