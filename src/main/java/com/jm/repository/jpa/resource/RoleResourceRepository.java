package com.jm.repository.jpa.resource;

import com.jm.repository.po.system.user.RoleResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *  角色菜单 htp
 */
public interface RoleResourceRepository extends JpaRepository<RoleResource, Integer>{

	List<RoleResource> findByRoleId(Integer roleId);

	@Modifying
	@Query(value = "delete from RoleResource where roleId=?1")
	void deleteByRoleId(Integer roleId);

	List<RoleResource> findByRoleIdAndSoftId(Integer roleId,Integer softId);

}
