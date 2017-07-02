package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.wx.level.WxUserLevelCo;
import com.jm.mvc.vo.wx.level.WxUserLevelUo;
import com.jm.mvc.vo.wx.level.WxUserLevelVo;
import com.jm.repository.po.wx.WxUserLevel;

/**
 * 微信用户等级转换
 * @author chenyy
 *
 */
public class WxUserLevelConverter {
	
	public static WxUserLevelVo toWxUserLevelVo(WxUserLevel wxUserLevel){
		WxUserLevelVo wxUserLevelVo = new WxUserLevelVo();
		BeanUtils.copyProperties(wxUserLevel,wxUserLevelVo);
		return wxUserLevelVo;
	}
	
	public static List<WxUserLevelVo> listToWxUserLevelVo(List<WxUserLevel> wxUserLevels){
		List<WxUserLevelVo> vos = new ArrayList<>();
		
		for(WxUserLevel wxUserLevel : wxUserLevels){
			WxUserLevelVo wxUserLevelVo = new WxUserLevelVo();
			BeanUtils.copyProperties(wxUserLevel,wxUserLevelVo);
			vos.add(wxUserLevelVo);
		}
		return vos;
	}
	
	public static WxUserLevel coToWxUserLevel(WxUserLevelCo wxUserLevelCo){
		WxUserLevel  userLevel = new WxUserLevel();
		BeanUtils.copyProperties(wxUserLevelCo,userLevel);
		return userLevel;
	}
	
	public static WxUserLevel uoToWxUserLevel(WxUserLevelUo wxUserLevelUo){
		WxUserLevel  userLevel = new WxUserLevel();
		BeanUtils.copyProperties(wxUserLevelUo,userLevel);
		return userLevel;
	}
}
