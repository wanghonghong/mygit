package com.jm.mvc.vo.order;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单及订单详情</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderInfoAndDetailCreateVo {
	

    @ApiModelProperty(value = "订单")
    OrderInfoCreateVo createvo;
    
    @ApiModelProperty(value = "订单详情")
    List<OrderDetailCreateVo> detailvo;
    
}
