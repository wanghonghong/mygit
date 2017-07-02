package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.template.WxTemplateMsgCo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgVo;
import com.jm.repository.po.wx.WxTemplateMsg;
import com.jm.staticcode.util.JsonMapper;

public class WxTemplateConverter {
	
	
	public static void c2p(WxTemplateMsgCo wxTemplateMsgCo,WxTemplateMsg wxTemplateMsg){
		BeanUtils.copyProperties(wxTemplateMsgCo, wxTemplateMsg);
	}
	
	public static PageItem<WxTemplateMsgVo> p2v(PageItem<Map<String,Object>> pageItemMap) throws Exception{
		PageItem<WxTemplateMsgVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<WxTemplateMsgVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps) {
			WxTemplateMsgVo wxTemplateMsgVo = JsonMapper.map2Obj(map,WxTemplateMsgVo.class);
            list.add(wxTemplateMsgVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
		
	}

}
