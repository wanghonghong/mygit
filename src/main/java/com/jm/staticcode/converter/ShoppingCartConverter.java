/**
 * 
 */
package com.jm.staticcode.converter;

import com.jm.repository.po.order.ShoppingCart;
import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.ShoppingCartCreateVo;
import com.jm.mvc.vo.ShopCartForUpdateVo;

/**
 * 
 *<p></p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
public class ShoppingCartConverter {
	
	 public static ShoppingCart toShopCart(ShoppingCartCreateVo shopCartVo) {
	        ShoppingCart shoppingCart = new ShoppingCart();
	        BeanUtils.copyProperties(shopCartVo,shoppingCart);
	        return shoppingCart;
	    }

    public static ShoppingCart toShopCart(ShopCartForUpdateVo shopCartVo) {
    	  ShoppingCart ShoppingCart = new ShoppingCart();
          BeanUtils.copyProperties(shopCartVo,ShoppingCart);
          return ShoppingCart;
    }
}
