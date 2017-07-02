package com.jm.mvc.vo.wx.level;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxUserLevelUo {
	
	@ApiModelProperty(value = "等级Id")
	private Integer id;
	
	@ApiModelProperty(value = "等级名称")
	private String lenvelName;
	
	private Integer wxUserId;

	@ApiModelProperty(value = "微信用户ID 批量用")
	private String wxUserIds;

}
