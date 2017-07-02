/**
 * 
 */
package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

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
@ApiModel(description = "订单发货信息")
public class OrderDeliveryCreateVo {

   @ApiModelProperty( value = "订单信息标识")
   private Long orderInfoId;

   @ApiModelProperty( value = "订单详情与发货信息")
   List<SendsVo> orderDetailAndSendsVos;
	
}
