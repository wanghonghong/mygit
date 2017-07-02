package com.jm.mvc.vo.wx.content;

import lombok.Data;

@Data
public class WxContentArticles {
	
		/**
		 * 缩略图媒体id
		 */
		private String thumbMediaId;
		/**
		 * 作者
		 */
		private String author;
		/**
		 * 标题
		 */
		private String title;
		/**
		 * 原文链接
		 */
		private String contentSourceUrl;
		/**
		 * 内容
		 */
		private String content;
		/**
		 * 描述
		 */
		private String digest;
		/**
		 * 是否显示封面，1为显示，0为不显示
		 */
		private Integer showCoverPic;

}
