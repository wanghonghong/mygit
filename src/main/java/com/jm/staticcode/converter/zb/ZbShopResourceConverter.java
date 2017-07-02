package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.zb.system.ZbShopResourceVo;
import com.jm.repository.po.zb.system.ZbShopResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.zb.ZbCosBaseConf;
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
public class ZbShopResourceConverter {

    public static List<ZbShopResourceVo> p2v(List<ZbShopResource> zbShopResources, Integer compress) {
        List<ZbShopResourceVo> shopResourceRos = new ArrayList<>();
        if (zbShopResources !=null && zbShopResources.size()>0){
            for (ZbShopResource zbShopResource : zbShopResources){
                ZbShopResourceVo zbShopResourceVo = new ZbShopResourceVo();
                BeanUtils.copyProperties(zbShopResource, zbShopResourceVo);
                if(1== zbShopResourceVo.getResType()||5== zbShopResourceVo.getResType()){
                    if (StringUtils.isNotEmpty(zbShopResourceVo.getResUrl())){
                        if (zbShopResourceVo.getResUrl().contains("/image/")){
                            zbShopResourceVo.setResUrl(ZbCosBaseConf.COS_PATH+ zbShopResourceVo.getResUrl());
                        }else{
                            if (compress==null){
                                zbShopResourceVo.setResUrl(Constant.IMAGE_URL+ zbShopResourceVo.getResUrl());
                            }else if (compress==5){
                                zbShopResourceVo.setResUrl(ImgUtil.compress720(Constant.IMAGE_URL+ zbShopResourceVo.getResUrl()));
                            }else {
                                zbShopResourceVo.setResUrl(Constant.IMAGE_URL+ zbShopResourceVo.getResUrl());
                            }
                        }
                    }
                }else if(3== zbShopResource.getResType()){
                    zbShopResourceVo.setResUrl(ZbCosBaseConf.COS_PATH+ zbShopResourceVo.getResUrl());
                }
                shopResourceRos.add(zbShopResourceVo);
            }
        }
        return shopResourceRos;
    }
}
