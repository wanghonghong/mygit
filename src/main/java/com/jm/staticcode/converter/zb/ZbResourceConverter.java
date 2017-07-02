package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.zb.system.ZbLeftMenuVo;
import com.jm.mvc.vo.zb.system.ZbResourceCo;
import com.jm.repository.po.zb.system.ZbResource;
import org.springframework.beans.BeanUtils;

/**
 * <p>Resource的bean转化</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/24
 */
public class ZbResourceConverter {
    public static ZbResource toResource(ZbResourceCo zbResourceCo){
        ZbResource zbResource = new ZbResource();
        BeanUtils.copyProperties(zbResourceCo, zbResource);
        return zbResource;
    }

    public static ZbLeftMenuVo toLeftMenu(ZbResource zbResource){
        ZbLeftMenuVo leftmenu = new ZbLeftMenuVo();
        BeanUtils.copyProperties(zbResource,leftmenu);
        return leftmenu;
    }


}
