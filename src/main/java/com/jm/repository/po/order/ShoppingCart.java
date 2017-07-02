package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>购物车</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity
@ApiModel(description = "购物车列表")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "购物车一条记录标识")
    private Long id;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品规格ID")
    private Integer productSpecId;

    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "修改购物车日期")
    private Date updateTime;

    @ApiModelProperty(value = "type：0是购物车，1是兴趣单")
    private int type;

}
