package com.jm.business.service.wb;

import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.wb.WbShopUserCo;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.jpa.wb.WbShopUserRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.wb.WbShopUser;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>微博商家用户</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/4 10:07
 */
@Slf4j
@Service
public class WbShopUserService {
    @Autowired
    private WbShopUserRepository wbShopUserRepository;
    @Autowired
    private ShopRepository  shopRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WbClient wbClient;

    @Transactional
    public JmMessage saveWbShopUser(WbShopUser wbShopUser, Integer shopId) throws Exception {
        if(0==shopId.intValue()){
            return new JmMessage(1,"授权失败，找不到店铺ID!!");
        }
        wbShopUserRepository.save(wbShopUser);
        Long uid = wbShopUser.getUid();
        Shop shop = shopRepository.findOne(shopId);
        shop.setWbUid(uid);
        shopRepository.save(shop);
        return new JmMessage(0,"授权成功!!");
    }

    public WbShopUser getWbShopUserByUid(Long  uid){
         return wbShopUserRepository.findWbShopUserByUid(uid);
    }

    /**
     * <p>保存微博商家用户信息</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 14:45
     */
    @ApiOperation("保存微博商家用户信息")
    public JmMessage saveWbShopToken(HttpServletRequest request, WbShopUserCo wbShopUserCo) throws Exception {
        String accessToken = wbShopUserCo.getAccessToken();
        Shop shop = shopService.findShopByWbUid(accessToken);
        if(shop != null){
            return new JmMessage(1,"授权失败，该token已经被其他店铺授权过，请认真核查!!");
        }
        WbShopUser wbShopUser = wbClient.getTokenInfo(accessToken);
        Long uid = Toolkit.obj2Long(wbShopUser.getUid());
        if(0L!=uid){
            wbShopUser.setCreateTime(new Date());
            log.info("---------------uid="+uid);
            //保存 微博商家信息
            return saveWbShopUser(wbShopUser,wbShopUserCo.getShopId());
        }
        return new JmMessage(1,"调用微博接口失败，获取不到用户UID!!");
    }
}
