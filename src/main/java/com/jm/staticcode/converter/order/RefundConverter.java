package com.jm.staticcode.converter.order;

import com.jm.mvc.vo.order.OrderRefundCo;
import com.jm.mvc.vo.order.OrderRefundGoodVo;
import com.jm.mvc.vo.order.OrderRefundGoodsCo;
import com.jm.mvc.vo.order.OrderRefundVo;
import com.jm.repository.po.order.OrderRefund;
import com.jm.repository.po.order.OrderRefundGoods;
import org.springframework.beans.BeanUtils;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/8
 */
public class RefundConverter {

    //退款
    public static OrderRefund toOrderRefund(OrderRefundCo orderRefundCo) {
        OrderRefund refund = new OrderRefund();
        BeanUtils.copyProperties(orderRefundCo,refund);
        return refund;
    }

    //退款
    public static OrderRefundVo toOrderRefundVo(OrderRefund refund) {
        OrderRefundVo refundVo = new OrderRefundVo();
        BeanUtils.copyProperties(refund,refundVo);
        return refundVo;
    }

    //确认退货创建退款
    public static OrderRefund toOrderRefund(OrderRefundGoods orderRefundGoods) {
        OrderRefund refund = new OrderRefund();
        BeanUtils.copyProperties(orderRefundGoods,refund);
        return refund;
    }

    //退货
    public static OrderRefundGoods toOrderRefundGoods(OrderRefundGoodsCo orderRefundGoodsCo) {
        OrderRefundGoods refund = new OrderRefundGoods();
        BeanUtils.copyProperties(orderRefundGoodsCo,refund);
        return refund;
    }

    //退货
    public static OrderRefundGoodVo toOrderRefundGoodVo(OrderRefundGoods refundGood) {
        OrderRefundGoodVo refundVo = new OrderRefundGoodVo();
        BeanUtils.copyProperties(refundGood,refundVo);
        return refundVo;
    }
}
