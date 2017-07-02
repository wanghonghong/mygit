package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.ActivityCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


 /**
 * <p>活动配置</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public interface ActivityConditionRepository extends JpaRepository<ActivityCondition, Integer>{

     @Modifying
     @Query(value = "delete  from  ActivityCondition a where a.activityId=?1")
     void deleteByActivityId(Integer activityId);

    ActivityCondition findByActivityId(Integer activityId);

}
