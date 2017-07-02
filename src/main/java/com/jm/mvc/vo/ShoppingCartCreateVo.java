/**
 * 
 */
package com.jm.mvc.vo;

import lombok.Data;


import io.swagger.annotations.ApiModelProperty;

/**
 * 
 *<p></p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
@Data
public class ShoppingCartCreateVo {
	
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

}
