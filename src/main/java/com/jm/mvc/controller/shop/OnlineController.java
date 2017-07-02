package com.jm.mvc.controller.shop;

import com.jm.business.service.online.HxUserService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.business.service.wx.WxGroupService;
import com.jm.business.service.wx.WxUserLevelService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.online.ChatUser;
import com.jm.mvc.vo.online.HxUserCo;
import com.jm.mvc.vo.qo.WxUserQueryVo;
import com.jm.mvc.vo.system.UserUo;
import com.jm.mvc.vo.system.WxUserUo;
import com.jm.mvc.vo.wx.level.WxUserLevelVo;
import com.jm.mvc.vo.wx.wxuser.OnlineUserVo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.online.HxUser;
import com.jm.repository.po.system.user.User;
import com.jm.repository.po.system.user.UserRole;
import com.jm.repository.po.wx.WxUserGroup;
import com.jm.repository.po.wx.WxUserLevel;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.wx.WxUserLevelConverter;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * <p>在线客服</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/8/8/006
 */
@Api
@RestController
@Slf4j
@RequestMapping(value = "/online")
public class OnlineController {
    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxUserLevelService wxUserLevelService;
    @Autowired
    private WxGroupService wxGroupService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private HxUserService hxUserService;


    @RequestMapping(value = "/wxUserList", method = RequestMethod.POST)
    public PageItemExt getUserList(@ApiParam(hidden=true) HttpServletRequest request,
                              @ApiParam("微信用户查询Qo") @RequestBody @Valid WxUserQueryVo wxuserQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        long start = System.currentTimeMillis();
        Integer userid = user.getUserId();
        PageItem<Map<String,Object>> pageItemRes = wxUserService.queryWxuser(wxuserQo,user.getAppId());
        List<Map<String,Object>> items = pageItemRes.getItems();
        for(Map wxuser:items){
            wxuser.put("nickname",Base64Util.getFromBase64((String) wxuser.get("nickname")));
            wxuser.put("lastMsg",Base64Util.getFromBase64((String)wxuser.get("lastMsg")));
        }
        User shopUser = userService.getUser(userid);
        //UserRole role = userRoleService.findShopMasterByShopId(user.getShopId());
        //UserRole role;
        long middle = System.currentTimeMillis();
        String hxAcct = userRoleService.initShopUserHxAcct(user);

        log.info("shop user used hx_account----------------------:"+hxAcct);
        OnlineUserVo onlineUser = new OnlineUserVo();
        onlineUser.setHxAccount(hxAcct);
        onlineUser.setNickname(shopUser.getUserName());
        onlineUser.setHeadImg(user.getImgUrl());
        onlineUser.setShopid(user.getShopId());
        onlineUser.setUserid(userid);
        String appid = user.getAppId();
        List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(appid);
        List<WxUserLevelVo> levelVos = WxUserLevelConverter.listToWxUserLevelVo(levels);
        List<WxUserGroup> groupList = wxGroupService.findAllGroup(appid);
        onlineUser.setLevels(levelVos);
        onlineUser.setGroups(groupList);
        long end = System.currentTimeMillis();
        log.info("start - end:"+(end-start));
        PageItemExt<Map<String,Object>,OnlineUserVo> ext = JdbcUtil.pageItem2Ext(pageItemRes,onlineUser);

        return ext;
    }

    @RequestMapping(value = "/HX/lastMsg",method = RequestMethod.POST)
    public String updateLastMsg(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("用户环信Uo") @RequestBody  WxUserUo userUo) throws Exception {
        Map<String,Object> res = new HashMap<String,Object>();
        //Integer userid = userUo.getUserId();
         wxUserService.updateUsersLastMsgByHxAccount(userUo.getWxUserList());
        res.put("retCode","0");
        return JsonMapper.toJson(res);
    }

    @RequestMapping(value = "/HX/hxAccount",method = RequestMethod.PUT)
    public String addHxAccount(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam("商户环信Uo")@RequestBody UserUo userUo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Map<String,Object> res = new HashMap<String,Object>();
        Integer userid = userUo.getUserId();
        String flag = userUo.getAccountFlag();
        if(null!=flag&&!"".equals(flag)&&userid!=null&&userid!=0){
            if("S".equals(flag)){
                //保存商家账号时候 获取登录员工 的 shopUserId shopId userId
                UserRole userRole = userRoleService.findOnlineUserByShopIdAndUserId(user.getShopId(),user.getUserId());   //userRoleService.findShopMasterByShopId(user.getShopId());
                if(null==userRole.getHxAccount()||"".equals(userRole.getHxAccount())){
                    userRole.setHxAccount(userUo.getHxAccount());
                    userRoleService.updateUser(userRole);
                    res.put("code","0");
                }else{
                    res.put("code","-1");
                }
            }else if("C".equals(flag)){
                HxUser hxUser = hxUserService.getHxUserByUid(userid);
                if(hxUser==null&&null!=userUo.getHxAccount()&&userUo.getUserId()!=null){
                    HxUserCo hxUserCo = new HxUserCo();
                    hxUserCo.setHxAccount(userUo.getHxAccount());
                    hxUserCo.setUserId(userUo.getUserId());
                    hxUserCo.setCreateTime(new Date());
                    hxUserService.save(hxUserCo);
                }else{
                    hxUser.setHxAccount(userUo.getHxAccount());
                    hxUserService.save(hxUser);
                }
                res.put("code","0");
            }
        }else{
            res.put("code","-1");
        }

        return JsonMapper.toJson(res);
    }

    @RequestMapping(value="/wxUsers",method =RequestMethod.GET)
    public JmMessage getWxUserByHxAccount(@ApiParam(hidden=true) HttpServletRequest request, String[] accounts){
        JmMessage message = new JmMessage();
        if(accounts==null||accounts.length<1){
            message.setCode(1);
            message.setMsg("查询失败");
            return message;
        }
        try{
            List<WxUserVo> users = wxUserService.getWxUserByHxAccounts(accounts);
            if(users==null||users.size()<1){
                List<Integer> userIds = new ArrayList<>();
                for(String account:accounts){
                    String[] strs = account.split("_");
                    String id = strs[1];
                    Integer userId = Integer.valueOf(id);
                    userIds.add(userId);
                }
            }
            Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
            for(WxUserVo wxUser:users){
                if(null!=wxUser){
                    Map<String,String> userDetail = new HashMap<>();
                    userDetail.put("headimgurl",wxUser.getHeadimgurl());
                    userDetail.put("nickname",Base64Util.getFromBase64(wxUser.getNickname()));
                    userDetail.put("appid",wxUser.getAppid());
                    userDetail.put("openid",wxUser.getOpenid());
                    userDetail.put("userid",wxUser.getUserId()+"");
                    userDetail.put("hxAcct",wxUser.getHxAccount());
                    result.put(wxUser.getHxAccount(),userDetail);
                }
            }
            message.setData(result);
            message.setCode(0);
        }catch (Exception e){
            e.printStackTrace();
            message.setCode(1);
            message.setMsg("查询失败");
        }
        return  message;
    }

    @RequestMapping(value="/HX/onlineUserService",method =RequestMethod.GET)
    public String getOnlineUserService(@ApiParam(hidden=true) HttpServletRequest request,String account,Integer shopId,String secretKey){
        if(account==null||shopId==null||!Constant.HX_SCRET_KEY.equals(secretKey)){
            return null;
        }
        String shopAccount = Equalizer.getOnlineService(shopId,account);
        if(shopAccount==null){
            shopAccount="";
        }
        return shopAccount;
    }
    @RequestMapping(value="/HX/customer",method =RequestMethod.GET)
    public boolean addCustmer(@ApiParam(hidden=true) HttpServletRequest request,String customer,String shopAcc,Integer shopId,String scretKey) throws IOException, ClassNotFoundException {
        log.info("======addUser act:"+customer);
        if(!Constant.HX_SCRET_KEY.equals(scretKey)||customer==null||shopAcc==null||shopId==null){
            return false;
        }
        Equalizer.addCustormer(customer,shopId,shopAcc);
        return true;
    }


    @RequestMapping(value = "/HX/oldFans",method = RequestMethod.POST)
    public PageItemExt getPayUser(@ApiParam(hidden=true) HttpServletRequest request,
                                  @ApiParam("微信用户查询Qo") @RequestBody @Valid WxUserQueryVo wxuserQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        User shopUser = userService.getUser(user.getUserId());
        wxuserQo.setAppid(user.getAppId());
        PageItem<ChatUser> pages =  wxUserService.findOldFans(wxuserQo);
        OnlineUserVo onlineUser = new OnlineUserVo();
        String hxAcct = userRoleService.initShopUserHxAcct(user);
        onlineUser.setHxAccount(hxAcct);
        onlineUser.setNickname(shopUser.getUserName());
        onlineUser.setHeadImg(user.getImgUrl());
        onlineUser.setShopid(user.getShopId());
        onlineUser.setUserid(user.getUserId());
        String appid = user.getAppId();
        List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(appid);
        List<WxUserLevelVo> levelVos = WxUserLevelConverter.listToWxUserLevelVo(levels);
        List<WxUserGroup> groupList = wxGroupService.findAllGroup(appid);
        onlineUser.setLevels(levelVos);
        onlineUser.setGroups(groupList);
        PageItemExt ext  = JdbcUtil.pageItem2Ext(pages, onlineUser);
        return ext;
    }


}
