package com.jm.mvc.vo.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户中心查询Qo")
public class UserQo {

	@ApiModelProperty(value = "手机号")
	private String phoneNumber;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	@ApiModelProperty(value = "每页显示条数")
	private Integer pageSize;

	public UserQo(){
		this.curPage = 0;
		this.pageSize = 20;
	}

}
