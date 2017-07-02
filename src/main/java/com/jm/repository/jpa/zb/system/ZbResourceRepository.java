package com.jm.repository.jpa.zb.system;
import com.jm.repository.po.zb.system.ZbResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ZbResourceRepository extends JpaRepository<ZbResource, Integer>{

    @Query( value = "select * from zb_resource where status=? order by sequence,parent_resource_id ,seq ",nativeQuery=true)
    List<ZbResource> findResourceByStatus(Integer status);

    @Query( value = "select * from zb_resource where resource_id in ?1 and  status=?2 order by sequence,parent_resource_id ,seq ",nativeQuery=true)
    List<ZbResource> findResourceByIdsAndStatus(List<Integer> ids, Integer status);
}
