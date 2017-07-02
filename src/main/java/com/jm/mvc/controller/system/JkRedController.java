package com.jm.mvc.controller.system;

import com.jm.business.service.wx.*;
import com.jm.staticcode.converter.shop.WxPubAccountConverter;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.domain.wx.WxRedDo;
import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.system.UserAccountService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.UserAccountVo;
import com.jm.mvc.vo.WxPubAccountVo;
import com.jm.mvc.vo.qo.JmRechargeOrderQo;
import com.jm.mvc.vo.shop.activity.ActivityUserQo;
import com.jm.mvc.vo.shop.activity.ActivityUserVo;
import com.jm.mvc.vo.system.JmRechargeOrderCo;
import com.jm.mvc.vo.system.JmRechargeOrderVo;
import com.jm.mvc.vo.wx.wxred.RedResultParam;
import com.jm.repository.client.dto.auth.AccessToken;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.repository.po.shop.activity.ActivityUser;
import com.jm.repository.po.system.JmRechargeOrder;
import com.jm.repository.po.system.user.UserAccount;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.system.UserAccountConverter;

/**
 * 聚客红包控制层
 * @author chenyy
 * @date 2016/12/3
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/jk")
public class JkRedController {
	
	@Autowired
	private WxPubAccountService wxPubAccountService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActivityService activityService;

	
	
	@ApiOperation("获取公众号列表")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<WxPubAccountVo> getWxPubAccount(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession jmUserSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<WxPubAccountVo> s = wxPubAccountService.findByUserId(jmUserSession.getUserId());
		return s;
	}
	
	
	@ApiOperation("获取用户账户")
	@RequestMapping(value = "/user_account", method = RequestMethod.GET)
	public UserAccountVo getUserAccount(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		UserAccount account = userAccountService.findByUserId(user.getUserId(), 0);
		return UserAccountConverter.p2v(account);
	}
	
	
	@ApiOperation("获取用户充值记录")
	@RequestMapping(value = "/recharge_list", method = RequestMethod.POST)
	public PageItem<JmRechargeOrderVo> getRechargeList(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("查询条件") @RequestBody JmRechargeOrderQo orderQo) throws Exception{
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		orderQo.setUserId(user.getUserId());
		orderQo.setType(0);
		PageItem<JmRechargeOrderVo> items = userAccountService.getJmRechargeList(orderQo);
		return items;
	}
	
	@ApiOperation("将公众号信息存入session")
	@RequestMapping(value = "/account_session", method = RequestMethod.POST)
	public JmMessage addSession(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("appid")  @RequestBody WxPubAccountVo wxPubAccountVo){
		 JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		 user.setAppId(wxPubAccountVo.getAppid());
		 user.setShopId(null);
		 request.getSession().setAttribute(Constant.SESSION_USER,user);
		 return new JmMessage(0, "ok");
	}
	
	
	@ApiOperation("创建充值订单")
	@RequestMapping(value = "/recharge_order", method = RequestMethod.POST)
	public JmMessage accountRecharge(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("账户充值数据") @RequestBody JmRechargeOrderCo orderCo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		orderCo.setUserId(user.getUserId());
		orderCo.setRechargeType(1);
		JmRechargeOrder returnOrder = userAccountService.createRechargeOrder(orderCo);
		return new JmMessage(0, returnOrder.getOrderInfoId()+"");
	}
	
	@ApiOperation("查询订单状态")
	@RequestMapping(value = "/order_status/{orderInfoId}", method = RequestMethod.GET)
	public JmMessage queryOrderStatus(@ApiParam("订单id") @PathVariable("orderInfoId") Long orderInfoId){
	 JmRechargeOrder order = userAccountService.findRechargeOrderById(orderInfoId);
		return new JmMessage(0, order.getStatus()+"");
		
	}
	
	@ApiOperation("查询已发送红包记录")
	@RequestMapping(value = "/sen_record", method = RequestMethod.POST)
	public PageItem<ActivityUserVo> sendRecord(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam("查询条件") @RequestBody ActivityUserQo activityUserQo) throws Exception{
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		activityUserQo.setUserId(user.getUserId());
		return activityService.findJkSendRecord(activityUserQo);
	}


	/**
	 * 聚客二维码
	 */
	@RequestMapping(value = "/jk_qrcode", method = RequestMethod.GET)
	public String jkQrcode (@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WxPubAccount pubAccount =wxPubAccountService.getWxPubAccount(user.getAppId());
		if ( pubAccount.getQrcodePosterUrl()==null || pubAccount.getQrcodePosterUrl().equals("") ){
			return pubAccount.getPubQrcodeUrl();
		}else{
			return ImgUtil.appendUrl(pubAccount.getQrcodePosterUrl());
		}
	}

}
