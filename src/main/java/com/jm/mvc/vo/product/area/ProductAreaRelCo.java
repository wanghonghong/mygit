package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@ApiModel(description = "地区供货")
public class ProductAreaRelCo {

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty(value = "供货地区编码")
    private String areaCode;

    @ApiModelProperty(value = "供货地区名称")
    private String areaName;

    @ApiModelProperty(value = "供货商userId")
    private Integer userId;

    @ApiModelProperty(value = "供货价")
    private Integer supplyPrice;
}
