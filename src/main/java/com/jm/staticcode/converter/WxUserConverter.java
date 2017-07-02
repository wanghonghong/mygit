package com.jm.staticcode.converter;

import com.jm.business.domain.WxAreaDo;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserForUpdateVo;
import com.jm.mvc.vo.wx.wxuser.WxUserRo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.client.dto.WxUserDto;
import com.jm.repository.client.dto.wxuser.WxUserUpdateDto;
import com.jm.repository.po.wx.WxUser;

import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/20
 */
public class WxUserConverter {

    /**
     * 通过省份名称跟城市名称获取区号
     * @param provinceName
     * @param cityName
     * @return
     */
    public static Integer getAreaCode(String provinceName,String cityName){
        for (WxAreaDo wxArea : Constant.WX_AREA_LIST) {
            if (provinceName.equals(wxArea.getParentAreaName())&&cityName.equals(wxArea.getAreaName())){
                return wxArea.getAreaId();
            }
        }
        return 0;
    }

    public static WxUser toWxUserForCreate(WxUserDto wxUserDto,String appid) {
        WxUser user = new WxUser();
        BeanUtils.copyProperties(wxUserDto,user);
        user.setAppid(appid);
        user.setIsSubscribe(wxUserDto.getSubscribe());
        if (user.getNickname()==null){
            user.setNickname("");
        }else{
            user.setNickname(Base64Util.enCoding(user.getNickname()));
        }
        user.setFristSubscribeTime(new Date(wxUserDto.getSubscribeTime()*1000l));
        user.setSubscribeTime(new Date(wxUserDto.getSubscribeTime()*1000l));
        user.setAreaCode(getAreaCode(wxUserDto.getProvince(),wxUserDto.getCity()));
        /*String headimgurl = wxUserDto.getHeadimgurl();
        if (StringUtils.isNotEmpty(headimgurl)){
            user.setHeadimgurl(headimgurl.substring(0,headimgurl.lastIndexOf("/"))+"/64");
        }*/
        return user;
    }

    public static WxUser toWxUserForUpdate(WxUser user,WxUserDto wxUserDto,String appid) {
        BeanUtils.copyProperties(wxUserDto,user);
        user.setAppid(appid);
        user.setIsSubscribe(wxUserDto.getSubscribe());
        if (user.getNickname()==null){
            user.setNickname("");
        }else{
            user.setNickname(Base64Util.enCoding(user.getNickname()));
        }
        user.setSubscribeTime(new Date(wxUserDto.getSubscribeTime()*1000l));
        user.setAreaCode(getAreaCode(wxUserDto.getProvince(),wxUserDto.getCity()));
        /*String headimgurl = wxUserDto.getHeadimgurl();
        if (StringUtils.isNotEmpty(headimgurl)){
            user.setHeadimgurl(headimgurl.substring(0,headimgurl.lastIndexOf("/"))+"/64");
        }*/
        return user;
    }


    /**
     * 定时更新用户信息转换器
     * @param user
     * @param wxUserUpdateDto
     * @param appid
     * @return
     */
    public static WxUser updateUserInfo(WxUser user, WxUserUpdateDto wxUserUpdateDto, String appid) {
        BeanUtils.copyProperties(wxUserUpdateDto,user);
        user.setAppid(appid);
        user.setIsSubscribe(wxUserUpdateDto.getSubscribe());
        user.setNickname(Base64Util.enCoding(user.getNickname()));
        user.setSubscribeTime(new Date(wxUserUpdateDto.getSubscribeTime()*1000l));
        return user;
    }


    public static PageItem<WxUserRo> toWxUserRo(Page<WxUser> wxUserRoPage) {
        PageItem<WxUserRo> pageItem = new PageItem<WxUserRo>();
        if(wxUserRoPage!=null){
            pageItem.setCount(wxUserRoPage.getTotalPages());
            List<WxUserRo> wxUserRos = new ArrayList<>();
            List<WxUser> wxUsers = wxUserRoPage.getContent();
            for(WxUser wxUser : wxUsers){
                WxUserRo wxUserRo = new WxUserRo();
                BeanUtils.copyProperties(wxUser,wxUserRo);
                wxUserRo.setNickname(Base64Util.getFromBase64(wxUserRo.getNickname()));
                wxUserRos.add(wxUserRo);
            }
            pageItem.setItems(wxUserRos);
            pageItem.setCount(wxUserRoPage.getTotalPages());
        }
        return pageItem;
    }
}
