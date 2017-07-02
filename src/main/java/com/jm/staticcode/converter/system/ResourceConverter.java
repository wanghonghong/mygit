package com.jm.staticcode.converter.system;


import com.jm.mvc.vo.shop.resource.ResourceCo;
import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.mvc.vo.system.LeftMenuVo;
import com.jm.repository.po.shop.ShopResource;
import com.jm.repository.po.system.user.Resource;
import com.jm.repository.po.system.user.JmResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Resource的bean转化</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/24
 */
public class ResourceConverter {

    public static LeftMenuVo toLeftMenu(Resource resource){
        LeftMenuVo leftmenu = new LeftMenuVo();
        BeanUtils.copyProperties(resource,leftmenu);
        return leftmenu;
    }

    public static LeftMenuVo toLeftMenu(JmResource resource){
        LeftMenuVo leftmenu = new LeftMenuVo();
        BeanUtils.copyProperties(resource,leftmenu);
        return leftmenu;
    }

    public static List<ShopResourceRo> p2v(List<ShopResource> shopResources,Integer compress) {
        List<ShopResourceRo> shopResourceRos = new ArrayList<>();
        if (shopResources!=null && shopResources.size()>0){
            for (ShopResource shopResource : shopResources){
                ShopResourceRo shopResourceRo = new ShopResourceRo();
                BeanUtils.copyProperties(shopResource,shopResourceRo);
                if(1==shopResourceRo.getResType()||5==shopResourceRo.getResType()){
                    if (StringUtils.isNotEmpty(shopResourceRo.getResUrl())){
                        if (shopResourceRo.getResUrl().contains("/image/")){
                            shopResourceRo.setResUrl(Constant.COS_PATH+shopResourceRo.getResUrl());
                        }else{
                            if (compress==null){
                                shopResourceRo.setResUrl(Constant.IMAGE_URL+shopResourceRo.getResUrl());
                            }else if (compress==5){
                                shopResourceRo.setResUrl(ImgUtil.compress720(Constant.IMAGE_URL+shopResourceRo.getResUrl()));
                            }else {
                                shopResourceRo.setResUrl(Constant.IMAGE_URL+shopResourceRo.getResUrl());
                            }
                        }
                    }
                }else if(3==shopResource.getResType()){
                    shopResourceRo.setResUrl(Constant.COS_PATH+shopResourceRo.getResUrl());
                }
                shopResourceRos.add(shopResourceRo);
            }
        }
        return shopResourceRos;
    }

    /**
     * 获取资源对象
     * @param fileId
     * @param resourceCo
     * @return
     */
    public static ShopResource toShopResource(String fileId,ResourceCo resourceCo){
        ShopResource shopResource = toShopResource(resourceCo);
        shopResource.setResGroupId(resourceCo.getGroupId());
        shopResource.setResUrl(fileId);
        return shopResource;
    }

    /**
     * 获取资源对象
     * @param resourceCo
     * @return
     */
    public static ShopResource toShopResource(ResourceCo resourceCo){
        ShopResource shopResource = new ShopResource();
        BeanUtils.copyProperties(resourceCo,shopResource);
        shopResource.setResGroupId(resourceCo.getGroupId());
        return shopResource;
    }

}
