package com.jm.repository.jpa.order.recycle;

import com.jm.repository.po.order.recycle.OrderBookConfig;
import com.jm.repository.po.order.recycle.RecycleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface OrderBookConfigRepository extends JpaRepository<OrderBookConfig, Integer> {

    OrderBookConfig findOrderBookConfigByShopId(Integer shopId);

}