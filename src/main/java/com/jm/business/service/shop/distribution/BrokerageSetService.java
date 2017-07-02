package com.jm.business.service.shop.distribution;

import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.distribution.*;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageConfigRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageProductRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageSetRepository;
import com.jm.repository.jpa.shop.distribution.PutSetRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.BrokerageConfig;
import com.jm.repository.po.shop.brokerage.BrokerageProduct;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.repository.po.shop.brokerage.PutSet;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.converter.shop.distribution.BrokerageConverter;
import com.jm.staticcode.converter.shop.distribution.PutSetConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */

@Log4j
@Service
public class BrokerageSetService {

    @Autowired
    private BrokerageSetRepository brokerageSetRepository;

    @Autowired
    private BrokerageConfigRepository brokerageConfigRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private PutSetRepository putSetRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    private SmallShopRepository smallShopRepository;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private BrokerageProductRepository brokerageProductRepository;
    /**
     * 佣金配置获取
     *
     * @param shopId
     * @return
     */
    public BrokerageSetVo getBrokerageSetting(Integer shopId) {
        BrokerageSetVo brokerageSetVo = new BrokerageSetVo();
        BrokerageSet brokerageSet = brokerageSetRepository.findByShopId(shopId);
        if (brokerageSet != null) {
            brokerageSetVo = BrokerageConverter.toBrokerageSettingVo(brokerageSet);
        }
        return brokerageSetVo;
    }

    /**
     * 佣金配置获取
     *
     * @param shopId
     * @return
     */
    @Cacheable(value ="BrokerageSet", key="#shopId")
    public BrokerageSet getCacheBrokerageSet(Integer shopId) {
        return brokerageSetRepository.findByShopId(shopId);
    }


    /**
     * 佣金设置
     *
     * @param shopId
     * @param brokerageSetCo
     */
    public BrokerageSetVo setBrokerageSetting(Integer shopId, BrokerageSetCo brokerageSetCo) {
        BrokerageSet brokerageSet = BrokerageConverter.toBrokerageSetting(brokerageSetCo);
        brokerageSet.setShopId(shopId);
        Shop shop = shopRepository.findShopByShopId(shopId);
        shop.setIsOpen(brokerageSet.getIsOpen());
        shopRepository.save(shop);
        brokerageSet = brokerageSetRepository.save(brokerageSet);
        BrokerageSetVo brokerageSetVo = new BrokerageSetVo();
        if (brokerageSet != null) {
            brokerageSetVo = BrokerageConverter.toBrokerageSettingVo(brokerageSet);
        }
        return brokerageSetVo;
    }

    /**
     * 佣金修改
     *
     * @param shopId
     * @param brokerageSetUo
     */
    public void updateBrokerageSetting(Integer shopId, BrokerageSetUo brokerageSetUo) {
        BrokerageSet brokerageSet = BrokerageConverter.toBrokerageSetting(brokerageSetUo, brokerageSetRepository.findByShopId(shopId));
        //brokerageSet.setShopId(shopId);
        Shop shop = shopRepository.findShopByShopId(shopId);
        shop.setIsOpen(brokerageSet.getIsOpen());
        shopRepository.save(shop);
        brokerageSet = brokerageSetRepository.save(brokerageSet);
        //更新我的小店的用户角色权限
        updateSmallShop(brokerageSet);
    }

    /**
     * 更新我的小店用户角色权限
     *
     * @param brokerageSet
     */
    @Transactional
    private void updateSmallShop(BrokerageSet brokerageSet) {
        String agent = null;
        int isMyShop = 0;
        if (brokerageSet.getIsOpen() == 1) {
            if (brokerageSet.getIsFansShop() == 1) {
                switch (brokerageSet.getType()) {
                    case 1:
                        return;
                    case 2:
                        agent = "1,2,3,4";
                        isMyShop = 3;//代理商
                        updateAgentShop(brokerageSet, agent, isMyShop);
                        return;
                    case 3:
                        agent = "5,6,7";
                        isMyShop = 4;//代理商
                        updateAgentShop(brokerageSet, agent, isMyShop);
                        return;
                    case 4:
                        return;
                    case 5:
                        agent = "1,2,3,4";
                        isMyShop = 3;//代理商
                        updateAgentShop(brokerageSet, agent, isMyShop);
                        agent = "5,6,7";
                        isMyShop = 4;//代理商
                        updateAgentShop(brokerageSet, agent, isMyShop);
                        return;
                    case 6:
                        return;
                    case 7:
                        return;
                    case 8:
                        return;
                }
            }
        }
    }

    /**
     * 更新角色对应的小店权限
     * @param brokerageSet
     * @param agent
     * @param isMyShop
     */
    @Transactional
    private void updateAgentShop(BrokerageSet brokerageSet, String agent, int isMyShop) {
        String sql = "UPDATE wx_user w SET w.is_my_shop = " + isMyShop + " WHERE EXISTS ( SELECT 1 FROM shop_user s WHERE w.shop_user_id = s.id and shop_id=" + brokerageSet.getShopId() + " AND s.agent_role IN (" + agent + "));";
        jdbcTemplate.update(sql);

    }

    /**
     * 是否是代理分销
     * */
    public boolean isBrokerage(BrokerageSet brokerageSet){
        if(brokerageSet==null||brokerageSet.getIsOpen()!=1||brokerageSet.getSettleType()!=1){
            return false;
        }
        int [] types = new int[]{2,4,5,6,7};
        brokerageSet.getType();
        for(int i=0;i<types.length;i++){
            if( brokerageSet.getType() == types[i]){
                return true;
            }
        }
        return false;
    }
    /**
     * 获取用户分销代理折扣
     * */
    public int getUserDiscount(ShopUser shopUser,BrokerageSet brokerageSet){
        int discount = 100;
        int agentRole = shopUser.getAgentRole();
        if(agentRole>=1&&agentRole<=4){
            switch (agentRole){
                case 1:
                    discount = brokerageSet.getAgentA()/10;
                    break;
                case 2:
                    discount = brokerageSet.getAgentB()/10;
                    break;
                case 3:
                    discount = brokerageSet.getAgentC()/10;
                    break;
                case 4:
                    discount = brokerageSet.getAgentD()/10;
                    break;
            }
        }
        return discount;
    }

    /**
     * 佣金收费获取
     *
     * @param feeType,shopId
     * @return
     */
    public BrokerageConfigVo getBrokerageConfig(int feeType, Integer shopId) {
        BrokerageConfigVo brokerageConfigVo = new BrokerageConfigVo();
        BrokerageConfig brokerageConfig = brokerageConfigRepository.findByFeeTypeAndShopId(feeType, shopId);
        if (brokerageConfig != null) {
            brokerageConfigVo = BrokerageConverter.toBrokerageConfigVo(brokerageConfig);
        }
        return brokerageConfigVo;
    }


    /**
     * 收费配置新增
     *
     * @param shopId
     * @param brokerageConfigCo
     * @return
     */
    public BrokerageConfigVo addBrokerageConfig(Integer shopId, BrokerageConfigCo brokerageConfigCo) {
        BrokerageConfig brokerageConfig = BrokerageConverter.toBrokerageConfig(brokerageConfigCo);
        brokerageConfig.setShopId(shopId);
        brokerageConfig = brokerageConfigRepository.save(brokerageConfig);
        return BrokerageConverter.toBrokerageConfigVo(brokerageConfig);
    }

    /**
     * 收费配置
     *
     * @param shopId
     * @param brokerageConfigUo
     * @return
     */
    public BrokerageConfigVo updateBrokerageConfig(Integer shopId, BrokerageConfigUo brokerageConfigUo) {
        BrokerageConfig brokerageConfig = brokerageConfigRepository.findOne(brokerageConfigUo.getId());
        brokerageConfig = BrokerageConverter.toBrokerageConfig(brokerageConfigUo, brokerageConfig);
        //brokerageConfig.setShopId(shopId);
        brokerageConfig = brokerageConfigRepository.save(brokerageConfig);
        return BrokerageConverter.toBrokerageConfigVo(brokerageConfig);
    }

    public void setBrokerageProduct(BrokerageProductDetailCo brokerageProductDetailCo) {
        List<BrokerageProductCo> brokerageProductCos = brokerageProductDetailCo.getBrokerageProductCos();
        List<BrokerageProduct> brokerageProducts = new ArrayList<BrokerageProduct>();
        if (brokerageProductCos !=null && brokerageProductCos.size()>0){
            for (BrokerageProductCo brokerageProductCo : brokerageProductCos){
                BrokerageProduct brokerageProduct = BrokerageConverter.toBrokerageProduct(brokerageProductCo);
                brokerageProducts.add(brokerageProduct);
            }
        }
        brokerageProductRepository.save(brokerageProducts);
    }

    /**
     * 支付设置获取
     *
     * @param shopId
     * @return
     */
    public PutSetVo getPutSetting(Integer shopId, int payType) {
        PutSet putSet = putSetRepository.findByShopIdAndPayType(shopId,payType);
        PutSetVo putSetVo = new PutSetVo();
        if (putSet != null) {
            putSetVo = PutSetConverter.toPutSettingVo(putSet);
        }
        return putSetVo;
    }

    /**
     * 支付设置新增
     *
     * @param shopId
     * @param putSetCo
     */
    public PutSetVo setPutSetting(Integer shopId, PutSetCo putSetCo) {
        PutSet putSet = PutSetConverter.toPutSetting(putSetCo);
        putSet.setShopId(shopId);
        putSet = putSetRepository.save(putSet);
        PutSetVo putSetVo = new PutSetVo();
        if (putSet != null) {
            putSetVo = PutSetConverter.toPutSettingVo(putSet);
        }
        return putSetVo;
    }

    /**
     * 支付设置修改
     *
     * @param shopId
     * @param putSetUo
     */
    public void updatePutSetting(Integer shopId, PutSetUo putSetUo) {
        PutSet putSet = PutSetConverter.toPutSetting(putSetUo, putSetRepository.findByShopIdAndPayType(shopId,putSetUo.getPayType()));
        putSetRepository.save(putSet);
    }

    public List<PutSet> findPutSetList(int payType) {
        return putSetRepository.findPutSetByPayType(payType);
    }

    /**
     * 开通免费版模式
     *
     * @param wxUser
     * @param shopId
     */
    public WxUser updateFreeMode(WxUser wxUser, Integer shopId) {
        BrokerageSet brokerageSet = brokerageSetRepository.findByShopId(shopId);
        brokerageSet.setFreeCount(brokerageSet.getFreeCount() - 1);
        brokerageSetRepository.save(brokerageSet);
        wxUser.setIsMyShop(1);//免费模式权限
        return wxUserService.saveUser(wxUser);
    }



//    public ShopSmallVo getSmallShop(Integer shopId) {
//        ShopSmallVo shopSmallVo = new ShopSmallVo();
//        ShopSmall shopSmall = smallShopRepository.findByShopId(shopId);
//        if(shopSmall !=null){
//            shopSmallVo = BrokerageConverter.toSmallShopVo(shopSmall);
//        }
//        return shopSmallVo;
//    }
//
//    public ShopSmallVo setSmallShop(Integer shopId, ShopSmallCo shopSmallCo) {
//        ShopSmall shopSmall = BrokerageConverter.toSmallShop(shopSmallCo);
//        shopSmall.setShopId(shopId);
//        shopSmall.setFreeCount(2000);
//        shopSmall = smallShopRepository.save(shopSmall);
//        return BrokerageConverter.toSmallShopVo(shopSmall);
//
//    }
//
//    public void initShopSmall(Integer shopId) {
//        ShopSmallCo shopSmallCo = new ShopSmallCo();
//        ShopSmall shopSmall = BrokerageConverter.toSmallShop(shopSmallCo);
//        shopSmall.setShopId(shopId);
//        shopSmall.setBackStyle(1);
//        shopSmall.setPreStyle(1);
//        shopSmall.setMode(1);
//        shopSmall.setFreeCount(2000);
//        smallShopRepository.save(shopSmall);
//    }
//
//    public ShopSmallVo updateSmallShop(Integer shopId, ShopSmallUo shopSmallUo) {
//        ShopSmall shopSmall = smallShopRepository.findOne(shopSmallUo.getId());
//        shopSmall = BrokerageConverter.toSmallShop(shopSmallUo, shopSmall);
//        //brokerageConfig.setShopId(shopId);
//        shopSmall = smallShopRepository.save(shopSmall);
//        return BrokerageConverter.toSmallShopVo(shopSmall);
//    }
//

    /**
     * 更新用户权限与店铺数量
     */
//    @Transactional
//    public void updateWxUserAndShopSmall(WxUserSession wxUserSession, int mode) {
//        ShopSmall shopSmall = smallShopRepository.findByShopId(wxUserSession.getShopId());
//        WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
//        switch (mode){
//            case 1:
//                shopSmall.setFreeCount(shopSmall.getFreeCount()-1);
//                smallShopRepository.save(shopSmall);
//                wxUser.setIsMyShop(1);//免费模式权限
//                wxUserService.saveUser(wxUser);
//                break;
//            case 2:
//                shopSmall.setFeeCount(shopSmall.getFeeCount()-1);
//                smallShopRepository.save(shopSmall);
//                break;
//        }
//    }

    /**
     * 更新用户权限与店铺数量
     */
    @Transactional
    public void updateSmallCountAndWxUser(Integer shopId, WxUser wxUser) {
        BrokerageSet  brokerageSet = getCacheBrokerageSet(shopId);
        switch (brokerageSet.getMode()){
            case 1:
                brokerageSet.setFreeCount(brokerageSet.getFreeCount()-1);
                brokerageSetRepository.save(brokerageSet);
                wxUser.setIsMyShop(1);//免费模式权限
                wxUserService.saveUser(wxUser);
                break;
            case 2:
//                shopSmall.setFeeCount(shopSmall.getFeeCount()-1);
//                smallShopRepository.save(shopSmall);
//                break;
                break;
        }

//        brokerageSet.setFreeCount(brokerageSet.getFreeCount()-1);
//        brokerageSetRepository.save(brokerageSet);
//        wxUser.setIsMyShop(2);
    }


}
