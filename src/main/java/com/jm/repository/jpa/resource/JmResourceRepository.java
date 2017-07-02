package com.jm.repository.jpa.resource;

import com.jm.repository.po.system.user.JmResource;
import com.jm.repository.po.wx.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * menu 资源查询
 * @author: cj
 * @date: 2016-6-3
 */
public interface JmResourceRepository extends JpaRepository<JmResource, Integer>{

	List<JmResource> findByParentResourceIdAndStatus(Integer parentResourceId, Integer status);

	List<JmResource> findByParentResourceIdAndStatusAndTypeOrderBySeqAsc(Integer parentResourceId, Integer status,Integer type);

	@Query(value = "select * from jm_resource where parent_resource_id in(?1) and status = ?2 ", nativeQuery = true)
	List<JmResource> findJmResourceByParentResourceIds(List<Integer> wxuserIds, Integer status);

}
