package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>商品类型</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@Entity
@ApiModel(description = "商品分组")
public class ProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "分组类型ID")
    private Integer  groupId;

    @ApiModelProperty(value = "分组名称")
    private String  groupName;
    
    @ApiModelProperty(value = "商家Id")
    private Integer shopId;
    
    @ApiModelProperty(value = "分组图片")
    private String  groupImagePath;
    
    @ApiModelProperty(value = "分组标语")
    private String  groupSlogan;

    @ApiModelProperty(value = "分组顺序")
    private int  groupSort;


}
