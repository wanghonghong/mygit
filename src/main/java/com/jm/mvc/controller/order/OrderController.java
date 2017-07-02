package com.jm.mvc.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.business.domain.shop.ActivityRedDo;
import com.jm.business.service.order.OrderRefundService;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.order.OrderSupplyService;
import com.jm.business.service.order.SendCompanysKd100Service;
import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.order.*;
import com.jm.mvc.vo.system.UserVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.system.user.User;
import com.jm.repository.po.system.user.UserRole;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

	@Autowired
    private OrderService orderService;
	@Autowired
    private WxUserService wxUserService;
	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private BrokerageService brokerageService;
	@Autowired
	private SendCompanysKd100Service sendCompanysKd100Service;
	@Autowired
	private UserService userService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private OrderSupplyService orderSupplyService;

	@ApiOperation("pc端保存订单中的卖家备注信息")
	@RequestMapping(value = "/save_customer/{id}", method = RequestMethod.PUT)
    public JmMessage addcostomer(@ApiParam(hidden=true) HttpServletRequest request,
    							  @ApiParam("订单标识id") @PathVariable("id") Long id,
    							  @RequestBody @Valid OrderInfoForUpdateVo updateVo){
        OrderInfo orderInfo = orderService.findOrderInfoById(id);
        orderInfo.setSellerNote(updateVo.getSellerNote());
		OrderInfo od=orderService.createOrder(orderInfo);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
    	return new JmMessage(1, "1");
    }

	@ApiOperation("pc端保存价格信息")
	@RequestMapping(value = "/save_price/{order_info_id}", method = RequestMethod.PUT)
    public JmMessage savePrice(@ApiParam(hidden=true) HttpServletRequest request,
							   @ApiParam("订单标识id") @PathVariable("order_info_id") Long orderInfoId,
							   @RequestBody @Valid OrderInfoForUpdateVo updateVo) throws Exception {
		if(updateVo.getRealPrice()<=0){
			updateVo.setRealPrice(1);
		}
		OrderInfo od=orderService.createOrder(orderInfoId,updateVo);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String url = Constant.DOMAIN;
		orderService.pushUpdatePriceWxMsg(user,od,url);
		return new JmMessage(1, "1");
    }

	//-------------------------------------------------------------------------------------------------------------------------//
	@ApiOperation("pc端订单管理页面或按订单管理订单状态查询")
	@RequestMapping(value = "/order_info_list/{types}", method = RequestMethod.POST)
	public PageItemExt orderManage(@ApiParam(hidden=true) HttpServletRequest request,
											 @ApiParam(hidden=true) HttpServletResponse response,
                                             @PathVariable("types") Integer types,
											 @RequestBody @Valid OrderInfoForQueryVo orderVo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer userid = user.getUserId();
		if(null!=user){
			orderVo.setShopId(user.getShopId());
		}
		User shopUser = userService.getUser(userid);
		OrderVo orderVos = new OrderVo();
		UserRole role;
		String hxAcct = user.getHxAccount();
		if(hxAcct==null||"".equals(hxAcct)){
			role = userRoleService.findOnlineUserByShopIdAndUserId(user.getShopId(),user.getUserId());
			log.info("首次获取客服人员信息"+role.getHxAccount());
			if(role.getHxAccount()!=null){
				hxAcct = role.getHxAccount();
				user.setHxAccount(hxAcct);
				request.getSession().setAttribute(Constant.SESSION_USER,user);
			}
		}
		if(hxAcct!=null){
			log.info("添加客服人员信息到内存中");
			Equalizer.addOnlineServiceUser(user.getShopId(),user.getUserId(),hxAcct);
		}
		//type为1表示pc端请求，type为2表示app端请求
		int type = 1;
		PageItem<OrderInfoVo> pageItem = new PageItem<>();
		if(types==1){
			pageItem = orderService.queryOrder(orderVo,type);
		}else{
			pageItem = orderService.queryWbOrder(orderVo,type);
		}
		orderVos.setHxAccount(hxAcct);
		orderVos.setUserid(userid);
		orderVos.setNickname(shopUser.getUserName());
		orderVos.setHeadImg(ImgUtil.appendUrl(shopUser.getHeadImg(),100));
		orderVos.setShopId(user.getShopId());
        PageItemExt<OrderInfoVo,OrderVo> ext = JdbcUtil.pageItem2Ext(pageItem,orderVos);
        return ext;
	}


	@ApiOperation("pc端订单管理页面礼品单查询")
	@RequestMapping(value = "/dispatch_order_list", method = RequestMethod.POST)
	public PageItemExt dispatchOrderManage(@ApiParam(hidden=true) HttpServletRequest request,
								   @ApiParam(hidden=true) HttpServletResponse response,
								   @RequestBody @Valid OrderInfoForQueryVo orderVo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer userid = user.getUserId();
		if(null!=user){
			orderVo.setShopId(user.getShopId());
		}
		User shopUser = userService.getUser(userid);
		OrderVo orderVos = new OrderVo();
		UserRole role;
		String hxAcct = user.getHxAccount();
		if(hxAcct==null||"".equals(hxAcct)){
			role = userRoleService.findOnlineUserByShopIdAndUserId(user.getShopId(),user.getUserId());
			log.info("首次获取客服人员信息"+role.getHxAccount());
			if(role.getHxAccount()!=null){
				hxAcct = role.getHxAccount();
				user.setHxAccount(hxAcct);
				request.getSession().setAttribute(Constant.SESSION_USER,user);
			}
		}
		if(hxAcct!=null){
			log.info("添加客服人员信息到内存中");
			Equalizer.addOnlineServiceUser(user.getShopId(),user.getUserId(),hxAcct);
		}
		//type为1表示pc端请求，type为2表示app端请求
		int type = 1;
		PageItem<OrderInfoVo> pageItem = orderService.queryDispatchOrder(orderVo,type);
		orderVos.setHxAccount(hxAcct);
		orderVos.setUserid(userid);
		orderVos.setNickname(shopUser.getUserName());
		orderVos.setHeadImg(ImgUtil.appendUrl(shopUser.getHeadImg(),100));
		orderVos.setShopId(user.getShopId());
		PageItemExt<OrderInfoVo,OrderVo> ext = JdbcUtil.pageItem2Ext(pageItem,orderVos);
		return ext;
	}

	@ApiOperation("修改订单信息")
	@RequestMapping(value = "/order_update/{order_info_id}", method = RequestMethod.PUT)
	public JmMessage update(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId,
							@ApiParam(hidden=true) HttpServletRequest request,
							@RequestBody @Valid OrderInfoForUpdateVo updateVo) throws Exception {
		//确认收货后调用红包接口
		OrderInfo orderInfo = orderService.getOrderInfo(orderInfoId);
		if(null!=orderInfo ){
			if(orderInfo.getStatus()==1 && updateVo.getStatus()==0 ){
				return new JmMessage(2, "操作成功！");
			}else{
				if(updateVo.getStatus()==3 && orderInfo.getStatus()==2 ){
					brokerageService.updateBrokerageOrder(orderInfoId);
					WxUser wxUser = wxUserService.getWxUser(orderInfo.getUserId());
					activityService.sendActivityRed(new ActivityRedDo(wxUser,request.getRemoteHost(),4));
				}
				orderService.updateOrder(orderInfoId,updateVo);
			}
		}
		return new JmMessage(2, "操作成功！");
	}

	@ApiOperation("pc端订单管理和发货管理查看订单详情")
	@RequestMapping(value = "/query_orders/{id}", method = RequestMethod.GET)
	public OrderInfoDetailVo queryOrder(@ApiParam(hidden=true) HttpServletRequest request,
									@ApiParam("订单标识id") @PathVariable("id")  Long id) throws IOException {
		int type = 0;//type=0 表示订单管理和发货管理查看详情
		OrderInfoDetailVo orderInfoDetailVo = orderService.queryOrders(id,type);
		log.info(orderInfoDetailVo.getTransMsg());
		return orderInfoDetailVo;
	}

	@ApiOperation("pc端订单管理查看订单详情")
	@RequestMapping(value = "/query_order/{id}", method = RequestMethod.GET)
	public OrderInfoDetailVo queryOrders(@ApiParam(hidden=true) HttpServletRequest request,
										@ApiParam("订单标识id") @PathVariable("id")  Long id) throws IOException {
		OrderInfoDetailVo orderInfoDetailVo = orderService.queryOrdersPc(id);
		return orderInfoDetailVo;
	}

	@ApiOperation("pc端查看订单")
	@RequestMapping(value = "/order_detail/{id}", method = RequestMethod.GET)
	public OrderInfoVo orderDetail(@ApiParam(hidden=true) HttpServletRequest request,
								   @ApiParam("订单标识id") @PathVariable("id")  Long id) throws IOException {
		OrderInfoVo orderInfoVo = orderService.findOrderByOrderInfoId(id);
		return orderInfoVo;
	}

	@ApiOperation("pc端同意退款页面")
	@RequestMapping(value = "/agreeForRefund/{order_info_id}", method = RequestMethod.GET)
	public OrderRefundVo agreeForRefund(@ApiParam(hidden=true) HttpServletRequest request,
									   @ApiParam("订单标识order_info_id") @PathVariable("order_info_id")  Long order_info_id){
		OrderRefundVo orderRefundVo = orderRefundService.findOrderRefundByOrderInfoId(order_info_id);
		return orderRefundVo;
	}

	/*******************************************地区供货******************************************************/
    @ApiOperation("pc端地区供货订单管理页面或按地区供货订单状态查询")
    @RequestMapping(value = "/order_supply_list", method = RequestMethod.POST)
    public PageItemExt orderSupplyManage(@ApiParam(hidden=true) HttpServletRequest request,
                                   @ApiParam(hidden=true) HttpServletResponse response,
                                   @RequestBody @Valid OrderInfoForQueryVo orderVo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer userid = user.getUserId();
        if(null!=user){
            orderVo.setShopId(user.getShopId());
        }
        User shopUser = userService.getUser(userid);
        OrderVo orderVos = new OrderVo();
		UserRole role;
		String hxAcct = user.getHxAccount();
		if(hxAcct==null||"".equals(hxAcct)){
			role = userRoleService.findOnlineUserByShopIdAndUserId(user.getShopId(),user.getUserId());
			log.info("首次获取客服人员信息"+role.getHxAccount());
			if(role.getHxAccount()!=null){
				hxAcct = role.getHxAccount();
				user.setHxAccount(hxAcct);
				request.getSession().setAttribute(Constant.SESSION_USER,user);
			}
		}
		if(hxAcct!=null){
			log.info("添加客服人员信息到内存中");
			Equalizer.addOnlineServiceUser(user.getShopId(),user.getUserId(),hxAcct);
		}
        //type为1表示pc端，type为2表示app端
        int type = 1;
        PageItem<OrderInfoVo> pageItem = orderSupplyService.queryOrderSupply(orderVo,type);
        orderVos.setHxAccount(hxAcct);
        orderVos.setUserid(userid);
        orderVos.setNickname(shopUser.getUserName());
        orderVos.setHeadImg(ImgUtil.appendUrl(shopUser.getHeadImg(),100));
        orderVos.setShopId(user.getShopId());
        PageItemExt<OrderInfoVo,OrderVo> ext = JdbcUtil.pageItem2Ext(pageItem,orderVos);
        return ext;
    }

    @ApiOperation("pc端地区供货订单管理页面或按地区供货订单状态查询")
    @RequestMapping(value = "/supply_configure_list", method = RequestMethod.POST)
    public PageItemExt orderSupplyConfigure(@ApiParam(hidden=true) HttpServletRequest request,
                                         @ApiParam(hidden=true) HttpServletResponse response,
										 @RequestBody @Valid SupplyForQueryVo supplyForQueryVo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer userid = user.getUserId();
        if(null!=user){
			supplyForQueryVo.setShopId(user.getShopId());
        }
        OrderVo orderVos = new OrderVo();
		//type为1表示供货管理的订单查询，type为2表示供货管理的供货配置查询
		int type = 1;
        PageItem<OrderInfoVo> pageItem = orderSupplyService.queryOrderSupply(supplyForQueryVo,type);
        orderVos.setUserid(userid);
		orderVos.setRoleId(user.getRoleId());
        PageItemExt<OrderInfoVo,OrderVo> ext = JdbcUtil.pageItem2Ext(pageItem,orderVos);
        return ext;
    }

	@ApiOperation("pc端查看店主信息")
	@RequestMapping(value = "/query_shopkeeper/{shop_id}", method = RequestMethod.GET)
	public UserVo queryShopkeeper(@ApiParam(hidden=true) HttpServletRequest request,
								  @ApiParam("店铺标识shop_id") @PathVariable("shop_id")  Integer shop_id) throws IOException {
		return orderSupplyService.queryShop(shop_id);
	}

	@ApiOperation("pc端查看店主信息")
	@RequestMapping(value = "/query_supply_user/{user_id}", method = RequestMethod.GET)
	public UserVo querySupplyUser(@ApiParam(hidden=true) HttpServletRequest request,
								  @ApiParam("店铺标识user_id") @PathVariable("user_id")  Integer user_id) throws IOException {
		User user = userService.getUser(user_id);
		UserVo userVo = new UserVo();
		userVo.setUserName(user.getUserName());
		userVo.setPhoneNumber(user.getPhoneNumber());
		return userVo;
	}

	@ApiOperation("pc端供货管理查看订单详情")
	@RequestMapping(value = "/query_supply_orders/{id}", method = RequestMethod.GET)
	public OrderInfoDetailVo querySupplyOrder(@ApiParam(hidden=true) HttpServletRequest request,
										@ApiParam("订单标识id") @PathVariable("id")  Long id) throws IOException {
		int type = 1;//type=1 表示供货管理查看详情
		OrderInfoDetailVo orderInfoDetailVo = orderService.queryOrders(id,type);
		return orderInfoDetailVo;
	}

	@ApiOperation("pc端送礼母订单详情查看")
	@RequestMapping(value =  "/queryDispatchAndDelivery/{id}", method = RequestMethod.GET)
	public OrderAndDispatchDeliveryVo queryDispatchAndDeliveryDetail(@ApiParam(hidden=true) HttpServletRequest request,
										@ApiParam("订单标识id") @PathVariable("id")  Long id) throws IOException {
		OrderAndDispatchDeliveryVo orderAndDispatchDeliveryVo = orderService.queryOrderAndDispatchDelivery(id);
		return orderAndDispatchDeliveryVo;
	}

	@ApiOperation("pc端订单导出功能")
	@RequestMapping(value = "/export_order", method = RequestMethod.GET)
	public void exportOrder(@ApiParam(hidden=true) HttpServletRequest request,
							@ApiParam(hidden=true) HttpServletResponse response) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		//json字符串
		String json = request.getParameter("orderManageVo").toString();
		ObjectMapper objectMapper=new ObjectMapper();
        OrderInfoForExportVo orderVo =objectMapper.readValue(json, OrderInfoForExportVo.class);
		if(null!=user){
			orderVo.setShopId(user.getShopId());
		}
		int type = 1;
		List<Map<String, Object>> orderInfoExportVos = orderService.exportOrder(orderVo,type);
		orderService.exportOrder(orderInfoExportVos,response);
	}

	@ApiOperation("pc端订单导出功能")
	@RequestMapping(value = "/export_delivery", method = RequestMethod.GET)
	public void exportDelivery(@ApiParam(hidden=true) HttpServletRequest request,
							@ApiParam(hidden=true) HttpServletResponse response) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		//json字符串
		String json = request.getParameter("orderManageVo").toString();
		ObjectMapper objectMapper=new ObjectMapper();
		OrderInfoForExportVo orderVo =objectMapper.readValue(json, OrderInfoForExportVo.class);
		if(null!=user){
			orderVo.setShopId(user.getShopId());
		}
		int type = 1;
		List<Map<String, Object>> orderInfoExportVos = orderService.exportDelivery(orderVo,type);
		orderService.exportDelivery(orderInfoExportVos,response);
	}

	@ApiOperation("pc端供货订单导出功能")
	@RequestMapping(value = "/export_supply", method = RequestMethod.GET)
	public void exportSupply(@ApiParam(hidden=true) HttpServletRequest request,
							   @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		//json字符串
		String json = request.getParameter("orderManageVo").toString();
		ObjectMapper objectMapper=new ObjectMapper();
		OrderInfoForExportVo orderVo =objectMapper.readValue(json, OrderInfoForExportVo.class);
		if(null!=user){
			orderVo.setShopId(user.getShopId());
		}
		int type = 1;
		List<Map<String, Object>> orderInfoExportVos = orderService.exportSupply(orderVo,type);
		orderService.exportSupply(orderInfoExportVos,response);
	}

	@ApiOperation("pc端供货清单导出功能")
	@RequestMapping(value = "/export_supply_order", method = RequestMethod.GET)
	public void exportSupplyOrder(@ApiParam(hidden=true) HttpServletRequest request,
							 @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		//json字符串
		String json = request.getParameter("supplyForQueryVo").toString();
		ObjectMapper objectMapper=new ObjectMapper();
		SupplyForQueryVo supplyForQueryVo =objectMapper.readValue(json, SupplyForQueryVo.class);
		if(null!=user){
			supplyForQueryVo.setShopId(user.getShopId());
		}
		int type = 1;
		List<Map<String, Object>> orderInfoExportVos = orderService.exportSupplyOrder(supplyForQueryVo,type,user.getRoleId());
		orderService.exportSupplyOrder(orderInfoExportVos,response,user.getRoleId());
	}

	@ApiOperation("pc端订单管理查看订单详情 cj-add - 2017-04-11")
	@RequestMapping(value = "/query_order_form/{orderNum}", method = RequestMethod.GET)
	public OrderInfoDetailVo queryOrdersForm(@ApiParam(hidden=true) HttpServletRequest request,
										 @ApiParam("订单标识id") @PathVariable("orderNum")  String orderNum) throws IOException {
		OrderInfoDetailVo orderInfoDetailVo = orderService.queryOrdersPcForm(orderNum);
		return orderInfoDetailVo;
	}
}
