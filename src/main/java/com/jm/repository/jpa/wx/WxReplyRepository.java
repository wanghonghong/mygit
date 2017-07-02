package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.wx.WxReply;

/**
 * 微信回复
 * @author chenyy
 *
 */
public interface WxReplyRepository extends JpaRepository<WxReply,Integer> {
	
	/**
	 * 根据appid与回复类型查询
	 * @param appid
	 * @param replyType
	 * @return
	 */
	public WxReply findByAppidAndReplyType(String appid,Integer replyType);
	
	 @Modifying
     @Query(value = "delete from WxReply  where appid=?1 and replyType=?2")
	public void deleteByAppidAndReplyType(String appid,Integer replyType);
	 
	 public List<WxReply> findByAppid(String appid);

}
