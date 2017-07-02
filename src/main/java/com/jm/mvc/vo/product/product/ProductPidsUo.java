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
@ApiModel(description = "多商品删除")
public class ProductPidsUo {
	
	@ApiModelProperty(value = "商品ids")
	private String ids;
	
}
