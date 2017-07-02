package com.jm.mvc.vo.product.trad;

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
@ApiModel(description = "用于交易模式商品修改状态--支持批量")
public class TradActivityStatusUo {
	
	@ApiModelProperty(value = "商品ids")
	private String ids;

	@ApiModelProperty(value = "修改状态")
	private int status;
}
