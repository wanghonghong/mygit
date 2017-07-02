package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * 主营类目
 * @author hantp
 * 2016.6.29
 */
@Data
@Entity
@ApiModel(description = "主营类目")
public class ShopCategory {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @ApiModelProperty(value = "类目ID")
	    private Integer  typeId;

	    @ApiModelProperty(value = "类目名称")
	    private String typeName;
}
