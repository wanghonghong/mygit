package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

/**
 * <p>订单详情</p>
 *
 * @author hantpData
 * @version latest
 * @date 2016/5/10
 */
@Data
@ApiModel(description = "订单详情")
public class OrderDetailCreateVo {

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "价格")
    private Integer price;
    
    @ApiModelProperty(value = "规格编号")
    private Integer productSpecId;

}
