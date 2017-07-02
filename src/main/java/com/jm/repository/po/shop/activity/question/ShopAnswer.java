package com.jm.repository.po.shop.activity.question;

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
@ApiModel(description = "问卷调查答案")
public class ShopAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "问题ID")
    private Integer questionId;

    @ApiModelProperty(value = "答案")
    private Integer answer;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

}
