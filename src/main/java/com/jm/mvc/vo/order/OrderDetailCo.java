package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/12/12
 */

@Data
public class OrderDetailCo {

    @ApiModelProperty(value = "订单详情标识")
    private Long orderDetailId;

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "规格编号")
    private Integer productSpecId;
}
