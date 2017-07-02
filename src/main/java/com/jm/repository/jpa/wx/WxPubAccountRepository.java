package com.jm.repository.jpa.wx;

import com.jm.repository.po.wx.WxPubAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>微信公众账号信息</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */
public interface WxPubAccountRepository extends JpaRepository<WxPubAccount, String> {

	List<WxPubAccount> findWxPubAccountByIsGet(Integer isGet);
	
	List<WxPubAccount> findByIsAuth(Integer isAuth);
	
	List<WxPubAccount> findByTypeAndUserIdAndIsAuth(int type,Integer userId,Integer isAuth);

	List<WxPubAccount> findByTypeAndIsAuth(int type,Integer isAuth);



}
