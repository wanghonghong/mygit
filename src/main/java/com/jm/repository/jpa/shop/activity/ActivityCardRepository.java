package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.ActivityCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * <p>活动卡卷子表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/12/03
 */
public interface ActivityCardRepository extends JpaRepository<ActivityCard, Integer>{

    List<ActivityCard> findByActivityId(Integer activityId);

//    @Modifying
//    @Query(value = "delete  from  activity_card a where a.activityId=?1")
//    void deleteByActivityId(Integer activityId);

//    @Query(value="select * from activity_card a where (a.card_id=?1 or a.card_ids like ?1 ) ",nativeQuery=true)
//    List<ActivityCard> countCardIdByActivity(Integer cardId);

}
