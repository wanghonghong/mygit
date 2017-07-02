package com.jm.repository.jpa.order.recycle;

import com.jm.repository.po.order.recycle.RecycleDetail;
import com.jm.repository.po.order.recycle.RecycleReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface RecycleRewardRepository extends JpaRepository<RecycleReward, Integer> {

    RecycleReward findRecyCleRewardByShopId(Integer shopId);

}