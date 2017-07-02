/**
 * 
 */
package com.jm.mvc.controller.order;

import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.OrderDeliveryDispatchCreateVo;
import com.jm.mvc.vo.order.OrderDeliveryForUpdateVo;
import com.jm.mvc.vo.order.SendMsgOrderDispatchCo;
import com.jm.mvc.vo.system.SendMsgCo;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.wx.SMSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jm.business.service.order.OrderDeliveryService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.order.OrderDeliveryCreateVo;
import com.jm.repository.po.order.OrderDelivery;

import java.util.List;

/**
 * <p>订单发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */
@Api
@RestController
@RequestMapping(value = "/orderDelivery")
public class OrderDeliveryController {

	@Autowired
	private OrderDeliveryService orderDeliveryService;

	@ApiOperation("添加订单发货信息")
    @RequestMapping(value = "/order_delivery/{order_info_id}", method = RequestMethod.POST)
    public JmMessage addOrderDelivery(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam("订单标识") @PathVariable("order_info_id") Long orderInfoId,
						 @ApiParam("添加订单发货") @RequestBody OrderDeliveryCreateVo createvo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		JmMessage jmMessage = orderDeliveryService.createOrderDelivery(orderInfoId,createvo,user);
    	return jmMessage;
    }

	@ApiOperation("添加送礼订单发货信息")
	@RequestMapping(value = "/order_delivery_dispatch", method = RequestMethod.POST)
	public JmMessage addOrderDeliveryDispatch(@ApiParam(hidden=true) HttpServletRequest request,
						 @ApiParam("添加订单发货") @RequestBody OrderDeliveryDispatchCreateVo orderDeliveryDispatchCreateVo) throws Exception {
		if(orderDeliveryDispatchCreateVo.getSendsDispatchVos().size()>0){
			JmMessage jmMessage = orderDeliveryService.createDispatchOrderDelivery(orderDeliveryDispatchCreateVo);
			return jmMessage;
		}else{
			return new JmMessage(1, "包裹未勾选！");
		}
	}
	
	@ApiOperation("根据订单id查询订单发货信息")
    @RequestMapping(value = "/delivery_list/{order_info_id}", method = RequestMethod.GET)
    public List<OrderDelivery> queryOrderDetail(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId){
		List<OrderDelivery> orderDelivery = orderDeliveryService.getOrderDelivery(orderInfoId);
        return orderDelivery;
    }

	@ApiOperation("pc端保存订单中的物流备注信息")
	@RequestMapping(value = "/save_send_note/{id}", method = RequestMethod.PUT)
	public JmMessage saveSendNote(@ApiParam(hidden=true) HttpServletRequest request,
								 @ApiParam("订单标识id") @PathVariable("id") Integer id,
								 @RequestBody @Valid OrderDeliveryForUpdateVo updateVo){
		OrderDelivery orderDelivery = orderDeliveryService.getOrderDelivery(id);
		orderDelivery.setDeliveryNote(updateVo.getDeliveryNote());
		OrderDelivery od = orderDeliveryService.createOrderDelivery(orderDelivery);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
		return new JmMessage(1, "1");
	}

	@ApiOperation("发送短信")
	@RequestMapping(value = "/sendmsg", method = RequestMethod.POST)
	public JmMessage sendmsg(@ApiParam(hidden=true) HttpServletRequest request,
							 @ApiParam("用户更新") @RequestBody @Valid SendMsgOrderDispatchCo sendMsgOrderDispatchCo){
		boolean bl = orderDeliveryService.isEmtry(sendMsgOrderDispatchCo);
		if(bl){
			String state = SMSUtil.sendMsg(sendMsgOrderDispatchCo.getGiverName() + "送您的【"
					+ sendMsgOrderDispatchCo.getProductName()+"】礼物已发货：\r\n物流公司："
					+ sendMsgOrderDispatchCo.getTransName()+"\r\n物流单号："
					+ sendMsgOrderDispatchCo.getTransNumber()+"\r\n" + "收货人："
					+ sendMsgOrderDispatchCo.getUserName()+"\r\n收货地址："
					+ sendMsgOrderDispatchCo.getDetailAddress()+"\r\n请注意查收 :)", sendMsgOrderDispatchCo.getPhoneNumber());
			if(state.equals("1")){
				return new JmMessage(0, "发送成功");
			}else{
				return new JmMessage(1, "发送失败","发送失败");
			}
		}else{
			return new JmMessage(2, "格式不正确，缺少字段！");
		}
	}


}
