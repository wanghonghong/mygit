package com.jm.business.service.system;

import com.jm.business.service.wx.WxService;
import com.jm.mvc.vo.system.ButtonVo;
import com.jm.mvc.vo.system.LeftMenuVo;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.jpa.resource.JmResourceRepository;
import com.jm.repository.jpa.system.*;
import com.jm.repository.po.shop.WxMenu;
import com.jm.repository.po.system.user.JmResource;
import com.jm.staticcode.converter.system.ResourceConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>创建微信菜单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016-8-10 15:42:51
 */
@Service
public class SystemService {

    @Autowired
    private WxMenuRepository wxMenuRepository;

    @Autowired
    private WxService wxService;

    @Autowired
    private JmResourceRepository jmResourceRepository;

    @Transactional
    public void saveMenu(ButtonVo[] buttonVos, String appid)throws Exception{
        wxMenuRepository.deleteByAppid(appid);
        if(buttonVos.length>0){
            List<WxMenu> subList = new ArrayList<>();
            for(ButtonVo buttonVo : buttonVos){
                WxMenu wxMenu = new WxMenu();
                BeanUtils.copyProperties(buttonVo,wxMenu);
                wxMenu.setAppid(appid);
                wxMenu.setWxKey(buttonVo.getKey());
                WxMenu wxMenuNew = wxMenuRepository.save(wxMenu); //保存父级菜单
                //保存子菜单
                ButtonVo[] subButtons = buttonVo.getSubButton();
                if(null!=subButtons && subButtons.length>0){
                    for(ButtonVo subButton : subButtons ){
                        WxMenu subMenu = new WxMenu();
                        BeanUtils.copyProperties(subButton,subMenu);
                        subMenu.setAppid(appid);
                        subMenu.setParentId(wxMenuNew.getId());
                        subMenu.setWxKey(subButton.getKey());
                        subList.add(subMenu);
                    }
                }
            }
            wxMenuRepository.save(subList);
        }
    }

    public ResultMsg createMenu(ButtonVo[] buttonVos, String appid, Integer shopId)throws Exception{
        ResultMsg resultMsg = wxService.createMenu(buttonVos,appid,shopId);
        saveMenu(buttonVos,appid);
        return resultMsg;
    }

    //获取菜单
    public List<ButtonVo> getMenu(String appid) throws Exception {
        List<WxMenu> wxMenus = wxMenuRepository.findWxMenuByAppid(appid);
        if (wxMenus==null || wxMenus.size()==0){  //没有菜单去微信取
            return wxService.getMenu(appid);
        }
        List<ButtonVo> buttonVos = new ArrayList<ButtonVo>();
        //添加父级菜单
        for(WxMenu wxMenu:wxMenus) {
            if (wxMenu.getParentId()==0){
                ButtonVo buttonVo = new ButtonVo();
                BeanUtils.copyProperties(wxMenu,buttonVo);
                buttonVo.setKey(wxMenu.getWxKey());
                buttonVos.add(buttonVo);
            }
        }
        //添加子级菜单
        for (ButtonVo buttonVo : buttonVos){
            List<ButtonVo> subButtonList = new ArrayList<>();
            for(WxMenu wxMenu:wxMenus) {
                if (wxMenu.getParentId()==buttonVo.getId()){
                    ButtonVo subButton = new ButtonVo();
                    BeanUtils.copyProperties(wxMenu,subButton);
                    subButton.setKey(wxMenu.getWxKey());
                    subButtonList.add(subButton);
                }
                if (!subButtonList.isEmpty()){
                    buttonVo.setSubButton(subButtonList.toArray( new ButtonVo[subButtonList.size()]) );
                }
            }
        }
        return buttonVos;
    }

    public List<JmResource> findByParentResourceIdAndStatus(Integer parentResourceId, Integer status){
        return jmResourceRepository.findByParentResourceIdAndStatus(parentResourceId,status);
    }

    /**
     * 左菜单列表
     * @param parentResourceId
     * @param status
     * @return
     */
    public List<LeftMenuVo> findLeftMenu(Integer parentResourceId, Integer status){
        List<JmResource>  resours=  jmResourceRepository.findByParentResourceIdAndStatus(parentResourceId,status);
        List<LeftMenuVo> leftmenus = new ArrayList<LeftMenuVo>();
        for (JmResource resource : resours) {
            LeftMenuVo leftmenu =  ResourceConverter.toLeftMenu(resource);
            //左菜单的子菜单
            List<JmResource>  sonMenu=  jmResourceRepository.findByParentResourceIdAndStatus(leftmenu.getResourceId(),status);
            leftmenu.setResources(sonMenu);
            leftmenus.add(leftmenu);
        }
        return leftmenus;
    }

    public List<WxMenu> findAllMenu(){
    	return wxMenuRepository.findAll();
    }
    
    
    public List<WxMenu> saveMenus(List<WxMenu> menus){
    	return wxMenuRepository.save(menus);
    }



}
