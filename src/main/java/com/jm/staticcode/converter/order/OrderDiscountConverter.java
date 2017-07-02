package com.jm.staticcode.converter.order;


import com.jm.mvc.vo.order.OrderDiscountCo;
import com.jm.repository.po.order.OrderDiscount;
import org.springframework.beans.BeanUtils;

public class OrderDiscountConverter {

    public static OrderDiscount c2p(OrderDiscountCo discountCo){
        OrderDiscount orderDiscount = new OrderDiscount();
        BeanUtils.copyProperties(discountCo,orderDiscount);
        return orderDiscount;
    }
}
