package com.jm.repository.po.dataAnalysis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户统计上下级（存储）</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/26 17:45
 */
@Data
@Entity
public class WxUserView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @ApiModelProperty(value = "")
    private String appid;

    private Integer levelOne;
    private Integer levelTwo;
    private Integer offCount;
    private Integer buyCount;

    @ApiModelProperty(value = "")
    private Date createTime;;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value="最后操作时间")
    private Date lastControlTime;

    @ApiModelProperty(value = "1男 2女 0未知")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "关注时间")
    private Date subscribeTime;

    @ApiModelProperty(value = "跑路时间")
    private Date unSubscribeTime;

    @ApiModelProperty(value = "是否关注  0:否  1是 98 ")
    private Integer isSubscribe;

    private Integer shopUserId;

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;



}
