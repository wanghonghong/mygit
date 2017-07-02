package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@Entity
@ApiModel(description = "活动")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty("活动类型 1:现金红包 2：卡券红包 3：金券红包 4：天天红包 5：聚客红包")
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

    /*@ApiModelProperty(value = "活动投放礼券总数量")
    private Integer totalCount;*/

    @ApiModelProperty(value ="预计吸引粉丝人数")
    private int preFansCount;

    @ApiModelProperty(value ="实际吸引粉丝人数")
    private int fansCount;

    @ApiModelProperty(value = "未中奖推送语")
    private String noWinInfo;

    @ApiModelProperty(value = "中奖推送语")
    private String winInfo;

    @ApiModelProperty(value = "状态  0：未开始 1 ：进行中 2：暂停 3：结束 9 删除")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    
    @ApiModelProperty(value = "是否结算 0：未结算    1：已结算  （此字段只有聚客红包有使用到）")
    private int isSettlement;

    @ApiModelProperty(value = "是否发送短信 0：未发送    1：已发送  @author cj 2017-02-06")
    private int sendSms;

    @ApiModelProperty(value = "中奖次数 1：一次即中 3：三次随机  摇一摇和红包雨都有用此字段")
    private int shakeCount;

    @ApiModelProperty(value = "领取方式 0：摇一摇  1：红包雨  红包和卡券都有用到")
    private int takeType;

    public Activity(){
        this.createTime = new Date();
        this.isSettlement=0;
        this.sendSms=0;
    }
}
