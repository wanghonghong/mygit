package com.jm.repository.jpa.zb.system;
import com.jm.repository.po.zb.user.ZbRoleResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *  角色菜单 whh
 */

public interface ZbRoleResourceRepository extends JpaRepository<ZbRoleResource, Integer>{

    List<ZbRoleResource> findByRoleId(Integer roleId);

    @Query( value = "select resource_id from zb_role_resource where role_id=?1",nativeQuery=true)
    List<Integer> findResourceIdsByRoleId(Integer roleId);
}
