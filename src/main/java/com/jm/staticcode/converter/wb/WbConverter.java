/**
 * 
 */
package com.jm.staticcode.converter.wb;

import com.jm.repository.client.dto.wb.WbUserDto;
import com.jm.repository.client.dto.wb.WbGroupDto;
import com.jm.repository.client.dto.wb.WbUserGroupDto;
import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserGroup;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 *<p></p>
 *
 * @author whh
 * @version latest
 * @data 2017年3月6日
 */
public class WbConverter {
	
	public static WbUser toWbUser(WbUserDto wbUserDto) {
		WbUser wbUser = new WbUser();
		BeanUtils.copyProperties(wbUserDto,wbUser);
		return wbUser;
	}

	public static WbUserRel toWbUserRel(Long pid,WbUserDto wbUserDto) {
		WbUserRel wbUserRel = new WbUserRel();
		wbUserRel.setPid(pid);
		wbUserRel.setUid(wbUserDto.getId());
		wbUserRel.setIsSubscribe(wbUserDto.getSubscribe());
		wbUserRel.setSubscribeTime(Toolkit.timestampToDate(wbUserDto.getSubscribeTime()));
		if (wbUserDto.getUnSubscribeTime()!=null){
			wbUserRel.setUnSubscribeTime(Toolkit.strToDate2(wbUserDto.getUnSubscribeTime()));
		}
		return wbUserRel;
	}

	public static WbUserRel toWbUserRelTwo(WbUserRel wbUserRel,Long pid,WbUserDto wbUserDto) {
		wbUserRel.setPid(pid);
		wbUserRel.setUid(wbUserDto.getId());
		wbUserRel.setIsSubscribe(wbUserDto.getSubscribe());
		wbUserRel.setSubscribeTime(Toolkit.timestampToDate(wbUserDto.getSubscribeTime()));
		if (wbUserDto.getUnSubscribeTime()!=null){
			wbUserRel.setUnSubscribeTime(Toolkit.strToDate2(wbUserDto.getUnSubscribeTime()));
		}
		return wbUserRel;
	}

	public static List<WbUserGroup> toWbUserGroupList(WbGroupDto wbGroupDto, long wbUid) {
		List<WbGroupDto> wbGroupDtos = wbGroupDto.getGroups();
		List<WbUserGroup> groups = new ArrayList<>();
		for (WbGroupDto dto : wbGroupDtos){
			WbUserGroup wbUserGroup = new WbUserGroup();
			Toolkit.copyPropertiesIgnoreNull(dto,wbUserGroup);
			wbUserGroup.setGroupid(dto.getId());
			wbUserGroup.setWbUid(wbUid);
			groups.add(wbUserGroup);
		}
		return groups;
	}

	public static WbUserGroup toWbUserGroup(WbUserGroupDto wbUserGroupDto,Long wbUid) {
		WbUserGroup wbUserGroup = new WbUserGroup();
		Map<String,String> group = wbUserGroupDto.getGroup();
		wbUserGroup.setGroupid(Long.valueOf(group.get("id")));
		wbUserGroup.setName(group.get("name"));
		wbUserGroup.setRuleType(2);
		wbUserGroup.setWbUid(wbUid);
		return wbUserGroup;
	}

}
