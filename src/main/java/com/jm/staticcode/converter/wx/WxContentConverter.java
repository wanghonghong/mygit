package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.content.WxContentArticles;
import com.jm.mvc.vo.wx.content.WxContentCo;
import com.jm.mvc.vo.wx.content.WxContentResult;
import com.jm.mvc.vo.wx.content.WxContentSubVo;
import com.jm.mvc.vo.wx.content.WxContentUo;
import com.jm.mvc.vo.wx.content.WxContentUpNews;
import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.staticcode.util.Toolkit;

/**
 * 微信群发转换器
 * @author chenyy
 *
 */
public class WxContentConverter {
	
	/**
	 * WxContentVo转为上传图文的对象
	 * @param wxContents
	 * @return
	 * @throws Exception 
	 */
	public static WxContentUpNews WxContentsToWxContentUpNews(WxContent wxContent,List<WxContentSub> wxContentSubs) throws Exception{
		WxContentUpNews news = new WxContentUpNews();
		WxContentArticles articles = new WxContentArticles();
		BeanUtils.copyProperties(wxContent,articles);
		news.getArticles().add(articles);
		if(wxContentSubs.size()>0){
			for (WxContentSub wxContentSub : wxContentSubs) {
				WxContentArticles articles2 = new WxContentArticles();
				BeanUtils.copyProperties(wxContentSub,articles2);
				news.getArticles().add(articles2);
			}
		}
		return news;
	}
	/**
	 * Vo集合转Po集合
	 * @param wxContentVo
	 * @return
	 */
	public static List<WxContent> vs2ps(List<WxContentCo> wxContentCos,String appid,Integer userId){
		List<WxContent> wxContentS = new ArrayList<>();
		for (WxContentCo wxContentCo : wxContentCos) {
			WxContent wxContent = new WxContent();
			BeanUtils.copyProperties(wxContentCo,wxContent);
			wxContent.setAppid(appid);
			wxContent.setUserId(userId);
			wxContentS.add(wxContent);
		}
		return wxContentS;
	}
	
	/**
	 * 内容主转为子
	 * @param contents
	 * @param contentId
	 * @return
	 */
	public static List<WxContentSub> content2sub(List<WxContent>contents,Integer contentId){
		List<WxContentSub> contentSubs = new ArrayList<>();
		for (WxContent wxContent : contents) {
			WxContentSub wxContentSub = new WxContentSub();
			BeanUtils.copyProperties(wxContent,wxContentSub);
			wxContentSub.setContentId(contentId);
			contentSubs.add(wxContentSub);
		}
		return contentSubs;
	}
	
	
	public static WxContent c2p(WxContentCo wxContentCo){
		WxContent wxContent = new WxContent();
		BeanUtils.copyProperties(wxContentCo,wxContent);
		return wxContent;
	}
	
	
	public static WxContent u2p(WxContentUo wxContentUo,String appid){
		WxContent wxContent = new WxContent();
		BeanUtils.copyProperties(wxContentUo,wxContent);
		wxContent.setAppid(appid);
		return wxContent;
	}
	
	
	public static  List<WxContentSub> us2subPs(List<WxContentUo> wxContentUos,Integer contentId){
		List<WxContentSub> subs = new ArrayList<>();
		for (WxContentUo wxContentUo : wxContentUos) {
			WxContentSub sub = new WxContentSub();
			BeanUtils.copyProperties(wxContentUo,sub);
			sub.setContentId(contentId);
			subs.add(sub);
		}
		return subs;
	}
	
	
	public static PageItem<WxContentResult> toWxcontetRestlt(Page<WxContent> wxContentPage){
		PageItem<WxContentResult> pageItem = new PageItem<WxContentResult>();
		if(wxContentPage!=null){
    		List<WxContentResult> results = new ArrayList<>();
    		List<WxContent> wxContents = wxContentPage.getContent();
    		for (WxContent wxContent : wxContents) {
    			WxContentResult result = new WxContentResult();
    			BeanUtils.copyProperties(wxContent, result);
    			results.add(result);
			}
    		pageItem.setItems(results);
    		pageItem.setCount(Toolkit.parseObjForInt(wxContentPage.getTotalElements()));
		}
		return pageItem;
	}
	/**
	 * 组装成1VN的格式返回
	 * @param results
	 * @param subs
	 * @return
	 */
	public static List<WxContentResult> toWxcontennResultSub(List<WxContentResult> results,List<WxContentSub> subs){
		List<WxContentResult> tmpResults = new ArrayList<>();
		for (WxContentResult wxContentResult : results) {
			if(subs.size()>0){
				for (WxContentSub wxContentSub : subs) {
					WxContentSubVo subVo = new WxContentSubVo();
					if(wxContentSub.getContentId().intValue()==wxContentResult.getId().intValue()){
						BeanUtils.copyProperties(wxContentSub, subVo);
						wxContentResult.getWxContentSubVos().add(subVo);
					}
				}
			}
			tmpResults.add(wxContentResult);
		}
		return tmpResults;
	}
	
	
	public static WxContentResult p2v(WxContent wxContent,List<WxContentSub>subs){
		WxContentResult result = new WxContentResult();
		BeanUtils.copyProperties(wxContent, result);
		if(subs.size()>0){
			for (WxContentSub wxContentSub : subs) {
				WxContentSubVo subVo = new WxContentSubVo();
				BeanUtils.copyProperties(wxContentSub, subVo);
				result.getWxContentSubVos().add(subVo);
			}
		}
		return result;
	}
	
	public static WxContentSubVo subP2V(WxContentSub wxContentSub){
		WxContentSubVo contentSubVo = new WxContentSubVo();
		BeanUtils.copyProperties(wxContentSub, contentSubVo);
		return contentSubVo;
	}
	
	
}
