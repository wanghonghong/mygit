package com.jm.business.service.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.wx.WxPubAccountRepository;
import com.jm.repository.po.wx.WxPubAccount;

@Service
public class WxpublicAccountService {
	
	@Autowired
	private WxPubAccountRepository pubAccountRepository;
	
	public WxPubAccount findWxPubAccountByAppid(String appid){
		return pubAccountRepository.findOne(appid);
	}
	
	public WxPubAccount saveAccount(WxPubAccount account){
		return pubAccountRepository.save(account);
	}

}
