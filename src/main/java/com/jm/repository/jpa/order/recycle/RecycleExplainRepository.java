package com.jm.repository.jpa.order.recycle;

import com.jm.repository.po.order.recycle.RecycleExplain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>回收说明</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface RecycleExplainRepository extends JpaRepository<RecycleExplain, Integer> {

    RecycleExplain findRecycleExplainByShopId(Integer shopId);

}