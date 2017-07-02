package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品管理")
public class OfferRole {

    @ApiModelProperty(value = "供货商userId")
    private Integer userId;

    @ApiModelProperty(value = "供货商名字")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

}
