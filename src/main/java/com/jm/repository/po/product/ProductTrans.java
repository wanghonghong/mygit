package com.jm.repository.po.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * Created by cj on 2016/7/5.
 */
@Data
public class ProductTrans {
    @ApiModelProperty("商品id")
    private Integer pid;
    @ApiModelProperty("配送地区id")
    private String areaId;
    @ApiModelProperty("购买个数")
    private Integer buyCount;

}
