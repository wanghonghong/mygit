package com.jm.staticcode.converter.shop.shopSetting;

import com.jm.mvc.vo.shop.shopSet.ShopMainCo;
import com.jm.mvc.vo.shop.shopSet.ShopMainUo;
import com.jm.mvc.vo.shop.shopSet.ShopMainVo;
import com.jm.repository.po.shop.shopSet.ShopMain;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ShopMainConverter {

	public static ShopMainVo toShopMainVo(ShopMain shopMain) {
		ShopMainVo shopMainVo = new ShopMainVo();
		BeanUtils.copyProperties(shopMain, shopMainVo);
		return shopMainVo;
	}

	public static ShopMain toShopMian(ShopMainCo shopMainCo) {
		ShopMain shopMain = new ShopMain();
		BeanUtils.copyProperties(shopMainCo, shopMain);
		return shopMain;
	}

	public static ShopMain toShopMian(ShopMainUo shopMainUo, ShopMain shopMain) {
		BeanUtils.copyProperties(shopMainUo, shopMain);
		return shopMain;
	}
}
