package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品管理")
public class ProductAreaRelVo {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "供货地区")
    private String areaName;

    @ApiModelProperty(value = "供货地区编码")
    private String areaCode;

    @ApiModelProperty(value = "供货商userId")
    private Integer userId;

    @ApiModelProperty(value = "供货商名字")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "供货价")
    private Integer supplyPrice;

}
