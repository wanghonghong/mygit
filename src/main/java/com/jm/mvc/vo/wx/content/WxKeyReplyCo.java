package com.jm.mvc.vo.wx.content;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxKeyReplyCo {
	
	@ApiModelProperty("规则名")
	private String ruleName;
	
	@ApiModelProperty("关键字多个用,隔开")
	private String keyName;
	
	@ApiModelProperty("回复的内容,文本类型用到")
	private String content;
	
	@ApiModelProperty("只给本地回填用的内容，带html；只有类型为商城链接的时候有用到")
	private String localContent;
	
	//图文消息需要多传图文类型与图文id ：imagTextType imgTextId  
	@ApiModelProperty("回复内容的类型 1：文本 2：表情 3： 商城链接   4：图片 5：音乐 6：语音 7：视频 8：外网链接")
	private Integer replyContentType;
	
	@ApiModelProperty("1、微信图文   2、项目图文     3、乐享图文  4、微博图文")
	private Integer imgTextType;
	
	@ApiModelProperty("图文id，只有类型为商城链接的时候才有，字段为空代表为文本消息，有值代表是图文消息")
	private Integer imgTextId;
	
	@ApiModelProperty("图片类型需要用到")
	private String picUrl;
	
	private String mediaId;
	

}
