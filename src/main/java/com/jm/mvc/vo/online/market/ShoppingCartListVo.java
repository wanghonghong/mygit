package com.jm.mvc.vo.online.market;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>每个用户下的购物车列表和兴趣单列表</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/25
 */

@Data
public class ShoppingCartListVo {

    @ApiModelProperty(value = "购物车一条记录标识")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "商品规格ID")
    private Integer productSpecId;

    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "总金额")
    private Integer totalPrice;

    @ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String picSquare;

    @ApiModelProperty(value = "修改购物车日期")
    private Date updateTime;

    @ApiModelProperty(value = "type：0是购物车，1是兴趣单")
    private int type;

    @ApiModelProperty(value = "总条数")
    private Integer total;
}
