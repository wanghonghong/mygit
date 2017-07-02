package com.jm.mvc.vo.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * <p>现金活动条件</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(description = "现金活动条件")
public class ActivityConditionUo {

    @ApiModelProperty(value = "0全部，1男，2女")
    private Integer sex;

    @ApiModelProperty(value = "角色限定 0关闭 1开启")
    private Integer roleLimit;

    @ApiModelProperty(value = "角色限定 0关闭 1开启")
    private int downRoleLimit;

    @ApiModelProperty(value = "地区限定 0关闭 1开启")
    private Integer areaLimit;

    @ApiModelProperty(value = "购买金额")
    private int buyMoney;

    @ApiModelProperty(value = "指定角色发送红包")
    private String roles;

    @ApiModelProperty(value = "已经关注购买粉丝 99：有")
    private Integer isBuy;

    @ApiModelProperty(value = "指定角色下的用户关注发红包")
    private String downRoles;

    @ApiModelProperty(value ="条件文本ids（用，分隔）")
    private String areaIds;

    @ApiModelProperty(value ="条件文本areaNames（用，分隔）")
    private String areaNames;

    @ApiModelProperty(value ="指定分组列表")
    private String groupIds;

}
