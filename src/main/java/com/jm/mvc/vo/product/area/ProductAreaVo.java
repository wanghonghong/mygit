package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "商品管理")
public class ProductAreaVo {

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品价格")
    private int price;

    @ApiModelProperty(value = "商品图片正方形")
    private String picSquare;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "商品数量")
    private int totalCount;

    @ApiModelProperty(value = "供货属性： 1平台供货  2地区供货")
    private int offerType;

    @ApiModelProperty(value = "商品状态")
    private int status;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ProductAreaVo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
