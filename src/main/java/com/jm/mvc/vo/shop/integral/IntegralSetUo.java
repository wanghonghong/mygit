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
public class IntegralSetUo {

    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "是否开通，0不开通，1开通")
    private Integer isOpen;

    @ApiModelProperty(value = "是否开通奖励，0不开通，1开通")
    private Integer isAward;


    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "每日登录奖励")
    private Integer loginAward;

    @ApiModelProperty(value = "一级推荐奖励")
    private Integer oneRecommendAward;

    @ApiModelProperty(value = "二级推荐奖励")
    private Integer twoRecommendAward;

//    @ApiModelProperty(value = "是否提现，0不开通，1开通")
//    private Integer isKit;

    @ApiModelProperty(value = "是否充值，0不开通，1开通")
    private Integer isPay;

    @ApiModelProperty(value = "是否购买返利，0不开通，1开通")
    private Integer isBuy;

    @ApiModelProperty(value = "是否开通签到，0不开通，1开通")
    private Integer isLogin;

    @ApiModelProperty(value = "是否推荐关注，0不开通，1开通")
    private Integer isRecommend;

    @ApiModelProperty(value = "是否开通积分换购，0不开通，1开通")
    private Integer isExchange;

    @ApiModelProperty(value = "第一种充值金额")
    private Integer onePayMoney;

    @ApiModelProperty(value = "第二种充值金额")
    private Integer twoPayMoney;

    @ApiModelProperty(value = "第三种充值金额")
    private Integer threePayMoney;

    @ApiModelProperty(value = "购买金额")
    private Integer buyMoney;

    @ApiModelProperty(value = "返还积分")
    private Integer returnIntegral;

    @ApiModelProperty(value = "是否开通内容奖励，0不开通，1开通")
    private Integer isContent;

    @ApiModelProperty(value = "内容奖励积分")
    private Integer contentCount;

    @ApiModelProperty(value = "次数限定")
    private Integer times;

    @ApiModelProperty(value = "随机积分")
    private String randomCount;

//    @ApiModelProperty(value = "最低提现")
//    private Integer minKit;
//
//    @ApiModelProperty(value = "每天提现次数")
//    private Integer kitCount;

}
