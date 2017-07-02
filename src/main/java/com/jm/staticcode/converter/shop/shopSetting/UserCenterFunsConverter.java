package com.jm.staticcode.converter.shop.shopSetting;

import com.jm.mvc.vo.shop.shopSet.UserCenterFunsVo;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class UserCenterFunsConverter {

	public static UserCenterFunsVo toShopTemplateVo(UserCenterFuns userCenterFuns) {
		UserCenterFunsVo userCenterFunsVo = new UserCenterFunsVo();
		BeanUtils.copyProperties(userCenterFuns, userCenterFunsVo);
		return userCenterFunsVo;
	}

}
