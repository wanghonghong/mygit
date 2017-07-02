package com.jm.mvc.vo.zb.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>加盟Ro</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/30
 */
@Data
@ApiModel(description = "加盟Vo")
public class ZbClassDataVo {

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
