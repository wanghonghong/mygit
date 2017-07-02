package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.commQrcode.*;
import com.jm.repository.po.shop.CommonQrcode;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommonQrcodeConverter {

    public static CommonQrcode toPo(CommonQrcodeCo co){
        CommonQrcode po = new CommonQrcode();
        po.setCreateTime(new Date());
        BeanUtils.copyProperties(co,po);
        return po;
    }

    public static PageItem<CommonQrcodeRo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<CommonQrcodeRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<CommonQrcodeRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CommonQrcodeRo ro =   JsonMapper.map2Obj(map,CommonQrcodeRo.class);
            list.add(ro);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }


    public static PageItem<CommonQrcodeDetailRo> p2vs(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<CommonQrcodeDetailRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<CommonQrcodeDetailRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CommonQrcodeDetailRo ro =   JsonMapper.map2Obj(map,CommonQrcodeDetailRo.class);
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
            list.add(ro);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }


}
