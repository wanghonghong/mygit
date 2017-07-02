package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.wx.WxContentSent;

public interface WxContentSentRepository  extends JpaRepository<WxContentSent, Integer> {
	
	WxContentSent findByContentId(Integer contentId);
	
	@Query(value = "select * from wx_content_sent s where s.send_time <= now() and s.status=0",nativeQuery=true)
	List<WxContentSent> findNotSend();
	
	
}
