package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>活动用户</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/12/03
 */
@Data
@Entity
@ApiModel(description = "活动用户")
public class ActivityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    
    @ApiModelProperty(value = "红包流水ID")
    private Integer redPayId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "红包金额 以分为单位")
    private Integer money;
    
    @ApiModelProperty(value = "领取状态： 0：未发送   1：未领取    2 已领取   3：发放失败    4：已退款")
    private int status;

     public ActivityUser(){
         this.createTime = new Date();
     }
}
