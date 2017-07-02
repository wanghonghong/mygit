package com.jm.mvc.vo.wx.template;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxTemplateMsgQo {
	
	@ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    public WxTemplateMsgQo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }

}
