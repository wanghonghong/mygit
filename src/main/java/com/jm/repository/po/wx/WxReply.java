package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;


@Data
@Entity
@ApiModel(description = "微信回复表")
public class WxReply {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("图文id，只有类型为商城链接的时候才有，字段为空代表为文本消息，有值代表是图文消息")
	private Integer imgTextId;
	
	@ApiModelProperty("本字段只有图片素材与微信图文素材用到")
	private String  mediaId;
	
	@ApiModelProperty("图片类型需要用到")
	private String picUrl;
	
	@ApiModelProperty("回复的类型 1：被添加关注回复    2：固定回复  3：尾链接")
	private Integer replyType;
	
	//图文消息只有在商城链接里面有
	@ApiModelProperty("回复内容的类型 1：文本 2：表情  3： 商城链接   4：图片 5：音乐 6：语音 7：视频 8：外网链接")
	private Integer replyContentType;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("回复内容，如果是图文消息、图片消息此字段为空")
	private String content;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("只给本地回填用的内容，带html；只有类型为商城链接的时候有用到")
	private String localContent;
	
	//图文消息才有用到本字段
	@ApiModelProperty("1、微信图文   2、项目图文     3、乐享图文  4、微博图文")
	private Integer imgTextType;
	
	@ApiModelProperty("保存时间")
	private Date saveTime;
	

	

}
