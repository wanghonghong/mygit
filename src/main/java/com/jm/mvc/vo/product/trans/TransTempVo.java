package com.jm.mvc.vo.product.trans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class TransTempVo {

	@ApiModelProperty(value = "模板标识")
	private Integer templatesId;

	@ApiModelProperty(value = "模板名称")
	private String templatesName;

	@ApiModelProperty(value = "商家Id")
	private Integer shopId;

}
