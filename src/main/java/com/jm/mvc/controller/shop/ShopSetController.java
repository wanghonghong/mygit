package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.ShopSettingService;
import com.jm.business.service.shop.shopSet.UserCenterCustomService;
import com.jm.business.service.shop.shopSet.UserCenterVersionService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.shopSet.*;
import com.jm.repository.jpa.shop.shopSetting.UserCenterConfigRepository;
import com.jm.repository.jpa.shop.shopSetting.UserCenterFunsRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import com.jm.repository.po.shop.shopSet.UserCenterVersion;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.ShopConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>店铺设置</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/7
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/shopsetting")
public class ShopSetController {
	@Autowired
    private ShopSettingService shopSettingService;

    @Autowired
    private ShopService shopService;
    @Autowired
    private UserCenterVersionService userCenterVersionService;
    @Autowired
    private UserCenterCustomService userCenterCustomService;

    @ApiOperation("获取当前模板")
    @RequestMapping(value = "/cur_template", method = RequestMethod.GET)
    public ShopVo getTemplate(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        ShopVo shopVo = ShopConverter.toShopVo(shopService.findShopById(user.getShopId()));
        return shopVo;
    }

    @ApiOperation("商城搭建模板设置")
    @RequestMapping(value = "/template", method = RequestMethod.PUT)
    public JmMessage setTemplate(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String id = request.getParameter("tempId");
        int tempId = Integer.valueOf(id);
        shopService.setTemplate(tempId,user.getShopId());
        return new JmMessage(0,"设置成功");
    }

    @ApiOperation("商城搭建模板设置获取")
    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    public List<ShopTemplateVo> getTemplate(){
        List<ShopTemplateVo> shopTempList = shopSettingService.getShopTempList();
        return shopTempList;
    }

    @ApiOperation("商城首页搭建获取")
    @RequestMapping(value = "/shopIndex", method = RequestMethod.GET)
    public ShopMainVo getShopIndex(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Shop shop = shopService.findShopById(user.getShopId());
        ShopMainVo shopMainVo = shopSettingService.getShopMain(user.getShopId());
        shopMainVo.setShopName(shop.getShopName());
        return shopMainVo;
    }

    @ApiOperation("商城首页搭建设置")
    @RequestMapping(value = "/shopIndex", method = RequestMethod.POST)
    public ShopMainVo creatShopIndex(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody ShopMainCo shopMainCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopSettingService.creatShopIndex(shopMainCo,user.getShopId());
        return shopSettingService.getShopMain(user.getShopId());
    }

    @ApiOperation("商城首页搭建修改")
    @RequestMapping(value = "/shopIndex", method = RequestMethod.PUT)
    public JmMessage updateShopIndex(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody ShopMainUo shopMainUo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopSettingService.updateShopIndex(shopMainUo,user.getShopId());
        return new JmMessage(0,"设置成功");
    }

    @ApiOperation("会员中心配置获取")
    @RequestMapping(value = "/user_center_fun", method = RequestMethod.GET)
    public List<UserCenterFunsVo> getUserCenterFunsList(){
        List<UserCenterFunsVo> userCenterFunsList = shopSettingService.getUserCenterFunsList();
        return userCenterFunsList;
    }

    @ApiOperation("会员中心获取")
    @RequestMapping(value = "/user_center", method = RequestMethod.GET)
    public UserCenterConfigVo getUserCenterConfig(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return shopSettingService.getUserCenterConfig(user.getShopId());
    }

    @ApiOperation("会员中心新增")
    @RequestMapping(value = "/user_center", method = RequestMethod.POST)
    public UserCenterConfigVo creatUserCenterConfig(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody UserCenterConfigCo userCenterConfigCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopSettingService.creatUserCenterConfig(userCenterConfigCo,user.getShopId());
        return shopSettingService.getUserCenterConfig(user.getShopId());
    }

    @ApiOperation("会员中心修改")
    @RequestMapping(value = "/user_center", method = RequestMethod.PUT)
    public JmMessage updateUserCenterConfig(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody UserCenterConfigUo userCenterConfigUo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopSettingService.updateUserCenterConfig(userCenterConfigUo,user.getShopId());
        return new JmMessage(0,"设置成功");
    }

    //**********************  NEW  *********************************
    @ApiOperation("会员中新配置获取")
    @RequestMapping(value = "/getUserCenterFuns", method = RequestMethod.GET)
    public UserCenterConfigRo getUserCenterFuns(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user.getShopId()==null){
            return null;
        }
        return userCenterVersionService.getUserCenterFuns(user.getShopId());
    }

    @ApiOperation("保存会员中心配置")
    @RequestMapping(value = "/saveUserCenterCustom", method = RequestMethod.POST)
    public JmMessage saveUserCenterCustom(@ApiParam(hidden=true) HttpServletRequest request,
                                                     @RequestBody UserCenterCustomCo co){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<UserCenterCustom> customs = userCenterCustomService.saveUserCenterCustom(co,user.getShopId());
        if(customs.size()>0 && customs!=null ){
            return  new JmMessage(0,"保存成功");
        }
        return  new JmMessage(1,"保存失败");
    }

}
