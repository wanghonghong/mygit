package com.jm.mvc.vo.wx;

import lombok.Data;

/**
 * 客服接口发送图文消息专用，被动回复图文消息不可用，因api要求的字段大小写不一样
 * @author chenyy
 *
 */
@Data
public class PicMsgArticle {
	// 图文消息名称  
    private String title;  
    // 图文消息描述  
    private String description;  
    // 点击图文消息跳转链接  
    private String url;  
    // 图片链接，支持JPG、PNG格式
    private String picurl;  

}
