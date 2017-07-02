package com.jm.repository.po.order.recycle;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class RecycleWeightConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String weight;

    @ApiModelProperty("RecycleReward表Id")
    private int recycleRewardId;

    @ApiModelProperty("上级奖励数量")
    private int upperCount;

    @ApiModelProperty("本人奖励数量")
    private int userCount;

}
