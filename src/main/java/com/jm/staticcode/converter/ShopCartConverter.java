/**
 * 
 */
package com.jm.staticcode.converter;

import com.jm.repository.po.order.ShoppingCart;
import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.ShopCartForCreateVo;
import com.jm.mvc.vo.ShopCartForUpdateVo;

/**
 * 
 *<p></p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
public class ShopCartConverter {
	
	public static ShoppingCart toShopCart(ShopCartForCreateVo shopCartVo) {
		ShoppingCart ShoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shopCartVo,ShoppingCart);
		return ShoppingCart;
	}

    public static ShoppingCart toUser(ShopCartForUpdateVo shopCartVo) {
    	  ShoppingCart ShoppingCart = new ShoppingCart();
          BeanUtils.copyProperties(shopCartVo,ShoppingCart);
          return ShoppingCart;
    }
}
