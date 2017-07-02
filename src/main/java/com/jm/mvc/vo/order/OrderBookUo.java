package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Data
@ApiModel(description = "订单")
public class OrderBookUo {

    @ApiModelProperty("订单状态")
    private int status;

}
