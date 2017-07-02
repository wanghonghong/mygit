package com.jm.staticcode.converter.system;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.repository.po.shop.ShopResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/9 10:01
 */
public class UploadFileConverter {

    public static PageItem<ShopResourceRo> toShopResourceRo(Page<ShopResource> shopResourcePage){
        PageItem<ShopResourceRo>  pageItem = new PageItem<>();
        if(null != shopResourcePage){
            List<ShopResource> shopResourceList = shopResourcePage.getContent();
            List<ShopResourceRo> shopResourceListNew = new ArrayList();
            for (ShopResource sr:shopResourceList) {
                ShopResourceRo srNew = new ShopResourceRo();
                BeanUtils.copyProperties(sr,srNew);
                if(2==srNew.getResType() || 4==srNew.getResType()){
                    shopResourceListNew.add(srNew);
                }else if(3==srNew.getResType()){
                    srNew.setResUrl(Constant.COS_PATH+srNew.getResUrl());
                    shopResourceListNew.add(srNew);
                }else{ //图片
                    if (StringUtils.isNotEmpty(srNew.getResUrl())) {
                        srNew.setResUrl(ImgUtil.appendUrls(srNew.getResUrl(),200));
                    }
                    shopResourceListNew.add(srNew);
                }
            }
            pageItem.setCount(Toolkit.parseObjForInt(shopResourcePage.getTotalElements()));
            pageItem.setItems(shopResourceListNew);
            return pageItem;
        }
        pageItem.setCount(0);
        return pageItem;
    }
}
