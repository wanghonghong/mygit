package com.jm.staticcode.converter.wx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.content.WxContentSentVo;
import com.jm.mvc.vo.wx.content.WxContentSubVo;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.staticcode.util.JsonMapper;

public class WxContentSentConverter {
	
	public static PageItem<WxContentSentVo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException{
			PageItem<WxContentSentVo> pageItem = new PageItem<>();
			List<Map<String,Object>> maps = pageItemMap.getItems();
			List<WxContentSentVo> list = new ArrayList<>();
			for (Map<String,Object> map : maps){
				WxContentSentVo wxContentAsVo = JsonMapper.map2Obj(map,WxContentSentVo.class);
	            list.add(wxContentAsVo);
			}
			pageItem.setCount(pageItemMap.getCount());
			pageItem.setItems(list);
			return pageItem;
	 }
	
	
	public static void addSub(PageItem<WxContentSentVo> pageItems,List<WxContentSub> subs){
		List<WxContentSentVo> ass = new ArrayList<>();
		for (WxContentSentVo wxContentAlreadysentVo : pageItems.getItems()) {
				for (WxContentSub wxContentSub : subs) {
					WxContentSubVo subVo = new WxContentSubVo();
					if(wxContentSub.getContentId().intValue()==wxContentAlreadysentVo.getContentId().intValue()){
						BeanUtils.copyProperties(wxContentSub, subVo);
						wxContentAlreadysentVo.getWxContentSubVos().add(subVo);
					}
				}
			ass.add(wxContentAlreadysentVo);
		}
		pageItems.setItems(ass);
		
	}

}
