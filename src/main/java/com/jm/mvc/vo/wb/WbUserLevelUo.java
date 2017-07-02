package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WbUserLevelUo {
	
	@ApiModelProperty(value = "等级Id")
	private Integer id;
	
	@ApiModelProperty(value = "等级名称")
	private String lenvelName;
	
	private Long wbUserRelId;

	@ApiModelProperty(value = "微博用户ID 批量用")
	private String wbUserRelIds;

}
