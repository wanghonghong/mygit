package com.jm.mvc.controller.shop.distribution;

import com.jm.business.service.order.OrderService;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.mvc.vo.shop.ChannelRecordQo;
import com.jm.mvc.vo.shop.distribution.*;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * <p>佣金模块</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/12
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/brokerage")
public class BrokerageController {

	@Autowired
	private BrokerageSetService brokerageSetService;

	@Autowired
	private BrokerageService brokerageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private ShopUserService shopUserService;

	@ApiOperation("分销设置Vo获取")
	@RequestMapping(value="/setting",method = RequestMethod.GET)
	public BrokerageSetVo getBrokerageSetting(@ApiParam(hidden = true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.getBrokerageSetting(user.getShopId());
	}

	@ApiOperation("分销设置新增")
	@RequestMapping(value="/setting/add",method = RequestMethod.POST)
	public BrokerageSetVo setBrokerageSetting(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageSetCo brokerageSetCo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.setBrokerageSetting(user.getShopId(), brokerageSetCo);
	}

	@ApiOperation("分销设置修改")
	@RequestMapping(value="/setting/update",method = RequestMethod.PUT)
	public JmMessage updateBrokerageSetting(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageSetUo brokerageSetUo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		brokerageSetService.updateBrokerageSetting(user.getShopId(), brokerageSetUo);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("分销收费配置获取")
	@RequestMapping(value="/config/{feeType}",method = RequestMethod.GET)
	public BrokerageConfigVo getBrokerageConfig(@ApiParam(hidden = true) HttpServletRequest request, @PathVariable("feeType") int feeType){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.getBrokerageConfig(feeType,user.getShopId());
	}

	@ApiOperation("分销收费配置新增")
	@RequestMapping(value="/config/add",method = RequestMethod.POST)
	public BrokerageConfigVo addBrokerageConfig(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageConfigCo brokerageConfigCo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.addBrokerageConfig(user.getShopId(),brokerageConfigCo);
	}

	@ApiOperation("分销收费配置修改")
	@RequestMapping(value="/config/update",method = RequestMethod.PUT)
	public JmMessage updateBrokerageConfig(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageConfigUo brokerageConfigUo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		brokerageSetService.updateBrokerageConfig(user.getShopId(),brokerageConfigUo);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("分销发放设置vo获取")
	@RequestMapping(value="/put_setting/{payType}",method = RequestMethod.GET)
	public PutSetVo getPutSetting(@ApiParam(hidden = true) HttpServletRequest request,@PathVariable("payType")  int payType){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.getPutSetting(user.getShopId(),payType);
	}

	@ApiOperation("分销设置新增")
	@RequestMapping(value="/put_setting/add",method = RequestMethod.POST)
	public PutSetVo setPutSetting(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody PutSetCo putSetCo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageSetService.setPutSetting(user.getShopId(), putSetCo);
	}

	@ApiOperation("分销设置修改")
	@RequestMapping(value="/put_setting/update",method = RequestMethod.PUT)
	public JmMessage updatePutSetting(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody PutSetUo putSetUo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		brokerageSetService.updatePutSetting(user.getShopId(), putSetUo);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("商品佣金")
	@RequestMapping(value="/brokerage_product",method = RequestMethod.POST)
	public PageItem<BrokerageProductVo> getBrokerageProductList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageProductQo brokerageProductQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageService.getBrokerageProductList(brokerageProductQo,user.getShopId());
	}


	@ApiOperation("商品佣金")
	@RequestMapping(value="/brokerage_product/add",method = RequestMethod.POST)
	public JmMessage setBrokerageProduct(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageProductDetailCo brokerageProductDetailCo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		brokerageSetService.setBrokerageProduct(brokerageProductDetailCo);
		return new JmMessage(0,"操作成功");
	}

//	@ApiOperation("我的小店获取")
//	@RequestMapping(value="/small_shop",method = RequestMethod.GET)
//	public ShopSmallVo getSmallShop(@ApiParam(hidden = true) HttpServletRequest request){
//		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		return brokerageSetService.getSmallShop(user.getShopId());
//	}
//
//	@ApiOperation("我的小店设置")
//	@RequestMapping(value="/small_shop/add",method = RequestMethod.POST)
//	public ShopSmallVo setSmallShop(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody ShopSmallCo shopSmallCo){
//		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		return brokerageSetService.setSmallShop(user.getShopId(), shopSmallCo);
//	}
//
//	@ApiOperation("我的小店修改")
//	@RequestMapping(value="/small_shop/update",method = RequestMethod.PUT)
//	public JmMessage updateSmallShop(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody ShopSmallUo shopSmallUo){
//		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		brokerageSetService.updateSmallShop(user.getShopId(), shopSmallUo);
//		return new JmMessage(0,"修改成功");
//	}

	@ApiOperation("渠道流水")
	@RequestMapping(value="/channel",method = RequestMethod.POST)
	public PageItem<ChannelRecordVo> getChannelRecordList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody ChannelRecordQo channelRecordQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return brokerageService.getChannelRecordList(channelRecordQo,user.getShopId());
	}

//	@ApiOperation("佣金待发清单列表1：客户提现，2：商家发放,3:别的客户")
//	@RequestMapping(value="/brokerage_list/{status}",method = RequestMethod.POST)
//	public PageItem<WxAccountVo> getBrokerageList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageQo brokerageListQo, @PathVariable("status") int status) throws IOException {
//		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		PageItem<WxAccountVo> list = new PageItem<WxAccountVo>();
//		if(brokerageListQo.getPlatForm()==0){
//			list = brokerageService.getBrokerageList(brokerageListQo,user.getShopId());
//		}else{
//			return list;
//		}
//		return list;
//	}

	@ApiOperation("佣金待发清单列表")
	@RequestMapping(value="/wx_account",method = RequestMethod.POST)
	public PageItem<WxAccountVo> getWxAccountList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageQo brokerageListQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<WxAccountVo> list = new PageItem<WxAccountVo>();
		if(brokerageListQo.getPlatForm()==0 || brokerageListQo.getPlatForm()==-1){
			list = brokerageService.getWxAccountList(brokerageListQo,user.getShopId());
		}else if(brokerageListQo.getPlatForm()==1 || brokerageListQo.getPlatForm()==-1){
			list = brokerageService.getwbAccountList(brokerageListQo,user.getShopId());
		}
		return list;
	}

	@ApiOperation("佣金提现列表")
	@RequestMapping(value="/wx_account_kit",method = RequestMethod.POST)
	public PageItem<WxAccountKitVo> getBrokerageList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageQo brokerageListQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<WxAccountKitVo> list = new PageItem<WxAccountKitVo>();
		if(brokerageListQo.getPlatForm()==0 || brokerageListQo.getPlatForm()==-1){
			list = brokerageService.getWxAccountKitList(brokerageListQo,user.getShopId());
		}else{
			return list;
		}
		return list;
	}

	@ApiOperation("获取佣金详情列表")
	@RequestMapping(value="/brokerage_detail",method = RequestMethod.POST)
	public PageItemExt<BrokerageDetailListVo,Integer> getBrokerageDetailList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageDetailListQo brokerageDetailListQo) throws IOException {
		PageItemExt<BrokerageDetailListVo,Integer>  pageItemExt= brokerageService.getBrokerageDetailList(brokerageDetailListQo);
		return pageItemExt;
	}


//	@ApiOperation("佣金流水列表")
//	@RequestMapping(value="/brokerage_record",method = RequestMethod.POST)
//	public PageItem<Map<String, Object>> getBrokerageRecordList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageRecordQo brokerageRecordQo){
//		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		PageItem<Map<String, Object>> list = brokerageService.getBrokerageRecordList(brokerageRecordQo,user.getShopId());
//		if (null != list.getItems() && list.getItems().size()>0){
//			for(Map<String, Object> map : list.getItems() ){
//				String weChatName =  map.get("nickname").toString();
//				map.put("nickname", Base64Util.getFromBase64(weChatName));
//			}
//		}
//		return list;
//	}

	@ApiOperation("佣金流水列表")
	@RequestMapping(value="/brokerage_record",method = RequestMethod.POST)
	public PageItem<BrokerageRecordVo> getBrokerageRecord(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageRecordQo brokerageRecordQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//		PageItem<BrokerageRecordVo> list = brokerageService.getBrokerageRecordList(brokerageRecordQo,user.getShopId());
		return brokerageService.getBrokerageRecord(brokerageRecordQo,user.getShopId());
	}

	@ApiOperation("佣金清单列表（基于订单产生的佣金）1：佣金清单，2：有效佣金")
	@RequestMapping(value="/brokerage_order",method = RequestMethod.POST)
	public PageItem<BrokerageOrderVo> getBrokerageOrderList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody BrokerageOrderQo brokerageOrderQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<BrokerageOrderVo> list = brokerageService.getBrokerageOrderList(brokerageOrderQo,user.getShopId());
		return list;
	}

	@ApiOperation("获取佣金清单详情")
	@RequestMapping(value = "/brokerage_detail/{orderInfoId}", method = RequestMethod.GET)
	public BrokerageOrderInfoVo getBrokerageDetail(@ApiParam("订单ID") @PathVariable("orderInfoId")  Long orderInfoId) {
		OrderInfoVo orderInfoVo = orderService.findOrderById(orderInfoId);
		WxUser wxUser = wxUserService.findWxUserByUserId(orderInfoVo.getUserId());
		ShopUser shopUser = null;
		if(wxUser!=null && wxUser.getShopUserId()!=null && wxUser.getShopUserId()>0){
			shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
		}
		orderInfoVo.setNickname(Base64Util.getFromBase64(orderInfoVo.getNickname()));
		List<BrokerageDetailVo> detailList = brokerageService.getDetailList(orderInfoId);
		BrokerageOrderInfoVo brokerageOrderInfoVo = new BrokerageOrderInfoVo();
		brokerageOrderInfoVo.setOrderInfo(orderInfoVo);
		brokerageOrderInfoVo.setBrokerageDetails(detailList);
		brokerageOrderInfoVo.setHeadimgurl(wxUser.getHeadimgurl());
		if(shopUser!=null){
			brokerageOrderInfoVo.setUserName(shopUser.getUserName());
			brokerageOrderInfoVo.setPhoneNumber(shopUser.getPhoneNumber());
		}
		brokerageOrderInfoVo.setNickname(Base64Util.getFromBase64(wxUser.getNickname()));
		return brokerageOrderInfoVo;
	}

}
