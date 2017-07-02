package com.jm.mvc.controller.system;

import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.system.UserAccountService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.*;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.JmRechargeOrderQo;
import com.jm.mvc.vo.shop.WxPubAccountRo;
import com.jm.mvc.vo.shop.activity.ActivityUserQo;
import com.jm.mvc.vo.shop.activity.ActivityUserVo;
import com.jm.mvc.vo.system.JmRechargeOrderCo;
import com.jm.mvc.vo.system.JmRechargeOrderVo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.po.system.JmRechargeOrder;
import com.jm.repository.po.system.user.UserAccount;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.WxPubAccountConverter;
import com.jm.staticcode.converter.system.UserAccountConverter;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 聚客红包控制层
 * @author chenyy
 * @date 2016/12/3
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/jkmanage")
public class JkManageController {
	
	@Autowired
	private WxPubAccountService wxPubAccountService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private WxUserService wxUserService;



	@ApiOperation("获取聚客红包公众号列表")
	@RequestMapping(value = "/jk_pub_list", method = RequestMethod.POST)
	public PageItem<WxPubAccountVo> jkPubList(@ApiParam(hidden=true) HttpServletRequest request,
										  WxPubAccountRo wxPubAccountRo) throws Exception {
		PageItem<WxPubAccountVo> wxPubAccounts = wxPubAccountService.findJkUser(wxPubAccountRo);
		return wxPubAccounts;
	}



	@ApiOperation("查询已发送红包记录")
	@RequestMapping(value = "/jk_order", method = RequestMethod.POST)
	public PageItem<ActivityUserVo> sendRecord(@ApiParam(hidden = true) HttpServletRequest request,
											   @ApiParam("查询条件") @RequestBody ActivityUserQo activityUserQo) throws Exception{
		PageItem<ActivityUserVo> actVos = activityService.findJkSendRecordToManage(activityUserQo);
		return actVos;
	}


	@ApiOperation("获取充值记录")
	@RequestMapping(value = "/recharge_list", method = RequestMethod.POST)
	public PageItem<JmRechargeOrderVo> getRechargeList(@ApiParam(hidden = true) HttpServletRequest request,
													   @ApiParam("查询条件") @RequestBody JmRechargeOrderQo orderQo) throws Exception{
		orderQo.setType(0);
		PageItem<JmRechargeOrderVo> items = userAccountService.jmRechargeToManage(orderQo);
		return items;
	}


	@ApiOperation("获取合作的第三方用户信息")
	@RequestMapping(value = "/extension_user", method = RequestMethod.POST)
	public PageItem<Map<String,Object>> findExtensionUser(@ApiParam(hidden = true) HttpServletRequest request,CustomerQo customerQo){
		customerQo.setAgentRole(8);//直接查分享客 8
		PageItem<Map<String,Object>> mps = wxUserService.agentUser(customerQo,Constant.JK_MANAGE_APPID);
		return mps;
	}

}
