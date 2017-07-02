package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>退货</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */

@Data
@ApiModel(description = "退货信息")
public class OrderRefundGoodUpdateVo {

    @ApiModelProperty(value = "退款信息标识")
    private Integer orderGoodId;

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "退货状态: 0:退货中; 1:已入库")
    private int goodStatus;

    @ApiModelProperty(value = "入库备注")
    private String storageNote;
}
