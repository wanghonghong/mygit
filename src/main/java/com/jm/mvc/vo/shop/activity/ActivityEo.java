package com.jm.mvc.vo.shop.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author wuxz
 * @version latest
 * @date 2017/1/6
 */
@Data
public class ActivityEo {

    @ApiModelProperty(value = "活动记录id")
    private Integer activityId;

    @ApiModelProperty("活动小类 1:首次关注发红包 2：已关注粉丝发红包 3：购买商品发红包 4：确认收货发红包 5：卡卷转赠发红包")
    private Integer subType;

    @ApiModelProperty(value = "活动总金额以分为单位")
    private Integer totalMoney;

    @ApiModelProperty(value ="预计吸引粉丝人数")
    private int preFansCount;

    @ApiModelProperty(value ="实际吸引粉丝人数")
    private int fansCount;

    @ApiModelProperty(value ="实际发放金额以分为单位")
    private Integer money;

    @ApiModelProperty(value ="卡卷总数量")
    private int cardTotalCount;

    @ApiModelProperty(value ="实际发放卡卷总数量")
    private int cardUseCount;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
