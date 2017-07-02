package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
public class RecycleDetailCo {

    @ApiModelProperty("上门地址")
    private Integer addressId;

    @ApiModelProperty("用户备注")
    private String userRemark;

    @ApiModelProperty("收货备注")
    private String receiveRemark;

    @ApiModelProperty("客服备注")
    private String customRemark;

    @ApiModelProperty("图片")
    @Column(length=200)
    private String imgUrl;

    @ApiModelProperty("重量")
    private int weight;

    @ApiModelProperty("指派上门用户")
    private Integer userId;

    @ApiModelProperty("回收物品类型")
    private int type;

    @ApiModelProperty("是否已奖励")
    private int reward;

    @ApiModelProperty("奖励类型")
    private int rewardType;
}
