package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.wx.WxKeyReply;

public interface WxKeyReplyRepository extends JpaRepository<WxKeyReply,Integer>  {
	
	List<WxKeyReply> findByAppid(String appid);
	
	@Query(value="select s from  WxKeyReply s where s.appid=?1")
	Page<WxKeyReply> findByAppid(String appid, Pageable pageable);

}
