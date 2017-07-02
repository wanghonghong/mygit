package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>微博用户</p>
 */
@Data
public class WbUserRo {

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像地址（中图），50×50像素")
    private String headimgurl;

    @ApiModelProperty(value = "关注时间")
    private Date subscribeTime;

    @ApiModelProperty(value = "上一级")
    private Long upperOne;

    @ApiModelProperty(value = "上二级")
    private Long upperTwo;

    @ApiModelProperty(value = "等级")
    private String levelName;

}
