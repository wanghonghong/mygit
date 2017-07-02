package com.jm.mvc.vo.product.trans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/9/2 14:29
 */
@Data
public class TransTemplateVo {

	@ApiModelProperty(value = "模板标识")
	private Integer templatesId;

	@ApiModelProperty(value = "模板名称")
	private String templatesName;


}
