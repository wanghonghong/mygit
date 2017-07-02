package com.jm.business.service.shop;

import java.util.List;
import java.util.Map;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxPubAccountVo;
import com.jm.mvc.vo.shop.WxPubAccountRo;
import com.jm.mvc.vo.shop.WxPubAccountUo;

import com.jm.mvc.vo.shop.activity.ActivityUserVo;
import com.jm.mvc.vo.wx.content.WxContentSentVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.converter.wx.WxContentSentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.jm.repository.jpa.wx.WxPubAccountRepository;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.staticcode.converter.shop.WxPubAccountConverter;

/**
 * <p>微信公众账号信息</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */
@Service
public class WxPubAccountService {
	 @Autowired
	 private WxPubAccountRepository wxPubAccountRepository;
	 @Autowired
	 JdbcTemplate jdbcTemplate;
	@Autowired
	protected JdbcUtil jdbcUtil;

	public WxPubAccount getWxPubAccount(String appid){
		return wxPubAccountRepository.getOne(appid);
	}

	@Cacheable(value ="wx_pub_account", key="#appid")
	public WxPubAccount getCacheWxPubAccount(String appid){
		return wxPubAccountRepository.getOne(appid);
	}

	 public WxPubAccount findWxPubAccountByAppid(String appid){
		return wxPubAccountRepository.findOne(appid);
	 }

	 public List<WxPubAccount> findByTypeAndIsAtuth(int type,int isAuth){
		 return wxPubAccountRepository.findByTypeAndIsAuth(type,isAuth);
	 }
	 
	 public WxPubAccount save(WxPubAccount wxPubAccount){
		 return wxPubAccountRepository.save(wxPubAccount);
	 }

	 public WxPubAccount updateWxPubAccount(WxPubAccountUo uo, String appid){
		 WxPubAccount wxPubAccount = findWxPubAccountByAppid(appid);
		 wxPubAccount.setAppKey(uo.getAppKey());
		 wxPubAccount.setMchId(uo.getMchId());
		 if(uo.getIssub().equals(2)){//代申请的微信支付账户
			 wxPubAccount.setIsSub(1);
		 }
		 if(uo.getIssub().equals(3)){//自有企业支付账户
			 wxPubAccount.setIsSub(0);
		 }
		 if(uo.getIssub().equals(4)){//委托申请
			 wxPubAccount.setIsSub(2);
		 }
		 return wxPubAccountRepository.save(wxPubAccount);
	 }


	 public List<WxPubAccount> findAll(Integer isAuth){
		 return wxPubAccountRepository.findByIsAuth(isAuth);
	 }
	 
	 /**
	  * 根据userId查询，聚客红包公众号列表
	  * @return
	  */
	 public List<WxPubAccountVo> findByUserId(Integer userId){
		 List<WxPubAccount> accounts = wxPubAccountRepository.findByTypeAndUserIdAndIsAuth(1, userId,1);
		 List<WxPubAccountVo> vos  = WxPubAccountConverter.ps2vs(accounts);
		 return vos;
	 }



	/**
	 * 查询聚客红包公众号列表
	 * @return
	 * @throws Exception
	 */
	public PageItem<WxPubAccountVo> findJkUser(WxPubAccountRo wxPubAccountRo) throws Exception{
		String sqlList = "select ac.*,u.phone_number from wx_pub_account ac " +
				" left join user u on ac.user_id = u.user_id" +
				" where ac.is_auth=1 and ac.type=1";
		StringBuilder sqlCondition = new StringBuilder();
		if(null!=wxPubAccountRo.getNickName()){
			sqlCondition.append(JdbcUtil.appendLike("au.nick_name",wxPubAccountRo.getNickName()));
		}
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,wxPubAccountRo.getCurPage(),wxPubAccountRo.getPageSize());
		PageItem<WxPubAccountVo> pageItems = WxPubAccountConverter.pos2vos(pageItem);
		return pageItems;
	}

}
