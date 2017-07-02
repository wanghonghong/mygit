package com.jm.repository.client.dto.zb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商家类Dto
 * Created by ME on 2016/11/22.
 */
@Data
public class BusinessDto {

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

    public BusinessDto(){
        this.curPage = 0;
        this.pageSize = 10;
    }
}
