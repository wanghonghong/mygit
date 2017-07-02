package com.jm.repository.jpa.system;

import com.jm.repository.po.system.user.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>用户角色</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/14
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	
	  @Query(value = "select * from user_role where shop_id=?1 and level between 2 and 4 ", nativeQuery = true)
	  List<UserRole> queryUserRole(int shopId);
	  
	  
	  @Query(value = "select * from user_role u left join role r on r.role_id = u.role_id where user_id=?1 and shop_id=?2  ", nativeQuery = true)
	  List<UserRole> findByUserAndType(Integer userId,Integer type);
	  
	  @Query(value = "select * from user_role where shop_id=?1 and role_id = ?2 ", nativeQuery = true)
	  List<UserRole> queryUserRoleByshopIdAndRoleid(int shopId,int roleid);

	@Modifying
	@Query(value = "delete from user_role where id=?1", nativeQuery = true)
	 void deleteUserRole(Integer userId);

	@Query(value = "select * from user_role where  shop_id=?1  and role_id =2 ", nativeQuery = true)
	UserRole queryUserMasterbyShopId(Integer shopId);


	UserRole findByShopIdAndUserId(Integer shopId,Integer userId);
}
