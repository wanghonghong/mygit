package com.jm.mvc.controller.wx;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jm.business.service.wx.WxKeyReplyService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.content.WxKeyReplyCo;
import com.jm.mvc.vo.wx.content.WxKeyReplyQo;
import com.jm.mvc.vo.wx.content.WxKeyReplyUo;
import com.jm.mvc.vo.wx.content.WxKeyReplyVo;
import com.jm.staticcode.constant.Constant;

/**
 * 关键字回复控制层
 * 
 * @author chenyy
 * 
 */
@Api
@RestController
@RequestMapping(value = "/keyreply")
public class WxKeyReplyController {

	@Autowired
	private WxKeyReplyService wxKeyReplyService;

	/**
	 * 新增关键字
	 * 
	 * @param request
	 * @throws Exception 
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public JmMessage addReplyKey(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("关键字回复Co") @RequestBody WxKeyReplyCo wxKeyReplyCo) throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		wxKeyReplyService.saveKeyAndRule(wxKeyReplyCo, jmUserSession.getAppId());
		return new JmMessage(0, "ok");
	}
	
	/**
	 * 修改
	 * @param wxKeyReplyUo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage updateKeyReply(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("关键字回复Uo") @RequestBody WxKeyReplyUo wxKeyReplyUo) throws Exception{
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		wxKeyReplyService.updateWxKeyReply(wxKeyReplyUo, jmUserSession.getAppId());
		return new JmMessage(0, "ok");
	}
	
	/**
	 * 删除关键字回复
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteKeyReply(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("分组id") @PathVariable("id") Integer id) throws Exception{
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		wxKeyReplyService.deleteKeyReply(id, jmUserSession.getAppId());
		return new JmMessage(0, "ok");
	}
	
	/**
	 * 获取列表
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageItem<WxKeyReplyVo>  list(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("条件Qo") @RequestBody WxKeyReplyQo wxKeyReplyQo){
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return wxKeyReplyService.findAllByAppid(wxKeyReplyQo, jmUserSession.getAppId());
	} 
	
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public WxKeyReplyVo getOne(@ApiParam("id") @PathVariable("id") Integer id){
		return wxKeyReplyService.findById(id);
	}

}
