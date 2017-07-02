package com.jm.repository.po.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ApiModel("预约单设置")
public class OrderBookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer  shopId;

    @ApiModelProperty("是否开启地区限制 0 开启 1 关闭")
    private int  areaLimit;


}
