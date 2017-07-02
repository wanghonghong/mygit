package com.jm.mvc.vo.wx.content;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;

import lombok.Data;

/**
 * 已发送消息Vo
 * @author chenyy
 *
 */
@Data
public class WxContentSentVo {
	
	public WxContentSentVo(){
		this.wxContentSubVos = new ArrayList<>();
	}
	
	private Integer id;
	
	@ApiModelProperty("appid")
	private String appid;
	
	@ApiModelProperty("标题")
	private String title;
	
	@ApiModelProperty("素材的id")
	private Integer contentId;
	
	@ApiModelProperty("描述")
	private String digest;
	
	@ApiModelProperty("上传图文消息后微信返回的媒体id")
	private String mediaId;
	
	@ApiModelProperty("处理过文章内图片的内容，微信发送专用")
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
	
	@ApiModelProperty("保存时间")
	private Date saveTime;
	
	@ApiModelProperty("发送类型 image：图片    voice：语音    video：视频      mpnews：图文     text：文本")
	private String sendType;
	
	@ApiModelProperty("发送时间")
	private Date sendTime;
	
	@ApiModelProperty("分组名称")
	private String groupName;
	
	@ApiModelProperty("接收总人数")
	private int count;
	
	@ApiModelProperty("性别")
	private Integer sex;
	
	@ApiModelProperty("角色名称")
	private Integer role;
	
	@ApiModelProperty("地区id")
	private String areaIds;
	
	@ApiModelProperty("地区名称")
	private String areaName;
	
	@ApiModelProperty("发送人")
	private String userName;
	
	@ApiModelProperty("发送状态  0等待发送 、 1发送成功 、2发送失败")
	private int status;
	
	List<WxContentSubVo> wxContentSubVos = new ArrayList<>();

}
