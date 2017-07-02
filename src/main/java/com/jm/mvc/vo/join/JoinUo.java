package com.jm.mvc.vo.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>加盟Uo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/30
 */
@Data
@ApiModel(description = "软件代理加盟Uo")
public class JoinUo {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer status;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("加盟子类型：1、个人加盟 2、公司加盟")
    private Integer subType;

    @ApiModelProperty("申请角色")
    private Integer applyRole;

    @ApiModelProperty("联系姓名")
    private String userName;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("联系邮箱")
    private String email;

    @ApiModelProperty("业绩")
    private String performance;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
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

}
