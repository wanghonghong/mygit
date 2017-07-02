package com.jm.business.service.wb;

import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.qo.WxUserQueryVo;
import com.jm.mvc.vo.wb.WbUserQo;
import com.jm.mvc.vo.wb.WbUserRo;
import com.jm.mvc.vo.wx.wxuser.WxUserRo;
import com.jm.repository.client.dto.wb.WbContenDto;
import com.jm.repository.client.dto.wb.WbUserDto;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.wb.WbMenuRepository;
import com.jm.repository.jpa.wb.WbShopUserRepository;
import com.jm.repository.jpa.wb.WbUserRelRepository;
import com.jm.repository.jpa.wb.WbUserRepository;
import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.converter.wb.WbConverter;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>微博用户关联服务层</p>
 * @version latest
 * @Author cj
 * @Date 2017/2/27 17:04
 */
@Slf4j
@Service
public class WbUserRelService {
    @Autowired
    private WbUserRelRepository wbUserRelRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    public WbUserRel getWbUserRel(Long id){
       return wbUserRelRepository.findOne(id);
    }

    public WbUserRel save(WbUserRel wbUserRel){
        return wbUserRelRepository.save(wbUserRel);
    }

    public List<WbUserRel> save(List<WbUserRel> wbUserRel){
        return wbUserRelRepository.save(wbUserRel);
    }

    public List<WbUserRel> findByIds(List<Long> ids){
        return wbUserRelRepository.findAll(ids);
    }

    public List<WbUserRel> findWbUserRelUpperOneListById(Long id){
        return wbUserRelRepository.findWbUserRelUpperOneListById(id);
    }

    public List<WbUserRel> findWbUserRelUpperTwoListById(Long id){
        return wbUserRelRepository.findWbUserRelUpperTwoListById(id);
    }



    public PageItem<WbUserRo> getWxUserUpperPageItem(Long id, WbUserQo qo, JmUserSession jmUserSession) throws IllegalAccessException, IOException, InstantiationException {
        if(id == null){
            return null;
        }
        if(jmUserSession == null){
            return null;
        }
        String sql = "";
        if(qo.getLastType()==0){
            sql =   " select a.subscribe_time,b.headimgurl,b.nickname,a.upper_one,a.upper_two from wb_user_rel a " +
                    " left join wb_user b on a.uid = b.id " +
                    " where a.upper_one = "+id+" or a.upper_two = "+id;
        }else if(qo.getLastType()==1){
            sql =   " select a.subscribe_time,b.headimgurl,b.nickname,a.upper_one,a.upper_two from wb_user_rel a " +
                    " left join wb_user b on a.uid = b.id " +
                    " where a.upper_one = "+id;
        }else if(qo.getLastType()==2){
            sql =   " select a.subscribe_time,b.headimgurl,b.nickname,a.upper_one,a.upper_two from wb_user_rel a " +
                    " left join wb_user b on a.uid = b.id " +
                    " where a.upper_two = "+id;
        }
        PageItem<WbUserRo> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize(),WbUserRo.class);
        return pageItem;
    }


}
