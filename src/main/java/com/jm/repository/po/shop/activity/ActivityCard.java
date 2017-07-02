package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class ActivityCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "卡卷ID")
    private Integer cardId;

    @ApiModelProperty(value = "卡卷ID列表，用逗号隔开,给随即卡卷")
    private String cardIds;

    @ApiModelProperty("卡卷使用数量")
    private int useCount;

    @ApiModelProperty("卡卷总数量")
    private int totalCount;

    @ApiModelProperty("卡卷已使用数量")
    private int usedCount;

}
