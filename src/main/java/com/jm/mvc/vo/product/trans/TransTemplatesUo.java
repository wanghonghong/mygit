package com.jm.mvc.vo.product.trans;

import com.jm.repository.po.product.TransTemplates;
import com.jm.repository.po.product.TransTemplatesRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>修改运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 14:51
 */
@Data
public class TransTemplatesUo {

	@ApiModelProperty(value = "模板标识")
	private Integer templatesId;

	@ApiModelProperty(value = "模板名称")
	private String templatesName;
	
	@ApiModelProperty(value = "分类关系表")
	private List<TransTemplatesRelationUo> transRelationList;

}
