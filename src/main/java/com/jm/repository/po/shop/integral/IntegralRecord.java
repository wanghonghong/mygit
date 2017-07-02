package com.jm.repository.po.shop.integral;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户积分明细表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/15
 */
@Data
@Entity
public class IntegralRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "积分类型：1 登录 2 推荐关注 3 购买返利")
    private Integer integralType;

    @ApiModelProperty(value = "积分数量")
    private int count;

    @ApiModelProperty(value = "用户编码")
    private Integer userId;

    @ApiModelProperty(value = "微信呢称")
    private String nickname;

    @ApiModelProperty(value = "店铺编码")
    private Integer shopId;

    @ApiModelProperty(value = "一级微客")
    private Integer oneUserId;

    @ApiModelProperty(value = "二级微客")
    private Integer twoUserId;

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public IntegralRecord(){
        this.createTime = new Date();
    }
}
