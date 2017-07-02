package com.jm.mvc.controller.system;

import com.jm.business.service.system.JmJoinService;
import com.jm.business.service.system.RoleService;
import com.jm.business.service.system.SystemService;
import com.jm.business.service.system.UserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.join.*;
import com.jm.mvc.vo.system.LeftMenuVo;
import com.jm.repository.client.dto.*;
import com.jm.repository.po.system.JmJoin;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.system.user.User;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.join.JoinConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


/**
 * <p>运营服务平台</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/30
 */

@Api
@RestController
@RequestMapping(value = "/join")
public class JmJoinController {

    @Autowired
    private JmJoinService jmJoinService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserService userService;

    @ApiOperation("申请首页获取")
    @RequestMapping(value = "/apply/{type}", method = RequestMethod.GET)
    public JoinVo apply( @ApiParam(hidden=true) HttpServletRequest request,
                         @ApiParam(hidden = true) @PathVariable("type") Integer type) throws Exception{
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        return jmJoinService.getJoinVo(user.getUserId(),type);
    }

//    @ApiOperation("加盟平台列表分页查询")
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    public PageItem<JoinVo> queryJoinList(@RequestBody @Valid JoinDataDto joinDataDto) throws Exception {
//        return  jmJoinService.queryJoinList(joinDataDto);
//    }

//    @ApiOperation("加盟数据返回总部平台")
//    @RequestMapping(value = "",method = RequestMethod.POST)
//    public JoinVo querySoftwareAgent(@RequestBody @Valid JoinDataDto joinDataDto)throws IOException {
//        JoinVo joinVo = jmJoinService.getJoinVo(joinDataDto);
//        return joinVo;
//    }

//    @ApiOperation("保存审核意见")
//    @RequestMapping(value = "/check",method = RequestMethod.POST)
//    public JmMessage addCheck(@RequestBody @Valid CheckDto checkDto){
//        jmJoinService.addCheck(checkDto);
//        return new JmMessage(0,"保存成功");
//    }

//    @ApiOperation("删除单个加盟数据")
//    @RequestMapping(value = "/delete",method = RequestMethod.POST)
//    public JmMessage deleteReview(@RequestBody @Valid JoinDataDto joinDataDto){
//        jmJoinService.deleteJoin(joinDataDto);
//        return new JmMessage(0,"删除成功");
//    }


    @ApiOperation("加盟申请页面获取")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView personalJoin(@ApiParam(hidden=true) HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer type = Toolkit.parseObjForInt(request.getParameter("type")) ;
        Integer subType =  Toolkit.parseObjForInt(request.getParameter("subType"));
        JmJoin jmJoin = jmJoinService.findJmJoin(user.getUserId(),type);
        String phoneNumber = userService.getUser(user.getUserId()).getPhoneNumber();
        request.setAttribute("phoneNumber", phoneNumber);
        if(null!=jmJoin){
            JoinVo joinVo = jmJoinService.findJmJoinVo(jmJoin);
            request.setAttribute("jmJoin", joinVo);
            String a = jmJoin.getBusinessLicense();
            if(null!=a && ""!=a){
                jmJoin.setBusinessLicense(ImgUtil.appendUrl(jmJoin.getBusinessLicense(),200));
            }
            String checkContext = jmJoinService.queryCheck(jmJoin.getUserId(),jmJoin.getType()).getCheckContext();
            request.setAttribute("checkContext", checkContext);
        }
        if(type==1){
            if(subType==1){
                view.setViewName("/pc/softwareAgent/personal_join");
            }else {
                view.setViewName("/pc/softwareAgent/company_join");
            }
        }else if(type==2){
            if(subType==1){
                view.setViewName("/pc/operation/personal_join");
            }else {
                view.setViewName("/pc/operation/company_join");
            }
            List<Role> roles= roleService.queryServiceRole();
            request.setAttribute("roles", roles);
        }
        return view;
    }

    @ApiOperation("加盟申请页面等待获取")
    @RequestMapping(value = "/wait", method = RequestMethod.GET)
    public ModelAndView pWait(@ApiParam(hidden=true) HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Integer type = Toolkit.parseObjForInt(request.getParameter("type")) ;
        Integer subType =  Toolkit.parseObjForInt(request.getParameter("subType"));
        if (type==1){
            if(subType==1){
                view.setViewName("/pc/softwareAgent/p_join_wait");
            }else {
                view.setViewName("/pc/softwareAgent/c_join_wait");
            }
        }else if (type==2){
            if(subType==1){
                view.setViewName("/pc/operation/p_join_wait");
            }else {
                view.setViewName("/pc/operation/c_join_wait");
            }
        }
        return view;
    }

    @ApiOperation("加盟通过后首页获取")
    @RequestMapping(value = "/index/{type}", method = RequestMethod.GET)
    public ModelAndView getIndex(@ApiParam(hidden = true) @PathVariable("type") Integer type,
                                @ApiParam(hidden=true) HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if(type==1){
            List<LeftMenuVo> leftMenus= systemService.findLeftMenu(14,0);
            request.setAttribute("leftMenus", leftMenus);
            view.setViewName("/pc/softwareAgent/index");
        }else if (type==2){
            view.setViewName("/pc/operation/business");
        }
        return view;
    }

    @ApiOperation("加盟申请页面保存")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JmMessage addJoin(@ApiParam("加盟Co") @RequestBody @Valid JoinCo joinCo,
                                      @ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        JmJoin jmJoin = jmJoinService.findJmJoin(user.getUserId(),joinCo.getType());
        if (null!=jmJoin){
            jmJoinService.addJoin(user.getUserId(),joinCo);
        }else{
            JmJoin jmJoinNew = JoinConverter.toJoin(joinCo);
            jmJoinNew.setUserId(user.getUserId());
            jmJoinService.saveJoin(jmJoinNew);
        }
        return new JmMessage(0 ,"提交成功！");
    }

    @ApiOperation("加盟信息更新")
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public JmMessage updateJoin(@ApiParam("加盟Uo") @RequestBody @Valid JoinUo joinUo){
        jmJoinService.updateJoin(joinUo);
        return new JmMessage(0,"修改成功！");
    }

//    @ApiOperation("总部平台用户中心客户中心分页查询")
//    @RequestMapping(value = "/customers", method = RequestMethod.POST)
//    public PageItem<JoinClassVo> queryClassVos(@RequestBody @Valid JoinClassDto joinClassDto) throws Exception {
//        PageItem<JoinClassVo> pageItem = jmJoinService.queryClassVos(joinClassDto);
//        return  pageItem;
//    }

//    @ApiOperation("获取总部平台代理类编辑弹窗基本资料Vo")
//    @RequestMapping(value = "/data",method = RequestMethod.POST)
//    public ClassDataVo queryClassDataVo(@RequestBody @Valid JoinDataDto joinDataDto)throws IOException {
//        ClassDataVo classDataVo = jmJoinService.getClassVo(joinDataDto);
//        return classDataVo;
//    }

//    @ApiOperation("总部平台代理类编辑弹窗基本资料页面保存")
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public JmMessage updateClassDto(@ApiParam("加盟Dto") @RequestBody @Valid ClassDataDto classDataDto) {
//        JmJoin jmJoin = jmJoinService.findJmJoin(classDataDto.getUserId(),classDataDto.getType());
//        User user = userService.getUser(classDataDto.getUserId());
//       jmJoinService.saveClassData(jmJoin,user,classDataDto);
//        return new JmMessage(0 ,"保存成功！");
//    }

    @ApiOperation("总部平台获取商家平台服务商角色信息和商家平台获取角色信息")
    @RequestMapping(value = "/roles",method = RequestMethod.POST)
    public List<Role> queryRoles(){
        List<Role> roles = roleService.queryServiceRole();
        return roles;
    }

//    @ApiOperation("总部派单加盟平台列表分页查询")
//    @RequestMapping(value = "/dispatch/list", method = RequestMethod.POST)
//    public PageItem<DispatchJoinVo> queryJoinList(@RequestBody @Valid DispatchJoinDto dispatchJoinDto) throws Exception {
//        return  jmJoinService.queryJoins(dispatchJoinDto);
//    }

//    @ApiOperation("角色数据返回总部平台")
//    @RequestMapping(value = "/dispatch/roles",method = RequestMethod.POST)
//    public DispatchVo getDispatchVo()throws IOException {
//        List<Role> roles = roleService.queryServiceRole();
//        DispatchVo dispatchVo = new DispatchVo();
//        dispatchVo.setRoles(roles);
//        return dispatchVo;
//    }

    @ApiOperation("获取总部平台派单列表")
    @RequestMapping(value = "/dispatchs/{type}",method = RequestMethod.POST)
    private PageItem getDispatchList (@ApiParam(hidden = true) @PathVariable("type") Integer type,
                                      @RequestBody @Valid DispatchDto dispatchDto,
                                      @ApiParam(hidden=true) HttpServletRequest request) throws IOException{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        dispatchDto.setJoinId(jmJoinService.findJmJoin(user.getUserId(),type).getId());
        return jmJoinService.getDispatchList(dispatchDto);
    }

    @ApiOperation("获取总部平台通知列表")
    @RequestMapping(value = "/notices",method = RequestMethod.POST)
    private PageItem getNoticeList (@RequestBody @Valid NoticeDto noticeDto) throws IOException{
        return jmJoinService.getNoticeList(noticeDto);
    }
    @ApiOperation("获取总部平台单个通知")
    @RequestMapping(value = "/notice",method = RequestMethod.POST)
    private NoticeContextDto getNotice (@RequestBody @Valid NoticeDto noticeDto) throws IOException{
        return jmJoinService.getNotice(noticeDto);
    }
}
