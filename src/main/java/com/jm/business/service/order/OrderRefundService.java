package com.jm.business.service.order;

import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.wb.WbPayService;
import com.jm.business.service.wx.WxPayService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.mvc.vo.order.OrderRefundCo;
import com.jm.mvc.vo.order.OrderRefundUpdateVo;
import com.jm.mvc.vo.order.OrderRefundVo;
import com.jm.mvc.vo.wx.RefundRecodCo;
import com.jm.repository.jpa.order.OrderRefundRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.OrderRefund;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.RefundConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/8
 */
@Slf4j
@Service
public class OrderRefundService {

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrokerageService brokerageService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WbPayService wbPayService;

    @Transactional
    public OrderRefund addRefund(OrderRefundCo orderRefundCo){
        OrderRefund refund = RefundConverter.toOrderRefund(orderRefundCo);
        OrderRefund orderRefund = orderRefundRepository.findByOrderInfoId(refund.getOrderInfoId());
        OrderRefund newRefund = new OrderRefund();
        if(orderRefund==null){
            refund.setCreateRefundDate(new Date());
            newRefund = orderRefundRepository.save(refund);
            orderService.updateOrderStatus(orderRefundCo.getOrderInfoId(),5);
        }
        return newRefund;
    }

    public OrderRefundVo findOrderRefundByOrderInfoId(Long order_info_id){
        OrderRefund orderRefund = orderRefundRepository.findByOrderInfoId(order_info_id);
        OrderRefundVo orderRefundVo = RefundConverter.toOrderRefundVo(orderRefund);
        OrderInfo orderInfo = orderService.getOrderInfo(order_info_id);
        orderRefundVo.setRealPrice(orderInfo.getRealPrice());
        return orderRefundVo;
    }

    @Transactional
    public JmMessage updateOrderRefund(JmUserSession user,Integer id, String appId, String clientIp, OrderRefundUpdateVo updateVo) throws Exception {
        OrderRefund orderRefund = orderRefundRepository.findOne(id);
        JmMessage message = null;
        if(updateVo.getRefundStatus()==1){  //同意退款
            orderRefund.setRefundMoney(updateVo.getRefundMoney());
            log.info("=========updateVo.getRefundMoney()======"+updateVo.getRefundMoney());
            RefundRecodCo refundRecodCo = new RefundRecodCo(orderRefund.getOrderInfoId(),orderRefund.getRefundMoney(),user.getUserId(),clientIp,appId);
            if(orderRefund.getPlatform()==0){
                //微信退款
                message = wxPayService.reFun(refundRecodCo);
                if(message.getCode().equals(0)){
                    orderRefund.setRefundStatus(updateVo.getRefundStatus());
                    orderRefund.setOperateWay(updateVo.getOperateWay());
                    orderRefund.setAgreeRefundDate(new Date());
                    brokerageService.refundBrokerageOrder(orderRefund.getOrderInfoId());
                    orderRefund.setRefuseReason(updateVo.getRefuseReason());
                    orderService.updateOrderStatus(orderRefund.getOrderInfoId(),orderRefund.getAgreeRefundDate(),5);
                    orderRefundRepository.save(orderRefund);
                    String url = Constant.DOMAIN;
                    orderService.pushRefundMoneyWxMsg(orderRefund.getOrderInfoId(),url);
                }
            }else{
                //微博退款
                message = wbPayService.toRefund(orderRefund);
                if(message.getCode().equals(100000)){
                    orderRefund.setRefundStatus(updateVo.getRefundStatus());
                    orderRefund.setOperateWay(updateVo.getOperateWay());
                    orderRefund.setAgreeRefundDate(new Date());
                    brokerageService.refundBrokerageOrder(orderRefund.getOrderInfoId());
                    orderRefund.setRefuseReason(updateVo.getRefuseReason());
                    orderService.updateOrderStatus(orderRefund.getOrderInfoId(),orderRefund.getAgreeRefundDate(),5);
                    orderRefundRepository.save(orderRefund);
                    /*String url = Constant.DOMAIN;
                    orderService.pushRefundMoneyWxMsg(orderRefund.getOrderInfoId(),url);*/
                }
            }
        }else{
            orderRefund.setRefundStatus(updateVo.getRefundStatus());
            orderRefund.setOperateWay(updateVo.getOperateWay());
            orderRefund.setAgreeRefundDate(new Date());
            orderRefund.setRefuseReason(updateVo.getRefuseReason());
            orderService.updateOrderStatus(orderRefund.getOrderInfoId(),5);
            orderRefundRepository.save(orderRefund);
            OrderInfoVo orderInfoVo = orderService.findOrderById(orderRefund.getOrderInfoId());
            //拒绝退款只有未收货的订单才有生成有效佣金
            if(orderInfoVo.getTakeDate()==null){
                brokerageService.refuse(orderRefund.getOrderInfoId());
            }
            message = new JmMessage(1, "操作成功！");
        }
        return message;
    }
}
