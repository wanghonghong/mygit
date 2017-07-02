package com.jm.mvc.controller.order;

import com.jm.business.service.order.OrderDeliveryService;
import com.jm.business.service.order.OrderRefundGoodService;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.order.SendCompanysKd100Service;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.order.DeliveryImmediateVo;
import com.jm.mvc.vo.order.OrderDeliveryVo;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.mvc.vo.order.OrderRefundGoodVo;
import com.jm.repository.po.order.OrderDelivery;
import com.jm.staticcode.converter.order.OrderConverter;
import com.jm.staticcode.converter.order.OrderDeliveryConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/5/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/send_company_kd100")
public class SendCompanysKd100Controller {

    @Autowired
    private SendCompanysKd100Service sendCompanysKd100Service;

    @Autowired
    private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderRefundGoodService orderRefundGoodService;

    @Autowired
    private OrderService orderService;

    @ApiOperation("pc端物流返回查询结果[普通订单]")
    @RequestMapping(value = "/send_list/{order_info_id}", method = RequestMethod.GET)
    public DeliveryImmediateVo getSendCompanys(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId) throws IOException {
        DeliveryImmediateVo deliveryImmediateVo = new DeliveryImmediateVo();
        deliveryImmediateVo.setSendCompanysKd100s(sendCompanysKd100Service.getSendCompanys());
        deliveryImmediateVo.setOrderDetails(OrderConverter.convertOrderDetailVo(orderService.queryOrderDetailPc(orderInfoId)));
        deliveryImmediateVo.setCount(orderService.queryOrderDetail(orderInfoId).size());
        return deliveryImmediateVo;
    }

    @ApiOperation("pc端物流返回查询结果[送礼订单]")
    @RequestMapping(value = "/send_dispatch_list/{order_info_id}", method = RequestMethod.GET)
    public DeliveryImmediateVo getDispatchSendCompanys(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId) throws IOException {
        DeliveryImmediateVo deliveryImmediateVo = new DeliveryImmediateVo();
        deliveryImmediateVo.setSendCompanysKd100s(sendCompanysKd100Service.getSendCompanys());
        deliveryImmediateVo.setOrderDetails(OrderConverter.convertOrderDetailVo(orderService.queryOrderDetailPc(orderInfoId)));
        deliveryImmediateVo.setCount(orderService.queryOrderDetail(orderInfoId).size());
        deliveryImmediateVo.setOrderInfoDispatchVos(OrderConverter.convertOrderDispatchVo(orderService.queryDispatchOrder(orderInfoId)));
        return deliveryImmediateVo;
    }

    @ApiOperation("pc端物流返回查询结果")
    @RequestMapping(value = "/supply_send_list/{order_info_id}", method = RequestMethod.GET)
    public DeliveryImmediateVo getSupplySendCompanys(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId) throws IOException {
        DeliveryImmediateVo deliveryImmediateVo = new DeliveryImmediateVo();
        deliveryImmediateVo.setSendCompanysKd100s(sendCompanysKd100Service.getSendCompanys());
        deliveryImmediateVo.setOrderDetails(OrderConverter.convertOrderDetailVo(orderService.queryOrderDetailSupplyPc(orderInfoId)));
        deliveryImmediateVo.setCount(orderService.queryOrderDetail(orderInfoId).size());
        return deliveryImmediateVo;
    }

    @ApiOperation("pc端物流返回查询结果")
    @RequestMapping(value = "/send_result/{order_info_id}", method = RequestMethod.GET)
    public JmMessage send_list(@ApiParam("订单标识")  @PathVariable("order_info_id") Integer orderDeliveryId){
        JmMessage message = new JmMessage();
        OrderDelivery orderDelivery = orderDeliveryService.getOrderDelivery(orderDeliveryId);
        String url = sendCompanysKd100Service.send_list(orderDelivery.getTransCode(),orderDelivery.getTransNumber());
        message.setCode(0);
        message.setData(url);
        return message;
    }

    @ApiOperation("app端物流返回查询结果")
    @RequestMapping(value = "/send_result1/{order_info_id}", method = RequestMethod.GET)
    public ModelAndView send_app_list(@ApiParam(hidden=true) HttpServletRequest request,
                                      @ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId){
        ModelAndView view = new ModelAndView();
        List<OrderDelivery> orderDeliveryList = orderDeliveryService.getOrderDelivery(orderInfoId);
        List<Map<String, Object>> map = orderService.querySendDetails(orderInfoId);
        OrderDeliveryConverter orderDeliveryConverter = new OrderDeliveryConverter();
        List<OrderDeliveryVo> orderDeliveryVoList = orderDeliveryConverter.p2v(orderDeliveryList,map,sendCompanysKd100Service);
        view.setViewName("/app/order/sendInformationApp");
        request.setAttribute("orderDeliveryVoList", orderDeliveryVoList);
        return view;
    }

    @ApiOperation("app端物流返回查询结果")
    @RequestMapping(value = "/refundGoodSend/{order_info_id}", method = RequestMethod.GET)
    public JmMessage refundGoodSend(@ApiParam(hidden=true) HttpServletRequest request,
                                      @ApiParam("订单标识")  @PathVariable("order_info_id") Long order_info_id){
        JmMessage message = new JmMessage();
        OrderRefundGoodVo OrderRefundGoodVo = orderRefundGoodService.findOrderRefundGoods(order_info_id);
        String url = sendCompanysKd100Service.send_list(OrderRefundGoodVo.getTransCode(),OrderRefundGoodVo.getTransId());
        if(url.contains("ERROR")){
            url = "ERROR";
        }
        message.setCode(0);
        message.setData(url);
        return message;
    }
}
