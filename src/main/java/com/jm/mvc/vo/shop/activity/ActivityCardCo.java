package com.jm.mvc.vo.shop.activity;

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
@ApiModel(description = "活动")
public class ActivityCardCo {

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "卡卷ID")
    private Integer cardId;

    @ApiModelProperty(value = "卡卷ID列表，用逗号隔开")
    private String cardIds;

    @ApiModelProperty("卡卷总数量")
    private int totalCount;

}
