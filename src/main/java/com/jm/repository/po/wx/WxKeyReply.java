package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity
public class WxKeyReply {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty(value = "appid")
	private String appid;
	
	@ApiModelProperty("规则名称")
	private String ruleName;
	
	@ApiModelProperty(value = "关键字，多个用,隔开")
	private String keyName;
	
	//图文消息需要多传图文类型与图文id ：imagTextType imgTextId  
	@ApiModelProperty("回复内容的类型 1：文本 2：表情 3： 商城链接   4：图片 5：音乐 6：语音 7：视频 8：外网链接")
	private Integer replyContentType;
	
	@ApiModelProperty("1、微信图文   2、项目图文     3、乐享图文  4、微博图文")
	private Integer imgTextType;
	
	@ApiModelProperty("图文id，只有类型为商城链接的时候才有，字段为空代表为文本消息，有值代表是图文消息")
	private Integer imgTextId;
	
	@ApiModelProperty("图片类型需要用到")
	private String picUrl;
	
	@ApiModelProperty("本字段只有图片类型与微信图文有用到")
	private String  mediaId;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("回复的内容")
	private String content;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("只给本地回填用的内容，带html；只有类型为商城链接的时候有用到")
	private String localContent;
	


}
