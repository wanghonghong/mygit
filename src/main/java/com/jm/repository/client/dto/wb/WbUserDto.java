package com.jm.repository.client.dto.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>微博用户表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/3/10
 */
@Data
@ApiModel(description = "微博用户表Dto")
public class WbUserDto {

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

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;

    @ApiModelProperty(value = "用户当前的语言版本，zh_CN 简体，zh_TW 繁体，en英语")
    private String language;

    @ApiModelProperty(value = "用户头像地址（中图），50×50像素")
    private String headimgurl;

    @ApiModelProperty(value = "用户头像地址（大图），180×180像素")
    private String headimgurlLarge;

    @ApiModelProperty(value = "用户头像地址（高清），高清头像原图")
    private String headimgurlHd;

    @ApiModelProperty(value = "用户是否订阅该账号，值为0时，代表此用户没有订阅该账号；1为订阅")
    private Integer subscribe;

    @ApiModelProperty(value = "订阅时间")
    private String subscribeTime;

    @ApiModelProperty(value = "取消订阅时间")
    private String unSubscribeTime;

    @ApiModelProperty(value = "该用户是否关注access_token中的uid，1：是，0：否")
    private Integer follow;

    @ApiModelProperty(value = "错误信息")
    private String error;

    @ApiModelProperty(value = "错误代码")
    private String error_code;

    @ApiModelProperty(value = "请求")
    private String request;



}
