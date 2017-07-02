package com.jm.mvc.vo.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *<p>商品详情查询vo</p>
 *
 * @author 吴克府
 * @version latest
 * @data 2016年7月21日
 */
@Data
public class ProductSpecVo {

	@ApiModelProperty(value = "规格名称1")
	private String specNameOne;

	@ApiModelProperty(value = "规格名称2")
	private String specNameTwo;

	@ApiModelProperty(value = "规格名称3")
	private String specNameThree;

	@ApiModelProperty(value = "规格值")
	private String specValueOne;

	@ApiModelProperty(value = "规格值")
	private String specValueTwo;

	@ApiModelProperty(value = "规格值")
	private String specValueThree;

	@ApiModelProperty(value = "商品规格图片")
	private String specPic;

}
