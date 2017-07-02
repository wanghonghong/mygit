package com.jm.staticcode.converter.order;

import com.jm.mvc.vo.order.OrderBookAreaCo;
import com.jm.repository.po.order.OrderBookArea;
import org.springframework.beans.BeanUtils;

public class OrderBookAreaConverter {

	public static OrderBookArea toOrderBookArea(OrderBookAreaCo co) {
		OrderBookArea area = new OrderBookArea();
		BeanUtils.copyProperties(co,area);
		return area;
	}

}
