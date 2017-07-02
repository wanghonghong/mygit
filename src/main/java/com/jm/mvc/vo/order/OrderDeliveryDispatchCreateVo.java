/**
 * 
 */
package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>订单发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/2/20
 */
@Data
@ApiModel(description = "订单发货信息")
public class OrderDeliveryDispatchCreateVo {

   @ApiModelProperty( value = "送礼母订单Id标识")
   private Long orderInfoId;

   @ApiModelProperty(value = "送礼人")
   private String giverName;

   @ApiModelProperty(value = "礼品名")
   private String productName;

   @ApiModelProperty( value = "发货信息")
   List<SendsDispatchVo> sendsDispatchVos;
	
}
