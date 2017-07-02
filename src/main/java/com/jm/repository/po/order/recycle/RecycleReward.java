package com.jm.repository.po.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>奖励方式</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
@Entity
@ApiModel("奖励方式")
public class RecycleReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("奖励方式 rewardType: 0 表示积分 1表示红包")
    private Integer rewardType;

    @ApiModelProperty("展示方式 showType：0表示摇一摇 1表示红包雨")
    private Integer showType;

    @ApiModelProperty("店铺id")
    private Integer shopId;

}
