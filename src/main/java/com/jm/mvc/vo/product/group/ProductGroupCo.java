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
public class ProductGroupCo {

    @ApiModelProperty(value = "分组类型ID")
    private Integer  groupId;

    @ApiModelProperty(value = "分组名称")
    private String  groupName;
    
    @ApiModelProperty(value = "分组图片")
    private String  groupImagePath;
    
    @ApiModelProperty(value = "分组标语")
    private String  groupSlogan;

    @ApiModelProperty(value = "分组顺序")
    private int  groupSort;

}
