package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>现金活动条件</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@Entity
@ApiModel(description = "现金活动条件")
public class ActivityCondition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("活动id")
    private Integer activityId;

    @ApiModelProperty(value = "0全部，1男，2女")
    private int sex;

    @ApiModelProperty(value = "角色限定 0关闭 1开启")
    private int roleLimit;

    @ApiModelProperty(value = "是否给下级发送 0是 1否")
    private int downRoleLimit;

    @ApiModelProperty(value = "地区限定 0关闭 1开启")
    private int areaLimit;

    @ApiModelProperty(value = "购买金额")
    private int buyMoney;

    @ApiModelProperty(value = "指定角色发送红包")
    private String roles;

    @ApiModelProperty(value = "角色 - 已经关注购买粉丝 99：有 0：无")
    private Integer isBuy;

    @ApiModelProperty(value = "指定角色下的用户关注发红包")
    private String downRoles;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "条件文本ids（用，分隔）")
    private String areaIds;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "条件文本areaNames（用，分隔）")
    private String areaNames;

    @ApiModelProperty(value ="指定分组列表")
    private String groupIds;

}
