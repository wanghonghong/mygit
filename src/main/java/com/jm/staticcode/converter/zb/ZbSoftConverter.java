package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.po.zb.system.ZbSoft;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class ZbSoftConverter {

    public static ZbSoft v2p(ZbSoftVo vo){
        ZbSoft zbSoft = new ZbSoft();
        BeanUtils.copyProperties(vo, zbSoft);
        zbSoft.setCreateDate(new Date());
        return zbSoft;
    }

}
