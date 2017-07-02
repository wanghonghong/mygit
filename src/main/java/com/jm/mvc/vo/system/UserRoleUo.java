package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author htp
 */

@Data
@ApiModel(description = "角色修改")
public class UserRoleUo {
	private Integer userId;
	private Integer roleId;
	private Integer userRoleId;
	private Integer type;
}
