package com.jm.mvc.controller.system;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.RoleService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wb.WbUserRelService;
import com.jm.business.service.wb.WbUserService;
import com.jm.business.service.wx.WxUserQrcodeService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.mvc.vo.qo.UserQo;
import com.jm.mvc.vo.system.UserForQueryVo;
import com.jm.mvc.vo.system.UserRo;
import com.jm.mvc.vo.system.UserRoleUo;
import com.jm.mvc.vo.wb.WbUserQo;
import com.jm.mvc.vo.wb.WbUserRo;
import com.jm.mvc.vo.wx.WxUserDetailRo;
import com.jm.mvc.vo.wx.wxuser.WxUserRo;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.system.user.User;
import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.staticcode.converter.UserConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.service.wx.WxAuthService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.WxUserForUpdateVo;
import com.jm.mvc.vo.qo.WxUserQueryVo;
import com.jm.repository.client.WxAuthClient;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.client.dto.auth.SetRemarkParam;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.wx.Base64Util;

/**
 * <p>客户中心</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/26
 */

@Api
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxAuthClient wxClient;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private WxUserQrcodeService wxUserQrcodeService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WbUserRelService wbUserRelService;
    @Autowired
    private WbUserService wbUserService;
    @Autowired
    private ShopService shopService;



    @ApiOperation("微信用户修改")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateWxUserRemark(@ApiParam(hidden=true) HttpServletRequest request,
                                        @ApiParam("用户修改VO") @RequestBody @Valid WxUserForUpdateVo userVo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        wxUserService.updateWxUserRemark(userVo.getUserId(), userVo.getRemark());
        //设置微信端 用户的备注
        String accessToken = wxAuthService.getAuthAccessToken(user.getAppId());
        SetRemarkParam remarkParam = new SetRemarkParam();
        remarkParam.setOpenid(userVo.getOpenId());
        remarkParam.setRemark(userVo.getRemark());
        ResultMsg rem= wxClient.setRemark(accessToken, remarkParam);
        if(rem.getErrcode()!=0){
            return new JmMessage(1, "失败","微信端用户备注失败");
        }
        return new JmMessage(0, "完成修改！");
    }

    @ApiOperation("下级微客数据")
    @RequestMapping(value = "/laseuser/{userid}", method = RequestMethod.POST)
    public PageItem<WxUserRo> getImageTextList(@ApiParam(hidden=true) HttpServletRequest request,
                                                 @RequestBody @Valid WxUserQueryVo qo,
                                               @ApiParam("用户标识")  @PathVariable("userid") Integer userid) throws ParseException, IOException {
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<WxUserRo> pageItem =  wxUserService.getWxUserUpperOnePageItem(userid,qo,userSession);
        return pageItem;
    }


    @ApiOperation("微信用户角色修改")
    @RequestMapping(value = "/updateUserRole/{userid}/{userRoleId}", method = RequestMethod.GET)
    public JmMessage updateUserRole(@ApiParam("用户标识")@PathVariable("userid")Integer userid,
                                    @ApiParam("用户标识")@PathVariable("userRoleId")Integer userRoleId
                                    ,@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(userSession.getRoleId().equals(2)||userSession.getRoleId().equals(25)){ //角色是店主和运营
            WxUser wxUser = wxUserService.getWxUser(userid);
            if(wxUser.getShopUserId() != null){
                ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
                if(shopUser!=null){
                    shopUser.setAgentRole(userRoleId);
                    shopUserService.save(shopUser);
                    if(wxUser.getIsMyShop()!=2){ //我的小店权限0：没有权限，1：免费版权限，2：收费版权限,3：代理商权限,4：分销商权限
                        if(userRoleId.equals(8)){
                            wxUser.setIsMyShop(0);
                        }else if( userRoleId < 5 && userRoleId >0  ){
                            wxUser.setIsMyShop(3);
                        }else if(userRoleId< 8 && userRoleId >4 ){
                            wxUser.setIsMyShop(4);
                        }
                        wxUserService.saveUser(wxUser);
                    }
                    return new JmMessage(0, "完成修改！");
                }else{
                    return new JmMessage(0, "修改用户不存在！");
                }
            }else{
                if(userRoleId.equals(0)){
                    return new JmMessage(1, "无需修改！");
                }else{
                    return new JmMessage(1, "请在手机端完成注册！");
                }
            }
        }else{
            return new JmMessage(1, "无权修改！");
        }
    }


    /**
     * 用户角色  页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/userRole/{userid}", method = RequestMethod.GET)
    public ModelAndView userRole (@ApiParam("用户标识")  @PathVariable("userid") int userid,@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        WxUser wxUser = wxUserService.getWxUser(userid);
        String roleName = "";
        if(wxUser.getIsSubscribe().equals(1)){
            roleName = "关注粉丝";
        }
        if(wxUser.getIsBuy()==1){
            roleName = "购买粉丝";
        }
        int agentRole = 0;
        if(wxUser.getShopUserId()!=null){
            ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
            if(shopUser!=null){
                agentRole = shopUser.getAgentRole();
                if(shopUser.getAgentRole()==0){
                    roleName = "普通用户";
                }else if(shopUser.getAgentRole()==1){
                    roleName = "代理商1档";
                }else if (shopUser.getAgentRole()==2){
                    roleName = "代理商2档";
                }else if (shopUser.getAgentRole()==3){
                    roleName = "代理商3档";
                }else if (shopUser.getAgentRole()==4){
                    roleName = "代理商4档";
                }else if (shopUser.getAgentRole()==5){
                    roleName = "分销商1档";
                }else if (shopUser.getAgentRole()==6){
                    roleName = "分销商2档";
                }else if (shopUser.getAgentRole()==7){
                    roleName = "分销商3档";
                }else if (shopUser.getAgentRole()==8){
                    roleName = "分享客";
                }
                request.setAttribute("userName", shopUser.getUserName());
                request.setAttribute("phoneNumber", shopUser.getPhoneNumber());

            }
        }
        request.setAttribute("agentRole", agentRole);
        request.setAttribute("nickName", Base64Util.getFromBase64(wxUser.getNickname()));
        request.setAttribute("wxuserid", userid);
        request.setAttribute("roleName", roleName);
        view.setViewName("/pc/customer/userRoleDiv");
        return view;
    }


    /**
     * 下级客户  页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/laseuser/{userid}", method = RequestMethod.GET)
    public ModelAndView laseuser (@ApiParam("用户标识")  @PathVariable("userid") int userid,@ApiParam(hidden=true) HttpServletRequest request,WxUserQueryVo qo) throws IOException {
        ModelAndView view = new ModelAndView();
        request.setAttribute("wxuserid", userid);
        view.setViewName("/pc/customer/lastuserDiv");
        return view;
    }

    //=============================================new=======================================================================


    /**
     *
     * @param request
     * @param response
     * @param qo
     * @param type  1：客户总表   2：关注客户  3：购买客户  4：分销客户 5:代理客户
     * @return
     * @throws ParseException
     */
    @ApiOperation("查询客户总表")
    @RequestMapping(value = "/findAll/{type}", method = RequestMethod.POST)
    public PageItem<CustomerRo> findAll(@ApiParam(hidden=true) HttpServletRequest request,
                                                 @ApiParam(hidden=true)HttpServletResponse response,
                                                 @RequestBody @Valid CustomerQo qo,
                                                 @ApiParam("访问类型")  @PathVariable("type") Integer type) throws ParseException, IOException {
        PageItem<CustomerRo> pageItem =  wxUserService.newfindAll(request,qo,type);
        return pageItem;
    }

    @ApiOperation("查询微博客户总表")
    @RequestMapping(value = "/findAllwb/{type}", method = RequestMethod.POST)
    public PageItem<CustomerRo> findAllwb(@ApiParam(hidden=true) HttpServletRequest request,
                                                 @RequestBody @Valid CustomerQo qo,
                                                 @ApiParam("访问类型")  @PathVariable("type") Integer type) throws ParseException, IOException {
        PageItem<CustomerRo> pageItem =  wbUserService.findAllwb(request,qo,type);
        return pageItem;
    }



    @ApiOperation("查询新粉丝列表")
    @RequestMapping(value = "/findNewFans", method = RequestMethod.POST)
    public PageItem<CustomerRo> findNewFans(@ApiParam(hidden=true) HttpServletRequest request,
                                            @RequestBody @Valid CustomerQo qo) throws ParseException {
        PageItem<CustomerRo> pageItem =  wxUserService.findNewFans(request,qo);
        return pageItem;
    }


    /**
     *  查询员工中心 服务商授权
     * @param request
     * @param qo
     * @param type 0 员工中心  1 服务商授权
     * @return
     * @throws ParseException
     * @throws IOException
     */
    @ApiOperation("员工中心/服务商授权")
    @RequestMapping(value = "/findUsers/{type}", method = RequestMethod.POST)
    public PageItem<UserRo> findUsers(@ApiParam(hidden=true) HttpServletRequest request,
                                      @RequestBody @Valid UserQo qo,
                                      @ApiParam("访问类型")  @PathVariable("type") Integer type) throws ParseException, IOException {
        PageItem<UserRo> pageItem =  wxUserService.findUsers(request,qo,type);
        return pageItem;
    }

    @ApiOperation("员工查询列表")
    @RequestMapping(value = "/queryuser", method = RequestMethod.POST)
    public JmMessage queryuser(@ApiParam("用户查询Vo") @RequestBody @Valid UserForQueryVo userVo,
                               @ApiParam(hidden=true) HttpServletRequest request){
        JmMessage msg = new JmMessage();
        JmUserSession user1 = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(null==user1){
            return new JmMessage(1, "未登录，无法操作！");
        }
        User user2 =userService.getUser(user1.getUserId());
        if(user2.getPhoneNumber().equals(userVo.getPhoneNumber())){
            return new JmMessage(1, "不可操作当前登录的用户！");
        }
        User user = userService.findUserByPhoneNumber(userVo.getPhoneNumber());
        if(user==null){
            return new JmMessage(1, "没有查找到用户");
        }else{
            msg.setCode(0);
            msg.setData(UserConverter.toUserRo(user));
            return msg;
        }
    }


    @RequestMapping(value = "/delroleuser/{id}", method = RequestMethod.POST)
    public JmMessage delroleuser(@ApiParam(hidden=true) HttpServletRequest request,
                                 @ApiParam("用户标识")  @PathVariable("id") Integer id) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user!=null){
            userRoleService.deleteUserRole(id);
            return new JmMessage(0, "删除成功");
        }
        return new JmMessage(1, "删除失败");
    }



    @ApiOperation("服务商角色")
    @RequestMapping(value = "/find_service_role", method = RequestMethod.POST)
    public List<Role> findRole4(@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user1 = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Shop shop = shopService.getShop(user1.getShopId());
        List<Role> role1= roleService.findByTypeAndSoftId(4,shop.getSoftId());
        List<Role> role2= roleService.findByTypeAndSoftId(2,shop.getSoftId());
        for (Role role : role2) {
            role1.add(role);
        }
        List<Role> role3= roleService.findByTypeAndSoftId(5,shop.getSoftId());
        for (Role role : role3) {
            role1.add(role);
        }
        return role1;
    }

    @ApiOperation("员工角色查询")
    @RequestMapping(value = "/find_role", method = RequestMethod.POST)
    public List<Role> findRole(@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user1 = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Shop shop = shopService.getShop(user1.getShopId());
        List<Role> role1= roleService.findByTypeAndSoftId(1,shop.getSoftId());
        return role1;
    }


    @ApiOperation("员工角色修改")
    @RequestMapping(value = "/update_user_role", method = RequestMethod.POST)
    public JmMessage updateuserrole(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("用户查询Vo") @RequestBody @Valid UserRoleUo userRoleUo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user!=null){
           return  userRoleService.updateUserRole(userRoleUo,user);
        }
        return new JmMessage(1, "未登录");
    }


    @ApiOperation("永久二维码")
    @RequestMapping(value = "/forever_qrcode/{userId}", method = RequestMethod.POST)
    public JmMessage foreverQrcode(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("用户标识")  @PathVariable("userId") Integer userId) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user == null){
            return new JmMessage(1, "","未登录");
        }

        WxUser wxUser =  wxUserService.getWxUser(userId);
        String baseQrcode =  wxUserQrcodeService.getBaseQrcode(wxUser,0,0);
        return new JmMessage(0, ImgUtil.appendUrl(baseQrcode),"获取成功");
    }


    @ApiOperation("设置上级用户")
    @RequestMapping(value = "/setTopUser/{userId}/{lastUserId}", method = RequestMethod.GET)
    public JmMessage setTopUser(@ApiParam(hidden=true) HttpServletRequest request
            ,@ApiParam("上级用户标识")  @PathVariable("userId") Integer userId
            ,@ApiParam("下级用户标识")  @PathVariable("lastUserId") Integer lastUserId) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user == null){
            return new JmMessage(1, "未登录","修改失败");
        }
        WxUser wxUser =  wxUserService.getWxUser(lastUserId);
        if(user.getRoleId().equals(2)||user.getRoleId().equals(25)){ //角色是店主和运营
            if (user.getAppId() != null && user.getAppId().equals(wxUser.getAppid())) {
                wxUser.setUpperOne(userId);
                wxUserService.saveUser(wxUser);
                return new JmMessage(0, "修改成功", "修改成功");
            }
        }
        return new JmMessage(1, "无权限","修改失败");
    }


    /**
     * 上级客户/上级客户修改  页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/wx_topuser/{userid}", method = RequestMethod.GET)
    public List<WxUserRo> topuser (@ApiParam("用户标识")  @PathVariable("userid") Integer userid,@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        WxUser wxUser = wxUserService.getWxUser(userid);
        if(userSession == null || wxUser == null || userSession.getAppId() ==null || wxUser.getAppid()==null){
            return null;
        }
        List<Integer> ids = new ArrayList<>();
        if(userSession.getAppId().equals(wxUser.getAppid())){
            if(wxUser.getUpperOne()!=null){
                ids.add(wxUser.getUpperOne());
            }
            if(wxUser.getUpperTwo()!=null){
                ids.add(wxUser.getUpperTwo());
            }
        }

        List<WxUserRo> ros = new ArrayList<>();
        if(ids.size()>0){
            List<WxUser> wxUsers = wxUserService.findAll(ids);
            for (WxUser user:wxUsers) {
                WxUserRo ro = new WxUserRo();
                ro.setNickname(Base64Util.getFromBase64(user.getNickname()));
                ro.setHeadimgurl(user.getHeadimgurl());
                ro.setSubscribeTime(user.getSubscribeTime());
                if(wxUser.getUpperOne()!=null && wxUser.getUpperOne().equals(user.getUserId())){
                    ro.setLevelName("1级微客");
                }
                if(wxUser.getUpperTwo()!=null && wxUser.getUpperTwo().equals(user.getUserId())){
                    ro.setLevelName("2级微客");
                }
                if(user.getShopUserId()!=null){
                   ShopUser shopUser = shopUserService.findShopUser(user.getShopUserId());
                    if(shopUser!=null){
                        ro.setUserName(shopUser.getUserName());
                        ro.setPhoneNumber(shopUser.getPhoneNumber());
                        ro.setAgentRole(shopUser.getAgentRole());
                    }
                }
                ros.add(ro);
            }
        }

        return ros;
    }

    /**
     * 微博 上级客户/上级客户修改  页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/getWbUperUser/{id}", method = RequestMethod.GET)
    public List<WbUserRo> getWbUperUser (@ApiParam("用户标识")  @PathVariable("id") Long id, @ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        WbUserRel wbUserRel = wbUserRelService.getWbUserRel(id);
        List<WbUserRo> ros = new ArrayList<>();
        List<Long> ids = new ArrayList<Long>();
        if(wbUserRel != null && userSession!=null){
            if(!wbUserRel.getPid().equals(userSession.getUid())){
                return null;
            }else{
                if(wbUserRel.getUpperOne()!=null){
                    ids.add(wbUserRel.getUpperOne());
                }
                if(wbUserRel.getUpperTwo()!=null){
                    ids.add(wbUserRel.getUpperTwo());
                }
            }
        }
        if(ids.size()>0){
            List<WbUserRel> wbUserRels = wbUserRelService.findByIds(ids);
            for (WbUserRel rel:wbUserRels) {
                WbUser user = wbUserService.getWbUser(rel.getUid());
                WbUserRo ro = new WbUserRo();
                ro.setNickname(user.getNickname());
                ro.setHeadimgurl(user.getHeadimgurl());
                ro.setSubscribeTime(rel.getSubscribeTime());
                if(wbUserRel.getUpperOne()!=null && wbUserRel.getUpperOne().equals(rel.getId())){
                    ro.setLevelName("1级微客");
                }
                if(wbUserRel.getUpperTwo()!=null && wbUserRel.getUpperTwo().equals(rel.getId())){
                    ro.setLevelName("2级微客");
                }

                ros.add(ro);
            }
        }
        return ros;
    }


    @ApiOperation("微博下级微客数据")
    @RequestMapping(value = "/getWbLastUser/{id}", method = RequestMethod.POST)
    public PageItem<WbUserRo> getWbLastUser(@ApiParam(hidden=true) HttpServletRequest request,
                                               @RequestBody @Valid WbUserQo qo,
                                               @ApiParam("用户标识")  @PathVariable("id") Long id) throws IllegalAccessException, IOException, InstantiationException {
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<WbUserRo> pageItem =  wbUserRelService.getWxUserUpperPageItem(id,qo,userSession);
        return pageItem;
    }


    @ApiOperation("用户详情")
    @RequestMapping(value = "/userDetail/{userId}", method = RequestMethod.GET)
    public WxUserDetailRo userDetail(@ApiParam(hidden=true) HttpServletRequest request,
                                 @ApiParam("用户标识")  @PathVariable("userId") Integer userId) {
        JmUserSession user1 = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Shop shop = shopService.getShop(user1.getShopId());
        WxUserDetailRo ro = wxUserService.getUserDetail(shop.getAppId(),userId);
        return ro;
        //return null;
    }


}
