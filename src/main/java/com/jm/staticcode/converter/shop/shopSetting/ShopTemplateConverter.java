package com.jm.staticcode.converter.shop.shopSetting;

import com.jm.mvc.vo.shop.shopSet.ShopTemplateVo;
import com.jm.repository.po.shop.shopSet.ShopTemplate;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ShopTemplateConverter {

	public static ShopTemplateVo toShopTemplateVo(
			ShopTemplate shopTemplate) {
		ShopTemplateVo shopTemplateVo = new ShopTemplateVo();
		BeanUtils.copyProperties(shopTemplate, shopTemplateVo);
		return shopTemplateVo;
	}
	
}
