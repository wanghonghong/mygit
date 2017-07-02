package com.jm.staticcode.converter.shop.shopResource;

import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.repository.po.shop.ShopResource;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>素材库bean转化</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/8 15:17
 */
public class ShopResourceConverter {

    public static List<ShopResourceRo> p2v(List<Map<String,Object>> maps) throws IOException {
        List<ShopResourceRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ShopResource shopResource = JsonMapper.map2Obj(map,ShopResource.class);
            ShopResourceRo srr = new ShopResourceRo();
            BeanUtils.copyProperties(shopResource,srr);
            srr.setResUrl(ImgUtil.appendUrl(srr.getResUrl()));
            list.add(srr);
        }
        return list;
    }

}
