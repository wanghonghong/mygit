package com.jm.mvc.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;


import lombok.Data;

@Data
@ApiModel(description = "商品管理")
public class ProductManagerForQueryVo {
	
	@ApiModelProperty(value = "商品标识")
    private Integer pid;

	@ApiModelProperty(value = "商品名称")
    private String name;

	@ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "分组id")
    private Integer groupId;

	@ApiModelProperty(value = "上下架")
    private Integer status;//0上架  1下架 2售完下架  4全部状态

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;
    
    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ProductManagerForQueryVo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
