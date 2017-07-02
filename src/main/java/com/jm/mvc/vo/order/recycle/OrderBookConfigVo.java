package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("预约单设置")
public class OrderBookConfigVo {

    private Integer id;

    private Integer  shopId;

    @ApiModelProperty("是否开启地区限制 0 开启 1 关闭")
    private int  areaLimit;


}
