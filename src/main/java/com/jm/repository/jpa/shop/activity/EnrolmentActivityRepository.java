package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.EnrolmentActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 * <p>活动报名</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 11:25
 */
public interface EnrolmentActivityRepository extends JpaRepository<EnrolmentActivity, Integer>{

    @Modifying
    @Query(value="update enrolment_activity set status = 1 where status=0 and SYSDATE() between start_date and end_date ",nativeQuery=true)
    void updateActivityTaskIng();

    @Modifying
    @Query(value="update enrolment_activity set status = 2 where SYSDATE() > end_date and status in(0,1)",nativeQuery=true)
    void updateActivityTaskOver();
}
