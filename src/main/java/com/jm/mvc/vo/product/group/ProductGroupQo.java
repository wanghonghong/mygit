package com.jm.mvc.vo.product.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品分组查询条件
 * @author zhengww
 *
 */
@Data
@ApiModel(description = "分组列表")
public class ProductGroupQo {

    @ApiModelProperty(value = "分组名称")
    private String  groupName;
    
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ProductGroupQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
