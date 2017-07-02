package com.jm.repository.jpa.order;

import com.jm.repository.po.order.OrderRefundGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>退货</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */
public interface OrderRefundGoodRepository  extends JpaRepository<OrderRefundGoods, Integer> {

    @Query( value ="select * from order_refund_goods where order_info_id=?1",nativeQuery=true)
    public OrderRefundGoods getOrderRefundGoods(Long orderInfoId);

}
