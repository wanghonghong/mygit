package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.commQrcode.ChannelRo;
import com.jm.mvc.vo.shop.commQrcode.EffectUserRo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQrcodeCo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQrcodeRo;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GoodsQrcodeConverter {

    public static PageItem<ChannelRo> p2vs(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<ChannelRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<ChannelRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ChannelRo customerRo =   JsonMapper.map2Obj(map,ChannelRo.class);
            customerRo.setNickname(Base64Util.getFromBase64(customerRo.getNickname()));
            list.add(customerRo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

    public static PageItem<ChannelQrcodeRo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<ChannelQrcodeRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<ChannelQrcodeRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ChannelQrcodeRo ro =   JsonMapper.map2Obj(map,ChannelQrcodeRo.class);
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
            list.add(ro);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }


    public static ChannelQrcode toPo(ChannelQrcodeCo co){
        ChannelQrcode po = new ChannelQrcode();
        po.setCreateTime(new Date());
        BeanUtils.copyProperties(co,po);
        return po;
    }


    public static PageItem<EffectUserRo> p2EffectUser(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<EffectUserRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<EffectUserRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            EffectUserRo ro =   JsonMapper.map2Obj(map,EffectUserRo.class);
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
            list.add(ro);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

}
