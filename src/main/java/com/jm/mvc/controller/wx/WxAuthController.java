package com.jm.mvc.controller.wx;

import com.jm.business.domain.shop.SubscribePushDo;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.wx.WxService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.WxPubAccountRo;
import com.jm.mvc.vo.shop.WxPubAccountUo;
import com.jm.mvc.vo.wx.*;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.staticcode.util.wx.WxUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.transform.dom.DOMSource;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.service.wx.WxAuthService;
import com.jm.staticcode.constant.Constant;


/**
 * <p>微信第三方</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Log4j
@RestController
public class WxAuthController {
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxService wxService;
	@Autowired
	private WxPubAccountService wxPubAccountService;

	@ApiOperation("获取拉取粉丝数")
	@RequestMapping(value = "/pushs", method = RequestMethod.GET)
	public Vector<SubscribePushDo> get() throws Exception{
		return Constant.SUBSCRIBE_PUSH_LIST;
	}

	@ApiOperation("获取拉取粉丝数")
	@RequestMapping(value = "/pushsize", method = RequestMethod.GET)
	public int getPushSize() throws Exception{
		return Constant.SUBSCRIBE_PUSH_LIST.size();
	}

	/**
	 * 微信推送授权信息
	 * @param request
     * @return
     */
	@RequestMapping(value="/wx/az",method = RequestMethod.POST)
	public String pushAuthInfo(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody(required=false) DOMSource domSource){
		try {
			WxAcceptVo wxAcceptVo = WxUtil.toWxAcceptVo(request,domSource);
			Map<String,String> resultMap = wxAcceptVo.getMap();
			String infoType = resultMap.get("InfoType");
			if(("component_verify_ticket").equals(infoType)){ //推送component_verify_ticket
				String componentVerifyTicket = resultMap.get("ComponentVerifyTicket");
				wxAuthService.setComponentVerifyTicket(componentVerifyTicket);
			}else if(("authorized").equals(infoType)){//表示授权成功
				//wxAuthService.saveAuthInfo(resultMap.get("AuthorizationCode"),null,null); //保存公众号信息，保存公众号token
			}else if(("updateauthorized").equals(infoType)){//授权更新通知

			}else if(("unauthorized").equals(infoType)){//取消授权通知
				String authorizerAppid = resultMap.get("AuthorizerAppid");//公众号appid
				wxAuthService.cancelAuth(authorizerAppid);//修改授权状态，删除店铺授权
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return "success";
	}

	/**
	 *<p>事件请求</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月19日
	 */
	@RequestMapping(value="/wx",method = RequestMethod.POST)
	public String weixinForPost(HttpServletRequest request, HttpServletResponse response,
								@RequestBody(required=false) DOMSource domSource) throws Exception{
		return wxService.processRequest(request,response,domSource);
	}


	/**
	 * （商城搭建）微信授权页面
	 * @return
	 */
	@RequestMapping(value="/wxauth/page",method = RequestMethod.GET)
	public ModelAndView wxauthpage(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		ModelAndView view = new ModelAndView();
		String authPageUrl= wxAuthService.getAuthPageUrl();
    	view.setViewName("/pc/shop/authpage");
		request.setAttribute("authPageUrl", authPageUrl+"?shop_id="+user.getShopId());
		return view;
	}

	@RequestMapping(value="/auth/page",method = RequestMethod.GET)
	public String getWxAuthpage() throws Exception {
		return wxAuthService.getAuthPageUrl();
	}
	
	/**
	 * 获取聚客红包授权url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/auth/page_jk",method = RequestMethod.GET)
	public ModelAndView getWxAUthPageToJk() throws Exception{
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:"+wxAuthService.getAuthPageUrlToJk());
		return view;
	}

	@RequestMapping(value="/auth/page1",method = RequestMethod.GET)
	public ModelAndView getWxAuthpage(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		ModelAndView view = new ModelAndView();
		String shopId = request.getParameter("shopId");
		if(shopId !=null && !shopId.equals("")){
			view.setViewName("redirect:"+wxAuthService.getAuthPageUrl()+"?shop_id="+shopId);
			return view;
		}
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		view.setViewName("redirect:"+wxAuthService.getAuthPageUrl()+"?shop_id="+user.getShopId());
		return view;
	}

	/**
	 * 微信服务器推送授权码
	 * @param authCode
	 * @throws IOException
	 */
	@RequestMapping(value="/authorization_code",method = RequestMethod.GET)
	public ModelAndView pushAuthCode(@ApiParam(hidden=true) HttpServletRequest request,
									@RequestParam("auth_code") String authCode,
									 @RequestParam("shop_id") int shopid) throws Exception{
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		int isok=wxAuthService.saveAuthInfo(authCode,shopid,jmUserSession.getUserId());
		if(isok==0){
			return new ModelAndView("redirect:/success");
		}
		if(isok==1){
			return new ModelAndView("redirect:/err");
		}
		if(isok==2){
			return new ModelAndView("redirect:/exist");
		}
		if(isok==3){
			return new ModelAndView("redirect:/noAuth");
		}
		return new ModelAndView("redirect:/err");
	}
	
	
	/**
	 * 微信服务器推送授权码(授权成功回调---聚客红包)
	 * @param authCode
	 * @throws Exception 
	 * @throws IOException
	 */
	@RequestMapping(value="/authorization_code_jk",method = RequestMethod.GET)
	public ModelAndView pushAuthCodeToJk(@ApiParam(hidden=true)HttpServletRequest request,
			@RequestParam("auth_code") String authCode) throws Exception{
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		int isok = wxAuthService.saveJkAuthInfo(authCode,user.getUserId());
		if(isok==1){
			return new ModelAndView("redirect:/success");
		}else if(isok==0){
			return new ModelAndView("redirect:/existToJk");
		}else if(isok==3){
			return new ModelAndView("redirect:/noAuth");
		}
		return new ModelAndView("redirect:/err");
	}
	

	@RequestMapping(value="/err",method = RequestMethod.GET)
	public ModelAndView err(){
		return new ModelAndView("/pc/shop/err");
	}

	@RequestMapping(value="/success",method = RequestMethod.GET)
	public ModelAndView success(){
		return new ModelAndView("/pc/shop/success");
	}

	@RequestMapping(value="/exist",method = RequestMethod.GET)
	public ModelAndView exist(){
		return new ModelAndView("/pc/shop/exist");
	}

	@RequestMapping(value="/noAuth",method = RequestMethod.GET)
	public ModelAndView noAuth(){
		return new ModelAndView("/pc/shop/noAuth");
	}
	
	@RequestMapping(value="/existToJk",method = RequestMethod.GET)
	public ModelAndView existToJk(){
		return new ModelAndView("/pc/shop/existToJk");
	}


	/**
	 * 付款方式
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wxauth/pay",method = RequestMethod.POST)
	public JmMessage paySet(@ApiParam(hidden=true) HttpServletRequest request,
							@RequestBody @Valid WxPubAccountUo uo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WxPubAccount wxPubAccount = wxPubAccountService.updateWxPubAccount(uo,user.getAppId());
		if (wxPubAccount != null ){
			return new JmMessage(0,"保存成功");
		}
		return new JmMessage(1,"保存失败");
	}

	/**
	 * 返回商户号、秘钥
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wxauth/pay",method = RequestMethod.GET)
	public WxPubAccountRo paySet(@ApiParam(hidden=true) HttpServletRequest request) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WxPubAccount wxPubAccount = wxPubAccountService.findWxPubAccountByAppid(user.getAppId());
		WxPubAccountRo ro = new WxPubAccountRo();
		ro.setMchId(wxPubAccount.getMchId());
		ro.setIssub(wxPubAccount.getIsSub());
		return ro;
	}


}
