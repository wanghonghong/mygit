package com.jm.mvc.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "图片返回类")
public class ImgResult {

	private boolean success = false;
	private String resp_code = "1";
	private String resp_desc;
	
	
}
