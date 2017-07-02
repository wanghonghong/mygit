package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jm.repository.po.wx.WxUserGroup;

public interface WxGroupRepository extends JpaRepository<WxUserGroup, Integer>{
	
	List<WxUserGroup> findWxUserGroupByAppid(String appid);

	WxUserGroup findWxUserGroupByAppidAndGroupid(String appid,Integer groupId);

    @Modifying
	@Query(value = "delete from wx_user_group where appid=?1",nativeQuery=true)
	void deleteWxUserGroupByAppid(String appid);

}
