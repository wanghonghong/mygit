package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 */
@Data
public class OrderBookAreaUo {

    @ApiModelProperty("快递编号")
    private Integer kdId;

    @ApiModelProperty("快递名称")
    private String kdName;

}
