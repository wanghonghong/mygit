package com.jm.mvc.vo.wx.wxmessage;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.jm.mvc.vo.wx.Article;
import com.jm.mvc.vo.wx.PicMsgArticle;
@Data
public class PicNews {
	private List<PicMsgArticle> articles;
	
	public PicNews(){
		this.articles = new ArrayList<>();
	}

}
