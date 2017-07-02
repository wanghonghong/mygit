package com.jm.mvc.vo.product.trans;

import com.jm.repository.po.product.TransTemplatesRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 14:51
 */
@Data
public class TransTemplatesRo {

	@ApiModelProperty(value = "模板标识")
	private Integer templatesId;

	@ApiModelProperty(value = "模板名称")
	private String templatesName;
	
	@ApiModelProperty(value = "分类关系表")
	private List<TransTemplatesRelationRo> transRelationList;

}
