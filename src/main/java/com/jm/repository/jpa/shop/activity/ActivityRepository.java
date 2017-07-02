package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.Activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;



import java.util.Date;
import java.util.List;


/**
 * <p>活动表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public interface ActivityRepository extends JpaRepository<Activity, Integer>{

    /**
     * 查询正在进行中的活动，一种类型只有一个活动
     * @param platform
     * @param appId
     * @param subType
     * @return
     */
    @Query(value="select * from activity a where a.status in(0,1) and sysdate() between a.start_time and a.end_time and a.platform=?1 and a.app_id=?2 and a.sub_type=?3",nativeQuery=true)
    List<Activity> findActivityIng(Integer platform,String appId,Integer subType);

    @Query(value="select * from activity a where a.status in(0,1) and a.start_time<=sysdate()  and a.platform=?1 and a.sub_type=?2",nativeQuery=true)
    List<Activity> findActivityIngByOldFans(Integer platform,Integer subType);

    @Query(value="select count(*) from wx_user w,shop_user s where w.shop_user_id=s.id and w.user_id in(?1) and s.agent_role in(?2)",nativeQuery=true)
    int agentRoleCount(String userIds,String roleIds);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.type=?2 and a.sub_type=?3 and (a.start_time between ?4 and ?5 or a.end_time between ?4 and ?5 or ?4 between a.start_time and a.end_time or ?5 between a.start_time and a.end_time) and (a.status=1 or a.status=2 or a.status=0) ",nativeQuery=true)
    int countActivity(Integer shopId, Integer type, Integer subType,Date startTime,Date endTime);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.type=?2 and a.sub_type=?3 and (a.start_time between ?4 and ?5 or a.end_time between ?4 and ?5 or ?4 between a.start_time and a.end_time or ?5 between a.start_time and a.end_time) and (a.status=1 or a.status=2 or a.status=0) and a.id !=?6",nativeQuery=true)
    int countActivity(Integer shopId, Integer type, Integer subType,Date startTime,Date endTime,Integer Id);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.sub_type=?2 and (a.start_time between ?3 and ?4 or a.end_time between ?3 and ?4 or ?3 between a.start_time and a.end_time or ?4 between a.start_time and a.end_time) and (a.status=1 or a.status=2 or a.status=0) ",nativeQuery=true)
    int countActivity(Integer shopId, Integer subType,Date startTime,Date endTime);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.sub_type=?2 and (a.start_time between ?3 and ?4 or a.end_time between ?3 and ?4 or ?3 between a.start_time and a.end_time or ?4 between a.start_time and a.end_time) and (a.status=1 or a.status=2 or a.status=0) and a.id !=?5",nativeQuery=true)
    int countActivity(Integer shopId, Integer subType,Date startTime,Date endTime,Integer Id);


    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.type=?2 and a.sub_type=?3 and a.start_time=?4 and (a.status=1 or a.status=2 or a.status=0) ",nativeQuery=true)
    int countOldActivity(Integer shopId, Integer type, Integer subType,Date startTime);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.type=?2 and a.sub_type=?3 and a.start_time = ?4 and (a.status=1 or a.status=2 or a.status=0) and a.id !=?5",nativeQuery=true)
    int countOldActivity(Integer shopId, Integer type, Integer subType,Date startTime,Integer Id);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.sub_type=?2 and a.start_time = ?3  and (a.status=1 or a.status=2 or a.status=0) ",nativeQuery=true)
    int countOldActivity(Integer shopId, Integer subType,Date startTime);

    @Query(value="select count(*) from activity a where a.shop_id=?1 and a.sub_type=?2 and a.start_time = ?3 and (a.status=1 or a.status=2 or a.status=0) and a.id !=?4",nativeQuery=true)
    int countOldActivity(Integer shopId, Integer subType,Date startTime,Integer Id);


    @Modifying
    @Query(value="update activity set status = 1 where status=0 and SYSDATE() between start_time and end_time ",nativeQuery=true)
    void updateActivityTaskIng();

    @Modifying
    @Query(value="update activity set status = 1 where status=0 and SYSDATE() >= start_time ",nativeQuery=true)
    void updateActivityOldFansTaskIng();

    @Modifying
    @Query(value="update activity set status = 3 where SYSDATE() > end_time and status in(0,1,2)",nativeQuery=true)
    void updateActivityTaskOver();

    @Modifying
    @Query(value="update activity set status = 3 where SYSDATE() >= start_time  and status in(2)",nativeQuery=true)
    void updateActivityOldFansTaskOver();
    
    List<Activity> findByAppId(String appid);
    
    @Query(value="select count(*) from activity a where a.app_id=?1 and a.type=?2 and a.sub_type=?3 and (a.start_time between ?4 and ?5 or a.end_time between ?4 and ?5 or ?4 between a.start_time and a.end_time or ?5 between a.start_time and a.end_time) and (a.status=1 or a.status=2 or a.status=0) ",nativeQuery=true)
    int countActivityByAppid(String appid, Integer type, Integer subType,Date startTime,Date endTime);

    @Query(value="select * from activity a where a.shop_id=?1 and  a.status in(1,2,3)  and a.platform=1 and a.type in ?2 and a.sub_type in ?3 ",nativeQuery=true)
    List<Activity> queryActivityName(Integer shopId, List type, List subType);

    @Query(value="select * from activity a where  a.status =3  and a.platform=1 and type != 5 and ifnull(a.send_sms,0) = 0 ",nativeQuery=true)
    List<Activity> findActivityTaskOver();

    @Modifying
    @Query(value="update activity set send_sms = 1 where id in ?1",nativeQuery=true)
    void updateActivityTaskOverSms(List ids);

}
