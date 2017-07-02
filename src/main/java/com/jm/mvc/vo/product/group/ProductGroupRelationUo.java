package com.jm.mvc.vo.product.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增商品分组
 * @author zhengww
 *
 */
@Data
@ApiModel(description = "新增商品分组")
public class ProductGroupRelationUo {

    @ApiModelProperty(value = "关系主键")
    private Integer  relationId;

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "商品分组类型ID")
    private Integer  groupId;
    
}
