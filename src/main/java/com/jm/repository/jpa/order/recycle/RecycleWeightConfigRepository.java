package com.jm.repository.jpa.order.recycle;


import com.jm.repository.po.order.recycle.RecycleWeightConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecycleWeightConfigRepository extends JpaRepository<RecycleWeightConfig, Integer> {

    List<RecycleWeightConfig> findRecycleWeightConfigByRecycleRewardId(Integer id);
    
}
