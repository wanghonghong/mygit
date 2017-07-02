package com.jm.mvc.vo.shop.activity;

import com.jm.repository.po.shop.activity.ActivityCard;
import com.jm.repository.po.shop.activity.ActivityCondition;
import com.jm.repository.po.shop.activity.ActivitySub;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
public class ActivityRo {

    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty("活动类型 1:现金红包 2：卡券红包 3：红包墙")
    private Integer type;

    @ApiModelProperty("活动小类 1:首次关注发红包 2：已关注粉丝发红包 3：购买商品发红包 4：确认收货发红包 5：卡卷转赠发红包")
    private Integer subType;

    @ApiModelProperty(value ="卡卷类型 1 一张，2 随机，3 大礼包 ")
    private int cardType;

    @ApiModelProperty("红包平台 / 1:微信商城 2：微博商城")
    private Integer platform;

    @ApiModelProperty("祝福语")
    private String blessings;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动总金额以分为单位")
    private Integer totalMoney;

    @ApiModelProperty(value ="预计吸引粉丝人数")
    private Integer preFansCount;

    @ApiModelProperty(value = "未中奖推送语")
    private String noWinInfo;

    @ApiModelProperty(value = "中奖推送语")
    private String winInfo;

    @ApiModelProperty(value = "状态  0：未开始 1 ：进行中 2：暂停 3：结束")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    private ActivityCondition activityCondition;

    private List<ActivitySub> activitySubList;

    private List<ActivityCard> activityCardList;

    @ApiModelProperty(value = "摇一摇次数 1：摇一次即中 3：摇三次随机中 （此字段只有聚客红包有使用到）")
    private int shakeCount;

    @ApiModelProperty(value = "领取方式 0：摇一摇  1：红包雨   红包和卡券都有用到")
    private int takeType;

}
