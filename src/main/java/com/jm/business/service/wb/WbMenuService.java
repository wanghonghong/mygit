package com.jm.business.service.wb;

import com.jm.mvc.vo.wb.WbButtonVo;
import com.jm.repository.client.dto.wb.*;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.wb.WbMenuRepository;
import com.jm.repository.po.wb.WbMenu;
import com.jm.repository.po.wb.WbShopUser;
import com.jm.staticcode.converter.wb.WbMenuConverter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>微博菜单服务层</p>
 * @version latest
 * @Author cj
 * @Date 2017/2/27 17:04
 */
@Slf4j
@Service
public class WbMenuService {
    @Autowired
    private WbClient wbClient;
    @Autowired
    private WbShopUserService wbShopUserService;
    @Autowired
    private WbMenuRepository wbMenuRepository;

    /**
     * <p>创建微博菜单</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 14:45
     */
    public WbResultMsg createWbMenu(Integer shopId, Long uid, WbButtonVo[] buttonVos) throws Exception {
        if(buttonVos.length>0){
            WbMenuConverter.converWbMenu(buttonVos);
        }
        WbMenuVo menu = new WbMenuVo();
        menu.setButton(Arrays.asList(buttonVos));
        WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(uid);
        WbResultMsg wbResultMsg =  wbClient.createWbMenu(uid,menu,wbShopUser); //调用微博接口创建菜单
        String result = wbResultMsg.getResult();
        if("true".equals(result)){  // 微博创建菜单成功后,保存入库
             saveMenu(buttonVos,shopId);
        }
        return wbResultMsg;
    }

    /**
     * <p>调接口获取微博端菜单</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/6 12:16
     */
    public List<WbButtonVo> getWbMenu(Long uid) throws Exception {
        List<WbButtonVo> wbMenuVos = new ArrayList<WbButtonVo>();
        WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(uid);
        String accessToken = wbShopUser.getAccessToken();
        WbMenuDto wbMenuDto =  wbClient.getMenu(accessToken);  // 获取微博端的菜单接口
        wbMenuVos = wbMenuDto.getMenu().getButton();
        return wbMenuVos;
    }

    /**
     * <p>获取系统微博菜单</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/6 11:09
     */
    public List<WbButtonVo> getMenu(Integer shopId, Long uid) throws Exception {
        List<WbMenu> wbMenus = wbMenuRepository.findWbMenuByShopId(shopId);
        if (wbMenus==null || wbMenus.size()==0){  //去微博拉取
            return getWbMenu(uid);
        }
        List<WbButtonVo> buttonVos = new ArrayList<WbButtonVo>();
        for(WbMenu wbMenu:wbMenus) {
            if (wbMenu.getParentId()==0){
                WbButtonVo buttonVo = new WbButtonVo();
                BeanUtils.copyProperties(wbMenu,buttonVo);
                buttonVos.add(buttonVo);
            }
        }
        for (WbButtonVo buttonVo : buttonVos){
            List<WbButtonVo> subButtonList = new ArrayList<>();
            for(WbMenu wbMenu:wbMenus) {
                if (wbMenu.getParentId()==buttonVo.getId()){
                    WbButtonVo subButton = new WbButtonVo();
                    BeanUtils.copyProperties(wbMenu,subButton);
                    subButtonList.add(subButton);
                }
                if (!subButtonList.isEmpty()){
                    buttonVo.setSubButton(subButtonList.toArray( new WbButtonVo[subButtonList.size()]) );
                }
            }
        }
        return buttonVos;
    }

    @ApiOperation("保存菜单到本地数据库")
    @Transactional
    public void saveMenu(WbButtonVo[] buttonVos, Integer shopId)throws Exception{
        wbMenuRepository.deleteByShopId(shopId);
        if(buttonVos.length>0){
            List<WbMenu> subList = new ArrayList<>();
            for(WbButtonVo buttonVo : buttonVos){
                WbMenu wbMenu = new WbMenu();
                BeanUtils.copyProperties(buttonVo,wbMenu);
                wbMenu.setShopId(shopId);
                WbMenu wbMenuNew = wbMenuRepository.save(wbMenu);
                WbButtonVo[] subButtons = buttonVo.getSubButton();
                if(null!=subButtons && subButtons.length>0){
                    for(WbButtonVo subButton : subButtons ){
                        WbMenu subMenu = new WbMenu();
                        BeanUtils.copyProperties(subButton,subMenu);
                        subMenu.setShopId(shopId);
                        subMenu.setParentId(wbMenuNew.getId());
                        subList.add(subMenu);
                    }
                }
            }
            wbMenuRepository.save(subList);
        }
    }
}
