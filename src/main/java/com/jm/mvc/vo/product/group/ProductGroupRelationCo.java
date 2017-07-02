package com.jm.mvc.vo.product.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@ApiModel(description = "商品类型与分组关系表")
public class ProductGroupRelationCo {
	
	@ApiModelProperty(value = "商品分组类型ID")
    private Integer  groupId;
	
}
