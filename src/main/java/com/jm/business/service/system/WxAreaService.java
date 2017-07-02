package com.jm.business.service.system;

import com.jm.business.domain.WxAreaDo;
import com.jm.mvc.vo.system.WxAreaVo;
import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.jpa.system.WxAreaRepository;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.system.WxArea;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.wx.WxAreaConverter;
import com.jm.staticcode.util.Toolkit;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>微信地区服务层</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/10 9:53
 */
@Service
public class WxAreaService {

    @Autowired
    private WxAreaRepository wxAreaRepository;
   
    public List<WxArea> findAll(){
    	return wxAreaRepository.findAll();
    }
    public List findByParentAreaId(Integer pAreaId){
        return wxAreaRepository.findByParentAreaId(pAreaId);
    };

    @ApiOperation("根据ids查询它的下级节点")
    public List findByPareaIds(String[] arr) {
        List optionIdsList = new ArrayList();
        if(arr.length>0){
            for (String areaId:arr) {
                String flag = areaId.substring(4);
                if("00".equals(flag)){
                    List areaIds =  this.findByParentAreaId(Toolkit.parseObjForInt(areaId));
                    for (Object obj : areaIds) {
                        optionIdsList.add(Toolkit.parseObjForStr(obj));
                    }
                }else{
                    optionIdsList.add(areaId);
                }

            }
        }
        return optionIdsList;
    }
    @ApiOperation("从静态微信地区--根据（父）ids查询它的下级节点")
    public List findStaticByPareaIds(String[] arr) {
        List optionIdsList = new ArrayList();
        if(arr.length>0){
            for (String areaId:arr) {
                String flag = areaId.substring(4);
                if("00".equals(flag)){
                    for (WxAreaDo wxArea : Constant.WX_AREA_LIST) {
                        if (Toolkit.parseObjForInt(areaId).intValue()==wxArea.getPAreaId()){
                            optionIdsList.add(wxArea.getAreaId());
                        }
                    }
                }
                optionIdsList.add(areaId);
            }
        }
        return optionIdsList;
    }

}
