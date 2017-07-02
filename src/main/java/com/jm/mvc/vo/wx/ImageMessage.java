package com.jm.mvc.vo.wx;

import java.util.List;

/**
 * 图片消息
 * @author Administrator
 *
 */
public class ImageMessage  extends BaseMessage {
	
	// 图片链接
	private String PicUrl;
	private WxImage Image = new WxImage();
	
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public WxImage getImage() {
		return Image;
	}
	public void setImage(WxImage image) {
		Image = image;
	}
	


	
	

}
