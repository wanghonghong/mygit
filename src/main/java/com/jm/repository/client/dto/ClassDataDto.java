package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class ClassDataDto {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("联系姓名")
    private String userName;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("微信")
    private String  wxnum;

    @ApiModelProperty("邮箱")
    private String  email;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty("经营人数")
    private Integer manageNum;

    @ApiModelProperty("公司网址")
    private String companyUrl;

    @ApiModelProperty("营业执照")
    private String businessLicense;

}
