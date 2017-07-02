package com.jm.business.service.user;

import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.user.ShopUserVo;
import com.jm.repository.jpa.user.ShopUserRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * <p>店铺用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/12
 */
@Service
public class ShopUserService {

    @Autowired
    private ShopUserRepository shopUserRepository;

    public ShopUser save(ShopUser shopUser){
        return shopUserRepository.save(shopUser);
    }

    public ShopUser saveShopUser(ShopUserVo vo,Shop shop,WxUser wxUser){
        if(shop != null){
            ShopUser shopUser=null;
            if(wxUser.getShopUserId()!=null && wxUser.getShopUserId()>0 ){
                shopUser = shopUserRepository.findOne(wxUser.getShopUserId());
            }else{
                shopUser = shopUserRepository.findByPhoneNumberAndShopId(vo.getPhoneNumber(),shop.getShopId());
            }
            if(shopUser==null){
                shopUser = new ShopUser();
            }
            shopUser.setCreateTime(new Date());
            shopUser.setAlipay(vo.getAlipay());
            shopUser.setPhoneNumber(vo.getPhoneNumber());
            shopUser.setPassword(vo.getPassword());
            shopUser.setShopId(shop.getShopId());
            shopUser.setUserName(vo.getUserName());
            return shopUserRepository.save(shopUser);
        }
        return null;
    }

    public ShopUser findShopUser(Integer id){
        return shopUserRepository.findOne(id);
    }


    public ShopUser updatePhoneNumber(String oldPhoneNumber, String newPhoneNumber, Integer shopId) {
        ShopUser shopUser= shopUserRepository.findByPhoneNumberAndShopId(oldPhoneNumber,shopId);
        if(shopUser!=null){
            shopUser.setPhoneNumber(newPhoneNumber);
            return shopUserRepository.save(shopUser);
        }
        return null;
    }

    public ShopUser updatePwd(String phoneNumber, String pwd,Integer shopId) {
        ShopUser shopUser= shopUserRepository.findByPhoneNumberAndShopId(phoneNumber,shopId);
        if(shopUser!=null){
            shopUser.setPassword(pwd);
            return shopUserRepository.save(shopUser);
        }
        return null;
    }

    public boolean isBrokerageUserRole(ShopUser shopUser){
        int agentRole =  shopUser.getAgentRole();
        int[] roles = new int[]{1,2,3,4};
        for(int role :roles){
            if(agentRole == role){
                return true;
            }
        }
        return false;
    }

    public List<ShopUser> findListById(List<Integer> ids){
        return shopUserRepository.findAll(ids);
    }

    public List<ShopUser> findByLikeUserName(String userName){
        userName = "%" + userName +"%";
        return shopUserRepository.findByLikeUserName(userName);
    }

    public List<ShopUser> findByLikePhoneNumber(String phoneNumber){
        phoneNumber = "%" + phoneNumber +"%";
        return shopUserRepository.findByLikePhoneNumber(phoneNumber);
    }


    public List<ShopUser> findByLikeUserNameAndPhoneNumber(String userName,String phoneNumber){
        userName = "%" + userName +"%";
        phoneNumber = "%" + phoneNumber +"%";
        return shopUserRepository.findByLikeUserNameAndPhoneNumber(userName,phoneNumber);
    }

    public ShopUser findByJmUserId(Integer userId){
       return  shopUserRepository.findByJmUserId(userId);
    }

}



