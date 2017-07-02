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
 * @date 2017/2/27
 */
@Data
@ApiModel(description = "微博用户表Dto")
public class WbUserDto3 {

    @ApiModelProperty(value = "用户UID,唯一性")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String screenName;

    @ApiModelProperty(value = "友好显示名称")
    private String name;

    @ApiModelProperty(value = "用户所在省级ID")
    private Integer province;

    @ApiModelProperty(value = "用户所在城市ID")
    private Integer city;

    @ApiModelProperty(value = "用户所在地")
    private String location;

    @ApiModelProperty(value = "用户个人描述")
    private String description;

    @ApiModelProperty(value = "用户博客地址")
    private String url;

    @ApiModelProperty(value = "用户头像地址（中图），50×50像素")
    private String profileImageUrl;

    @ApiModelProperty(value = "用户的微博统一URL地址")
    private String profileUrl;

    @ApiModelProperty(value = "用户的个性化域名")
    private String userDomain;

    @ApiModelProperty(value = "性别，m：男、f：女、n：未知")
    private String gender;

    @ApiModelProperty(value = "粉丝数")
    private Integer followersCount;

    @ApiModelProperty(value = "关注数")
    private Integer friendsCount;

    @ApiModelProperty(value = "微博数")
    private Integer statusesCount;

    @ApiModelProperty(value = "收藏数")
    private Integer favouritesCount;

    @ApiModelProperty(value = "是否允许所有人给我发私信，true：是，false：否")
    private Boolean allowAllActMsg;

    @ApiModelProperty(value = "是否是微博认证用户，即加V用户，true：是，false：否")
    private Boolean verified;

    @ApiModelProperty(value = "是否允许所有人对我的微博进行评论，true：是，false：否")
    private Boolean allowAllComment;

    @ApiModelProperty(value = "该用户是否关注当前登录用户，true：是，false：否")
    private Boolean followMe;

    @ApiModelProperty(value = "用户头像地址（大图），180×180像素")
    private String avatarLarge;

    @ApiModelProperty(value = "用户的在线状态，0：不在线、1：在线")
    private Integer onlineStatus;

    @ApiModelProperty(value = "用户的最近一条微博信息字段")
    private Object status;

    @ApiModelProperty(value = "用户的互粉数")
    private Integer biFollowersCount;

    @ApiModelProperty(value = "用户备注信息，只有在查询用户关系时才返回此字段")
    private String remark;

    @ApiModelProperty(value = "用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语\n")
    private String lang;

    @ApiModelProperty(value = "认证原因")
    private String verifiedReason;

    @ApiModelProperty(value = "用户的微号")
    private String weihao;

    @ApiModelProperty(value = "用户的最近一条微博信息字段Id")
    private String statusId;

    private List<WbUserDto3> users;

    @ApiModelProperty(value = "")
    private Integer nextCursor;

    @ApiModelProperty(value = "")
    private Integer previousCursor;

    @ApiModelProperty(value = "")
    private Integer totalNumber;

    @ApiModelProperty(value = "")
    private Integer displayTotalNumber;

    @ApiModelProperty(value = "错误信息")
    private String error;

    @ApiModelProperty(value = "错误代码")
    private String error_code;

    @ApiModelProperty(value = "请求")
    private String request;

}
