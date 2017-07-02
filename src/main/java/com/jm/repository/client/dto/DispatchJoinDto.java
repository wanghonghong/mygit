package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>总部派单Dto</p>
 *
 * @author whh
 * @version latest
 * @date 2016/11/1
 */
@Data
public class DispatchJoinDto {

    private String userName;

    private String phoneNumber;

    private Integer applyRoleId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商 ")
    private Integer type;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;



}
