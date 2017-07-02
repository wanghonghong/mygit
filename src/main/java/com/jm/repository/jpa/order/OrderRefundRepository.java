package com.jm.repository.jpa.order;

import com.jm.repository.po.order.OrderRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/8
 */
public interface OrderRefundRepository extends JpaRepository<OrderRefund, Integer> {

    @Query( value="select * from order_refund where order_info_id=?1",nativeQuery=true )
    OrderRefund findByOrderInfoId(Long order_info_id);
}
