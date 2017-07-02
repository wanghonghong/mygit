package com.jm.mvc.controller.wx;

import com.jm.business.service.wx.*;
import com.jm.mvc.vo.*;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.wx.*;
import com.jm.staticcode.util.ImgUtil;

import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.domain.wx.WxSendDo;
import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.mvc.vo.shop.WxPubAccountUo;
import com.jm.mvc.vo.wx.content.WxContentSentVo;
import com.jm.mvc.vo.wx.content.WxContentCo;
import com.jm.mvc.vo.wx.content.WxContentResult;
import com.jm.mvc.vo.wx.content.WxContentRo;
import com.jm.mvc.vo.wx.content.WxContentSubVo;
import com.jm.mvc.vo.wx.content.WxContentUo;
import com.jm.mvc.vo.wx.content.WxContentVo;
import com.jm.mvc.vo.wx.reply.WxReplyCo;
import com.jm.mvc.vo.wx.reply.WxReplyVo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgCo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgQo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgVo;
import com.jm.repository.client.WxClient;
import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxContentSent;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxReply;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.WxPubAccountConverter;
import com.jm.staticcode.converter.wx.WxContentConverter;
import com.jm.staticcode.converter.wx.WxReplyConverter;

/**
 * <p>
 * 微信群发消息控制器
 * </p>
 * 
 * @author chenyy
 * @version latest
 * @data 2016年7月29日
 */
@Api
@RestController
@RequestMapping(value = "/content")
public class WxContentController {

	@Autowired
	private WxContentService wxContentService;
	@Autowired
	private WxAuthService wxAuthService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private WxPubAccountService wxPubAccountService;
	@Autowired
	private WxClient wxClient;
	@Autowired
	private WxUserQrcodeService wxUserQrcodeService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private WxGroupService wxGroupService;
	@Autowired
	private WxService wxService;

	/**
	 * 上传图文消息内的图片获取URL(图片仅支持jpg/png格式，大小必须在1MB以下。)此方法因为需要传图片路径，因此使用post方式
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/picurl", method = RequestMethod.POST)
	public String getPicUrl(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图片路径") @RequestBody WxContentRo contentRo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		return wxContentService.getPicUrl(accessToken, contentRo.getFilePath());
	}

	/**
	 * 批量url，上传图文消息内的图片获取URL(图片仅支持jpg/png格式，大小必须在1MB以下。)
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchPicurl", method = RequestMethod.POST)
	public WxContentRo batchMediaId(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图片路径列表") @RequestBody WxContentRo contentRo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		WxContentRo bigRo = new WxContentRo();
		// 处理多张文章内图片
		if (contentRo.getContents().size() > 0) {
			for (WxContentRo con : contentRo.getContents()) {
				WxContentRo ro = new WxContentRo();
				ro.setPicId(con.getPicId());
				ro.setFilePath(wxContentService.getPicUrl(accessToken,
						con.getFilePath()));
				bigRo.getContents().add(ro);
			}
		}
		// 处理多张缩略图
		if (contentRo.getThumbUrls().size() > 0) {
			for (WxContentRo con : contentRo.getThumbUrls()) {
				WxContentRo ro = new WxContentRo();
				ro.setThumbId(con.getThumbId());
				ro.setThumbMediaId(wxContentService.getMediaId(accessToken,
						con.getThumbUrl()));
				bigRo.getThumbUrls().add(ro);
			}
		}

		return bigRo;

	}

	/**
	 * 获取媒体ID，群发消息缩略图需要使用(图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mediaid", method = RequestMethod.POST)
	public String getMediaId(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图片路径") @RequestBody WxContentRo contentRo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		return wxContentService
				.getMediaId(accessToken, contentRo.getFilePath());
	}

	/**
	 * 获取图文消息列表
	 * 
	 * @param request
	 * @param contentRo
	 *            查询条件
	 * @return
	 */
	@RequestMapping(value = "/content_list", method = RequestMethod.POST)
	public PageItem<WxContentResult> list(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息查询条件") @RequestBody WxContentRo contentRo) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		return wxContentService.getContentList(contentRo,
				jmUserSession.getAppId());
	}

	/**
	 * 获取已发送列表
	 * 
	 * @param request
	 * @param contentRo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/content_as_list", method = RequestMethod.POST)
	public PageItem<WxContentSentVo> getContentAsList(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息查询条件") @RequestBody WxContentRo contentRo)
			throws IOException {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		return wxContentService.findAsList(contentRo, jmUserSession.getAppId());

	}

	/**
	 * 获取主图详情
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/content_detail/{id}", method = RequestMethod.GET)
	public WxContent wxContentDetail(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文id") @PathVariable("id") Integer id) {
		WxContent wxContent = wxContentService.findById(id);
		return wxContent;

	}

	/**
	 * 去微信图文主图详情页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail_page/{id}", method = RequestMethod.GET)
	public ModelAndView wxContentDetailPage(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文id") @PathVariable("id") Integer id) throws Exception {
		WxContent wxContent = wxContentService.findById(id);
		// 二维码
		WxUserSession wxUserSession = (WxUserSession) request.getSession()
				.getAttribute(Constant.SESSION_WX_USER);
		WxUser wxUser = null;
		if (wxUserSession.getUserId() == 0) {// 游客
			wxUser = wxUserService.findWxUserByUserId(wxUserSession
					.getShareid());
		} else {
			wxUser = wxUserService
					.findWxUserByUserId(wxUserSession.getUserId());
		}
		String qrcode = "";
		if (wxContent != null) {
			if (wxContent.getQrcodeType() != null) {
				qrcode = ImgUtil.appendUrl(
						wxUserQrcodeService.getBaseQrcode(wxUser, 3,
								wxContent.getQrcodeType()), 720);
			}
		}
		// 二维码end

		ModelAndView view = new ModelAndView();
		ModelMap modelMap = view.getModelMap();
		modelMap.addAttribute("wxContent", wxContent);
		modelMap.addAttribute("qrCode", qrcode);
		view.setViewName("/app/wechat/wechatDetail");
		return view;
	}

	/**
	 * 去微信图文子详情页面
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/sub_detail_page/{id}", method = RequestMethod.GET)
	public ModelAndView wxContentSubDetailPage(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文id") @PathVariable("id") Integer id) throws Exception {
		WxContentSub wxContentSub = wxContentService.findWxContetSubById(id);

		// 二维码
		WxUserSession wxUserSession = (WxUserSession) request.getSession()
				.getAttribute(Constant.SESSION_WX_USER);
		WxUser wxUser = null;
		if (wxUserSession.getUserId() == 0) {// 游客
			wxUser = wxUserService.findWxUserByUserId(wxUserSession
					.getShareid());
		} else {
			wxUser = wxUserService
					.findWxUserByUserId(wxUserSession.getUserId());
		}
		String qrcode = "";
		if (wxContentSub != null) {
			if (wxContentSub.getQrcodeType() != null) {
				qrcode = ImgUtil.appendUrl(wxUserQrcodeService.getBaseQrcode(
						wxUser, 3, wxContentSub.getQrcodeType()), 720);
			}
		}
		// 二维码end

		ModelAndView view = new ModelAndView();
		ModelMap modelMap = view.getModelMap();
		modelMap.addAttribute("wxContent", wxContentSub);
		modelMap.addAttribute("qrCode", qrcode);
		view.setViewName("/app/wechat/wechatDetail");
		return view;
	}

	/**
	 * 获取子类详情
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/content_sub_detail/{id}", method = RequestMethod.GET)
	public WxContentSubVo wxContentSubDetail(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("id") @PathVariable("id") Integer id) {
		WxContentSub sub = wxContentService.findWxContetSubById(id);
		WxContentSubVo vo = WxContentConverter.subP2V(sub);
		WxContent wxContent = wxContentService.findById(sub.getContentId());
		vo.setSaveTime(wxContent.getSaveTime());
		return vo;
	}

	/**
	 * 获取图文详情(大图带小图方式一起返回)
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public WxContentResult contentDetail(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文id") @PathVariable("id") Integer id) {

		WxContent wxContent = wxContentService.findById(id);
		List<WxContentSub> subs = wxContentService
				.findeContentSubByContentId(wxContent.getId());
		WxContentResult result = WxContentConverter.p2v(wxContent, subs);
		return result;
	}

	/**
	 * 保存图文消息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public JmMessage saveContent(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息列表") @RequestBody WxContentCo wxContentCo)throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession.getAppId());
		List<WxContent> contents = WxContentConverter.vs2ps(wxContentCo.getWxContentCos(), jmUserSession.getAppId(),jmUserSession.getUserId());
		WxContent returnWxContent = wxContentService.saveWxContentAndSub(contents, accessToken,jmUserSession.getUserId());
		return new JmMessage(0, "ok", returnWxContent.getId());
	}

	/**
	 * 预览图文消息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preview", method = RequestMethod.POST)
	public JmMessage previewMpnews(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息Vo") @RequestBody WxContentVo wxContentVo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		WxContent wxcontent = wxContentService.findById(wxContentVo
				.getContentId());
		return wxContentService.previewMpnews(wxcontent.getMediaId(),
				accessToken, wxContentVo.getTowxname());
	}

	/**
	 * 修改图文消息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage updateContnt(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息列表") @RequestBody WxContentUo wxContentUo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		wxContentService.updateContetnAndSub(wxContentUo, accessToken,
				jmUserSession.getAppId());
		return new JmMessage(0, "ok");
	}

	/**
	 * 群发图文消息
	 * 
	 * @param request
	 * @param wxContentVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendmpnews", method = RequestMethod.POST)
	public JmMessage sendContent(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文消息列表") @RequestBody WxContentVo wxContentVo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		WxContent wxContent = wxContentService.findById(wxContentVo
				.getContentId());
		WxContentSent wxContentSent =wxContentService.saveAlreadySent(wxContent, wxContentVo);// 新增发送消息(定时发送或者直接发送)
		if (wxContentVo.getIsTiming() == 0) {// 选择即时发送
			WxSendDo wxSendDo = new WxSendDo();
			wxSendDo.setContent(wxContent.getMediaId());
			wxSendDo.setGroupid(wxContentVo.getGroupid());
			wxSendDo.setMsgtype(wxContent.getSendType());
			wxSendDo.setType(wxContentVo.getType());
			wxSendDo.setOpenids(wxContentVo.getOpenids());
			wxContentService.sendContent(wxSendDo, accessToken,
					wxContentSent.getId());
		}
		return new JmMessage(0, "ok");
	}

	/**
	 * 删除图文消息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteMpnews(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("图文id") @PathVariable("id") Integer id) throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		WxContent wxContent = wxContentService.findById(id);
		if (wxContent == null
				|| !wxContent.getAppid().equals(jmUserSession.getAppId())) {
			return new JmMessage(-1, "信息不存在或无权限");
		}
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		wxContentService.deleteContent(id, accessToken);
		return new JmMessage(0, "ok");

	}

	/**
	 * 删除已发送图文消息
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete_as/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteAs(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("已发送图文id") @PathVariable("id") Integer id) {
		WxContentSent as = wxContentService.findAsById(id);
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		if (null == as || !as.getAppid().equals(jmUserSession.getAppId())) {
			return new JmMessage(-1, "信息不存在或无权限");
		}
		wxContentService.deleteAsById(id);
		return new JmMessage(0, "ok");
	}

	/**
	 * 筛选群发对象
	 * 
	 * @param request
	 * @param wxContentVo
	 * @return
	 */
	@RequestMapping(value = "/openids", method = RequestMethod.POST)
	public WxContentVo getOpenidS(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("筛选条件") @RequestBody WxContentVo wxContentVo) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		return wxContentService.handleSentType(wxContentVo,
				jmUserSession.getAppId());
	}

	/**
	 * 筛选群发精推消息对象
	 * 
	 * @param request
	 * @param wxContentVo
	 * @return
	 */
	@RequestMapping(value = "/openids_template", method = RequestMethod.POST)
	public WxContentVo getOpenidsToTemplate(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("筛选条件") @RequestBody WxContentVo wxContentVo) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		return wxContentService.findSendTemplateOpenids(wxContentVo,
				jmUserSession.getAppId());
	}

	/**
	 * 发送其他群发消息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public JmMessage sendOther(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("群发消息内容") @RequestBody WxContentVo wxContentVo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		WxContent wxContent = WxContentConverter.c2p(wxContentVo
				.getWxContentCo());
		String content = "";
		// 区分是哪种类型消息
		if ("image".equals(wxContent.getSendType())) {
			wxContent.setThumbMediaId(wxContentService.getMediaId(accessToken,
					wxContentVo.getWxContentCo().getThumbUrl()));
			content = wxContent.getThumbMediaId();
		} else if ("text".equals(wxContent.getSendType())) {
			content = wxContent.getContent();
		}
		wxContent.setSaveTime(new Date());
		wxContent.setAppid(jmUserSession.getAppId());
		WxContent returnWxContent = wxContentService.saveWxContent(wxContent);
		WxContentSent wxContentSent = wxContentService.saveAlreadySent(returnWxContent, wxContentVo);// 新增发送消息(定时发送或者直接发送)
		if (wxContentVo.getIsTiming() == 0) {// 选择即时发送
			WxSendDo wxSendDo = new WxSendDo();
			wxSendDo.setMsgtype(returnWxContent.getSendType());
			wxSendDo.setContent(content);
			wxSendDo.setOpenids(wxContentVo.getOpenids());
			wxSendDo.setGroupid(wxContentVo.getGroupid());
			wxSendDo.setType(wxContentVo.getType());
			wxContentService.sendContent(wxSendDo, accessToken,wxContentSent.getId());
		}
		return new JmMessage(0, "ok");
	}

	/**
	 * 新增回复
	 * 
	 * @param request
	 * @param wxReplyCo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.PUT)
	public JmMessage createReply(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("回复内容") @RequestBody WxReplyCo wxReplyCo)
			throws Exception {

		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		WxReply reply = WxReplyConverter.c2p(wxReplyCo);
		reply.setAppid(jmUserSession.getAppId());
		String accessToken = wxAuthService.getAuthAccessToken(jmUserSession
				.getAppId());
		WxReply returnReply = wxContentService.saveReply(reply, wxReplyCo, accessToken,jmUserSession.getAppId());
		return new JmMessage(0, "ok",returnReply.getId());
	}

	/**
	 * 修改公众号主表设置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/wxaccount", method = RequestMethod.POST)
	public JmMessage updateAccount(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("微信公众号数据") @RequestBody WxPubAccountUo wxPubAccountUo) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		WxPubAccount account = wxPubAccountService
				.findWxPubAccountByAppid(jmUserSession.getAppId());
		if (null != wxPubAccountUo.getIsFixedReply()) {
			// 修改开启固定回复
			account.setIsFixedReply(wxPubAccountUo.getIsFixedReply());
		}
		if (null != wxPubAccountUo.getIsLastLink()) {
			// 修改开启尾连接
			account.setIsLastLink(wxPubAccountUo.getIsLastLink());
		}
		wxPubAccountService.save(account);
		return new JmMessage(0, "ok");

	}

	/**
	 * 获取回复的内容
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/reply_list", method = RequestMethod.GET)
	public List<WxReplyVo> getReplyList(
			@ApiParam(hidden = true) HttpServletRequest request) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		List<WxReply> list = wxContentService.findWxReplyByAppid(jmUserSession
				.getAppId());
		List<WxReplyVo> wxReplys = WxReplyConverter.ps2vs(list);
		return wxReplys;
	}

	/**
	 * 获取公众号基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public WxPubAccountVo getAccount(
			@ApiParam(hidden = true) HttpServletRequest request) {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		WxPubAccount account = wxPubAccountService
				.findWxPubAccountByAppid(jmUserSession.getAppId());
		return WxPubAccountConverter.p2v(account);

	}

	/**
	 * 获取基础二维码
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/pub_qrcode", method = RequestMethod.GET)
	public JmMessage pubQrcode(
			@ApiParam(hidden = true) HttpServletRequest request)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		JmMessage msg = new JmMessage(0, "ok");
		msg.setData(jmUserSession.getPubQrcodeUrl());
		return msg;
	}

	/**
	 * 精推模板消息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/templatemsg", method = RequestMethod.POST)
	public JmMessage createTemplateMsg(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("模板消息") @RequestBody WxTemplateMsgCo wxTemplateMsgCo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession()
				.getAttribute(Constant.SESSION_USER);
		JmMessage ss = null;
		// 保存
		WxTemplateMsg msg = wxMessageService.saveWxTemplateMsg(wxTemplateMsgCo,
				jmUserSession.getUserId(), jmUserSession.getAppId());
		wxContentService.saveAlreadySent(msg, wxTemplateMsgCo);// 新增发送消息(定时发送或者直接发送)
		if (wxTemplateMsgCo.getIsTiming() == 0) {// 为即时发送
			//现精推消息也改为发送图文消息
			List<PicMsgArticle> articles = new ArrayList<>();
			PicMsgArticle article = new PicMsgArticle();
			article.setTitle(wxTemplateMsgCo.getFirst());
			article.setDescription(wxTemplateMsgCo.getRemark());
			article.setPicurl(wxTemplateMsgCo.getPicUrl());
			article.setUrl(wxTemplateMsgCo.getUrl());
			articles.add(article);
			JmMessage jmMessage = wxMessageService.accurateSendImgText(wxTemplateMsgCo.getOpenids(),msg.getId(),articles,jmUserSession.getAppId());
			return jmMessage;
		} else {
			ss = new JmMessage(0, "ok");
		}
		return ss;
	}

	/**
	 * 获取已发送的精推消息
	 * 
	 * @param request
	 * @param wxTemplateMsgQo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/template_list", method = RequestMethod.POST)
	public PageItem<WxTemplateMsgVo> getTempList(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("查询条件") @RequestBody WxTemplateMsgQo wxTemplateMsgQo)
			throws Exception {
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return wxMessageService.findAsTemplate(wxTemplateMsgQo,jmUserSession.getAppId());
	}

	
	/*@RequestMapping(value = "/test_update_user", method = RequestMethod.GET)
	public void testTemplate(@ApiParam(hidden = true) HttpServletRequest request) throws Exception{
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		wxAuthService.updateUserInfo();
		//ResultMsg sss = wxService.defaultMenu(jmUserSession.getAppId(), jmUserSession.getShopId());
	}*/


}
