package com.jm.mvc.controller.shop.distribution;

import com.jm.business.service.shop.distribution.BrokerageRedService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.wx.RedSendParam;
import com.jm.repository.po.shop.brokerage.WxAccountKit;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>佣金红包发放</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/12
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/brokerage/red")
public class BrokerageRedController {

	@Autowired
	private BrokerageRedService brokerageRedService;

	@RequestMapping(value="/hand_send/{userId}",method = RequestMethod.GET)
	public JmMessage handSendBrokerage(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("佣金发放") @PathVariable Integer userId) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		RedSendParam redSendParam = brokerageRedService.getRedSendParam(request.getRemoteHost(),userId,user.getShopId(),20000,1,0,0,null);
		String result = brokerageRedService.brokerageRedSend(redSendParam);
		return new JmMessage(0,result);
	}

	@RequestMapping(value="/user_kit/{id}",method = RequestMethod.GET)
	public JmMessage userKitBrokerage(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("提现id") @PathVariable Integer id) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WxAccountKit wxAccountKit = brokerageRedService.getBrokerageKit(id);
		String result=null;
		if(wxAccountKit != null){
			RedSendParam redSendParam = brokerageRedService.getRedSendParam(request.getRemoteHost(),wxAccountKit.getUserId(),user.getShopId(),20000,2,5,wxAccountKit.getKitMoney(),id);
			result = brokerageRedService.brokerageRedSend(redSendParam);
		}else{
			result = "无该提现记录";
		}
		return new JmMessage(0,result);
	}


}
