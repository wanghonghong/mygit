package com.jm.repository.po.order.recycle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p></p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
@Entity
public class RecycleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("订单id")
    private Long orderId;

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

    @ApiModelProperty("回收物品类型 0:衣服,1:鞋帽,2:包包,3:混合")
    private int type;

    @ApiModelProperty("是否已奖励 0:未奖励   1：已奖励")
    private int reward;

    @ApiModelProperty("奖励方式 rewardType: 0 表示积分 1表示红包")
    private Integer rewardType;

    @ApiModelProperty("本人奖励数")
    private int rewardCount;

    @ApiModelProperty("上级奖励数")
    private int upperCount;

    @ApiModelProperty("地区id")
    private Integer areaId;

}
