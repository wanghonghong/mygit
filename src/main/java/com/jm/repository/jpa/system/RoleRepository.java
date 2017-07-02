package com.jm.repository.jpa.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.system.user.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

   @Query(value = "select * from role where role_id between 3 and 9", nativeQuery = true)
    List<Role> queryRole();

	@Query(value = " select  *"
			+ " from role r "
			+ " left join user_role ur  on ur.role_id = r.role_id  "
			+ " left join user u on ur.user_id  = u.user_id "
			+ " WHERE u.user_id=?1  ",nativeQuery=true)
	List<Role> queryRoleByUserId(Integer userId);

	@Query(value = "select * from role where type=4", nativeQuery = true)
	List<Role> queryServiceRole();

	List<Role> findByType(int type);

	List<Role> findByTypeAndSoftId(int type,Integer softId);

	@Query(value = "select * from role where type between 0 and 5", nativeQuery = true)
	List<Role> findRoleByType();

	@Query(value = "select * from role where soft_id=?1 and type > ?2 ", nativeQuery = true)
	List<Role> findBySoftId(Integer softId,Integer type);

	@Modifying
	@Query(value = "delete from Role where softId=?1 and type>0")
	void deleteBySoftId(Integer softId);

}
