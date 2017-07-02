package com.jm.repository.jpa.order.recycle;

import com.jm.repository.po.order.recycle.RecycleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface RecycleDetailRepository extends JpaRepository<RecycleDetail, Integer> {

    RecycleDetail findRecycleDetailByOrderId(Long orderId);

}