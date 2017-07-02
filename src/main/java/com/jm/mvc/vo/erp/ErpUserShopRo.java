package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(discriminator = "总部系统 店铺列表")
public class ErpUserShopRo {

    @ApiModelProperty(value = "店铺二维码")
    private String pubQrcodeUrl;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "粉丝用户人数")
    private String userCount;

    @ApiModelProperty(value = "代理商名称")
    private String userName;

    @ApiModelProperty(value = "注册日期")
    private java.sql.Date createDate;

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

    @ApiModelProperty(value = "官方直营 0:未认证 1：已认证")
    private int directSell;

    @ApiModelProperty(value = "平安品质保险 0:未认证 1：已认证")
    private int safety;

    @ApiModelProperty(value = "店铺主图地址")
    private String imgUrl;

    @ApiModelProperty(value = "经营者用户名")
    private String operUserName;

    @ApiModelProperty(value = "经营者手机号")
    private String operPhoneNumber;

    @ApiModelProperty("店铺状态 0：开启  1：关闭 ")
    private int shopStatus;

    @ApiModelProperty("店铺编号")
    private Integer shopId;

}
