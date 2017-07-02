package com.jm.staticcode.converter;

import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.system.UserCo;
import com.jm.mvc.vo.system.UserRo;
import com.jm.repository.po.system.user.User;

import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
public class UserConverter {

    public static User toUser(UserCo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo,user);
        user.setCreateDate(new Date());
        return user;
    }


    public static JmUserSession toUserSession(User user) {
    	JmUserSession jmuserSession = new JmUserSession();
        BeanUtils.copyProperties(user,jmuserSession);
        return jmuserSession;
    }


    public static List<UserRo> toUserRos(List<Map<String, Object>> list) throws IOException {
        List<UserRo> ls = new ArrayList<UserRo>();
        for (Map<String, Object> map:list) {
            UserRo ro =   JsonMapper.map2Obj(map,UserRo.class);
            String headImg = Toolkit.parseObjForStr(map.get("head_img"));
            if(headImg.equals("")){
                headImg= Constant.THIRD_URL+"/img/pc/no_picture.png";
            }else{
                headImg = ImgUtil.appendUrl(headImg,720);
            }
            ro.setHeadImg(headImg);
            ls.add(ro);
        }
        return ls;
    }

    public static UserRo toUserRo(User user) {
        UserRo ro = new UserRo();
        if(user.getHeadImg()==null||user.getHeadImg()==""){
            user.setHeadImg(Constant.THIRD_URL+"/img/pc/no_picture.png");
        }else{
            user.setHeadImg(ImgUtil.appendUrl(user.getHeadImg(),720));
        }
        if(user.getUserName()==null){
            user.setUserName("");
        }
        BeanUtils.copyProperties(user,ro);
        return ro;
    }
}
