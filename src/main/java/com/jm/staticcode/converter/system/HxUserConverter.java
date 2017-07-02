package com.jm.staticcode.converter.system;

import com.jm.mvc.vo.HxUserVo;
import com.jm.mvc.vo.JmUserSession;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 */
public class HxUserConverter {

    public static HxUserVo toHxUser(JmUserSession userVo) {
        HxUserVo hxUserVo = new HxUserVo();
        BeanUtils.copyProperties(userVo,hxUserVo);
        if(userVo.getHeadImg()!=null){
            if (!userVo.getHeadImg().contains(Constant.IMAGE_URL)){
                hxUserVo.setHeadImg(ImgUtil.appendUrl(userVo.getHeadImg()));
            }
        }
        return hxUserVo;
    }

}
