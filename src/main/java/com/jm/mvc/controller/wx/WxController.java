package com.jm.mvc.controller.wx;

import com.jm.business.service.online.HxUserService;
import com.jm.business.service.shop.LinkService;
import com.jm.business.service.system.SystemService;
import com.jm.business.service.wx.WxAuthService;
import com.jm.business.service.wx.WxMessageService;
import com.jm.business.service.wx.WxService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.online.Customer;
import com.jm.mvc.vo.system.ButtonVo;
import com.jm.mvc.vo.wx.wxmessage.WxMessageQo;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.po.online.HxUser;
import com.jm.repository.po.shop.WxMenu;
import com.jm.repository.po.system.Link;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.FortyEightUserUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.WeixinUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;


/**
 * <p>微信接口管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/")
public class WxController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private WxService wxService;

    @Autowired
    private LinkService linkService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxMessageService wxMessageService;
    @Autowired
    private HxUserService hxWxUserService;

    @ApiOperation("菜单创建")
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    public ResultMsg createButton(@ApiParam("菜单内容") @RequestBody ButtonVo[] buttonVo,HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = "";
        if(user!=null){
            appid = user.getAppId();
        }
        return systemService.createMenu(buttonVo,appid,user.getShopId());
    }

    @ApiOperation("菜单获取")
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public List<ButtonVo> queryButton(HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = "";
        if(user!=null){
            appid = user.getAppId();
        }
        return systemService.getMenu(appid);
    }


    @ApiOperation("菜单管理")
    @RequestMapping(value = "/menu/manage", method = RequestMethod.GET)
    public ModelAndView toMenuManage(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/menu/menu_manage");
        view.getModelMap().addAttribute("domain",Constant.DOMAIN);
        return view;
    }


    @ApiOperation("点击菜单链接设置")
    @RequestMapping(value = "/menu/setMenuLink", method = RequestMethod.GET)
    public String setMenuLink() throws IOException{
        List<Link> link = linkService.getLinks();
        return JsonMapper.toJson(link);
    }

    @RequestMapping(value="/kf/message",method = RequestMethod.POST)
    public JmMessage sendWxTextMsg(@RequestBody WxMessageQo wxMessageQo,
                                   @ApiParam(hidden=true)HttpServletRequest request) throws Exception {
        JmMessage jMsg;
        JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(userSession.getRefreshTime()==null||System.currentTimeMillis()-userSession.getRefreshTime()>=Constant.REFRESH_TIME){
            log.info("五分钟刷新在线时间");
            userSession.setRefreshTime(System.currentTimeMillis());
            Equalizer.addOnlineServiceUser(userSession.getShopId(),userSession.getUserId(),userSession.getHxAccount());

        }
        HxUser hxUser = hxWxUserService.getHxUserByAppidAndOpenid(wxMessageQo.getAppid(),wxMessageQo.getOpenid());
        Customer to = Equalizer.getCustormer(hxUser.getHxAccount());
        if(to==null){
            Equalizer.addCustormer(hxUser.getHxAccount(),userSession.getShopId(),userSession.getHxAccount());
        }
        String openid = wxMessageQo.getOpenid();
        String appid = wxMessageQo.getAppid();
        String msg = wxMessageQo.getMsg();
        if(null!=openid&&!"".equals(openid)&&null!=appid&&!"".equals(appid)){
            String token = wxAuthService.getAuthAccessToken(appid);
            msg = URLDecoder.decode(msg,"utf-8");
            wxMessageService.sendMsg(openid, token, msg,appid);
            hxWxUserService.upReply(openid);
            log.info("update is calling");
            jMsg = new JmMessage(0,"发送成功");
        }else{
            jMsg = new JmMessage(1,"参数错误");
        }
        return jMsg;
    }
    @RequestMapping(value="/kf/file",method =RequestMethod.POST )
    public JmMessage sendWxFileMsg(@RequestParam("type")String type, @RequestParam("openid")String openid,
                                   @RequestParam("appid")String appid,
                                   @ApiParam(hidden=true)HttpServletRequest request) {
        JmMessage jMsg =null;
        if(null!=openid&&!"".equals(openid)&&null!=appid&&!"".equals(appid)){
            try{
                //Map<String,Object> message = new HashMap<>();
                JmUserSession userSession = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
                if(userSession.getRefreshTime()==null||System.currentTimeMillis()-userSession.getRefreshTime()>=Constant.REFRESH_TIME){
                    log.info("五分钟刷新在线时间");
                    userSession.setRefreshTime(System.currentTimeMillis());
                    Equalizer.addOnlineServiceUser(userSession.getShopId(),userSession.getUserId(),userSession.getHxAccount());

                }
                HxUser hxUser = hxWxUserService.getHxUserByAppidAndOpenid(appid,openid);
                Customer to = Equalizer.getCustormer(hxUser.getHxAccount());
                if(to==null){
                    Equalizer.addCustormer(hxUser.getHxAccount(),userSession.getShopId(),userSession.getHxAccount());
                }
                String token = wxAuthService.getAuthAccessToken(appid);
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> mapFile = multipartRequest.getFileMap();
                for(String name :mapFile.keySet()){
                    MultipartFile multiFile =  mapFile.get(name);
                    InputStream is = multiFile.getInputStream();
                    WeixinUtil util = new WeixinUtil();
                    String mediaId = util.getMediaId(is,token,type,multiFile.getOriginalFilename());
                    wxMessageService.sendImg(mediaId, token, openid);
                    hxWxUserService.upReply(openid);
                    jMsg = new JmMessage(0,"发送成功");
                }
            }catch (Exception e){
                jMsg = new JmMessage(1,"发送失败");
            }
        }else{
            jMsg = new JmMessage(1,"参数错误");
        }
        return jMsg;
    }

    /**
     * 微信加载ftl模板
     * */
    @RequestMapping(value="/template/{project}/{path}/{name}",method =RequestMethod.GET)
    public ModelAndView template(@PathVariable("name")String name,
                                 @PathVariable("path")String path,
                                 @PathVariable("project")String project,
                                 @ApiParam(hidden=true)HttpServletRequest request){
        ModelAndView view  = new ModelAndView("/"+project+"/"+path+"/"+name);
        return view;
    }


    
    @ApiOperation("批量修改原有菜单，本接口其他地方没有用到，完毕删除")
    @RequestMapping(value = "/menu_update", method = RequestMethod.GET)
    public List<WxMenu> testSendRed() throws Exception{
    	List<WxMenu> menus = systemService.findAllMenu();
     	for (WxMenu menu : menus) {
			if(null != menu.getUrl()){
				String newUrl  = menu.getUrl().substring(7);
				newUrl="https://app."+newUrl;
				menu.setUrl(newUrl);
			}
		}
     	return systemService.saveMenus(menus);
    }

}
