package com.jm.staticcode.converter.shop;

import java.util.ArrayList;
import java.util.List;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderInfoCreateVo;
import com.jm.mvc.vo.order.OrderInfoForUpdateVo;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.mvc.vo.shop.ShopEntityForCreateVo;
import com.jm.mvc.vo.shop.ShopEntityPageItem;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopEntity;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
public class ShopEntityConverter {

    public static ShopEntity toEntityStore(ShopEntityForCreateVo entityVo) {
        ShopEntity entity = new ShopEntity();
        BeanUtils.copyProperties(entityVo,entity);
        if(entityVo.getStoreImg()!=null){
            entity.setStoreImg(ImgUtil.substringUrl(entityVo.getStoreImg()));
        }
        return entity;
    }

    public static ShopEntity p2v(ShopEntity shopEntity) {
        shopEntity.setStoreImg(ImgUtil.appendUrl(shopEntity.getStoreImg()));
        return shopEntity;
    }

    public static ShopEntityPageItem<ShopEntity> p2vs(Page<ShopEntity> page,Shop shop) {
        ShopEntityPageItem<ShopEntity> pageItem = new ShopEntityPageItem<ShopEntity>();
        pageItem.setCount( Toolkit.parseObjForInt(page.getTotalElements()));
        List<ShopEntity> list =   page.getContent();
        for (ShopEntity shopEntity : list){
            shopEntity.setStoreImg(ImgUtil.appendUrl(shopEntity.getStoreImg(),720));
        }
        pageItem.setItems(list);
        pageItem.setIsEntity(shop.getIsEntity());
        return pageItem;
    }
}
