package com.jm.mvc.vo.product.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品管理")
public class ProductQo {

	@ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    @ApiModelProperty(value = "创建开始时间")
    private Date startDate;

    @ApiModelProperty(value = "创建结束时间")
    private Date endDate;

    @ApiModelProperty(value = "售价区间")
    private Integer minPrice;

    @ApiModelProperty(value = "售价区间")
    private Integer maxPrice;

	@ApiModelProperty(value = "状态")
    private Integer status;//0上架  1下架 2售完下架  4全部状态 9删除

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ProductQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
