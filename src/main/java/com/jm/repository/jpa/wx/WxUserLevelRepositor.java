package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.wx.WxUserLevel;

public interface WxUserLevelRepositor extends JpaRepository<WxUserLevel, Integer> {
	
	public List<WxUserLevel> findByAppid(String appid);

}
