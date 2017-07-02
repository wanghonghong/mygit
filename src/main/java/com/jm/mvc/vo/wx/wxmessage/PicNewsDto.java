package com.jm.mvc.vo.wx.wxmessage;

import lombok.Data;

/**
 * 客服接口图文消息对象
 * @author chenyy
 *
 */
@Data
public class PicNewsDto {
	private String touser;
	private String msgtype;
	private PicNews news;
	
	public PicNewsDto(){
		this.news = new PicNews();
	}
}
