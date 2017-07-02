package com.jm.mvc.vo.shop.integral;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@ApiModel(description = "积分商品")
public class IntegralProductVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ApiModelProperty(value = "商品ID")
    private Integer pid;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品价格")
    private int price;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "0 全额抵扣 1 限额抵扣")
    private Integer type;
    
    @ApiModelProperty(value = "抵扣积分")
    private Integer integral;

    @ApiModelProperty(value="商品规格id")
    private Integer productSpecId;

}
