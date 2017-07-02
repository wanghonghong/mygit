package com.jm.mvc.vo.wx.wxuser;

/**
 * <p></p>
 *
 * @author Administrator
 * @version latest
 * @date 2016/8/19
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class WxUserRo {

    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String headimgurl;

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("关注时间")
    private Date subscribeTime;

    @ApiModelProperty("最后对话时间")
    private Date finalTalksTime;

    @ApiModelProperty("最后购买时间")
    private Date finalBuyTime;

    @ApiModelProperty("备注")
    private String  remark;

    @ApiModelProperty(value = "上一级")
    private Integer upperOne;

    @ApiModelProperty(value = "上二级")
    private Integer upperTwo;

    @ApiModelProperty("等级")
    private String  levelName;

    @ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
    private int agentRole ;
}
