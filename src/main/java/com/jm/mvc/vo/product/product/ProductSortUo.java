package com.jm.mvc.vo.product.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *<p>商品新增vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
@ApiModel(description = "修改商品顺序")
public class ProductSortUo {
	
	@ApiModelProperty(value = "商品id")
	private Integer pid;

	@ApiModelProperty(value = "商品顺序")
	private Integer sort;
	
}
