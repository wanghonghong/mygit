package com.jm.business.service.order;

import com.jm.mvc.vo.order.OrderRefundGoodUpdateVo;
import com.jm.mvc.vo.order.OrderRefundGoodVo;
import com.jm.mvc.vo.order.OrderRefundGoodsCo;
import com.jm.repository.jpa.order.OrderRefundGoodRepository;
import com.jm.repository.jpa.order.OrderRefundRepository;
import com.jm.repository.jpa.order.OrderRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.OrderRefund;
import com.jm.repository.po.order.OrderRefundGoods;
import com.jm.staticcode.converter.order.RefundConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>退货</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */
@Service
public class OrderRefundGoodService {

    @Autowired
    private OrderRefundGoodRepository orderRefundGoodRepository;

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    public OrderRefundGoods getOrderRefundGoods(Long orderInfoId){
        return orderRefundGoodRepository.getOrderRefundGoods(orderInfoId);
    }

    public OrderRefundGoods getOrderRefundGoods(Integer orderGoodId){
        OrderRefundGoods orderRefundGood = orderRefundGoodRepository.findOne(orderGoodId);
        return orderRefundGood;
    }

    public OrderRefundGoods createOrderRefundGood(OrderRefundGoods orderRefundGoods){
        return orderRefundGoodRepository.save(orderRefundGoods);
    }

    public OrderRefundGoodVo findOrderRefundGoods(Long orderInfoId){
        OrderRefundGoods orderRefundGood = orderRefundGoodRepository.getOrderRefundGoods(orderInfoId);
        OrderRefundGoodVo orderRefundGoodVo = RefundConverter.toOrderRefundGoodVo(orderRefundGood);
        return orderRefundGoodVo;
    }
    //创建退货
    @Transactional
    public OrderRefundGoods addOrderRefundGoods(OrderRefundGoodsCo orderRefundGoodsCo){
        OrderRefundGoods orderRefundGoods = RefundConverter.toOrderRefundGoods(orderRefundGoodsCo);
        OrderRefundGoods good = orderRefundGoodRepository.getOrderRefundGoods(orderRefundGoods.getOrderInfoId());
        OrderRefundGoods goods = new OrderRefundGoods();
        if(good==null){
            orderRefundGoods.setCreateGoodDate(new Date());
            goods = orderRefundGoodRepository.save(orderRefundGoods);
            orderService.updateGoodStatus(goods.getOrderInfoId(),goods.getCreateGoodDate(),1);
        }
        return goods;
    }


    //修改退货中的状态
    @Transactional
    public OrderRefundGoods updateOrderRefundGoods(OrderRefundGoodUpdateVo orderRefundGoodUpdateVo){
        OrderRefundGoods orderRefundGoods = orderRefundGoodRepository.getOrderRefundGoods(orderRefundGoodUpdateVo.getOrderInfoId());
        orderRefundGoods.setGoodStatus(orderRefundGoodUpdateVo.getGoodStatus());
        orderRefundGoods.setStorageDate(new Date());//记录卖家入库时间
        //确认退货创建退款
        OrderRefund orderRefund = RefundConverter.toOrderRefund(orderRefundGoods);
        orderRefund.setRefundType(2);
        orderRefund.setCreateRefundDate(new Date());
        OrderRefund refund = orderRefundRepository.findByOrderInfoId(orderRefund.getOrderInfoId());
        if(refund==null){
            orderRefundRepository.save(orderRefund);
        }
        //修改订单状态
        OrderInfo orderInfo = orderService.getOrderInfo(orderRefundGoods.getOrderInfoId());
        orderInfo.setStatus(5);
        orderInfo.setLookStatus(5);
        orderRepository.save(orderInfo);
        return orderRefundGoodRepository.save(orderRefundGoods);
    }

}
