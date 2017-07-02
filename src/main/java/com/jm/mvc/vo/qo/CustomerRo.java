package com.jm.mvc.vo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;


@Data
@ApiModel(discriminator = "客户总表")
public class CustomerRo {
    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    private String susername;

    private String headimgurl;

    private String sphonenumber;

    private Date subscribeTime;

    private Date fristSubscribeTime;

    private Integer sex;

    private String nickname;

    private String  remark;

    /**
     * 购买总金额
     */
    private Integer allprice;

    /**
     * 购买次数
     */
    private Integer frequency;

    @ApiModelProperty(value = "购买平均金额")
    private String average;

    @ApiModelProperty(value = "最后购买时间")
    private Date lasttime;

    @ApiModelProperty(value = "累计佣金")
    private Integer totalCount;

    @ApiModelProperty(value = "佣金金额")
    private Integer balance;

    private String unissued;

    private Date updateTime;

    @ApiModelProperty(value = "等级名")
    private String levelName;

    @ApiModelProperty(value = "分组名称")
    private String groupname;

    @ApiModelProperty(value = "角色")
    private Integer agentRole;

    private String appid;

    private String openid;

    @ApiModelProperty(value = "佣金金额")
    private int commissionPrice;

    @ApiModelProperty(value = "订单生成时间")
    private java.sql.Date orderDate;

    @ApiModelProperty(value = "升级时间")
    private Date suUpdateTime;

    @ApiModelProperty(value = "加盟时间")
    private Date suCreateTime;

    @ApiModelProperty(value = "0未招呼 1已招呼")
    private Integer isReply;

    @ApiModelProperty(value = "跑路时间")
    private Date unSubscribeTime;

    @ApiModelProperty(value = "是否关注  0:否  1是 98 ")
    private Integer isSubscribe;

    @ApiModelProperty(value = "等级编号")
    private Integer levelId;

    @ApiModelProperty(value = "分组编号")
    private Integer  groupid;

    @ApiModelProperty(value = "地区")
    private String areaName;
}
