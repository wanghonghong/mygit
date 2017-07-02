package com.jm.repository.jpa.shop.activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.shop.activity.ActivityUser;


/**
 * <p>活动用户</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public interface ActivityUserRepository extends JpaRepository<ActivityUser, Integer>{
	
	ActivityUser findByUserId(Integer userId);
	
	List<ActivityUser> findByActivityId(Integer activityId);
	
	ActivityUser findByRedPayId(Integer redPayId);

}
