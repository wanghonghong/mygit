package com.jm.repository.jpa.order;


import com.jm.repository.po.order.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {

    @Query( value = "select * from order_discount where order_info_id=?1 and type=?2",nativeQuery=true)
    OrderDiscount findByOrderDiscount(Long orderInfoId,int type);

    @Query( value = "select * from order_discount where order_info_id=?1",nativeQuery=true)
    List<OrderDiscount> findByOrderDiscountByOrderInfoId(Long orderInfoId);

    @Modifying
    @Query( value = "update order_discount od set od.order_info_id=?1 where od.order_info_id=?2",nativeQuery=true)
    void updateOrderDiscount(Long newOrderInfoId,Long orderInfoId);

}
