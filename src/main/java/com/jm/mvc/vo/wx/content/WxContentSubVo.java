package com.jm.mvc.vo.wx.content;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

@Data
public class WxContentSubVo {
	
	@ApiModelProperty("id")
    private Integer id;

	@ApiModelProperty("图文的父类id")
	private Integer contentId;
	
	@ApiModelProperty("标题")
	private String title;
	
	@ApiModelProperty("描述")
	private String digest;
	
	@ApiModelProperty("内容")
	private String content;
	
	@ApiModelProperty("原始内容，没有替换过图片url的")
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
	
	@ApiModelProperty("图文排序")
	private Integer sort;
	
	@ApiModelProperty("保存时间")
	private Date saveTime;

}
