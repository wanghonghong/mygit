package com.jm.mvc.vo.product.trans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/31 16:36
 */
@Data
public class TransShopUo {
    @ApiModelProperty("同个订单多商品出现免运费产品，该单免运费")
    private Integer transCondition0ne;
    @ApiModelProperty("同个订单多商品使用统一模板，只收最高那件商品的运费金额")
    private Integer transConditionTwo;
    @ApiModelProperty("同个订单多商品使用<不同运费模板>,只收最高那商品的运费")
    private Integer transConditionThree;
}
