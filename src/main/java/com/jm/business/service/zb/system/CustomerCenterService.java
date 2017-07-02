package com.jm.business.service.zb.system;


import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.join.ZbClassDataVo;
import com.jm.mvc.vo.zb.join.ZbJoinClassVo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.repository.client.MsaClient;
import com.jm.repository.client.dto.zb.ZbClassDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by ME on 2016/8/17.
 */
@Service
public class CustomerCenterService {

    /**
     *
     * @param
     * @return
     */

    @Autowired
    private MsaClient msaClient;

    public PageItem getClassDtos(ZbJoinClassVo zbJoinClassVo) throws IOException {
        PageItem classDtos = msaClient.queryClassList(zbJoinClassVo);
        return  classDtos;
    }

    public ZbClassDataDto getClassDto(ZbJoinVo zbJoinVo) throws IOException {
        ZbClassDataDto zbClassDataDto = msaClient.queryClassData(zbJoinVo);
        return zbClassDataDto;
    }

    public JmMessage updateClassDto(ZbClassDataVo zbClassDataVo) throws IOException {
        JmMessage message = msaClient.updateClassData(zbClassDataVo);
        return  message;
    }


}
