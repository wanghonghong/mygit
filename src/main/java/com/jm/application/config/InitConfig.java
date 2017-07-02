package com.jm.application.config;

import com.jm.business.domain.AreaDo;
import com.jm.business.domain.WxAreaDo;
import com.jm.business.domain.zb.ResourceDo;
import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.jpa.system.WxAreaRepository;
import com.jm.repository.jpa.wb.WbAuthRepository;
import com.jm.repository.jpa.wx.WxAuthRepository;
import com.jm.repository.jpa.zb.system.ZbResourceRepository;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.system.WxArea;
import com.jm.business.domain.ZtreeNode;
import com.jm.repository.po.wb.WbAuth;
import com.jm.repository.po.wx.WxAuth;
import com.jm.repository.po.zb.system.ZbResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.ZbConstant;
import com.jm.staticcode.util.ApplicationContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;


/**
 * <p></p>初始化配置信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/12
 */
@Configuration
public class InitConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initWxArea(event); // 初始化微信地区数据
        intiArea(event);//初始化地区和地区树
        initAllWxArea(event);
        initWbAuth(event); // 初始化微博第三方应用信息 cj 2017-03-04
        initWxAuth(event);//初始化微信开放平台配置
        intiResource(event);//总部菜单
    }

    private void initWbAuth(ContextRefreshedEvent event) {
        WbAuthRepository wbAuthRepository = event.getApplicationContext().getBean(WbAuthRepository.class);
        List<WbAuth> list = wbAuthRepository.findAll();
        if (list.size()>0){
            WbAuth wbAuth = list.get(0);
            Constant.CLIENT_ID = wbAuth.getAppKey();
            Constant.CLIENT_SECRET = wbAuth.getAppSecret();
        }
    }


    private void intiArea(ContextRefreshedEvent event) {
        AreaRepository areaRepository = event.getApplicationContext().getBean(AreaRepository.class);
        List<Area> list = areaRepository.findAll();
        for (Area area : list) {
            AreaDo areaDo = new AreaDo();
            BeanUtils.copyProperties(area,areaDo);
            Constant.AREA_LIST.add(areaDo);
        }
        for (AreaDo areaDo : Constant.AREA_LIST) {
            ZtreeNode z = new ZtreeNode();
            z.setId(areaDo.getAreaId());
            z.setPId(areaDo.getParentAreaId());
            z.setName(areaDo.getAreaName());
            z.setOpen("false");
            Constant.AREA_ZTREE_LIST.add(z);
        }
    }

    private void intiResource(ContextRefreshedEvent event) {
        ZbResourceRepository zbResourceRepository = event.getApplicationContext().getBean(ZbResourceRepository.class);
        List<ZbResource> list = zbResourceRepository.findAll();
        for (ZbResource zbResource : list) {
            ResourceDo resourceDo = new ResourceDo();
            BeanUtils.copyProperties(zbResource,resourceDo);
            ZbConstant.RESOURCE_LIST.add(resourceDo);
        }
        for (ResourceDo resourceDo : ZbConstant.RESOURCE_LIST) {
            ZtreeNode z = new ZtreeNode();
            z.setId(resourceDo.getResourceId());
            z.setPId(resourceDo.getParentResourceId());
            z.setName(resourceDo.getResourceName());
            z.setOpen("false");
            ZbConstant.RESOURCE_ZTREE_LIST.add(z);
        }
    }
    /**
     * 从数据库加载微信地区列表到内存
     * @param event
     */
    private void initWxArea(ContextRefreshedEvent event){
        WxAreaRepository wxAreaRepository = event.getApplicationContext().getBean(WxAreaRepository.class);
        List<WxArea> list = wxAreaRepository.findAll();
        for (WxArea wxArea : list){
            if (wxArea.getLevel()==3){
                WxAreaDo wxAreaDo = new WxAreaDo();
                wxAreaDo.setAreaId(wxArea.getAreaId());
                wxAreaDo.setAreaName(wxArea.getAlias());
                wxAreaDo.setParentAreaName(getParentName(list,wxArea.getParentAreaId()));
                wxAreaDo.setPAreaId(wxArea.getParentAreaId());
                Constant.WX_AREA_LIST.add(wxAreaDo);
            }
        }
    }
    /**
     * 从数据库加载微信地区列表到内存 cj  全部数据
     * @param event
     */
    private void initAllWxArea(ContextRefreshedEvent event){
        WxAreaRepository wxAreaRepository = event.getApplicationContext().getBean(WxAreaRepository.class);
        List<WxArea> list = wxAreaRepository.findAll();
        for (WxArea wxArea : list) {
            ZtreeNode z = new ZtreeNode();
            z.setId(wxArea.getAreaId());
            z.setPId(wxArea.getParentAreaId());
            z.setName(wxArea.getAreaName());
            z.setOpen("false");
            Constant.WX_AREA_LIST_ALL.add(z);
        }
    }

    private String getParentName(List<WxArea> list,Integer areaId){
        for (WxArea wxArea : list){
            if (wxArea.getAreaId().equals(areaId)){
                return wxArea.getAlias();
            }
        }
        return "";
    }

    private void initWxAuth(ContextRefreshedEvent event){
       // WxAuthRepository wxAuthRepository = ApplicationContextUtil.getApplicationContext().getBean(WxAuthRepository.class);
       WxAuthRepository wxAuthRepository = event.getApplicationContext().getBean(WxAuthRepository.class);
        WxAuth wxAuth = wxAuthRepository.findOne(1);
        if(wxAuth!=null){
            Constant.APP_DOMAIN = wxAuth.getAppDomain();
            Constant.PC_DOMAIN = wxAuth.getPcDomain()+"/msa";
            Constant.COMPONENT_APP_ID = wxAuth.getAppid();
            Constant.COMPONENT_APPSECRET = wxAuth.getAppsecret();
            Constant.DOMAIN = wxAuth.getAppDomain();
            System.out.print("====================="+Constant.APP_DOMAIN+"======================");
        }

    }

}
