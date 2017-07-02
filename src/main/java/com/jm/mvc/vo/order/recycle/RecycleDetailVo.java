package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
public class RecycleDetailVo {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("上门地址")
    private Integer addressId;

    @ApiModelProperty("收货备注")
    private String receiveRemark;

    @ApiModelProperty("客服备注")
    private String customRemark;


}
