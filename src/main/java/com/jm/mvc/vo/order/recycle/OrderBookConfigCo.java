package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@ApiModel("预约单设置")
public class OrderBookConfigCo {

    private Integer  shopId;

    @ApiModelProperty("是否开启地区限制 0 开启 1 关闭")
    private int  areaLimit;


}
