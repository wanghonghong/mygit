package com.jm.repository.jpa.resource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.jm.repository.po.system.user.Resource;

/**
 * menu 资源查询
 * @author: zww
 * @date: 2016-6-3
 */
public interface ResourceRepository extends JpaRepository<Resource, Integer>{

	@Query( value = "select * from resource where status=? order by parent_rsource_id ,sequence,seqencing ",nativeQuery=true)
	List<Resource> findResourceByStatus(Integer status);

	@Query( value = "select * from resource where resource_id in ?1 and  status=?2 order by parent_rsource_id ,sequence,seqencing ",nativeQuery=true)
	List<Resource> findResourceByIdsAndStatus(List<Integer> ids , Integer status);
}
