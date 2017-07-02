package com.jm.staticcode.constant;


/**
 * <p>订单状态</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class OrderStatus {
	
	public static final Integer WAIT_PAY = 0; //待付款
			
	public static final Integer ALREADY_PAY = 1;//已付款，待发货
	
	public static final Integer ALREADY_SEND = 2;//已发货，待收货
	
	public static final Integer ALREADY_GET_ORDER = 3;//订单管理（已收货）
	
	public static final Integer APPLY_REFUND = 4;//申请退款
	
	public static final Integer REFUND = 5;//退货退款中
	
	public static final Integer ALREADY_GET_DELIVERY = 6;//已退款，发货管理（已收货）
	
	public static final Integer ORDER_CLOSE = 7;//订单关闭
}
