package com.jm.business.service.online;

import com.jm.business.service.system.UserRoleService;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.online.HxUserCo;
import com.jm.mvc.vo.online.HxUserUo;
import com.jm.mvc.vo.online.HxUserVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.online.HxUserRepository;
import com.jm.repository.po.online.HxUser;
import com.jm.repository.po.system.user.UserRole;
import com.jm.staticcode.converter.online.HxImUserConverter;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.FortyEightUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class HxUserService {
    @Autowired
    private HxUserRepository hxUserRepository;

    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private UserRoleService userRoleService;


    public HxUser findUserById(Integer hxId){
        return hxUserRepository.findHxUserByHxId(hxId);
    }

    public HxUser findUserByHxAccount(String hxAccount){
        return hxUserRepository.findHxUserByHxAccount(hxAccount);
    }

    public void updateHxUser(HxUserUo hxUserUo){
        HxUser hxUser = HxImUserConverter.uo2po(hxUserUo);
        hxUserRepository.save(hxUser);
    }

    public void save(HxUserCo hxUserCo){
        HxUser hxUser = HxImUserConverter.co2po(hxUserCo);
        hxUserRepository.save(hxUser);
    }

    public void save(HxUser hxUser){
        hxUserRepository.save(hxUser);
    }

    public HxUserVo getHxUserByUserId(Integer userId){
        HxUser hxUser = hxUserRepository.findHxUserByUserId(userId);
        if(null==hxUser){
            return null;
        }
        HxUserVo hxUserVo = HxImUserConverter.po2vo(hxUser);
        return hxUserVo;
    }

    public HxUser getHxUserByUid(Integer userId){
        return  hxUserRepository.findHxUserByUserId(userId);
    }

    @Cacheable(value ="HxUser", key="#userId")
    public HxUser getCacheHxUser(Integer userId){
        return  hxUserRepository.findHxUserByUserId(userId);
    }

    public HxUser getHxUserByAppidAndOpenid(String appid,String openid) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select * FROM hx_user hu WHERE EXISTS (SELECT 1 FROM wx_user wu WHERE wu.appid = '"+appid+"' AND wu.openid = '"+openid+"' AND wu.user_id = hu.user_id )";
        List<HxUser> hxUsers= jdbcUtil.queryList(sql,HxUser.class);
        if(hxUsers!=null&&hxUsers.size()>0){
            return hxUsers.get(0);
        }
        return null;
    }

    @Transactional
    public void upReply(String openid){
        FortyEightUserUtil.updateCallingUser(openid);
    }

    public String initHxUser(WxUserSession wxUser){
        if(wxUser==null){
            return null;
        }
        if(null==wxUser.getHxAccount()||"".equals(wxUser.getHxAccount())){
            HxUserVo hxUserVo =  getHxUserByUserId(wxUser.getUserId());
            if(null!=hxUserVo&&!"".equals(wxUser.getHxAccount())){
                wxUser.setHxAccount(hxUserVo.getHxAccount());
            }
        }
        String shopHxAccount = Equalizer.getOnlineService(wxUser.getShopId(),wxUser.getHxAccount());
        if(shopHxAccount==null){
            UserRole userRole = userRoleService.findShopMasterByShopId(wxUser.getShopId());
            if(userRole==null||null==shopHxAccount||"".equals(shopHxAccount)){
                userRole = userRoleService.findShopMasterByShopId(wxUser.getShopId());
            }
            shopHxAccount = userRole.getHxAccount();
        }

        return shopHxAccount;
    }
}
