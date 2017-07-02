package com.jm.repository.client.dto.zb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by ME on 2016/9/21.
 */
@Data
public class AgentDto {

    private Integer id;

    @ApiModelProperty("申请状态：0:未申请审核 1、审核中 2、已通过审核")
    private Integer status;

    @ApiModelProperty("申请角色")
    private String applyRole;

    @ApiModelProperty("加盟类型：1、个人加盟 2、公司加盟")
    private Integer type;

    @ApiModelProperty("联系姓名")
    private String userName;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("联系邮箱")
    private String email;

    @ApiModelProperty("业绩")
    private String performance;

    @ApiModelProperty("服务能力1")
    private String serviceAbility1;

    @ApiModelProperty("服务能力2")
    private String serviceAbility2;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "省地区代码")
    private String provinceCode;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "市地区代码")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "区地区代码")
    private String districtCode;

    @ApiModelProperty(value = "公司详细地址")
    private String companyAddr;

    @ApiModelProperty(value = "个人详细地址")
    private String address;

    @ApiModelProperty("公司姓名")
    private String companyName;

    @ApiModelProperty("营业执照")
    private String businessLicense;

    @ApiModelProperty("经营人数")
    private Integer manageNum;

    @ApiModelProperty("公司网址")
    private String companyUrl;

    @ApiModelProperty("公司简介")
    private String companyDesc;

    @ApiModelProperty(value="审核时间")
    private String reviewTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

//    @ApiModelProperty("部门列表")
//    private List<ZbDepartment> zbDepartmentList;
//
    @ApiModelProperty("审核建议")
    private List reviewAdviceList;
}
