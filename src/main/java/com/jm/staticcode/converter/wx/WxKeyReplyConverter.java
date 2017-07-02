package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.content.WxKeyReplyCo;
import com.jm.mvc.vo.wx.content.WxKeyReplyUo;
import com.jm.mvc.vo.wx.content.WxKeyReplyVo;
import com.jm.repository.po.wx.WxKeyReply;
import com.jm.staticcode.util.Toolkit;

public class WxKeyReplyConverter {
	
	
	public static WxKeyReply c2p(WxKeyReplyCo wxKeyReplyCo,String appid){
		WxKeyReply reply  = new WxKeyReply();
		BeanUtils.copyProperties(wxKeyReplyCo,reply);
		reply.setAppid(appid);
		return reply;
	}
	
	public static WxKeyReply u2p(WxKeyReplyUo wxKeyReplyUo,String appid){
		WxKeyReply reply  = new WxKeyReply();
		BeanUtils.copyProperties(wxKeyReplyUo,reply);
		reply.setAppid(appid);
		return reply;
	}
	
	
	public static  PageItem<WxKeyReplyVo>ps2vs(Page<WxKeyReply> replyPage){
		PageItem<WxKeyReplyVo> pageItem = new PageItem<>();
		if(replyPage!=null){
			List<WxKeyReply> replys = replyPage.getContent();
			List<WxKeyReplyVo> replyVos = new ArrayList<>();
			for (WxKeyReply wxKeyReply : replys) {
				WxKeyReplyVo replyVo = new WxKeyReplyVo();
				BeanUtils.copyProperties(wxKeyReply,replyVo);
				replyVos.add(replyVo);
			}
			pageItem.setItems(replyVos);
			pageItem.setCount(Toolkit.parseObjForInt(replyPage.getTotalElements()));
		}else{
			pageItem.setItems(new ArrayList<WxKeyReplyVo>());
			pageItem.setCount(0);
		}
		return pageItem;
		
	}
	
	public static WxKeyReplyVo p2v(WxKeyReply reply){
		WxKeyReplyVo replyVo = new WxKeyReplyVo();
		BeanUtils.copyProperties(reply,replyVo);
		return replyVo;
	}

}
