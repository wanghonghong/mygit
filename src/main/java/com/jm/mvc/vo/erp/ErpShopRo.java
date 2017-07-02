package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(discriminator = "总部系统 商家类")
public class ErpShopRo {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "用户头像")
    private String headImg;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;


    @ApiModelProperty(value = "店铺数量")
    private Integer shopCount;

    @ApiModelProperty(value = "用户注册日期")
    private Date createDate;
}
