package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.wx.reply.WxReplyCo;
import com.jm.mvc.vo.wx.reply.WxReplyVo;
import com.jm.repository.po.wx.WxReply;

/**
 * 微信回复转换器
 * @author chenyy
 *
 */
public class WxReplyConverter {
	
	public static WxReply c2p(WxReplyCo wxReplyCo){
		WxReply wxReply = new WxReply();
		BeanUtils.copyProperties(wxReplyCo,wxReply);
		return wxReply;
	}
	
	public static List<WxReplyVo> ps2vs(List<WxReply> replys){
		List<WxReplyVo>  vos = new ArrayList<>();
		for (WxReply wxReply : replys) {
			WxReplyVo vo = new WxReplyVo();
			BeanUtils.copyProperties(wxReply,vo);
			vos.add(vo);
		}
		return vos;
	}

}
