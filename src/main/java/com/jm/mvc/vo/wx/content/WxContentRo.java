package com.jm.mvc.vo.wx.content;


import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @author chenyy
 *
 */
@Data
public class WxContentRo {
	
	private String filePath;
	
	@ApiModelProperty(value = "接收客户端传的处理图文内图片的临时id")
	private String picId;
	
	@ApiModelProperty("缩略图url")
	private String thumbUrl;
	
	@ApiModelProperty("接收客户端传的处理缩略图的临时id")
	private String thumbId;
	
	List<WxContentRo> thumbUrls;
	
	@ApiModelProperty("缩略图媒体id")
	private String thumbMediaId;
	
	List<WxContentRo> contents ;
	
	@ApiModelProperty(value = "标题")
	private String title;
	
	@ApiModelProperty(value = "作者")
	private String author;
	
	@ApiModelProperty(value = "描述、摘要")
	private String digest;
	
	@ApiModelProperty(value = "群发类型")
	private String sendType;
	
	@ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    public WxContentRo(){
    	this.contents = new ArrayList<>();
    	this.thumbUrls = new ArrayList<>();
    	this.curPage = 0;
    	this.pageSize = 10;
    }

}
