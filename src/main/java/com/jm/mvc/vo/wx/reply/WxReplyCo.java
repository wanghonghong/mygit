package com.jm.mvc.vo.wx.reply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxReplyCo {
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("图片内容回复媒体id，如果是文本消息与图文消息，本字段为空")
	private String mediaId;
	
	@ApiModelProperty("图片类型需要用到")
	private String picUrl;
	
	@ApiModelProperty("回复的类型 1：被添加关注回复    2：固定回复  3:尾链接 ")
	private Integer replyType;
	//图文消息只有在商城链接里面有
	@ApiModelProperty("回复内容的类型 1：文本   2：表情    3： 商城链接   4：图片  5：音乐 6：语音 7：视频 8：外网链接")
	private Integer replyContentType;
	
	@ApiModelProperty("回复内容，如果是图文消息或图片消息，此字段为空")
	private String content;
	
	@ApiModelProperty("只给本地回填用的内容，带html；只有类型为商城链接的时候有用到")
	private String localContent;
	
	@ApiModelProperty("1、微信图文   2、项目图文     3、乐享图文  4、微博图文")
	private Integer imgTextType;
	
	@ApiModelProperty("图文id，只有类型为商城链接的时候才有，字段为空代表为文本消息，有值代表是图文消息")
	private Integer imgTextId;
	

}
