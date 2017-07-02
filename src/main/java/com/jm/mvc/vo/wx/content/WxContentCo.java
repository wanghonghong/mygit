package com.jm.mvc.vo.wx.content;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data	
public class WxContentCo {
	
	public WxContentCo(){
		this.wxContentCos = new ArrayList<>();
	}
	
		@ApiModelProperty("标题")
		private String title;
		
		@ApiModelProperty("描述")
		private String digest;
		
		@ApiModelProperty("内容")
		private String content;
		
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
		
		@ApiModelProperty("发送类型 image：图片    voice：语音    video：视频      mpnews：图文     text：文本")
		private String sendType;

		@ApiModelProperty("图文排序")
		private Integer sort;
		
		@ApiModelProperty("保存时间")
		private Date saveTime;
		
		
		private List<WxContentCo> wxContentCos;
	}