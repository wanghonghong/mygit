package com.jm.mvc.vo.shop.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * @author wuxz
 * @version latest
 * @date 2017/1/6
 */
@Data
public class ActivityUserEo {

    @ApiModelProperty(value = "发放记录id")
    private Integer activityUserId;

    @ApiModelProperty(value = "活动记录id")
    private Integer activityId;

    @ApiModelProperty(value = "礼券记录id")
    private Integer cardId;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "红包金额 以分为单位")
    private Integer money;

    @ApiModelProperty(value = "现金红包领取状态： 0：未发送   1：未领取    2 已领取   3：发放失败    4：已退款")
    private int moneyStatus;

    @ApiModelProperty(value = "卡券名称")
    private String cardName;

    @ApiModelProperty(value = "卡券金额")
    private int cardMoney;

    @ApiModelProperty(value = "礼券红包使用状态：0未领取,1未使用，2已使用 ,9删除")
    private Integer cardStatus;

    @ApiModelProperty(value = "创建时间")
    private Date cardCreateTime;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ActivityUserEo(){
        this.curPage = 0;
        this.pageSize = 10;
    }
}
