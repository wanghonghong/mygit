package com.jm.staticcode.converter.order;

import com.jm.mvc.vo.shop.ContactusUo;
import com.jm.mvc.vo.shop.ShopForCreateVo;
import com.jm.mvc.vo.shop.ShopForUpdateVo;
import com.jm.mvc.vo.shop.shopSet.ShopVo;
import com.jm.repository.po.shop.Shop;

import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/13
 */
public class ShopConverter {

    public static Shop toShop(ShopForCreateVo shopVo) {
        Shop shop = new Shop();
        BeanUtils.copyProperties(shopVo,shop);
        return shop;
    }

    
    public static Shop toShop(ShopForUpdateVo shopVo) {
        Shop shop = new Shop();
        BeanUtils.copyProperties(shopVo,shop);
        return shop;
    }

    public static ContactusUo toContactusUo( Shop shop) {
        ContactusUo uo = new ContactusUo();
        BeanUtils.copyProperties(shop,uo);
        uo.setImgUrl(ImgUtil.appendUrl(shop.getImgUrl(),720));
        return uo;
    }

    public static ShopVo toShopVo(Shop shop) {
        ShopVo shopVo = new ShopVo();
        BeanUtils.copyProperties(shop,shopVo);
        return shopVo;
    }
}
