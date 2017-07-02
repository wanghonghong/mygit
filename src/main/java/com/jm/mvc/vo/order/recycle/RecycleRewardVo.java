package com.jm.mvc.vo.order.recycle;

import com.jm.repository.po.order.recycle.RecycleWeightConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>奖励方式</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class RecycleRewardVo {

    private Integer id;

    @ApiModelProperty("奖励方式")
    private Integer rewardType;

    @ApiModelProperty("展示方式")
    private Integer showType;

    @ApiModelProperty("店铺id")
    private Integer shopId;

    List<RecycleWeightConfig> recycleWeightConfigList;

}
