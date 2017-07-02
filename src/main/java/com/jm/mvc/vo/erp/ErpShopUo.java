package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(discriminator = "总部系统 店铺更新")
public class ErpShopUo {

    @ApiModelProperty("店铺id")
    private int shopId;

    @ApiModelProperty("店铺状态 0：开启  1：关闭 ")
    private int shopStatus;

}
