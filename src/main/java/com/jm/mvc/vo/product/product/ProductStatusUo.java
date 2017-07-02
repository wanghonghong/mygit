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
@ApiModel(description = "用于商品批量修改状态")
public class ProductStatusUo {
	
	@ApiModelProperty(value = "商品ids")
	private String ids;

	@ApiModelProperty(value = "修改状态")
	private int status;
}
