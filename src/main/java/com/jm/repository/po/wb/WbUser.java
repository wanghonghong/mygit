package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微博用户表</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/3 9:28
 */
@Data
@Entity
@ApiModel(description = "微博用户")
public class WbUser {
    @Id
    @ApiModelProperty(value = "用户UID,唯一性")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "性别，1：男、2：女、0：未知")
    private Integer sex;

    @ApiModelProperty(value = "用户所在省级")
    private String province;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "用户当前的语言版本，zh_CN 简体，zh_TW 繁体，en英语")
    private String language;

    @ApiModelProperty(value = "用户头像地址（中图），50×50像素")
    private String headimgurl;

    @ApiModelProperty(value = "用户头像地址（大图），180×180像素")
    private String headimgurlLarge;

    @ApiModelProperty(value = "用户头像地址（高清），高清头像原图")
    private String headimgurlHd;

    @ApiModelProperty(value = "该用户是否关注access_token中的uid，1：是，0：否 2:游客")
    private Integer follow;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;

    public WbUser(){
        this.createTime = new Date();
    }

}
