package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(discriminator = "总部系统 商家类")
public class ErpShopQo {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "用户编号")
    private Integer userId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty("店铺状态 0：开启  1：关闭 ")
    private Integer shopStatus;

    @ApiModelProperty("开始时间")
    private String starTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ErpShopQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }
}
