package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户查询vo")
public class UserQueryVo {
	
	 @ApiModelProperty(value = "手机号码")
	 private String phoneNumber;
	 
	 @ApiModelProperty(value = "用户id")
	 private String ids;
}
