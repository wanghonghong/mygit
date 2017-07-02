/**
 * 
 */
package com.jm.repository.jpa.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.order.OrderDelivery;

import java.util.List;

/**
 * <p>订单发货信息数据操作</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */
public interface OrderDeliveryRepository extends JpaRepository<OrderDelivery, Long> {
	
	 @Query( value = "select * from order_delivery where order_info_id=?1",nativeQuery=true)
	 List<OrderDelivery> findOrderDeliveryByOrderInfoId(Long orderInfoId);

	 @Query( value = "select * from order_delivery where order_delivery_id=?1",nativeQuery = true)
	 OrderDelivery findOrderDeliveryByOrderDeliveryId(Integer orderDeliveryId);

	 @Query( value = "select * from order_delivery where order_info_id=?1 and status=?2",nativeQuery = true)
	 List<OrderDelivery> findOrderDeliveryByOrderInfoIdAndStatus(Long orderInfoId, int status);
}
