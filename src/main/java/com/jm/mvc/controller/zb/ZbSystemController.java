package com.jm.mvc.controller.zb;

import com.jm.business.service.shop.InitDataService;
import com.jm.business.service.shop.ShopCategoryService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.wx.WxAuthService;
import com.jm.business.service.wx.WxService;
import com.jm.business.service.zb.ZbResourceService;
import com.jm.business.service.zb.ZbUserRoleService;
import com.jm.business.service.zb.ZbUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbPost;
import com.jm.repository.po.zb.system.ZbResource;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.staticcode.constant.ZbConstant;
import com.jm.staticcode.converter.zb.ZbUserConverter;
import com.jm.staticcode.util.BaseUtil;
import com.jm.staticcode.util.wx.SMSUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>系统管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@RestController
@RequestMapping(value = "/zb")
public class ZbSystemController {

    @Autowired
    private ZbUserService zbUserService;

    @Autowired
    private ZbUserRoleService zbUserRoleService;

    @Autowired
    private ZbResourceService zbResourceService;

   /* @ApiOperation("登录页面")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/system/login");
        return view;
    }
*/

    @Autowired
    private ShopService shopService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxPubAccountService wxPubAccountService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private InitDataService initDataService;
    @Autowired
    private WxService wxService;

    @ApiOperation("登录按钮")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JmMessage login(@ApiParam(hidden = true) HttpServletRequest request,
                           @ApiParam("登录信息") @RequestBody @Valid ZbLoginVo zbLoginVo)throws Exception{
        ZbUser zbUser = zbUserService.findUser(zbLoginVo);
        if (zbUser == null){
            return new JmMessage(1, "登录失败", "用户名或密码不正确");
        }else {
            JmUserSession userSession = ZbUserConverter.toUserSession(zbUser);

            Integer shopId = 1;
            Shop shop = shopService.findShopById(shopId);

            if(shop == null ){
                return new JmMessage(1, "店铺不存在");
            }
            if(shop.getAppId() == null || shop.getAppId().equals("") ){
                return new JmMessage(1, "店铺未授权");
            }

            WxPubAccount account = wxPubAccountService.findWxPubAccountByAppid(shop.getAppId());
            if(account == null){
                return new JmMessage(1, "店铺未授权");
            }
            if(account.getPubQrcodeUrl()==null || "".equals(account.getPubQrcodeUrl())){
                String accessToken =wxAuthService.getAuthAccessToken(shop.getAppId());
                String pubQrcodeUrl = wxService.getForeverQrcode(accessToken);
                account.setPubQrcodeUrl(pubQrcodeUrl);
                WxPubAccount returnACcount = wxPubAccountService.save(account);
                userSession.setPubQrcodeUrl(returnACcount.getPubQrcodeUrl());
            }else{
                userSession.setPubQrcodeUrl(account.getPubQrcodeUrl());
            }
            zbUserService.saveLoginLog(request,zbUser);
            userSession.setShopId(shopId);
            userSession.setAppId(shop.getAppId());
            userSession.setHeadImg(shop.getImgUrl());
            userSession.setRoleId(zbUserRoleService.findByUserId(zbUser.getUserId()).getRoleId());
            userSession.setShopName(shop.getShopName());
            userSession.setUid(shop.getWbUid()); // cj 2017-03-04  微博商家用户uid 加入session

            request.getSession().setAttribute(ZbConstant.SESSION_USER, userSession);
        }
        return new JmMessage(0, "登录成功");
    }

    @ApiOperation("退出登录")
    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public ModelAndView loginout(@ApiParam(hidden=true) HttpServletRequest request) {
        request.getSession().removeAttribute(ZbConstant.SESSION_USER);
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/zb/system/login");
        return view;
    }

    @ApiOperation("忘记密码")
    @RequestMapping(value = "/backPwd", method = RequestMethod.GET)
    public ModelAndView backPwd() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/zb/system/backPwd");
        return view;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ZbUserVo user(@ApiParam(hidden = true) HttpServletRequest request){
        JmUserSession jmuser= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        return zbUserService.getUserVo(jmuser.getUserId());
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public JmMessage updateUser(@ApiParam(hidden = true) HttpServletRequest request,
                                  @RequestBody ZbUserUo zbUserUo){
        JmUserSession jmuser= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        ZbUser zbUser = zbUserService.update(jmuser.getUserId(), zbUserUo);
        if(zbUser !=null){
            return new JmMessage(0,"更新成功");
        }
        return new JmMessage(1,"更新失败");
    }

    @ApiOperation("用户修改")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
    public JmMessage updateUser(@ApiParam("用户标识") @PathVariable("userId") Integer userId,
                                  @ApiParam("用户修改VO") @RequestBody @Valid ZbUserUo zbUserUo){
        zbUserService.updateUser(userId, zbUserUo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("用户修改")
    @RequestMapping(value = "/staff_user", method = RequestMethod.PUT)
    public JmMessage updateStaffUser(@ApiParam("用户修改VO") @RequestBody @Valid UserForStaffUo userUo){
        ZbUser zbUser = zbUserService.getUserForUser(userUo.getUserId());
        zbUserService.updateStaffUser(zbUser,userUo);
        zbUserRoleService.updateUserRole(zbUser.getUserId(),userUo.getRoleId());
        return new JmMessage(0,"保存成功");
    }

   @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ZbUser addUser(@RequestBody @Valid ZbUserCo zbUserCo){
        return zbUserService.add(zbUserCo);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public JmMessage delUser(@PathVariable("id") int id){
        zbUserService.del(id);
        return new JmMessage(0,"删除成功！");
    }
    @ApiOperation("用户批量删除")
    @RequestMapping(value = "/user_list", method = RequestMethod.DELETE)
    public JmMessage deleteUsers(@ApiParam("用户标识")  @RequestBody @Valid List<Integer> ids){
        zbUserService.deleteUsers(ids);
        return new JmMessage(0,"删除成功！");
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ZbUserRo getUser(@PathVariable("id") int userId){
        return zbUserService.getUserRo(userId);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public ZbUserRo getUserRo(@PathVariable("id") int userId){
        return zbUserService.getUserRo(userId);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<ZbUserRo> queryUser(){
        return zbUserService.queryUser();
    }

    @RequestMapping(value = "/userlist", method = RequestMethod.POST)
    public PageItem<ZbUserRo> queryUserList(@RequestBody @Valid ZbUserQo zbUserQo) throws Exception{
        return zbUserService.queryUserList(zbUserQo);
    }

    @RequestMapping(value = "/system/backPwd", method = RequestMethod.GET)
    public ModelAndView backPwd(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/zb/system/backPwd");
        return view;
    }

    @ApiOperation("注册页")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        String path = BaseUtil.getPath(request);
        request.setAttribute("basePath", path);
        view.setViewName("/pc/zb/system/register");
        return view;
    }

    @ApiOperation("关注扫码页")
    @RequestMapping(value = "/register/focus_scan", method = RequestMethod.GET)
    public ModelAndView focusScan(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/zb/system/focus_scan");
        return view;
    }

    @ApiOperation("完成注册页")
    @RequestMapping(value = "/register/finish_register", method = RequestMethod.GET)
    public ModelAndView finishRegister(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/zb/system/finish_register");
        return view;
    }


    @ApiOperation("用户创建")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public JmMessage createUser(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("用户创建Co") @RequestBody @Valid UserForCreateCo userForCreateCo){

        String jmCode = (String)request.getSession().getAttribute("jmRegCode");
        String jmRegPhone = (String)request.getSession().getAttribute("jmRegPhone");

        if(null!=jmCode){
            if(!userForCreateCo.getCode().equals(jmCode)){
                return new JmMessage(3, "创建失败" ,"验证码输入错误！");
            }
        }else{
            return new JmMessage(3, "创建失败" ,"请先发送验证码！");
        }
        if(!jmRegPhone.equals(userForCreateCo.getPhoneNumber())){
            return new JmMessage(3,"创建失败","发送验证码的手机与注册填写的手机号不匹配！");
        }
        ZbUser zbUser = zbUserService.findUserByPhoneNumber(userForCreateCo.getPhoneNumber());
        if (zbUser ==null){
            ZbUser zbUser1 = zbUserService.createUser(ZbUserConverter.toUser(userForCreateCo));
            if (zbUser1 ==null){
                return new JmMessage(1,"创建失败","系统内部错误！");
            }else {
                request.getSession().setAttribute(ZbConstant.SESSION_USER, ZbUserConverter.toUserSession(zbUser1));
                return new JmMessage(0,"创建成功！");
            }
        }else {
            return new JmMessage(2, "创建失败", "手机号已被注册！");
        }
    }

    @ApiOperation("发送短信")
    @RequestMapping(value = "/sendmsg/{status}", method = RequestMethod.POST)
    public JmMessage sendmsg(@ApiParam("发送短信状态") @PathVariable("status") Integer status,
                               @ApiParam(hidden=true) HttpServletRequest request,
                               @ApiParam("用户创建CO") @RequestBody UserForCreateCo userForCreateCo){
        String sixCode=SMSUtil.getSixCode();
        String state="0";
        if(status.equals(0)){
            state = SMSUtil.sendMsg("【聚米为谷】注册验证码:["+sixCode+"]", userForCreateCo.getPhoneNumber());
            request.getSession().setAttribute("jmRegCode", sixCode);
            request.getSession().setAttribute("jmRegPhone", userForCreateCo.getPhoneNumber());
        }
        if(status.equals(1)){
            state = SMSUtil.sendMsg("【聚米为谷】修改密码验证码:["+sixCode+"]", userForCreateCo.getPhoneNumber());
            request.getSession().setAttribute("jmBackCode", sixCode);
            request.getSession().setAttribute("jmBackPhone", userForCreateCo.getPhoneNumber());
        }
        if(status.equals(2)){
            state = SMSUtil.sendMsg("【聚米为谷】修改手机号码验证码:["+sixCode+"]", userForCreateCo.getPhoneNumber());
            request.getSession().setAttribute("jmModifyCode", sixCode);
            request.getSession().setAttribute("jmModifyPhone", userForCreateCo.getPhoneNumber());
        }

        if(state.equals("1")){
            return new JmMessage(0, "发送成功");
        }else{
            return new JmMessage(1, "发送失败","发送失败");
        }
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/system/backPwd", method = RequestMethod.POST)
    public JmMessage backPwd(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam("用户更新VO") @RequestBody @Valid ZbUserUo zbUserUo){
        //判断验证码
        String jmcode=(String) request.getSession().getAttribute("jmBackCode");
        String jmBackPhone=(String) request.getSession().getAttribute("jmBackPhone");
        if(null!=jmcode){
            if(!zbUserUo.getCode().equals(jmcode)){

                return new JmMessage(1, "失败" ,"验证码输入错误！");
            }
        }else{
            return new JmMessage(1, "失败" ,"请先发送验证码！");
        }

        if(!jmBackPhone.equals(zbUserUo.getPhoneNumber())){
            return new JmMessage(3, "创建失败" ,"发送验证码的手机号与填写的手机号不匹配！");
        }

        ZbUser zbUser1 = zbUserService.findUserByPhoneNumber(zbUserUo.getPhoneNumber());
        if(zbUser1 ==null){
            return new JmMessage(1, "失败！","当前手机号未注册！");
        }else{
            zbUser1.setPassword(zbUserUo.getPassword());
            ZbUser zbUser = zbUserService.saveUser(zbUser1);
            if(zbUser !=null){
                request.getSession().setAttribute(ZbConstant.SESSION_USER, ZbUserConverter.toUserSession(zbUser));
                return new JmMessage(0, "成功","修改成功！");
            }else{
                return new JmMessage(-1, "失败","系统内部错误！");
            }
        }
    }

    @ApiOperation("根据角色获取菜单")
    @RequestMapping(value = "/role_resource/{status}", method = RequestMethod.GET)
    public List<ZbResource> findResourceByRole(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam("状态") @PathVariable("status") Integer status) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        return zbResourceService.findResourceByRole(user,status);
    }

    @ApiOperation("获取部门列表")
    @RequestMapping(value = "/department/list", method = RequestMethod.GET)
    public List<ZbDepartment> getDepartmentList(){
       List<ZbDepartment>  list = zbUserService.getDepartmentList();
        return list;
    }

    @ApiOperation("获取登录日志")
    @RequestMapping(value = "/login/log", method = RequestMethod.POST)
    public PageItem<LoginLogVo> getLoginLogs(@ApiParam(hidden=true) HttpServletRequest request,
                                             @ApiParam("登录日志qo") @RequestBody LoginLogQo loginLogQo) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        loginLogQo.setUserId(user.getUserId());
        PageItem<LoginLogVo> loginLogVos = zbUserService.getLoginLogs(loginLogQo);
        return loginLogVos;
    }

    @ApiOperation("新增部门")
    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public JmMessage saveDepartment(@ApiParam(hidden=true) HttpServletRequest request,
                                    @ApiParam("部门co") @RequestBody  DepartmentCo departmentCo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        ZbDepartment zbDepartment = ZbUserConverter.toZbDepartment(departmentCo);
        zbDepartment.setUserId(user.getUserId());
        zbUserService.addDepartment(zbDepartment);
        return new JmMessage(0,"保存成功!");
    }

    @ApiOperation("更新部门")
    @RequestMapping(value = "/department", method = RequestMethod.PUT)
    public JmMessage updateDepartment(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody DepartmentUo departmentUo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        departmentUo.setUserId(user.getUserId());
        zbUserService.updateDepartment(departmentUo);
        return new JmMessage(0,"修改成功");
    }

    @ApiOperation("删除部门")
    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteDepartment(@ApiParam("部门id") @PathVariable("id") int id){
        return zbUserService.deleteDepartment(id);
    }

    @ApiOperation("部门分页查询")
    @RequestMapping(value = "/departments", method = RequestMethod.POST)
    public PageItem<DepartmentVo> queryRoleTypes(@RequestBody @Valid DepartmentQo departmentQo)throws Exception{
        PageItem<DepartmentVo> departmentVoPageItem = zbUserService.queryDepartments(departmentQo);
        return departmentVoPageItem;
    }

    @ApiOperation("新增部门")
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public JmMessage savePost(@ApiParam(hidden=true) HttpServletRequest request,
                                    @ApiParam("部门co") @RequestBody  PostCo postCo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        ZbPost zbPost = ZbUserConverter.toZbPost(postCo);
        zbPost.setUserId(user.getUserId());
        zbUserService.addPost(zbPost);
        return new JmMessage(0,"保存成功!");
    }

    @ApiOperation("更新岗位")
    @RequestMapping(value = "/post", method = RequestMethod.PUT)
    public JmMessage updatePost(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody PostUo postUo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        postUo.setUserId(user.getUserId());
        zbUserService.updatePost(postUo);
        return new JmMessage(0,"修改成功");
    }

    @ApiOperation("删除部门")
    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    public JmMessage deletePost(@ApiParam("岗位id") @PathVariable("id") int id){
        return zbUserService.deletePost(id);
    }

    @ApiOperation("部门分页查询")
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public PageItem<PostVo> queryPosts(@RequestBody @Valid PostQo postQo)throws Exception{
        PageItem<PostVo> postVoPageItem = zbUserService.queryPosts(postQo);
        return postVoPageItem;
    }

}
