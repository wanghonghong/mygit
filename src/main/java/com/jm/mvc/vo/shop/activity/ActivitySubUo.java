package com.jm.mvc.vo.shop.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivitySubUo {

    private Integer id;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "获奖比例>>分子都是1分母如下 1 2 3 4 5")
    private int winScale;

    @ApiModelProperty(value = "消耗金额（分）")
    private int consumeMoney;

    @ApiModelProperty(value = "红包金额（分）")
    private int redMoney;

    @ApiModelProperty(value = "不中奖比例0-10/存入的数值 1=代表1:1;  2=代表1：2 以此类推")
    private int noWinScale;

    @ApiModelProperty(value = "中奖人数")
    private int winCount;

    @ApiModelProperty(value = "不中奖人数")
    private int noWinCount;

    @ApiModelProperty(value = "红包派发规则顺序")
    private int seq;

    @ApiModelProperty(value = "已关注粉丝数量")
    private int fansCount;

    @ApiModelProperty(value = "老粉丝使用 分批个数（个）")
    private int oldFansCount;

    @ApiModelProperty(value = "状态  0：该规则红包未发完 1 ：该规则已发完红包")
    private int status;

}
