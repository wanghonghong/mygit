package com.jm.business.service.shop;

import com.jm.mvc.vo.shop.shopSet.*;
import com.jm.repository.jpa.shop.shopSetting.*;
import com.jm.repository.po.shop.shopSet.*;
import com.jm.staticcode.converter.shop.shopSetting.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺设置</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/7
 */
@Service
public class ShopSettingService {

    @Autowired
    private ShopMainRepository shopMainRepository;
    @Autowired
    private ShopTemplateRepository shopTemplateRepository;
    @Autowired
    private UserCenterConfigRepository userCenterConfigRepository;
    @Autowired
    private UserCenterFunsRepository userCenterFunsRepository;


    /**
     *商城搭建模板设置获取
     * @return List<ShopTemplateVo>
     */
    public List<ShopTemplateVo> getShopTempList() {
        List<ShopTemplate> shopTempList =  shopTemplateRepository.findShopTemplateList();
        List<ShopTemplateVo> shopTempVoList = new ArrayList<ShopTemplateVo>();
        if (shopTempList != null && shopTempList.size()>0){
            for (ShopTemplate shopTemplate : shopTempList){
                ShopTemplateVo shopTemplateVo = ShopTemplateConverter.toShopTemplateVo(shopTemplate);
                shopTempVoList.add(shopTemplateVo);
            }
        }
        return shopTempVoList;
    }

    /**
     * 商城首页搭建获取
     * @param shopId
     * @return
     */
    public ShopMainVo getShopMain(Integer shopId) {
        ShopMainVo shopVo = new ShopMainVo();
        ShopMain shopMain = shopMainRepository.findShopMainByShopId(shopId);
        if(shopMain != null){
            shopVo = ShopMainConverter.toShopMainVo(shopMain);
        }
        return shopVo;
    }

    /**
     * 商城首页搭建设置
     * @param shopMainCo
     * @param shopId
     * @return
     */
    public ShopMain creatShopIndex(ShopMainCo shopMainCo, Integer shopId) {
        ShopMain shopMain = ShopMainConverter.toShopMian(shopMainCo);
        shopMain.setShopId(shopId);
        return shopMainRepository.save(shopMain);
    }

    /**
     * 商城首页搭建修改
     * @param shopMainUo
     * @param shopId
     */
    public void updateShopIndex(ShopMainUo shopMainUo, Integer shopId) {
        ShopMain shopMain = shopMainRepository.findShopMainByShopId(shopId);
        if(shopMain != null){
            shopMain = ShopMainConverter.toShopMian(shopMainUo,shopMain);
            shopMainRepository.save(shopMain);
        }
    }

    /**
     * 会员中心配置获取
     * @return
     */
    public List<UserCenterFunsVo> getUserCenterFunsList() {
        List<UserCenterFuns> userCenterFunsList =  userCenterFunsRepository.findUserCenterFunsList();
        List<UserCenterFunsVo> userCenterFunsVoList = new ArrayList<UserCenterFunsVo>();
        if (userCenterFunsList != null && userCenterFunsList.size()>0){
            for (UserCenterFuns userCenterFuns : userCenterFunsList){
                UserCenterFunsVo userCenterFunsVo = UserCenterFunsConverter.toShopTemplateVo(userCenterFuns);
                userCenterFunsVoList.add(userCenterFunsVo);
            }
        }
        return userCenterFunsVoList;
    }

    /**
     * 会员中心获取
     * @param shopId
     * @return
     */
    public UserCenterConfigVo getUserCenterConfig(Integer shopId) {
        UserCenterConfig userCenterConfig = userCenterConfigRepository.findUserCenterConfigByShopId(shopId);
        UserCenterConfigVo userCenterConfigVo = new UserCenterConfigVo();
        if(userCenterConfig != null){
            userCenterConfigVo = UserCenterConfigConverter.toUserCenterConfigVo(userCenterConfig);
        }
        return userCenterConfigVo;
    }

    /**
     * 会员中心新增
     * @param userCenterConfigCo
     * @param shopId
     * @return
     */
    public UserCenterConfig creatUserCenterConfig(UserCenterConfigCo userCenterConfigCo, Integer shopId) {
        UserCenterConfig userCenterConfig = UserCenterConfigConverter.toUserCenterConfig(userCenterConfigCo);
        userCenterConfig.setShopId(shopId);
        return userCenterConfigRepository.save(userCenterConfig);
    }

    /**
     * 会员中心修改
     * @param userCenterConfigUos
     * @param shopId
     * @return
     */
    public UserCenterConfig updateUserCenterConfig(UserCenterConfigUo userCenterConfigUo, Integer shopId) {
        UserCenterConfig userCenterConfig = userCenterConfigRepository.findUserCenterConfigByShopId(shopId);
        userCenterConfig = UserCenterConfigConverter.toUserCenterConfig(userCenterConfigUo,userCenterConfig);
        return userCenterConfigRepository.save(userCenterConfig);
    }



    public ShopTemplateVo getShopTemp(Integer tempId){
        ShopTemplate shopTemplate=  shopTemplateRepository.findShopTemplateByTempId(tempId);
        ShopTemplateVo shopTemplateVo =new ShopTemplateVo();
        if(shopTemplate!=null){
            shopTemplateVo = ShopTemplateConverter.toShopTemplateVo(shopTemplate);
        }
        return  shopTemplateVo;
    }

//    /**
//     * 获取底部菜单信息
//     * @param shopId
//     * @return
//     */
//    public BottomMenuVo getBottomMenu(Integer shopId,Integer edition) {
//        BottomMenu bottomMenu = bottomMenuRepository.findByShopIdAndEdition(shopId,edition);
//        BottomMenuVo bottomMenuVo = new BottomMenuVo();
//        if(bottomMenu != null){
//            bottomMenuVo = BottomMenuConverter.toBottomMenuVo(bottomMenu);
//        }
//        return bottomMenuVo;
//    }
}
