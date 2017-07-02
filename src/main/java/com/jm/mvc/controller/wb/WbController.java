package com.jm.mvc.controller.wb;

import com.jm.business.service.wb.*;
import com.jm.mvc.vo.wb.WbButtonVo;
import com.jm.mvc.vo.wb.WbPushVo;
import com.jm.repository.client.dto.wb.WbAccessToken;
import com.jm.repository.client.dto.wb.WbContenDto;
import com.jm.repository.client.dto.wb.WbResultMsg;
import com.jm.repository.client.dto.wb.WbUidDto;
import com.jm.repository.client.dto.wb.WbUserDto3;
import com.jm.repository.po.wb.WbUserGroup;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.wb.WbShopUserCo;
import com.jm.repository.client.wb.WbClient;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.util.List;

/**
 * <p>微博</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/28 17:25
 */
@Api
@RestController
@RequestMapping(value = "/")
public class WbController {
    @Autowired
    private WbAuthService wbAuthService;  // cj add 微博授权服务层
    @Autowired
    private WbClient wbClient;
    @Autowired
    private WbShopUserService wbShopUserService;
    @Autowired
    private WbMenuService wbMenuService;
    @Autowired
    private WbUserService wbUserService;
    @Autowired
    private WbUserGroupService wbUserGroupService;

/*
    @ApiOperation("进入微博网页授权页面")
    @RequestMapping(value = "/wbOauth", method = RequestMethod.GET)
    public void oauth(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        wbAuthService.getCode(response,"");
    }
*/

    /*@ApiOperation("进入微博静默授权(用户关注授权)")
    @RequestMapping(value = "/wbOauth/scope", method = RequestMethod.GET)
    public void oauthSnsapi(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        wbAuthService.getCode(response,"snsapi_base","/redirectWbOauth");
    }*/

    @ApiOperation("网页授权完重定向到微博，返回code")
    @RequestMapping(value = "/redirectWbOauth", method = RequestMethod.GET)
    public void redirectOauth(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView();
        WbAccessToken wbAccessToken  = wbAuthService.oauth(request,response);
    }

    @ApiOperation("粉丝服务-申请成为开发者模式")
    @RequestMapping(value = "/developWb", method = RequestMethod.GET)
    public void developWb(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response) throws ServletException, IOException {
        ModelAndView view = new ModelAndView();
        wbAuthService.developWb(request,response);
    }

    @ApiOperation("保存商家微博token")
    @RequestMapping(value = "/saveWbShopToken", method = RequestMethod.POST)
    public JmMessage saveWbShopToken(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response,
                                     @RequestBody WbShopUserCo wbShopUserCo) throws Exception {
        String accessToken = Toolkit.parseObjForStr(wbShopUserCo.getAccessToken());
        if(!"".equals(accessToken)){
           return wbShopUserService.saveWbShopToken(request,wbShopUserCo);
        }
        return new JmMessage(1,"授权失败，accessToken为空!!");
    }

    @ApiOperation("创建微博菜单")
    @RequestMapping(value = "/wb/menu", method = RequestMethod.POST)
    public WbResultMsg createWbMenu(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response
                            , @RequestBody WbButtonVo[] wbMenuCoList) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Long uid = 0L;
        Integer shopId = 0;
        if(user!=null){
            uid = user.getUid();
            shopId = user.getShopId();
        }
        return wbMenuService.createWbMenu(shopId,uid,wbMenuCoList);
    }

    @ApiOperation("菜单获取")
    @RequestMapping(value = "/wb/menu", method = RequestMethod.GET)
    public List<WbButtonVo> queryButton(HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId =0;
        Long uid = 0L;
        if(user!=null){
            shopId = user.getShopId();
            uid = user.getUid();
        }
        return wbMenuService.getMenu(shopId,uid);
    }

    @ApiOperation("菜单管理")
    @RequestMapping(value = "/wbMenu/manage", method = RequestMethod.GET)
    public ModelAndView toMenuManage(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/menu/wb/wb_menu_manage");
        view.getModelMap().addAttribute("domain",Constant.DOMAIN);
        return view;
    }

    @ApiOperation("微博用户信息")
    @RequestMapping(value = "/wb/user", method = RequestMethod.GET)
    public WbUserDto3 getWbUser( ) throws Exception{
        String access_token = "2.00nbm_VBCjEIsC0c6812cf325623cB";
        String uid = "5919515026";
        WbUserDto3 dto = wbUserService.getWbUser(access_token,uid);
        return null;
    }

    @ApiOperation("获取用户的关注列表")
    @RequestMapping(value = "/wb/friends", method = RequestMethod.GET)
    public WbUserDto3 getWbFriends( ) throws Exception{

        String access_token = "2.00oQgb9GlocjdBb61fc89067rrsRxC";
        String uid = "5919515026";
        WbUserDto3 dto = wbUserService.getWbFriends(access_token,uid);
        return dto;
    }

    @ApiOperation("获取某个用户最新发表的微博列表")
    @RequestMapping(value = "/wb/new_context", method = RequestMethod.GET)
    public WbContenDto getNewWbContext( ) throws Exception{

        String access_token = "2.00oQgb9GlocjdBb61fc89067rrsRxC";
        String uid = "5919515026";
        WbContenDto dto = wbUserService.getNewWbContext(access_token,uid);
        return dto;
    }

    @ApiOperation("获取订阅列表")
    @RequestMapping(value = "/wb/uids", method = RequestMethod.GET)
    public WbUidDto getWbUids(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        WbUidDto dto = wbUserService.getWbUids(access_token,user.getUid());
        return dto;
    }

    @ApiOperation("粉丝服务-申请成为开发者模式")
    @RequestMapping(value = "/developWb", method = RequestMethod.POST)
    public void listenEvent(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response,@RequestBody WbPushVo wbPushVo) throws Exception {
        wbUserService.processRequest(request,wbPushVo);
    }

    @ApiOperation("拉取微博用户分组并保存")
    @RequestMapping(value = "/wb/group", method = RequestMethod.GET)
    public void getWbUserGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        wbUserGroupService.getWbUserGroup(access_token,"2",user.getUid());
    }

    @ApiOperation("查询商家所有用户所在分组并保存")
    @RequestMapping(value = "/wb/save_group", method = RequestMethod.GET)
    public void saveWbUserGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        wbUserGroupService.saveWbUserGroup(access_token,user.getUid());
    }

    @ApiOperation("创建微博分组")
    @RequestMapping(value = "/wb/create_group", method = RequestMethod.GET)
    public void createWbUserGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        String name = "股票";
        wbUserGroupService.createWbGroup(access_token,name,user.getUid());
    }

    @ApiOperation("删除微博分组")
    @RequestMapping(value = "/wb/delete_group", method = RequestMethod.GET)
    public JmMessage deleteWbUserGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        Long groupid = 4087344363563908L;
        return  wbUserGroupService.deleteWbGroup(access_token,groupid,user.getUid());
    }

    @ApiOperation("二次（直接）删除微博分组")
    @RequestMapping(value = "/wb/delete_group2", method = RequestMethod.GET)
    public JmMessage deleteWbGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        Long groupid = 4087344363563908L;
        return  wbUserGroupService.deleteGroup(access_token,groupid,user.getUid());
    }

    @ApiOperation("修改分组名")
    @RequestMapping(value = "/wb/update_group", method = RequestMethod.GET)
    public JmMessage updateWbGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
            JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
            String access_token = wbUserService.getAccessToken(user.getUid());
            Long groupid = 4087303079616700L;
            String name = "电视剧";
        return  wbUserGroupService.updateGroup(access_token,groupid,name,user.getUid());
    }

    @ApiOperation("移动用户分组")
    @RequestMapping(value = "/wb/move_group", method = RequestMethod.GET)
    public JmMessage moveWbGroup(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String access_token = wbUserService.getAccessToken(user.getUid());
        Long groupid = 4087344363563908L;
        Long followerId = 1684726464L;
        return  wbUserGroupService.moveUserGroup(access_token,followerId,groupid,user.getUid());
    }
}
