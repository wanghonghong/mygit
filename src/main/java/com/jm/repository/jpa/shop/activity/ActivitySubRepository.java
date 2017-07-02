package com.jm.repository.jpa.shop.activity;
import com.jm.repository.po.shop.activity.ActivitySub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>活动子表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public interface ActivitySubRepository extends JpaRepository<ActivitySub, Integer>{

    List<ActivitySub> findByActivityId(Integer activityId);

    @Modifying
    @Query(value="delete from ActivitySub where activityId= ?1 ")
    void deleteByActivityId(int activityId);

   /* @Query(value = "from ActivitySub where shopId=? and appId=? order by seq ")
    List<ActivitySub> getSubActivityListByshopIdandAppid(int shopId, int appId);

    @Modifying
    @Query(value="delete from ActivitySub where activityId= ?1 ")
    public void deleteSubActivityByActivityId(int activityId);

    @Query(value = "from ActivitySub where activityId=? order by seq ")
    public List<ActivitySub>  findSubActivityByActivityId(int activityId);*/

}
