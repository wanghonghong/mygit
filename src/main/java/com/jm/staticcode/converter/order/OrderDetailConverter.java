package com.jm.staticcode.converter.order;

import com.jm.mvc.vo.order.OrderDetailCreateVo;
import com.jm.repository.po.order.OrderDetail;

import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
public class OrderDetailConverter {

    public static OrderDetail toDetail(OrderDetailCreateVo detailVo) {
    	OrderDetail detail = new OrderDetail();
        BeanUtils.copyProperties(detailVo,detail);
        return detail;
    }

}
