package com.jm.mvc.vo.qo;

import java.util.Date;
import java.util.List;

import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 
 *<p>查询商品详情信息</p>
 *
 * @author hantp
 * @version latest
 * @data 2016年5月19日
 */
@Data
public class OrderDetailQo {
	
	 	@ApiModelProperty(value = "订单信息")
	    private OrderInfo orderInfo;

	    @ApiModelProperty(value = "订单详情信息")
	    private List<OrderDetail> detailList;
	
}
