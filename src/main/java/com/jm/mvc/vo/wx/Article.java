package com.jm.mvc.vo.wx;

import lombok.Data;

@Data
public class Article {
	// 图文消息名称  
    private String Title;
    // 图文消息描述  
    private String Description;  
    // 点击图文消息跳转链接  
    private String Url;  
    // 图片链接，支持JPG、PNG格式
    private String PicUrl;  
   
}
