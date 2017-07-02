package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(discriminator = "总部系统 公众号列表")
public class ErpWxUserRo {
    @ApiModelProperty(value = "用户编号")
    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "1男 2女 0未知")
    private Integer sex;

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "是否关注  0:否  1是 ")
    private Integer isSubscribe;

    @ApiModelProperty(value = "是否有购买过  0:无  1有")
    private int isBuy;

    @ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 ")
    private int agentRole ;

}
