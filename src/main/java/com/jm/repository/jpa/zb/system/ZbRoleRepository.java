package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.user.ZbRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZbRoleRepository extends JpaRepository<ZbRole, Integer>{

    Page<ZbRole> findAll(Pageable pageable);

   @Modifying
   @Query(value = "delete from zb_role where id in ?1",nativeQuery=true)
    void deleteByIds(List<Integer> ids);

   List<ZbRole> findByRoleType(Integer roleType);
}
