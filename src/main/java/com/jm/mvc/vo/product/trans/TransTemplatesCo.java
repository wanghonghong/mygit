package com.jm.mvc.vo.product.trans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

import com.jm.repository.po.product.TransTemplates;
import com.jm.repository.po.product.TransTemplatesRelation;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>新增运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 14:49
 */
@Data
public class TransTemplatesCo {

	@ApiModelProperty(value = "模板名称")
	private String templatesName;

	@ApiModelProperty(value = "分类关系表")
	private List<TransTemplatesRelationCo> transRelationList;
	
}
