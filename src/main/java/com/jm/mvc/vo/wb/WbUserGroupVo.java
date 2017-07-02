package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WbUserGroupVo {
	/**
	 * 微博上的分组id
	 */
	private String name;

	private Long groupid;

	@ApiModelProperty(value = "用户关系表编号")
	private Long relId;

	private String  remark;

}
