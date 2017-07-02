package com.jm.staticcode.converter.online;


import com.jm.mvc.vo.online.HxUserCo;
import com.jm.mvc.vo.online.HxUserUo;
import com.jm.mvc.vo.online.HxUserVo;
import com.jm.repository.po.online.HxUser;
import org.springframework.beans.BeanUtils;

public class HxImUserConverter {

    public static HxUser co2po(HxUserCo hxUserCo){
        HxUser hxUser = new HxUser();
        BeanUtils.copyProperties(hxUserCo,hxUser);
        return hxUser;
    }

    public static HxUser uo2po(HxUserUo hxUserUo){
        HxUser hxUser = new HxUser();
        BeanUtils.copyProperties(hxUserUo,hxUser);
        return hxUser;
    }

    public static HxUserVo po2vo(HxUser hxUser){
        HxUserVo hxUserVo = new HxUserVo();
        BeanUtils.copyProperties(hxUser,hxUserVo);
        return hxUserVo;
    }

    public static HxUser vo2po(HxUserVo hxUserVo){
        HxUser hxUser = new HxUser();
        BeanUtils.copyProperties(hxUserVo,hxUser);
        return hxUser;
    }


    public static HxUserUo vo2uo(HxUserVo hxUserVo) {
        HxUserUo hxUserUo = new HxUserUo();
        BeanUtils.copyProperties(hxUserVo,hxUserUo);
        return hxUserUo;
    }

}
