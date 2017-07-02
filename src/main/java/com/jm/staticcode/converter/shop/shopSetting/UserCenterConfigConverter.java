package com.jm.staticcode.converter.shop.shopSetting;

import com.jm.mvc.vo.shop.shopSet.UserCenterConfigCo;
import com.jm.mvc.vo.shop.shopSet.UserCenterConfigUo;
import com.jm.mvc.vo.shop.shopSet.UserCenterConfigVo;
import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 *
 * @author zhengww
 */
public class UserCenterConfigConverter {

    public static UserCenterConfigVo toUserCenterConfigVo(UserCenterConfig userCenterConfig) {
        UserCenterConfigVo userCenterConfigVo = new UserCenterConfigVo();
        BeanUtils.copyProperties(userCenterConfig, userCenterConfigVo);
        if (StringUtil.isNotNull(userCenterConfigVo.getTopPic())) {
            String topPic = ImgUtil.appendUrl(userCenterConfigVo.getTopPic(), 720);
            userCenterConfigVo.setTopPic(topPic);
        }
        return userCenterConfigVo;
    }

    public static UserCenterConfig toUserCenterConfig(UserCenterConfigCo userCenterConfigCo) {
        UserCenterConfig userCenterConfig = new UserCenterConfig();
        BeanUtils.copyProperties(userCenterConfigCo, userCenterConfig);
        if (StringUtil.isNotNull(userCenterConfig.getTopPic())) {
            String topPic = ImgUtil.substringUrl(userCenterConfig.getTopPic());
            userCenterConfig.setTopPic(topPic);
        }
        return userCenterConfig;
    }

    public static UserCenterConfig toUserCenterConfig(UserCenterConfigUo userCenterConfigUo, UserCenterConfig userCenterConfig) {
        BeanUtils.copyProperties(userCenterConfigUo, userCenterConfig);
        if (StringUtil.isNotNull(userCenterConfig.getTopPic())) {
            String topPic = ImgUtil.substringUrl(userCenterConfig.getTopPic());
            userCenterConfig.setTopPic(topPic);
        }
        return userCenterConfig;
    }
}
