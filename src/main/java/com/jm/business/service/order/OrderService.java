package com.jm.business.service.order;

import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.online.HxUserService;
import com.jm.business.service.product.ProductTransPriceService;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.*;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.*;
import com.jm.repository.client.WxClient;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderDetailRepository;
import com.jm.repository.jpa.order.OrderDiscountRepository;
import com.jm.repository.jpa.order.OrderRepository;
import com.jm.repository.jpa.product.ProductAreaRelRepository;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.product.ProductSpecRepository;
import com.jm.repository.jpa.shop.activity.ShopCardRepository;
import com.jm.repository.jpa.wx.WxUserCardRepository;
import com.jm.repository.po.order.*;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.OrderConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.csv.CsvToolUtil;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Log4j
@Service
public class OrderService {
	@Autowired
	private JdbcRepository jdbcRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductSpecRepository productSpecRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private WxAuthService wxAuthService;
	@Autowired
	private WxpublicAccountService wxpublicAccountService;
	@Autowired
	private WxClient wxClient;
	@Autowired
	private ProductTransPriceService productTransPriceService;
	@Autowired
	private HxUserService hxUserService;
	@Autowired
	protected JdbcUtil jdbcUtil;
	@Autowired
	private WxService wxService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCardRepository shopCardRepository;
	@Autowired
	private OrderDeliveryService orderDeliveryService;
	@Autowired
	private OrderRefundService orderRefundService;
	@Autowired
	private OrderFavorableService orderFavorableService;
	@Autowired
	private BrokerageSetService brokerageSetService;

	@Autowired
	private SendCompanysKd100Service sendCompanysKd100Service;

	@Autowired
	private WxUserCardRepository wxUserCardRepository;

	@Autowired
	private ProductAreaRelRepository productAreaRelRepository;
	@Autowired
	private WxUserAccountService wxUserAccountService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private ShopUserService shopUserService;


	@Autowired
	private OrderDiscountRepository orderDiscountRepository;

	@Autowired
	private OrderDetailService orderDetailService;

	public OrderInfo createOrder(OrderInfo order) {
		return orderRepository.save(order);
	}

	@Transactional
	public OrderInfo createOrder(Long orderInfoId, OrderInfoForUpdateVo updateVo) {
		OrderInfo orderInfo = orderRepository.findOne(orderInfoId);
		OrderInfo order = new OrderInfo();
		String orderNum = order.getOrderNum();
		BeanUtils.copyProperties(orderInfo, order);
		order.setOrderNum(orderNum);
		order.setTotalPrice(updateVo.getTotalPrice());
		order.setSendFee(updateVo.getSendFee());
		order.setOrderInfoId(null);
		order.setUpdatePriceDate(new Date());
		order.setRealPrice(updateVo.getRealPrice());
		orderInfo.setStatus(4);
		orderRepository.save(orderInfo);
		OrderInfo orderNew = orderRepository.save(order);
		//修改订单时修改OrderDiscount表信息
		orderDiscountRepository.updateOrderDiscount(orderNew.getOrderInfoId(),orderInfo.getOrderInfoId());
		List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderInfoId(orderInfoId);
		for (OrderDetail orderDetail : orderDetails) {
			OrderDetail orderDetailNew = new OrderDetail();
			BeanUtils.copyProperties(orderDetail, orderDetailNew);
			orderDetailNew.setOrderDetailId(null);
			orderDetailNew.setOrderInfoId(orderNew.getOrderInfoId());
			orderDetailRepository.save(orderDetailNew);
		}
		return orderNew;
	}

	public OrderInfo updateOrder(Long id, OrderInfoForUpdateVo orderInfoForUpdate) throws Exception {
		OrderInfo orderInfo = orderRepository.findOne(id);
		if (orderInfoForUpdate.getStatus() == 4) {
			//只有待付款才能取消订单
			if (orderInfo.getStatus() == 0) {
				orderInfo.setStatus(orderInfoForUpdate.getStatus());
			}
		} else if (orderInfoForUpdate.getStatus() == 3) {
			//只有待收货才能确认收货
			if (orderInfo.getStatus() == 2) {
				orderInfo.setStatus(orderInfoForUpdate.getStatus());
				orderInfo.setTakeDate(new Date());
				orderInfo.setLookStatus(orderInfoForUpdate.getStatus());
				OrderInfo orderInfo1 = orderRepository.save(orderInfo);
				pushConfirmWxMsg(id,orderInfo1);
				return orderInfo1;
			}
		} else {
			orderInfo.setStatus(orderInfoForUpdate.getStatus());
		}

		if (!StringUtils.isEmpty(orderInfoForUpdate.getRemark())) {
			orderInfo.setRemark(orderInfoForUpdate.getRemark());
		}
		orderInfo.setLookStatus(orderInfoForUpdate.getStatus());
		return orderRepository.save(orderInfo);
	}

	//查询微信订单信息
	public PageItem<OrderInfoVo> queryOrder(OrderInfoForQueryVo orderVo,int type) throws IOException {
		PageItem<OrderInfoVo> pageItem = queryOrderList(orderVo,type);
		PageItem<OrderInfoVo> pageItemRes = new PageItem<>();
		List<OrderInfoVo> orderInfoVos = pageItem.getItems();
		if (orderInfoVos == null || orderInfoVos.size() == 0) {
			pageItemRes.setCount(0);
			pageItemRes.setItems(orderInfoVos);
			return pageItemRes;
		}
		String orderInfoIds = "";
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			orderInfoIds += orderInfoVo.getOrderInfoId() + ",";
		}
		orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
		List<Map<String, Object>> orderDetails = queryOrderDetails(orderInfoIds);
		//合并订单列表跟 订单详情列表
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			for (Map<String, Object> map : orderDetails) {
				if (orderInfoVo.getOrderInfoId().equals(map.get("order_info_id"))) {
					OrderDetailVo orderDetailVo = new OrderDetailVo();
					orderDetailVo = getOrderInfoVo(map,orderInfoVo,orderDetailVo);
					orderInfoVo.getOrderDetails().add(orderDetailVo);
				}
			}
		}
		pageItemRes.setCount(Integer.parseInt(pageItem.getCount() + ""));
		pageItemRes.setItems(orderInfoVos);
		return pageItemRes;
	}

	//查询订单信息
	public PageItem<OrderInfoVo> queryDispatchOrder(OrderInfoForQueryVo orderVo,int type) throws IOException {
		PageItem<OrderInfoVo> pageItem = queryDispatchOrderList(orderVo,type);
		PageItem<OrderInfoVo> pageItemRes = new PageItem<>();
		List<OrderInfoVo> orderInfoVos = pageItem.getItems();
		if (orderInfoVos == null || orderInfoVos.size() == 0) {
			pageItemRes.setCount(0);
			pageItemRes.setItems(orderInfoVos);
			return pageItemRes;
		}
		String orderInfoIds = "";
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			orderInfoIds += orderInfoVo.getOrderInfoId() + ",";
		}
		orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
		List<Map<String, Object>> orderDetails = queryOrderDetails(orderInfoIds);
		//合并订单列表跟 订单详情列表
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			for (Map<String, Object> map : orderDetails) {
				if (orderInfoVo.getOrderInfoId().equals(map.get("order_info_id"))) {
					OrderDetailVo orderDetailVo = new OrderDetailVo();
					orderDetailVo = getOrderInfoVo(map,orderInfoVo,orderDetailVo);
					orderInfoVo.getOrderDetails().add(orderDetailVo);
				}
			}
		}
		pageItemRes.setCount(Integer.parseInt(pageItem.getCount() + ""));
		pageItemRes.setItems(orderInfoVos);
		return pageItemRes;
	}

	public OrderInfo findOrderInfoById(Long id) {
		return orderRepository.findOrderInfoById(id);
	}

	public OrderInfoVo findOrderById(Long id){
		OrderInfo orderInfo = orderRepository.findOrderInfoById(id);
		OrderInfoVo orderInfoVo = OrderConverter.toOrderVo(orderInfo);
		return orderInfoVo;
	}

	public OrderInfoVo findOrderByOrderInfoId(Long id){
		OrderInfo orderInfo = orderRepository.findOrderInfoById(id);
		OrderInfoVo orderInfoVo = OrderConverter.toOrderVo(orderInfo);
		List<OrderDiscount> orderDiscountList = orderDiscountRepository.findByOrderDiscountByOrderInfoId(id);
		if(orderDiscountList.size()>0){
			for(OrderDiscount orderDiscount:orderDiscountList){
				if(orderDiscount.getType()==0){
					//积分
					orderInfoVo.setBenefits(orderDiscount.getCount());
				}else if(orderDiscount.getType()==1){
					//优惠券
					orderInfoVo.setCoupon(orderDiscount.getCount());
				}else if(orderDiscount.getType()==2){
					//分销商折扣
					orderInfoVo.setDiscount(orderDiscount.getCount());
				}
			}
		}
		return orderInfoVo;
	}

	public OrderInfo getOrderInfo(Long id) {
		return orderRepository.findOne(id);
	}

	public List<Object> queryOrderStatus(Integer userId, Integer shopId) {
		List<Object> list = orderRepository.queryOrderStatus(userId, shopId);
		if (list == null) {
			list = new ArrayList<Object>();
		}
		return list;
	}

	public OrderInfo updateOrderStatus(Long orderInfoId, Integer status) {
		OrderInfo info = orderRepository.findOne(orderInfoId);
		info.setStatus(status);
		info.setLookStatus(status);
		return orderRepository.save(info);
	}

	public OrderInfo updateOrderStatus(Long orderInfoId, Date date,Integer status) {
		OrderInfo info = orderRepository.findOne(orderInfoId);
		info.setStatus(status);
		info.setLookStatus(status);
		info.setAgreeRefundDate(date);
		return orderRepository.save(info);
	}

	public OrderInfo updateGoodStatus(Long orderInfoId, Date date, Integer goodStatus) {
		OrderInfo info = orderRepository.findOne(orderInfoId);
		info.setStatus(5);
		info.setLookStatus(5);
		info.setGoodStatus(1);
		info.setCreateGoodDate(date);
		return orderRepository.save(info);
	}

	public OrderInfo updateOrder(PayRecord payRecord,Long orderInfoId) {
		OrderInfo info = orderRepository.findOne(orderInfoId);
		info.setStatus(1);
		info.setPayId(payRecord.getPayId());
		info.setLookStatus(1);
		info.setPayDate(payRecord.getPayDate());
		return orderRepository.save(info);
	}

	/**
	 * 统计购买数量用于限购数量
	 *
	 * @param userId
	 * @return
	 */
	public int getBuyCount(int userId, Integer pid) {
		return jdbcRepository.queryBuyCountByUserId(userId, pid);
	}

	/**
	 * 用于统计待付款用户可购买数量
	 * @throws Exception
	 */
	public boolean getbuyCountByProduct(List<OrderProductQo> orderProducts,Integer userId){
		List pids = new ArrayList<>();
		//查询商品
		if(orderProducts!=null && orderProducts.size()>0){
			for(OrderProductQo orderProduct:orderProducts){
				pids.add(orderProduct.getPid());
			}
		}
		//获取订单的商品
		List<Product> products = productRepository.findAll(pids);
		for(Product product : products){
			for(OrderProductQo orderProduct:orderProducts){
				if(product.getPid().equals(orderProduct.getPid())){//循环遍历出相等的商品
					log.info("=========product.getIsLimitCount()========="+product.getIsLimitCount());
					if(product.getIsLimitCount()==1){//判断该商品是否限购
						//获取用户该商品购买数量
						log.info("=========orderProduct.getCount()========="+orderProduct.getCount());
						if(product.getLimitCount()-orderProduct.getCount()<0){//判断可限购数量-当前订单数量是否满足购买提交
							log.info("=========product.getLimitCount()-orderProduct.getCount()=========");
							return false;
						}else{
							int buyCount = getBuyCount(product.getPid(),userId);
							//限购数量-已购买数量-订单数量
							if(product.getLimitCount()-buyCount-orderProduct.getCount()<0){//
								log.info("=========product.getLimitCount()-buyCount-orderProduct.getCount()=========");
								return false;
							}
						}

					}
				}
			}
		}
		return true;
	}

	/**
	 * 微信推送商品改价通知消息
	 */
	public void pushUpdatePriceWxMsg(JmUserSession user, OrderInfo orderInfo, String url) throws Exception {
		WxUser wxuser = wxUserService.getWxUser(orderInfo.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append("\r\n");
		sb.append(wxClient.long2short(wxAuthService.getAuthAccessToken(user.getAppId()),url + "/order/order_all/0?&shopId=" + orderInfo.getShopId()) + "\"");
		String content = "你选购的商品价格已经修改，\r\n\r\n立即付款请点击" + "\b" + sb;
		wxMessageService.sendMsg(wxuser.getOpenid(), wxAuthService.getAuthAccessToken(user.getAppId()), content,user.getAppId());
	}

	/**
	 * 微信推送支付成功消息
	 */
	public void pushPayWxMsg(WxUser user,OrderInfo orderInfo, String url) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append("\r\n");
		sb.append(wxClient.long2short(wxAuthService.getAuthAccessToken(user.getAppid()),url + "/shop/index?shopId=" + orderInfo.getShopId()) + "\"");
		String content = "你选购的商品已付款成功，" + "\r\n我们将尽快为您发货：）" + "\r\n订单号：" + orderInfo.getOrderNum() + "\r\n\r\n" + "选购其他商品，请点击" + "\b" + sb;
		wxMessageService.sendMsg(user.getOpenid(), wxAuthService.getAuthAccessToken(user.getAppid()), content,user.getAppid());
	}

	/**
	 * 微信推送已发货消息
	 */
	public void pushDeliverWxMsg(Long orderInfoId, String url) throws Exception {
		OrderInfo orderInfo = orderRepository.findOne(orderInfoId);
		List<OrderDelivery> orderDelivery = orderDeliveryService.getOrderDelivery(orderInfoId);
		WxUser wxuser = wxUserService.getWxUser(orderInfo.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append(url + "/order/order_all/2?shopId=" + orderInfo.getShopId());
		WxTemplateDo wxTemplateDo = new WxTemplateDo();
		String first = "您的商品已发货,请注意查收哦！";
		String remark = "查看待收货商品请点击:)";
		wxTemplateDo = getWxTemplateDo(wxTemplateDo,wxuser,sb.toString(),orderInfo,orderDelivery.get(orderDelivery.size()-1),first,remark,1);
		wxMessageService.sendTemplate(wxTemplateDo);
	}

	/**
	 * 微信推送已收货消息
	 */
	public void pushConfirmWxMsg(Long orderInfoId,OrderInfo orderInfo) throws Exception {
		List<OrderDelivery> orderDelivery = orderDeliveryService.getOrderDelivery(orderInfoId);
		WxUser wxuser = wxUserService.getWxUser(orderInfo.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append(Constant.DOMAIN + "/order/order_all/3?shopId=" + orderInfo.getShopId());
		WxTemplateDo wxTemplateDo = new WxTemplateDo();
		String first = "您选购的商品已签收,请确认是否本人签收！";
		String remark = "若有疑问,请直接回复本公众号,查看已收货商品请点击:)";
		if(orderDelivery.size()==0){
			wxTemplateDo = getWxTemplateDo(wxTemplateDo,wxuser,sb.toString(),orderInfo,new OrderDelivery(),first,remark,3);
		}else{
			wxTemplateDo = getWxTemplateDo(wxTemplateDo,wxuser,sb.toString(),orderInfo,orderDelivery.get(orderDelivery.size()-1),first,remark,3);
		}
		wxTemplateDo.setSingTime(Toolkit.dateToStr(orderInfo.getTakeDate(),"yyyy-MM-dd HH:mm:ss"));
		wxTemplateDo.setOrderStatus("已签收");
		wxMessageService.sendTemplate(wxTemplateDo);
	}

	/**
	 * 微信退货已收消息
	 */
	public void pushRefundWxMsg(Long orderInfoId, String url) throws Exception {
		OrderInfo orderInfo = orderRepository.findOne(orderInfoId);
		List<OrderDetail> orderDetails = orderDetailRepository.orderDetail(orderInfo.getOrderInfoId().toString());
		WxUser wxuser = wxUserService.getWxUser(orderInfo.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append(url + "/order/order_all/5?shopId=" + orderInfo.getShopId());
		WxTemplateDo wxTemplateDo = new WxTemplateDo();
		String first = "您所退回的商品，我们已经收到了；";
		String remark = "感谢您的支持与谅解！退款售后查看请点击:)";
		wxTemplateDo = getWxTemplateDo(wxTemplateDo,wxuser,sb.toString(),orderInfo,first,remark,2);
		wxTemplateDo.setOrderNum(orderInfo.getOrderNum());
		int count = 0;
		for(OrderDetail orderDetail:orderDetails){
			count= count + orderDetail.getCount();
		}
		wxTemplateDo.setRefundCount(String.valueOf(count));
		wxTemplateDo.setRefundStatus("快递");
		wxMessageService.sendTemplate(wxTemplateDo);
	}

	/**
	 * 微信退款已收消息
	 */
	public void pushRefundMoneyWxMsg(Long orderInfoId, String url) throws Exception {
		OrderInfo orderInfo = orderRepository.findOne(orderInfoId);
		OrderRefundVo orderRefundVo = orderRefundService.findOrderRefundByOrderInfoId(orderInfoId);
		double refundMoney = orderRefundVo.getRefundMoney();
		WxUser wxuser = wxUserService.getWxUser(orderInfo.getUserId());
		StringBuilder sb = new StringBuilder();
		sb.append(url + "/order/order_all/5?shopId=" + orderInfo.getShopId());
		WxTemplateDo wxTemplateDo = new WxTemplateDo();
		String first = "您的退款,已经通过订单支付原路径退款！";
		String remark = "到账可能会有延迟,请注意【微信支付】退款到账通知,如有误差,请联系客服!退款售后查看请点击:)";
		wxTemplateDo = getWxTemplateDo(wxTemplateDo,wxuser,sb.toString(),orderInfo,first,remark,4);
		wxTemplateDo.setRefundAmount(String.valueOf(refundMoney/100));
		wxTemplateDo.setRefundReason(orderRefundVo.getRefundReason());
		wxMessageService.sendTemplate(wxTemplateDo);
	}

	/**
	 * 超过两天未支付订单，关闭订单
	 */
	public void closeOrder(){
		jdbcRepository.closeOrder();
	}

	@Transactional
	public void confimOrder(){
		jdbcRepository.confimOrder();
	}

	//查看普通订单列表
	private PageItem<OrderInfoVo> queryOrderList(OrderInfoForQueryVo orderVo,int type) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.delivery_note,oi.create_date,oi.status,oi.type,oi.real_price,"
				       + "oi.parent_order_id,oi.remark,oi.user_id,oi.send_fee,oi.seller_note,oi.total_price,oi.good_status,"
				       + "wx.headimgurl,wx.nickname,rr.refund_id,pr.transaction_id,wxadd.user_name,wxadd.phone_number,"
				       + "wxadd.detail_address from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				       + "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
                       + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
		               + "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id where oi.platform=0";
		StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				if(orderVo.getStatus()==1 && type==1){
					sqlCondition.append(JdbcUtil.appendIsNull("oi.parent_order_id"));
				}
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
        sqlCondition.append(JdbcUtil.appendAnd("wxadd.phone_number",orderVo.getConsigneePhone()));
        sqlCondition.append(JdbcUtil.appendLike("wxadd.user_name",orderVo.getConsigneeName()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendAnd("pr.transaction_id",orderVo.getTransactionId()));
        sqlCondition.append(JdbcUtil.appendAnd("rr.refund_id",orderVo.getRefundId()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
        sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderVo.getOrderBeginDate(),orderVo.getOrderEndDate()));
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		if(!StringUtils.isEmpty(orderVo.getType())){
			if(orderVo.getType()==1){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",0));
			}else if(orderVo.getType()==2){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",5));
				sqlCondition.append(JdbcUtil.appendOr("oi.type",6));
			}
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,orderVo.getCurPage(),orderVo.getPageSize());
		return OrderConverter.p2v(pageItem);
	}

	//查询送礼订单
	private PageItem<OrderInfoVo> queryDispatchOrderList(OrderInfoForQueryVo orderVo,int type) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.delivery_note,oi.create_date,oi.status,oi.type,"
				+ "oi.remark,oi.user_id,oi.send_fee,oi.seller_note,oi.total_price,oi.good_status,oi.real_price,"
				+ "wx.headimgurl,wx.nickname,rr.refund_id,pr.transaction_id,wxadd.user_name,wxadd.phone_number,"
				+ "wxadd.detail_address from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
				+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id where oi.parent_order_id is not null";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
		sqlCondition.append(JdbcUtil.appendAnd("wxadd.phone_number",orderVo.getConsigneePhone()));
		sqlCondition.append(JdbcUtil.appendLike("wxadd.user_name",orderVo.getConsigneeName()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
		sqlCondition.append(JdbcUtil.appendAnd("pr.transaction_id",orderVo.getTransactionId()));
		sqlCondition.append(JdbcUtil.appendAnd("rr.refund_id",orderVo.getRefundId()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
		sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderVo.getOrderBeginDate(),orderVo.getOrderEndDate()));
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,orderVo.getCurPage(),orderVo.getPageSize());
		return OrderConverter.p2v(pageItem);
	}

	//pc端发货管理查看订单详情
	public OrderInfoDetailVo queryOrders(Long orderInfoId,int type) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.status,oi.send_fee,oi.delivery_note,oi.create_date,"
				+ "oi.pay_date,oi.take_date,oi.seller_note,oi.remark,oi.update_price_date,oi.total_price,oi.send_date,"
				+ "oi.type,wx.nickname,pr.transaction_id,org.create_good_date,org.storage_date,wxadd.detail_address,"
				+ "ors.agree_refund_date,ors.refund_status,ors.create_refund_date,wxadd.user_name,wxadd.phone_number "
                + "from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
				+ "LEFT JOIN order_refund ors on ors.order_info_id=oi.order_info_id "
                + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				+ "LEFT JOIN pay_record pr on pr.pay_id=oi.pay_id where oi.order_info_id=" + orderInfoId;
		List<Map<String, Object>> ms = jdbcUtil.queryList(sqlList);
		String sqlList1 = "";
		if(type==0){//type为0表示订单管理和发货管理查看详情和物流信息
			sqlList1 = " select od.order_delivery_id,od.create_time,od.trans_company,od.trans_code,od.trans_number,"
					 + "od.delivery_note from order_delivery od where od.order_info_id="+orderInfoId;
		}else{//供货管理查看订单详情和物流信息
			sqlList1 = " select ody.order_delivery_id,ody.create_time,ody.trans_company,ody.trans_code,ody.trans_number,"
					 + "ody.delivery_note from order_delivery ody where ody.order_delivery_id IN (SELECT odd.order_delivery_id"
					 + " FROM order_delivery_detail odd WHERE order_detail_id IN (SELECT od.order_detail_id FROM order_detail od"
					 + " WHERE od.supply_user_id IS NOT NULL AND order_info_id ="+orderInfoId+"))";
		}
		List<Map<String, Object>> ms1 = jdbcUtil.queryList(sqlList1);
		OrderConverter orderConverter = new OrderConverter();
		List<Map<String, Object>> maps = queryOrderDetails(orderInfoId);
		OrderInfoDetailVo orderInfoDetailVos =  orderConverter.m2l(ms,ms1,maps,sendCompanysKd100Service,type);
		return orderInfoDetailVos;
	}

	public OrderInfoDetailVo queryOrdersPc(Long orderInfoId) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.status,oi.send_fee,oi.delivery_note,oi.create_date,"
				       + "oi.pay_date,oi.take_date,oi.seller_note,oi.remark,oi.update_price_date,oi.total_price,wx.nickname,"
				       + "pr.transaction_id,wxadd.user_name,wxadd.phone_number,wxadd.detail_address "
				       + "from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				       + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				       + "LEFT JOIN pay_record pr on pr.pay_id=oi.pay_id where oi.order_info_id=" + orderInfoId;
		List<Map<String, Object>> ms = jdbcUtil.queryList(sqlList);
		OrderInfoDetailVo orderInfoDetailVos = OrderConverter.m2v(ms);
		List<Map<String, Object>> maps = queryOrderDetail(orderInfoId);
		List<OrderDetailVo> orderDetails = OrderConverter.convertOrderDetailVo(maps);
		orderInfoDetailVos.setOrderDetails(orderDetails);
		orderInfoDetailVos.setTotal(orderDetails.size());
		return orderInfoDetailVos;
	}

	//pc端送礼母订单详情查看
	public OrderAndDispatchDeliveryVo queryOrderAndDispatchDelivery(Long orderInfoId) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.pay_date,wx.nickname,od.trans_company,od.trans_code,"
				       + "od.trans_number,wxadd.user_name,wxadd.phone_number,wxadd.detail_address,pr.transaction_id "
				       + "from order_info oi LEFT JOIN order_delivery od on od.order_info_id=oi.order_info_id "
				       + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				       + "LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				       + "LEFT JOIN pay_record pr on pr.pay_id=oi.pay_id "
				       + "where oi.parent_order_id="+orderInfoId;
		List<Map<String, Object>> ms = jdbcUtil.queryList(sqlList);
		OrderAndDispatchDeliveryVo orderAndDispatchDeliveryVo = OrderConverter.convertOrderAndDeliveryVo(ms,sendCompanysKd100Service);
		List<Map<String, Object>> maps = queryOrderDetail(orderInfoId);
		List<OrderDetailVo> orderDetails = OrderConverter.convertOrderDetailVo(maps);
		orderAndDispatchDeliveryVo.setOrderDetails(orderDetails);
		return orderAndDispatchDeliveryVo;
	}

	private List<Map<String, Object>> queryOrderDetails(String orderInfoIds){
		String sqlList = "select od.order_info_id,p.pid,p.NAME,p.pic_square,od.price,od.count,ps.product_spec_id,ps.spec_pic,"
				       + "ps.spec_value_one,ps.spec_value_two,ps.spec_value_three,ore.refuse_reason,ore.refund_status,"
				       + "org.order_good_id,org.good_status FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
				       + "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id LEFT JOIN order_refund ore "
				       + "ON ore.order_info_id=od.order_info_id LEFT JOIN order_refund_goods org ON org.order_info_id=od.order_info_id "
				       + "WHERE od.order_info_id in("+orderInfoIds+")";
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String, Object>> queryOrderDetail(Long orderInfoId){
		String sqlList = "select od.order_detail_id,p.pid,p.name,p.pic_square,od.count,od.price,ps.spec_pic,"
				+ "ps.spec_value_one,ps.spec_value_two,ps.spec_value_three"
				+ " FROM order_detail od LEFT JOIN product p ON p.pid = od.pid"
				+ " LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id"
				+ " WHERE od.order_info_id in("+orderInfoId+")";
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String,Object>> queryDispatchOrder(Long orderInfoId){
		String sqlList = "select oi.order_info_id,oi.status,wxadd.user_name,wxadd.phone_number,"
				       + "wxadd.detail_address,ode.trans_company,ode.trans_code,ode.trans_number,ode.delivery_note "
				       + "from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				       + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				       + "LEFT JOIN order_delivery ode ON ode.order_info_id=oi.order_info_id "
					   + "where oi.parent_order_id="+orderInfoId;
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String, Object>> queryOrderDetailPc(Long orderInfoId){
		String sqlList = "select od.order_detail_id,p.pid,p.name,p.pic_square,od.count,od.price,ps.spec_pic,"
				+ "ps.spec_value_one,ps.spec_value_two,ps.spec_value_three,odd.order_delivery_id,ode.trans_code,"
				+ "ode.trans_number,ode.trans_company,ode.delivery_note FROM order_detail od LEFT JOIN product p ON p.pid = od.pid"
				+ " LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id"
				+ " LEFT JOIN order_delivery_detail odd ON odd.order_detail_id = od.order_detail_id"
				+ " LEFT JOIN 	order_delivery ode ON ode.order_delivery_id=odd.order_delivery_id"
				+ " WHERE od.order_info_id in("+orderInfoId+")";
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String, Object>> queryOrderDetailSupplyPc(Long orderInfoId){
		String sqlList = "select od.order_detail_id,p.pid,p.name,p.pic_square,od.count,od.price,ps.spec_pic,"
				+ "ps.spec_value_one,ps.spec_value_two,ps.spec_value_three,odd.order_delivery_id,ode.trans_code,"
				+ "ode.trans_number,ode.trans_company,ode.delivery_note FROM order_detail od LEFT JOIN product p ON p.pid = od.pid"
				+ " LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id"
				+ " LEFT JOIN order_delivery_detail odd ON odd.order_detail_id = od.order_detail_id"
				+ " LEFT JOIN 	order_delivery ode ON ode.order_delivery_id=odd.order_delivery_id"
				+ " WHERE od.supply_user_id is not NULL and od.order_info_id in("+orderInfoId+")";
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String, Object>> queryOrderDetails(Long orderInfoId){
		String sqlList = " select od.order_detail_id,p.pid,p.name,p.pic_square,od.count,od.price,od.supply_user_id,"
				       + " ps.spec_pic,ps.spec_value_one,ps.spec_value_two,ps.spec_value_three,odd.order_delivery_id"
				       + " FROM order_detail od LEFT JOIN product p ON p.pid = od.pid"
					   + " LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id"
				       + " LEFT JOIN order_delivery_detail odd ON odd.order_detail_id=od.order_detail_id"
				       + " WHERE od.order_info_id in("+orderInfoId+")";
		return jdbcUtil.queryList(sqlList);
	}

	public List<Map<String, Object>> querySendDetails(Long orderInfoId){
        String sqlList = " select p.pic_square,ps.spec_pic,odd.order_delivery_id"
                + " FROM order_detail od LEFT JOIN product p ON p.pid = od.pid"
                + " LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id"
                + " LEFT JOIN order_delivery_detail odd ON odd.order_detail_id=od.order_detail_id"
                + " WHERE od.order_info_id in("+orderInfoId+")";
        return jdbcUtil.queryList(sqlList);
    }

	public void pushPayHxMsg(WxUser user,Long orderInfoId) throws Exception {
		String content = "订单支付成功！）" + "\r\n订单号：";
		pushHxMsg(user,orderInfoId,content);
	}


	/**
	 * 环信接收推送消息（pc端操作）
	 */
	private void pushHxMsg(JmUserSession user, Long orderInfoId,String content) throws Exception{

	}

	/**WxUser user
	 * 环信接收推送消息（app端操作）
	 */
	private void pushHxMsg(WxUser user, Long orderInfoId,String content) throws Exception{

	}

    public OrderDetailVo getOrderInfoVo(Map<String, Object> map,OrderInfoVo orderInfoVo,OrderDetailVo orderDetailVo){
		if (!StringUtils.isEmpty(map.get("name"))) {
			orderDetailVo.setName(String.valueOf(map.get("name")));
		} else {
			orderDetailVo.setName("");
		}

		if (!StringUtils.isEmpty(map.get("spec_pic"))) {
			orderDetailVo.setPic(ImgUtil.appendUrl(map.get("spec_pic").toString(), 100));
		} else if(!StringUtils.isEmpty(map.get("pic_square"))){
			orderDetailVo.setPic(ImgUtil.appendUrl(map.get("pic_square").toString(), 100));
		} else {
			orderDetailVo.setPic("");
		}

		if (!StringUtils.isEmpty(map.get("pid"))) {
			orderDetailVo.setPid(Integer.valueOf(map.get("pid").toString()));
		} else {
			orderDetailVo.setPid(null);
		}
		orderDetailVo.setCount(Integer.valueOf(map.get("count").toString()));
		orderDetailVo.setPrice(Integer.valueOf(map.get("price").toString()));
		if (!StringUtils.isEmpty(map.get("product_spec_id"))) {
			orderDetailVo.setProductSpecId(Integer.valueOf(map.get("product_spec_id").toString()));
		}
		if (!StringUtils.isEmpty(map.get("spec_value_one"))) {
			orderDetailVo.setSpecValueOne(String.valueOf(map.get("spec_value_one")));
		} else {
			orderDetailVo.setSpecValueOne("");
		}
		if (!StringUtils.isEmpty(map.get("spec_value_two"))) {
			orderDetailVo.setSpecValueTwo(String.valueOf(map.get("spec_value_two")));
		} else {
			orderDetailVo.setSpecValueTwo("");
		}
		if (!StringUtils.isEmpty(map.get("spec_value_three"))) {
			orderDetailVo.setSpecValueThree(String.valueOf(map.get("spec_value_three")));
		} else {
			orderDetailVo.setSpecValueThree("");
		}
		if (!StringUtils.isEmpty(orderInfoVo.getTotalCount())) {
			orderInfoVo.setTotalCount(orderInfoVo.getTotalCount() + orderDetailVo.getCount());
		} else {
			orderInfoVo.setTotalCount(orderDetailVo.getCount());
		}
		if (!StringUtils.isEmpty(map.get("refund_status"))) {
			orderDetailVo.setRefundStatus(Integer.valueOf(map.get("refund_status").toString()));
		} else {
			orderDetailVo.setRefundStatus(6);
		}
		if (!StringUtils.isEmpty(map.get("refuse_reason"))) {
			orderDetailVo.setRefuseReason(String.valueOf(map.get("refuse_reason")));
		} else {
			orderDetailVo.setRefuseReason("");
		}
		if (!StringUtils.isEmpty(map.get("order_good_id"))) {
			orderDetailVo.setOrderGoodId(Integer.valueOf(map.get("order_good_id").toString()));
		} else {
			orderDetailVo.setOrderGoodId(0);
		}
		if (!StringUtils.isEmpty(map.get("good_status"))) {
			orderDetailVo.setGoodStatus(Integer.valueOf(map.get("good_status").toString()));
		} else {
			orderDetailVo.setGoodStatus(6);
		}
		if(!StringUtils.isEmpty(map.get("supply_user_id"))){
			orderDetailVo.setSupplyUserId(Integer.valueOf(map.get("supply_user_id").toString()));
		}else {
			orderDetailVo.setSupplyUserId(0);
		}
		if(!StringUtils.isEmpty(map.get("supply_user_name"))){
			orderDetailVo.setSupplyUserName(map.get("supply_user_name").toString());
		}else {
			orderDetailVo.setSupplyUserName("");
		}
    	return orderDetailVo;
	}

    private WxTemplateDo getWxTemplateDo(WxTemplateDo wxTemplateDo,WxUser wxuser,String url,OrderInfo orderInfo,OrderDelivery orderDelivery,String first,String remark,int type ){
		wxTemplateDo.setTouser(wxuser.getOpenid());
		wxTemplateDo.setAppid(wxuser.getAppid());
		wxTemplateDo.setType(type);
		wxTemplateDo.setUrl(url);
		wxTemplateDo.setFirst(first);
		wxTemplateDo.setRemark(remark);
		wxTemplateDo.setOrderNum(orderInfo.getOrderNum());
		if(!StringUtils.isEmpty(orderDelivery.getTransCompany())){
			wxTemplateDo.setLogisticsName(orderDelivery.getTransCompany());
		}else{
			wxTemplateDo.setLogisticsName("送礼单无物流信息");
		}
		if(!StringUtils.isEmpty(orderDelivery.getTransNumber())){
			wxTemplateDo.setLogisticsNum(orderDelivery.getTransNumber());
		}else{
			wxTemplateDo.setLogisticsNum("送礼单无物流信息");
		}
		return wxTemplateDo;
	}

	private WxTemplateDo getWxTemplateDo(WxTemplateDo wxTemplateDo,WxUser wxuser,String url,OrderInfo orderInfo,String first,String remark,int type ){
		wxTemplateDo.setTouser(wxuser.getOpenid());
		wxTemplateDo.setAppid(wxuser.getAppid());
		wxTemplateDo.setType(type);
		wxTemplateDo.setUrl(url);
		wxTemplateDo.setFirst(first);
		wxTemplateDo.setRemark(remark);
		wxTemplateDo.setOrderNum(orderInfo.getOrderNum());
		return wxTemplateDo;
	}

	/**
	 *订单导出功能
	 * @param items
	 */
	public void exportOrder(List<Map<String, Object>> items,HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition","attachment;filename=order.xls");
		String headers = "订单编号,支付流水号,退款流水号,商品名称,单价,数量,规格1,规格2,规格3,订单类型,下单时间,订单状态,实付金额,运费,买家昵称,收货姓名,收货手机,收货地址,买家留言,卖家留言";
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		csvToolUtil.exportCsv(items,headers,"订单报表-"+df.format(new Date()),response);
	}

	/**************************************************订单导出功能用到的查询*************************************************/
	public List<Map<String, Object>> exportOrder(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		List<Map<String, Object>> maps = exportOrderList(orderVo,type);
		return doOrderMaps(maps);
	}

	private List<Map<String, Object>> doOrderMaps(List<Map<String, Object>> maps){
		for(Map<String, Object> map:maps){
			if(!StringUtils.isEmpty(map.get("nickname"))){
				map.put("nickname",Base64Util.getFromBase64(map.get("nickname").toString()));
			}
			if(!StringUtils.isEmpty(map.get("type"))){
				if(map.get("type").equals(0)){
					map.put("type","标准单");
				}else if(map.get("type").equals(5)){
					map.put("type","礼品单");
				}else if(map.get("type").equals(6)){
					map.put("type","礼品单");
				}else{
					map.put("type","标准单");
				}
			}
			if(!StringUtils.isEmpty(map.get("status"))){
				if(map.get("status").equals(0)){
					map.put("status","待付款");
				}else if(map.get("status").equals(1)){
					map.put("status","待发货");
				}else if(map.get("status").equals(2)){
					map.put("status","已发货");
				}else if(map.get("status").equals(3)){
					map.put("status","已收货");
				}else if(map.get("status").equals(4)){
					map.put("status","订单关闭");
				}else if(map.get("status").equals(5)){
					if(!StringUtils.isEmpty(map.get("refund_status"))){
						if(map.get("refund_status").equals(0)){
							map.put("status","申请退款");
						}else if(map.get("refund_status").equals(1)){
							map.put("status","已退款");
						}else if(map.get("refund_status").equals(2)){
							map.put("status","拒绝退款");
						}
					}else{
						if(!StringUtils.isEmpty(map.get("good_status"))&&map.get("good_status").equals(1)){
							map.put("status","退货中");
						}
					}
				}
			}
			if(!StringUtils.isEmpty(map.get("price"))){
				Double price = Double.parseDouble(String.valueOf(map.get("price")))/100;
				map.put("price",price);
			}
			if(!StringUtils.isEmpty(map.get("real_price"))){
				Double realPrice = Double.parseDouble(String.valueOf(map.get("real_price")))/100;
				map.put("real_price",realPrice);
			}
			if(!StringUtils.isEmpty(map.get("send_fee"))){
				Double sendFee = Double.parseDouble(String.valueOf(map.get("send_fee")))/100;
				map.put("send_fee",sendFee);
			}
			map.put("good_status","");
			map.put("refund_status","");
		}
		return maps;
	}

	private List<Map<String, Object>> exportOrderList(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		String sqlList = "select oi.order_num,pr.transaction_id,rr.refund_id,p.NAME,od.price,od.count,"
				+ "ps.spec_value_one,ps.spec_value_two,ps.spec_value_three,oi.type,oi.create_date,oi.status,"
				+ "oi.real_price,oi.send_fee,wx.nickname,wxadd.user_name,wxadd.phone_number,wxadd.detail_address,"
				+ "oi.remark,oi.seller_note,ors.refund_status,oi.good_status "
				+ "FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
				+ "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
				+ "LEFT JOIN order_info oi on oi.order_info_id=od.order_info_id "
				+ "LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
                + "LEFT JOIN order_refund ors on ors.order_info_id=oi.order_info_id "
				+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
				+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id where 1=1";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
		sqlCondition.append(JdbcUtil.appendAnd("wxadd.phone_number",orderVo.getConsigneePhone()));
		sqlCondition.append(JdbcUtil.appendLike("wxadd.user_name",orderVo.getConsigneeName()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
		sqlCondition.append(JdbcUtil.appendAnd("pr.transaction_id",orderVo.getTransactionId()));
		sqlCondition.append(JdbcUtil.appendAnd("rr.refund_id",orderVo.getRefundId()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if(!StringUtils.isEmpty(orderVo.getOrderBeginDate()) && !StringUtils.isEmpty(orderVo.getOrderEndDate())){
			java.util.Date orderBeginDate=sdf.parse(orderVo.getOrderBeginDate().toString());
			java.util.Date OrderEndDate=sdf.parse(orderVo.getOrderEndDate().toString());
			sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderBeginDate,OrderEndDate));
		}
		if(!StringUtils.isEmpty(orderVo.getType())){
			if(orderVo.getType()==1){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",0));
			}else if(orderVo.getType()==2){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",5));
				sqlCondition.append(JdbcUtil.appendOr("oi.type",6));
			}
		}
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		return jdbcUtil.queryList(sqlList+sqlCondition);
	}

	/**
	 *订单导出功能
	 * @param items
	 */
	public void exportDelivery(List<Map<String, Object>> items,HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition","attachment;filename=order.xls");
		String headers = "订单编号,支付流水号,商品名称,单价,数量,规格1,规格2,规格3,订单类型,下单时间,订单处理,买家昵称,收货姓名,收货地址,收货手机,买家留言,卖家留言";
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		csvToolUtil.exportCsv(items,headers,"订单报表-"+df.format(new Date()),response);
	}

	public List<Map<String, Object>> exportDelivery(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		List<Map<String, Object>> maps = exportDeliveryList(orderVo,type);
		return doDeliveryMaps(maps);
	}

	private List<Map<String, Object>> exportDeliveryList(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		String sqlList = "select oi.order_num,pr.transaction_id,p.NAME,od.price,od.count,ps.spec_value_one,"
				+ "ps.spec_value_two,ps.spec_value_three,oi.type,oi.create_date,oi.status,wx.nickname,wxadd.user_name,"
				+ "wxadd.detail_address,wxadd.phone_number,oi.remark,oi.seller_note,org.good_status "
				+ "FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
				+ "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
				+ "LEFT JOIN order_info oi on oi.order_info_id=od.order_info_id "
				+ "LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				+ "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
				+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
				+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id where 1=1";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
		sqlCondition.append(JdbcUtil.appendAnd("wxadd.phone_number",orderVo.getConsigneePhone()));
		sqlCondition.append(JdbcUtil.appendLike("wxadd.user_name",orderVo.getConsigneeName()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if(!StringUtils.isEmpty(orderVo.getOrderBeginDate()) && !StringUtils.isEmpty(orderVo.getOrderEndDate())){
			java.util.Date orderBeginDate=sdf.parse(orderVo.getOrderBeginDate().toString());
			java.util.Date OrderEndDate=sdf.parse(orderVo.getOrderEndDate().toString());
			sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderBeginDate,OrderEndDate));
		}
		if(!StringUtils.isEmpty(orderVo.getType())){
			if(orderVo.getType()==1){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",0));
			}else if(orderVo.getType()==2){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",5));
				sqlCondition.append(JdbcUtil.appendOr("oi.type",6));
			}
		}
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		return jdbcUtil.queryList(sqlList+sqlCondition);
	}


	/**
	 *订单导出功能
	 * @param items
	 */
	public void exportSupply(List<Map<String, Object>> items,HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition","attachment;filename=order.xls");
		String headers = "订单编号,收款流水号,商品名称,单价,数量,规格1,规格2,规格3,订单类型,下单时间,代发店铺,收货地址,订单处理,买家昵称,买家留言,卖家留言";
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		csvToolUtil.exportCsv(items,headers,"供货订单报表-"+df.format(new Date()),response);
	}

	public List<Map<String, Object>> exportSupply(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		List<Map<String, Object>> maps = exportSupplyList(orderVo,type);
		return doDeliveryMaps(maps);
	}

	private List<Map<String, Object>> exportSupplyList(OrderInfoForExportVo orderVo,int type) throws IOException, ParseException {
		String sqlList = "select oi.order_num,pr.transaction_id,p.NAME,od.price,od.count,ps.spec_value_one,"
				+ "ps.spec_value_two,ps.spec_value_three,oi.type,oi.create_date,s.shop_name,wxadd.detail_address,"
				+ "oi.status,wx.nickname,oi.remark,oi.seller_note,org.good_status "
				+ "FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
				+ "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
				+ "LEFT JOIN order_info oi on oi.order_info_id=od.order_info_id "
				+ "LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
				+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
				+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				+ "LEFT JOIN shop s ON s.shop_id=oi.shop_id "
				+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id "
				+ "where od.supply_user_id IS NOT NULL and od.supply_user_id!=0";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
		sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if(!StringUtils.isEmpty(orderVo.getOrderBeginDate()) && !StringUtils.isEmpty(orderVo.getOrderEndDate())){
			java.util.Date orderBeginDate=sdf.parse(orderVo.getOrderBeginDate().toString());
			java.util.Date OrderEndDate=sdf.parse(orderVo.getOrderEndDate().toString());
			sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderBeginDate,OrderEndDate));
		}
		if(!StringUtils.isEmpty(orderVo.getType())){
			if(orderVo.getType()==1){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",0));
			}else if(orderVo.getType()==2){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",5));
				sqlCondition.append(JdbcUtil.appendOr("oi.type",6));
			}
		}
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		return jdbcUtil.queryList(sqlList+sqlCondition);
	}

	private List<Map<String, Object>> doDeliveryMaps(List<Map<String, Object>> maps){
		for(Map<String, Object> map:maps){
			if(!StringUtils.isEmpty(map.get("nickname"))){
				map.put("nickname",Base64Util.getFromBase64(map.get("nickname").toString()));
			}
			if(!StringUtils.isEmpty(map.get("type"))){
				if(map.get("type").equals(0)){
					map.put("type","标准单");
				}else if(map.get("type").equals(5)){
					map.put("type","礼品单");
				}else if(map.get("type").equals(6)){
					map.put("type","礼品单");
				}else{
					map.put("type","标准单");
				}
			}
			if(!StringUtils.isEmpty(map.get("status"))){
				if(map.get("status").equals(1)){
					map.put("status","待发货");
				}else if(map.get("status").equals(2)){
					map.put("status","已发货");
				}else if(map.get("status").equals(3)){
					map.put("status","已收货");
				}else if(map.get("status").equals(5)){
					if(!StringUtils.isEmpty(map.get("good_status"))){
						if(map.get("good_status").equals(0)){
							map.put("status","退货中");
						}else if(map.get("good_status").equals(1)){
							map.put("status","已入库");
						}
					}
				}
			}
			if(!StringUtils.isEmpty(map.get("price"))){
				Double price = Double.parseDouble(String.valueOf(map.get("price")))/100;
				map.put("price",price);
			}
			map.put("good_status","");
			map.put("refund_status","");
		}
		return maps;
	}

	public void updateOrderInfoParentOrderId(List<Long> orderInfoId){
		Date sendTime = new Date();
		orderRepository.updateOrderInfo(orderInfoId,sendTime);
	}

	/**
	 *供货清单导出功能（商家角色）
	 * @param items
	 */
	public void exportSupplyOrder(List<Map<String, Object>> items,HttpServletResponse response,Integer roleId) throws IOException {
		response.setHeader("Content-Disposition","attachment;filename=order.xls");
		String headers = "";
		if(roleId==2){
			headers = "订单编号,商品名称,供货价,数量,小计,发货时间,订单状态,供货商";
		}else if(roleId==9){
			headers = "订单编号,商品名称,供货价,数量,小计,发货时间,订单状态,代发店铺";
		}
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		csvToolUtil.exportCsv(items,headers,"供货清单-"+df.format(new Date()),response);
	}

	/**
	 *供货清单导出功能（商家角色）
	 *
	 */
	public List<Map<String, Object>> exportSupplyOrder(SupplyForQueryVo supplyForQueryVo,int type,Integer roleId) throws IOException, ParseException {
		List<Map<String, Object>> maps = exportSupplyOrderList(supplyForQueryVo,type,roleId);
		return doDeliveryMaps(maps);
	}

	/**
	 *供货清单导出功能（商家角色）
	 *
	 */
	private List<Map<String, Object>> exportSupplyOrderList(SupplyForQueryVo supplyForQueryVo,int type,Integer roleId) throws IOException, ParseException {
		String sqlList = returnSql(roleId);
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",supplyForQueryVo.getShopId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.status",supplyForQueryVo.getStatus()));
		sqlCondition.append(JdbcUtil.appendAnd("u.user_name",supplyForQueryVo.getShopUserName()));
		sqlCondition.append(JdbcUtil.appendAnd("p.name",supplyForQueryVo.getProductName()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.send_date",supplyForQueryVo.getSupplySettlementBeginDate(),supplyForQueryVo.getSupplySettlementEndDate()));
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		return jdbcUtil.queryList(sqlList+sqlCondition);
	}

	private String returnSql(Integer roleId){
		String sqlList = "";
		if(roleId==2){
			sqlList = "select oi.order_num,p.name,od.count,od.supply_price/100,od.count*od.supply_price/100,"
					+ "oi.send_date,oi.status,u.user_name from order_info oi "
					+ "LEFT JOIN order_detail od on od.order_info_id=oi.order_info_id "
					+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
					+ "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
					+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
					+ "LEFT JOIN shop s ON s.shop_id=oi.shop_id "
					+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id "
					+ "LEFT JOIN user u ON u.user_id=od.supply_user_id "
					+ "LEFT JOIN product p ON p.pid = od.pid "
					+ "where od.supply_user_id IS NOT NULL and od.supply_user_id!=0 "
					+ "and oi.status!=4 and oi.status!=0";
		}else if (roleId==9){
			sqlList = "select oi.order_num,p.name,od.count,od.supply_price/100,od.count*od.supply_price/100,"
					+ "oi.send_date,oi.status,s.shop_name from order_info oi "
					+ "LEFT JOIN order_detail od on od.order_info_id=oi.order_info_id "
					+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
					+ "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
					+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
					+ "LEFT JOIN shop s ON s.shop_id=oi.shop_id "
					+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id "
					+ "LEFT JOIN product p ON p.pid = od.pid "
					+ "where od.supply_user_id IS NOT NULL and od.supply_user_id!=0 "
					+ "and oi.status!=4 and oi.status!=0";
		}
		return sqlList;
	}
	@ApiOperation("报表收支明细-查询订单详情 cj-add-2017-04-11")
	public OrderInfoDetailVo queryOrdersPcForm(String orderNum) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.status,oi.send_fee,oi.delivery_note,oi.create_date,"
				+ "oi.pay_date,oi.take_date,oi.seller_note,oi.remark,oi.update_price_date,oi.total_price,wx.nickname,"
				+ "pr.transaction_id,wxadd.user_name,wxadd.phone_number,wxadd.detail_address "
				+ "from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
				+ "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
				+ "LEFT JOIN pay_record pr on pr.pay_id=oi.pay_id where oi.order_num=" + orderNum;
		List<Map<String, Object>> ms = jdbcUtil.queryList(sqlList);
		OrderInfoDetailVo orderInfoDetailVos = OrderConverter.m2v(ms);
		Long orderInfoId = orderInfoDetailVos.getOrderInfoId();
		List<Map<String, Object>> maps = queryOrderDetail(orderInfoId);
		List<OrderDetailVo> orderDetails = OrderConverter.convertOrderDetailVo(maps);
		orderInfoDetailVos.setOrderDetails(orderDetails);
		orderInfoDetailVos.setTotal(orderDetails.size());
		return orderInfoDetailVos;
	}


	//*************************************************微博订单*********************************************************//
	//查询微信订单信息
	public PageItem<OrderInfoVo> queryWbOrder(OrderInfoForQueryVo orderVo,int type) throws IOException {
		PageItem<OrderInfoVo> pageItem = queryWbOrderList(orderVo,type);
		PageItem<OrderInfoVo> pageItemRes = new PageItem<>();
		List<OrderInfoVo> orderInfoVos = pageItem.getItems();
		if (orderInfoVos == null || orderInfoVos.size() == 0) {
			pageItemRes.setCount(0);
			pageItemRes.setItems(orderInfoVos);
			return pageItemRes;
		}
		String orderInfoIds = "";
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			orderInfoIds += orderInfoVo.getOrderInfoId() + ",";
		}
		orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
		List<Map<String, Object>> orderDetails = queryOrderDetails(orderInfoIds);
		//合并订单列表跟 订单详情列表
		for (OrderInfoVo orderInfoVo : orderInfoVos) {
			for (Map<String, Object> map : orderDetails) {
				if (orderInfoVo.getOrderInfoId().equals(map.get("order_info_id"))) {
					OrderDetailVo orderDetailVo = new OrderDetailVo();
					orderDetailVo = getOrderInfoVo(map,orderInfoVo,orderDetailVo);
					orderInfoVo.getOrderDetails().add(orderDetailVo);
				}
			}
		}
		pageItemRes.setCount(Integer.parseInt(pageItem.getCount() + ""));
		pageItemRes.setItems(orderInfoVos);
		return pageItemRes;
	}

	//查看微博订单列表
	public PageItem<OrderInfoVo> queryWbOrderList(OrderInfoForQueryVo orderVo,int type) throws IOException {
		String sqlList = "select oi.order_info_id,oi.order_num,oi.delivery_note,oi.create_date,oi.status,oi.type,oi.real_price,"
				+ "oi.parent_order_id,oi.remark,oi.uid,oi.send_fee,oi.seller_note,oi.total_price,oi.good_status,"
				+ "oi.platform,wb.headimgurl,wb.nickname,rr.refund_id,pr.transaction_id,wbadd.user_name,wbadd.phone_number,"
				+ "wbadd.detail_address from order_info oi LEFT JOIN wb_user wb on wb.id=oi.uid "
				+ "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
				+ "LEFT JOIN wb_user_address wbadd on wbadd.id=oi.user_addr_id "
				+ "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id where oi.platform=1";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
		if(!StringUtils.isEmpty(orderVo.getStatus())){
			if (11!=orderVo.getStatus()) {
				if(orderVo.getStatus()==1 && type==1){
					sqlCondition.append(JdbcUtil.appendIsNull("oi.parent_order_id"));
				}
				sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
			}else{
				if(type==2){
					sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
				}
			}
		}
		sqlCondition.append(JdbcUtil.appendAnd("wbadd.phone_number",orderVo.getConsigneePhone()));
		sqlCondition.append(JdbcUtil.appendLike("wbadd.user_name",orderVo.getConsigneeName()));
		sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
		sqlCondition.append(JdbcUtil.appendAnd("pr.transaction_id",orderVo.getTransactionId()));
		sqlCondition.append(JdbcUtil.appendAnd("rr.refund_id",orderVo.getRefundId()));
		if(!StringUtils.isEmpty(orderVo.getNickname())) {
			sqlCondition.append(JdbcUtil.appendLike("wb.nickname", Base64Util.enCoding(orderVo.getNickname())));
		}
		sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderVo.getOrderBeginDate(),orderVo.getOrderEndDate()));
		if (orderVo.getGoodStatus() == 1) {
			//退货管理条件查询
			sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
		}
		if(!StringUtils.isEmpty(orderVo.getType())){
			if(orderVo.getType()==1){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",0));
			}else if(orderVo.getType()==2){
				sqlCondition.append(JdbcUtil.appendAnd("oi.type",5));
				sqlCondition.append(JdbcUtil.appendOr("oi.type",6));
			}
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,orderVo.getCurPage(),orderVo.getPageSize());
		return OrderConverter.p2v(pageItem);
	}

	public OrderInfoVo findOrderAndDetailById(Long orderId) throws IllegalAccessException, IOException, InstantiationException {
		OrderInfoVo order = findOrderById(orderId);
		order.setNickname(Base64Util.getFromBase64(order.getNickname()));
		String sql ="SELECT p.*,p.pic_square as pic, ps.spec_value_one, ps.spec_value_two, ps.spec_value_three, ps.spec_price, ps.spec_pic,D.count,D.product_spec_id FROM order_detail D " +
				"LEFT JOIN product p ON p.pid = D.pid " +
				"LEFT JOIN product_spec ps ON D.product_spec_id = ps.product_spec_id " +
				"WHERE  D.order_info_id = "+orderId;
		List<OrderDetailVo> orderDetails =  jdbcUtil.queryList(sql,OrderDetailVo.class);
		for(OrderDetailVo detailVo:orderDetails){
			detailVo.setPic(ImgUtil.appendUrl(detailVo.getPic()));
			if(detailVo.getSpecPic()==null||detailVo.getSpecPic().equals("")){
				continue;
			}
			detailVo.setSpecPic(ImgUtil.appendUrl(detailVo.getSpecPic()));

		}
		order.setOrderDetails(orderDetails);
		return order;
	}
}
