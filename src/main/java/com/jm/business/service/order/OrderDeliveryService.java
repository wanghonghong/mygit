/**
 * 
 */
package com.jm.business.service.order;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderDeliveryDetailRepository;
import com.jm.repository.jpa.order.OrderDeliveryRepository;
import com.jm.repository.jpa.order.OrderDetailRepository;
import com.jm.repository.jpa.order.OrderRepository;
import com.jm.repository.po.order.OrderDelivery;
import com.jm.repository.po.order.OrderDeliveryDetail;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.wx.SMSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>订单发货信息业务处理</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */

@Service
public class OrderDeliveryService {

	@Autowired
	private OrderDeliveryRepository orderDeliveryRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private JdbcUtil jdbcUtil;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDeliveryDetailRepository orderDeliveryDetailRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private OrderDetailService orderDetailService;

	/***
	 *
	 *<p>保存订单发货信息</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年6月6日
	 */
	@Transactional
	public JmMessage createOrderDelivery(Long orderInfoId, OrderDeliveryCreateVo createvo, JmUserSession user) throws Exception {
		List<SendsVo> sendsVoList = createvo.getOrderDetailAndSendsVos();
		List<OrderDeliveryDetail> lst = new ArrayList<>();
		List<OrderDetail> orderDetails = orderDetailService.queryOrderDetail(orderInfoId);
		OrderInfoVo orderInfoVo = orderService.findOrderById(orderInfoId);
		for(SendsVo sendsVo:sendsVoList){
			OrderDelivery orderDelivery = new OrderDelivery();
			orderDelivery.setOrderInfoId(orderInfoId);
			orderDelivery.setTransCompany(sendsVo.getTransCompany());
			orderDelivery.setTransCode(sendsVo.getTransCode());
			orderDelivery.setTransNumber(sendsVo.getTransNumber());
			orderDelivery.setDeliveryNote(sendsVo.getDeliveryNote());
			orderDelivery.setStatus(sendsVo.getStatus());
			OrderDelivery newOrderDelivery = null;
			//return ;
			String[] orderDetailList= sendsVo.getOrderDetailId();
			String orderDetailIds = "";
			for (int i=0;i<orderDetailList.length;i++) {
				orderDetailIds += orderDetailList[i]+ ",";
			}
			if(orderDetailIds.length()>0){
				orderDetailIds = orderDetailIds.substring(0, orderDetailIds.length() - 1);
			}
			//只有订单status=1的才能发货
			if(orderInfoVo.getStatus()==1){
				//保存发货的包裹信息
				List<OrderDeliveryDetail> list = orderDeliveryDetailRepository.findOrderDeliveryDetailByOrderDetailId(orderDetailIds);
				//防止订单重复发货，根据orderDetailId判断是否已经发货
				if(list.size()==0){
					newOrderDelivery = orderDeliveryRepository.save(orderDelivery);
					//判断是否有把商品全部提交，如果有一次性提交全部订单，则表示单包裹，否则表示多包裹。
					if(orderDetails.size()!=orderDetailList.length){
						for(int i=0;i<orderDetailList.length;i++){
							OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
							orderDeliveryDetail.setOrderDeliveryId(newOrderDelivery.getOrderDeliveryId());
							orderDeliveryDetail.setOrderDetailId(Long.parseLong(orderDetailList[i]));
							lst.add(orderDeliveryDetail);
						}
					}
				}else{
					return new JmMessage(1,"1");
				}
			}else{
				return new JmMessage(1,"1");
			}
		}
		if(lst.size()>0) {
		    //保存多包裹与商品的关系信息
			orderDeliveryDetailRepository.save(lst);
			Date sendTime = new Date();
			int count = orderRepository.updateOrder(orderInfoId,sendTime);
			if(count>0){
				String url = Constant.DOMAIN;
				orderService.pushDeliverWxMsg(orderInfoId,url);
			}
		}else{
            //处理单包裹
			Date sendTime = new Date();
			orderRepository.updateOrder1(orderInfoId,sendTime);
			String url = Constant.DOMAIN;
			orderService.pushDeliverWxMsg(orderInfoId,url);
		}
		return new JmMessage(0,"0");
	}

	/***
	 *
	 *<p>保存订单发货信息</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2017年2月16日
	 */
	@Transactional
	public JmMessage createDispatchOrderDelivery(OrderDeliveryDispatchCreateVo orderDeliveryDispatchCreateVo) throws Exception {
		List<OrderDelivery> lst = new ArrayList<>();
		if(StringUtils.isEmpty(orderDeliveryDispatchCreateVo.getGiverName())){
			return new JmMessage(5,"没有送礼人！");
		}
		if(StringUtils.isEmpty(orderDeliveryDispatchCreateVo.getProductName())){
			return new JmMessage(6,"没有商品名！");
		}
		List<SendsDispatchVo> sendsVoList = orderDeliveryDispatchCreateVo.getSendsDispatchVos();
		List<SendMsgOrderDispatchCo> sendMsgOrderDispatchCoList = new ArrayList<>();
		//录入发货信息
		List<Long> orderInfoIds = new ArrayList<>();
		for(SendsDispatchVo sendsDispatchVo:sendsVoList){
			if(StringUtils.isEmpty(sendsDispatchVo.getTransCode())){
				return new JmMessage(2,"勾选包裹没有选择物流公司！");
			}
			if(StringUtils.isEmpty(sendsDispatchVo.getTransNumber())){
				return new JmMessage(3,"勾选包裹没有填写物流单号！");
			}
			OrderDelivery orderDelivery = new OrderDelivery();
			orderDelivery.setOrderInfoId(sendsDispatchVo.getOrderInfoId());
			orderDelivery.setTransCompany(sendsDispatchVo.getTransCompany());
			orderDelivery.setTransCode(sendsDispatchVo.getTransCode());
			orderDelivery.setTransNumber(sendsDispatchVo.getTransNumber());
			orderDelivery.setDeliveryNote(sendsDispatchVo.getDeliveryNote());
			orderDelivery.setStatus(sendsDispatchVo.getStatus());
			SendMsgOrderDispatchCo sendMsgOrderDispatchCo = new SendMsgOrderDispatchCo();
			sendMsgOrderDispatchCo.setGiverName(orderDeliveryDispatchCreateVo.getGiverName());
			sendMsgOrderDispatchCo.setDetailAddress(sendsDispatchVo.getDetailAddress());
			sendMsgOrderDispatchCo.setPhoneNumber(sendsDispatchVo.getPhoneNumber());
			sendMsgOrderDispatchCo.setProductName(orderDeliveryDispatchCreateVo.getProductName());
			sendMsgOrderDispatchCo.setUserName(sendsDispatchVo.getUserName());
			sendMsgOrderDispatchCo.setTransName(sendsDispatchVo.getTransCompany());
			sendMsgOrderDispatchCo.setTransNumber(sendsDispatchVo.getTransNumber());
			lst.add(orderDelivery);
			sendMsgOrderDispatchCoList.add(sendMsgOrderDispatchCo);
			orderInfoIds.add(sendsDispatchVo.getOrderInfoId());
		}

		//送出总礼品子单总的个数
		List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderInfoId(orderDeliveryDispatchCreateVo.getOrderInfoId());
		if(orderDetails.size()>0){
			//保存发货信息
			orderDeliveryRepository.save(lst);
			if(orderInfoIds.size()>0){
				//更新送礼订单的子订单状态
				orderService.updateOrderInfoParentOrderId(orderInfoIds);
			}
		}
		//已发货的子单的个数
		List<OrderInfo> orderInfoList = orderRepository.queryOrderDispatch(orderDeliveryDispatchCreateVo.getOrderInfoId());

		//送出总礼品子单总的个数 = 已发货的子单的个数
		if(orderDetails.get(0).getCount().equals(orderInfoList.size())){
			Date sendTime = new Date();
			orderRepository.updateOrder1(orderDeliveryDispatchCreateVo.getOrderInfoId(),sendTime);
		}
		return sendMsg(sendMsgOrderDispatchCoList);
	}
	
	public OrderDelivery createOrderDelivery(OrderDelivery orderDelivery){
		return orderDeliveryRepository.save(orderDelivery);
	}
	
	/***
	 * 
	 *<p>根据订单号查询发货信息</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年6月6日
	 */
	public List<OrderDelivery> getOrderDelivery(Long orderInfoId){
		return orderDeliveryRepository.findOrderDeliveryByOrderInfoId(orderInfoId);
	}

	/***
	 *
	 *<p>根据发货单号查询发货信息</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年6月6日
	 */
	public OrderDelivery getOrderDelivery(Integer orderDeliveryId){
		return orderDeliveryRepository.findOrderDeliveryByOrderDeliveryId(orderDeliveryId);
	}

	private JmMessage sendMsg(List<SendMsgOrderDispatchCo> sendMsgOrderDispatchCoList){
		//为发货的订单把物流信息通过短信通知买家
		for(SendMsgOrderDispatchCo sendMsgOrderDispatchCo:sendMsgOrderDispatchCoList){
			SMSUtil.sendMsg("【聚米为谷】"+sendMsgOrderDispatchCo.getGiverName() + "送您的【"
					+ sendMsgOrderDispatchCo.getProductName()+"】礼物已发货：\r\n物流公司："
					+ sendMsgOrderDispatchCo.getTransName()+"\r\n物流单号："
					+ sendMsgOrderDispatchCo.getTransNumber()+"\r\n" + "收货人："
					+ sendMsgOrderDispatchCo.getUserName()+"\r\n收货地址："
					+ sendMsgOrderDispatchCo.getDetailAddress()+"\r\n请注意查收 :)", sendMsgOrderDispatchCo.getPhoneNumber());
		}
		return new JmMessage(0, "短信发送成功！");
	}

	public boolean isEmtry(SendMsgOrderDispatchCo sendMsgOrderDispatchCo){
		if(sendMsgOrderDispatchCo!=null){
			if(!StringUtils.isEmpty(sendMsgOrderDispatchCo.getGiverName())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getPhoneNumber())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getTransName())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getTransNumber())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getUserName())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getDetailAddress())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getProductName())
					&&!StringUtils.isEmpty(sendMsgOrderDispatchCo.getTransName())
			){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

}
