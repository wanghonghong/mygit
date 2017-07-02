package com.jm.mvc.vo.zb.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/11/1.
 */
@Data
@ApiModel(description = "总部派单历史新增Co")
public class DispatchHistoryCo {

    @ApiModelProperty(value = "派单Id")
    private String dispatchId;

    @ApiModelProperty(value = "派单类型：1、业务派单 2、投诉派单 ")
    private Integer type;

    @ApiModelProperty(value = "派单负责渠道商Id")
    private Integer joinId;

    @ApiModelProperty(value = "渠道商姓名")
    private String userName;

    @ApiModelProperty(value = "渠道商联系手机")
    private String phoneNumber;

    @ApiModelProperty(value = "渠道商公司姓名")
    private String companyName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "角色等级")
    private String roleName;

    @ApiModelProperty(value = "总部派单客服id")
    private Integer checkId;

}
