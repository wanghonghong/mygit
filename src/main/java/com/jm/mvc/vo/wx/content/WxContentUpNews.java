package com.jm.mvc.vo.wx.content;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WxContentUpNews {
	
	private List<WxContentArticles> articles;
	
	public WxContentUpNews(){
		this.articles = new ArrayList<>();
	}

}
