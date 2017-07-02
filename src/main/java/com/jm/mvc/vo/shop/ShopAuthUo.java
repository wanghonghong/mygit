package com.jm.mvc.vo.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class ShopAuthUo {
    @ApiModelProperty(value = "企业认证")
    private int companyAuth;

    @ApiModelProperty(value = "个人认证 0:未认证 1：已认证")
    private int userAuth;

    @ApiModelProperty(value = "聚米优商认证  0:未认证 1：已认证")
    private int jmAuth;

    @ApiModelProperty(value = "无假货承诺 0:未认证 1：已认证")
    private int promise;

    @ApiModelProperty(value = "7天无理由退换 0:未认证 1：已认证")
    private int exchange;

    @ApiModelProperty(value = "官方直营")
    private int directSell;

    @ApiModelProperty(value = "平安品质保险")
    private int safety;
}
