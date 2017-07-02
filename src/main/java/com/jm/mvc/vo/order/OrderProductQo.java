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
@ApiModel(description = "代付款订单商品限购查询")
public class OrderProductQo {

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "商品购买数量")
    private Integer count;

}
