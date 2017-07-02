package com.jm.repository.po.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Entity
@ApiModel(description = "商品类型与分组关系表")
public class ProductGroupRelation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "关系主键")
    private Integer  relationId;
	
	@ApiModelProperty(value = "商品标识")
    private Integer pid;
	
	@ApiModelProperty(value = "商品分组类型ID")
    private Integer  groupId;

    @ApiModelProperty(value = "状态")
    private int  status;
	
}
