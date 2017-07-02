package com.jm.staticcode.converter.online.market;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.online.market.ShoppingCartListVo;
import com.jm.mvc.vo.online.market.ShoppingCartVo;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/9
 */
public class ShoppingCartConverter {

    public static List<ShoppingCartListVo> p2v(List<Map<String,Object>> maps) throws  IOException {
        List<ShoppingCartListVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ShoppingCartListVo shoppingCartListVo = JsonMapper.map2Obj(map,ShoppingCartListVo.class);
            if(!StringUtils.isEmpty(map.get("spec_pic"))) {
                shoppingCartListVo.setPicSquare(ImgUtil.appendUrl(map.get("spec_pic").toString(),100));
            }else{
                shoppingCartListVo.setPicSquare(ImgUtil.appendUrl(shoppingCartListVo.getPicSquare(),100));
            }
            if(shoppingCartListVo.getType()==0){
                shoppingCartListVo.setTotalPrice(shoppingCartListVo.getPrice()*shoppingCartListVo.getCount());
            }
            list.add(shoppingCartListVo);
        }
        return list;
    }

    public static PageItem<ShoppingCartVo> p2v(PageItem<Map<String,Object>> pageItemMap,List<ShoppingCartListVo> ls) throws IOException {
        PageItem<ShoppingCartVo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<ShoppingCartVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ShoppingCartVo shoppingCartVo = JsonMapper.map2Obj(map,ShoppingCartVo.class);
            List<ShoppingCartListVo> shoppingCartListVoList = new ArrayList<>();
            for(ShoppingCartListVo shoppingCartListVo:ls){
                if(!StringUtils.isEmpty(shoppingCartListVo.getUserId())){
                    if(shoppingCartListVo.getUserId().equals(shoppingCartVo.getUserId())){
                        shoppingCartListVoList.add(shoppingCartListVo);
                    }
                }
            }
            if(!StringUtils.isEmpty(map.get("nickname"))){
                shoppingCartVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
            }
            if(!StringUtils.isEmpty(map.get("headimgurl"))) {
                shoppingCartVo.setHeadimgurl(map.get("headimgurl").toString());
            }
            shoppingCartVo.setShoppingCartListVoList(shoppingCartListVoList);
            list.add(shoppingCartVo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }
}
