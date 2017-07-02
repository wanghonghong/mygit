package com.jm.repository.jpa.wx;


import com.jm.repository.po.wx.WxUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * <p>微信用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/18
 */
public interface WxUserRepository extends JpaRepository<WxUser, Integer> {
	
	@Query(value = "select count(*) from wx_user where upper_one=?1", nativeQuery = true)
	Object getWxUserUpperOneById(Integer wxuserId);
	
	@Query(value = "select count(*) from wx_user where upper_two=?1", nativeQuery = true)
	Object getWxUserUpperTwoById(Integer wxuserId);

	@Query(value = "select * from wx_user where upper_one=?1 ORDER BY user_id DESC", nativeQuery = true)
	List<WxUser> getWxUserUpperOneListById(Integer wxuserId);

	@Query(value = "select * from wx_user where upper_two=?1 ORDER BY user_id DESC", nativeQuery = true)
	List<WxUser> getWxUserUpperTwoListById(Integer wxuserId);

	WxUser findWxUserByAppidAndOpenid(String appid,String openid);
	
	
	@Query(value = "select * from wx_user where upper_one=?1 or upper_two=?1 ", nativeQuery = true)
	List<WxUser> findWxUserList(Integer userid);

	@Modifying
	@Query( value = " update wx_user_qrcode a inner join " +
			" ( select a.id from wx_user_qrcode a " +
			" left join wx_user b on a.user_id=b.user_id " +
			" where b.appid=?1) b " +
			" on a.id=b.id set a.user_qrcode = null ",nativeQuery=true)
    //@Query( value = "update wx_user  set user_qrcode_poster='' where appid=?1 ",nativeQuery=true)
    void updateAllWxUser(String appid);



	WxUser findWxUserByUserId(Integer userId);

	@Query(value = "select * from wx_user where user_id in(?1) ", nativeQuery = true)
	List<WxUser> findWxUserByUserIds(List<Integer> wxuserIds);

	Page<WxUser> findAll(Specification<WxUser> specification, Pageable pageable);
	
	List<WxUser> findAll(Specification<WxUser> specification);

	@Query(value = "select u.openid  from wx_user u where u.appid=?1 and is_subscribe=1 ", nativeQuery = true)
	List<String> findOpenidByAppidAndIsSub(String appid);

    /*@Query(value="select u from WxUser u  " +
            " where u.nickname like ?1 and ifnull(u.userName,'') like ?2 and ifnull(u.phoneNumber,'') like ?3 and u.sex in(?4) and u.appid=?5 "+
            "  and u.subscribeTime between ?6 and ?7  "
    )
    Page findAll(String nickname, String username, String phoneNumber, List<Integer> sex, String appid, Date startTime, Date endTime, Pageable pageable);*/


	@Query(value = "select * from wx_user where shop_user_id=?1 ", nativeQuery = true)
	List<WxUser> findWxUserByShopUserId(Integer shopUserId);

	WxUser findByAppidAndUserId(String appId,Integer userId);

}
