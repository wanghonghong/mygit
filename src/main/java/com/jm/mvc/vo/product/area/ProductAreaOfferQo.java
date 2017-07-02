package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品管理")
public class ProductAreaOfferQo {

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ProductAreaOfferQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
