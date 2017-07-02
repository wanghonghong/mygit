package com.jm.mvc.vo.shop.integral;

/**
 * <p>店铺积分设置表</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/9/18
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "店铺积分")
public class IntegralSetCo {

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "是否开通，0不开通，1开通")
    private int isOpen;

    @ApiModelProperty(value = "是否开通奖励，0不开通，1开通")
    private int isAward;

    @ApiModelProperty(value = "单位积分")
    private int unit;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "每日登录奖励")
    private int loginAward;

    @ApiModelProperty(value = "一级推荐奖励")
    private int oneRecommendAward;

    @ApiModelProperty(value = "二级推荐奖励")
    private int twoRecommendAward;

//    @ApiModelProperty(value = "是否提现，0不开通，1开通")
//    private Integer isKit;

    @ApiModelProperty(value = "是否充值，0不开通，1开通")
    private int isPay;

    @ApiModelProperty(value = "是否购买返利，0不开通，1开通")
    private int isBuy;

    @ApiModelProperty(value = "是否开通签到，0不开通，1开通")
    private int isLogin;

    @ApiModelProperty(value = "是否推荐关注，0不开通，1开通")
    private int isRecommend;

    @ApiModelProperty(value = "是否开通积分换购，0不开通，1开通")
    private int isExchange;

    @ApiModelProperty(value = "第一种充值金额")
    private int onePayMoney;

    @ApiModelProperty(value = "第二种充值金额")
    private int twoPayMoney;

    @ApiModelProperty(value = "第三种充值金额")
    private int threePayMoney;

    @ApiModelProperty(value = "购买金额")
    private int buyMoney;

    @ApiModelProperty(value = "返还积分")
    private int returnIntegral;

    @ApiModelProperty(value = "是否开通内容奖励，0不开通，1开通")
    private int isContent;

    @ApiModelProperty(value = "内容奖励积分")
    private int contentCount;

    @ApiModelProperty(value = "次数限定")
    private int times;

    @ApiModelProperty(value = "随机积分")
    private String randomCount;

//    @ApiModelProperty(value = "最低提现")
//    private Integer minKit;
//
//    @ApiModelProperty(value = "每天提现次数")
//    private Integer kitCount;

}
