/**
 * 
 */
package com.jm.mvc.vo.qo;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 *<p>查询购物车商品信息</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月17日
 */
@Data
public class ShoppingCartQo {
	
	@ApiModelProperty(value = "购物车一条记录标识")
	private Long id;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品规格ID")
    private Integer productSpecId;

    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "价格")
    private Integer price;
    
    @ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;

    @ApiModelProperty(value = "商品名称")
    private String productName;
    
    @ApiModelProperty(value = "商品图片")
    private String pic;
    
    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "是否限购,0不限购，1限购")
    private int isLimitCount;

    @ApiModelProperty(value = "限购数量")
    private int limitCount;

    @ApiModelProperty(value = "商品库存数量")
    private int stock;

}
