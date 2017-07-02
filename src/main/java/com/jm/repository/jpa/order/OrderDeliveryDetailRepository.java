package com.jm.repository.jpa.order;

import com.jm.repository.po.order.OrderDelivery;
import com.jm.repository.po.order.OrderDeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/23
 */
public interface OrderDeliveryDetailRepository extends JpaRepository<OrderDeliveryDetail, Integer> {

    @Query( value = "select * from order_delivery_detail where order_detail_id in(?1) ",nativeQuery=true)
    List<OrderDeliveryDetail> findOrderDeliveryDetailByOrderDetailId(String orderDetailIds);

}
