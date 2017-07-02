package com.jm.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.jm.business.service.order.OrderDetailService;
import com.jm.business.service.order.OrderService;
import com.jm.mvc.vo.order.OrderInfoForQueryVo;
import com.jm.mvc.vo.qo.OrderManageQo;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;

public class OrderServiceTest extends BaseServiceTest{

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDetailService orderDetailService;

    @Test
    public void test() throws Exception {
    	OrderInfoForQueryVo vo = new OrderInfoForQueryVo();
    	vo.setConsigneeName("张三");
    	vo.setConsigneePhone("");
    	vo.setOrderNum("20160525115");
    	vo.setPayOrderNum("");
        vo.setNickname("");
    	vo.setStatus(1);
    	//List<OrderManageQo> orderManageQo = orderService.queryOrderManage(vo, 1, 5);
    	//System.out.println(orderManageQo.size());
    }
    
    /*@Test
    public void test() throws Exception {
    	// List<OrderDetailQo> qo=orderService.getOrderList(1, 1);
    	 List<OrderDetail> details= orderDetailService.getOrderDetailList(1, 1);
        
        Assert.assertTrue(details.size()>0);
    }*/


}
