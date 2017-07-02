package com.jm.staticcode.converter;

import com.jm.mvc.vo.user.ShopUserRo;
import com.jm.repository.po.shop.ShopUser;
import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 */
public class ShopUserConverter {

    public static ShopUserRo toShopUserVo(ShopUser shopUser) {
        ShopUserRo ro = new ShopUserRo();
        BeanUtils.copyProperties(shopUser,ro);
        return ro;
    }

}
