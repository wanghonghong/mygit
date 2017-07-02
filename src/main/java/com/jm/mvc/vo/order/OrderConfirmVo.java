package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderConfirmVo {

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "产品标识")
    private Integer productId;

    @ApiModelProperty(value = "产品规格标识")
    private Integer productSpecId;


}
