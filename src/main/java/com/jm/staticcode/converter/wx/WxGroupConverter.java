package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;

import com.jm.repository.client.dto.wxuser.WxGroupDto;
import com.jm.repository.client.dto.wxuser.WxGroupListDto;
import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.wx.group.WxGroupVo;
import com.jm.repository.po.wx.WxUserGroup;

/**
 *<p>微信分组转换</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年8月13日
 */
public class WxGroupConverter {

	public static List<WxUserGroup> d2p(WxGroupListDto dto,String appid) {
		List<WxUserGroup> groupList= new ArrayList<>();
		for(WxGroupDto wxGroupDto : dto.getGroups()){
			WxUserGroup userGroup = new WxUserGroup();
			userGroup.setName(wxGroupDto.getName());
			userGroup.setGroupid(wxGroupDto.getId());
			userGroup.setCount(wxGroupDto.getCount());
			userGroup.setAppid(appid);
			groupList.add(userGroup);
		}
		return groupList;
	}

	public static WxUserGroup v2p(WxGroupVo wxGroupVo){
		WxUserGroup userGroup = new WxUserGroup();
		BeanUtils.copyProperties(wxGroupVo, userGroup);
		return userGroup;
	}

}
