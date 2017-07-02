package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.user.ZbUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>用户角色</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
public interface ZbUserRoleRepository extends JpaRepository<ZbUserRole, Integer>{

	  @Query(value = "select * from zb_user_role where user_id=?1 and role_id=?2  ", nativeQuery = true)
	  List<ZbUserRole> querybyRoleIdAnduserId(Integer userId, Integer roleId);

	@Modifying
	@Query( value = "delete from zb_user_role where user_id=?1",nativeQuery=true)
	void deleteUserRoleByUserId(Integer userId);

	ZbUserRole findByUserId(Integer userId);
}
