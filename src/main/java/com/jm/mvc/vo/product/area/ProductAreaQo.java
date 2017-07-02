package com.jm.mvc.vo.product.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品管理")
public class ProductAreaQo {

	@ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "地区code")
    private String areaCode;

    @ApiModelProperty(value = "供货属性： 0:暂无 1平台供货  2地区供货")
    private int offerType;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;



    public ProductAreaQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
