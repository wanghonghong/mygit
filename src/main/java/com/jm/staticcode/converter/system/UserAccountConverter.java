package com.jm.staticcode.converter.system;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.UserAccountVo;
import com.jm.repository.po.system.user.UserAccount;

public class UserAccountConverter {
	
	public static UserAccountVo p2v(UserAccount account){
		UserAccountVo vo = new UserAccountVo();
		BeanUtils.copyProperties(account, vo);
		return vo;
	}
}
