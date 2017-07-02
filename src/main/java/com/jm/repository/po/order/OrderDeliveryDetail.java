/**
 * 
 */
package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>订单发货包裹信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */
@Data
@Entity
@ApiModel(description = "订单发货包裹信息")
public class OrderDeliveryDetail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @ApiModelProperty(value = "订单发货包裹信息标识")
   private Integer id;

   @ApiModelProperty(value = "订单发货信息标识")
   private Integer orderDeliveryId;

   @ApiModelProperty(value = "订单详情标识")
   private Long orderDetailId;

}
