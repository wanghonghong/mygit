package com.jm.repository.po.wx;

import java.util.Date;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "微信群发消息主表")
public class WxContent {
	
	public WxContent(){
		this.status=1; //默认状态为正常
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("标题")
	private String title;
	
	@ApiModelProperty("描述")
	private String digest;
	
	@ApiModelProperty("上传图文消息后微信返回的媒体id")
	private String mediaId;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("处理过文章内图片的内容，微信发送专用")
	private String content;
	
	@Lob
    @Column(columnDefinition="TEXT", length = 65535)
	@ApiModelProperty("原文内容，未做过图片处理的内容")
	private String jmContent;
	
	@ApiModelProperty("缩略图")
	private String thumbUrl;
	
	@ApiModelProperty("缩略图媒体id")
	private String thumbMediaId;
	
	@ApiModelProperty("作者")
	private String author;
	
	@ApiModelProperty("原文链接")
	private String contentSourceUrl;
	
	@ApiModelProperty("是否显示封面，1为显示，0为不显示")
	private Integer showCoverPic;
	
	@ApiModelProperty("二维码板式类型")
	private Integer qrcodeType;
	
	@ApiModelProperty("打赏板式类型")
	private Integer rewardType;
	
	@ApiModelProperty("保存时间")
	private Date saveTime;
	
	@ApiModelProperty("发送类型 image：图片    voice：语音    video：视频      mpnews：图文     text：文本")
	private String sendType;
	
	@ApiModelProperty("发送人")
	private Integer userId;
	
	@ApiModelProperty("1：正常    0：删除")
	private Integer status;
	

}
