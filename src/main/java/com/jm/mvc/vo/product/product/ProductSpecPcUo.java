package com.jm.mvc.vo.product.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *<p>商品详情查询vo</p>
 *
 * @author zww
 * @version latest
 * @data 2016年7月21日
 */
@Data
public class ProductSpecPcUo {
	@ApiModelProperty(value = "规格id")
	private Integer productSpecId;

	@ApiModelProperty(value = "规格标识1")
	private Integer specIdOne;

	@ApiModelProperty(value = "规格标识2")
	private Integer specIdTwo;

	@ApiModelProperty(value = "规格标识3")
	private Integer specIdThree;

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

	@ApiModelProperty(value = "商品规格价格")
	private Integer specPrice;

	@ApiModelProperty(value = "商品规格图片")
	private String specPic;

	@ApiModelProperty(value = "商品数量")
	private Integer totalCount;

	@ApiModelProperty(value = "商品货号")
	private String productCode;



}
