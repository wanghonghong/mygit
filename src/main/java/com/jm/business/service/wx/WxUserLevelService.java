package com.jm.business.service.wx;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.wx.WxUserLevelRepositor;
import com.jm.repository.po.wx.WxUserLevel;

/**
 * 用户等级service
 * @author chenyy
 *
 */
@Slf4j
@Service
public class WxUserLevelService {
		
	@Autowired
	private WxUserLevelRepositor levelRepositor;
	
	/**
	 * 获取所有用户等级
	 * @param appid
	 * @return
	 */
	public List<WxUserLevel> findAllWxUserLevel(String appid){
		return levelRepositor.findByAppid(appid);
	}
	
	/**
	 * 新增
	 * @param wxUserLevel
	 */
	public void saveLevel(WxUserLevel wxUserLevel){
		levelRepositor.save(wxUserLevel);
	}
	
	public WxUserLevel findWxUserLevelById(Integer id){
		return levelRepositor.findOne(id);
	}
	
	public void deleteLevel(Integer id){
		levelRepositor.delete(id);
	}

	public void setDefaultLevel(String appid){
		WxUserLevel level1 = new WxUserLevel();
		level1.setAppid(appid);
		level1.setLevelName("金");
		WxUserLevel level2 = new WxUserLevel();
		level2.setAppid(appid);
		level2.setLevelName("银");
		WxUserLevel level3 = new WxUserLevel();
		level3.setAppid(appid);
		level3.setLevelName("铜");
		levelRepositor.save(level1);
		levelRepositor.save(level2);
		levelRepositor.save(level3);
	}
	
}
