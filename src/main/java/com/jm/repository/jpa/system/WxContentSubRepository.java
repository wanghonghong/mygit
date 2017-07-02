package com.jm.repository.jpa.system;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.repository.po.wx.WxUser;

public interface WxContentSubRepository extends JpaRepository<WxContentSub,Integer> {
	
	
	@Query(value = "select * from wx_content_sub where content_id=?1 and status=1 order by sort",nativeQuery=true)
	public List<WxContentSub> findByContentId(Integer contentId);
	
	@Query(value = "select * from wx_content_sub where content_id in(?1) and status=1 order by sort",nativeQuery=true)
	List<WxContentSub> findAllByContentIds(List<Integer> contentIds);
	
	@Query(value = "select * from wx_content_sub where id in(?1) and status=1 order by sort",nativeQuery=true)
	List<WxContentSub> findAllByIds(List<Integer> ids);
	
    @Modifying
	@Query(value = "delete from wx_content_sub where content_id=?1",nativeQuery=true)
	void deleteByContentId(Integer contentId);
    
    @Modifying
    @Query(value = "delete from wx_content_sub where id in(?1) and status=1",nativeQuery=true)
	public void batchDeteleByIds(List<Integer> arrIds);

}
