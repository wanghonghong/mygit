package com.jm.mvc.controller.shop.dataAnalysis;

import com.jm.business.service.shop.dataAnalysis.*;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.mvc.vo.wx.menu.WxMenuVo;
import com.jm.repository.jpa.system.WxMenuRepository;
import com.jm.repository.po.shop.WxMenu;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>数据分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/10 9:48
 */
@RestController
@RequestMapping(value = "/data_analysis")
public class DataAnalysisController {
    @Autowired
    private UserAnalysisService userAnalysisService;
    @Autowired
    private TradeAnalysisService tradeAnalysisService;
    @Autowired
    private RpAnalysisService rpAnalysisService;
    @Autowired
    private ProductAnalysisService productAnalysisService;
    @Autowired
    private ManagerAnalysisService managerAnalysisService;
    @Autowired
    private ActivityAnalysisService activityAnalysisService;
    @Autowired
    private MenuAnalysisService menuAnalysisService;
    @Autowired
    private WxMenuRepository wxMenuRepository;

    @ApiOperation("用户分析")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Map findUserData(@ApiParam(hidden=true) HttpServletRequest request,
                             @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Long shopUid = user.getUid();
        String appid = Toolkit.parseObjForStr(user.getAppId());
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return  userAnalysisService.findUserData(shopId,appid,shopUid,paramsQo);
    }

    @ApiOperation("商品分析")
    @RequestMapping(value = "/good", method = RequestMethod.POST)
    public Map findGoodData(@ApiParam(hidden=true) HttpServletRequest request,
                            @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return productAnalysisService.findGoodData(shopId,paramsQo);
    }
    @ApiOperation("商品分析--导出")
    @RequestMapping(value = "/productCsv", method = RequestMethod.GET)
    public void exportProductCsv(@ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        productAnalysisService.exportCsv(request,response,shopId);
    }

    @ApiOperation("交易分析")
    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public Map findTradeData(@ApiParam(hidden=true) HttpServletRequest request,
                            @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        String appid = Toolkit.parseObjForStr(user.getAppId());
        long shopUid = Toolkit.obj2Long(user.getUid());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        Map map = tradeAnalysisService.findTradeData(appid,shopUid,shopId,paramsQo);
        return map ;
    }

    @ApiOperation("收支分析")
    @RequestMapping(value = "/rp", method = RequestMethod.POST)
    public Map findRpData(@ApiParam(hidden=true) HttpServletRequest request,
                            @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        long shopUid = Toolkit.obj2Long(user.getUid());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        Map map = rpAnalysisService.findRpData(shopUid,shopId,paramsQo);
        return map ;
    }
    @ApiOperation("收支分析--导出")
    @RequestMapping(value = "/rpCsv", method = RequestMethod.GET)
    public void exportRpCsv(@ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        long shopUid = Toolkit.obj2Long(user.getUid());
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
       rpAnalysisService.exportCsv(request,response,shopId,shopUid);
    }
    @ApiOperation("菜单分析--导出")
    @RequestMapping(value = "/menuCsv", method = RequestMethod.GET)
    public void exportMenuCsv(@ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = Toolkit.parseObjForStr(user.getAppId());
        if("".equals(appid)){
            throw new Exception("session丢失appid");
        }
       menuAnalysisService.exportCsv(request,response,appid);
    }

    @ApiOperation("经营概况")
    @RequestMapping(value = "/manager", method = RequestMethod.POST)
    public Map findManagerData(@ApiParam(hidden=true) HttpServletRequest request,
                            @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        String appid = Toolkit.parseObjForStr(user.getAppId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        Long shopUid = user.getUid();
        Map map = managerAnalysisService.findManagerData(appid,shopUid,shopId,paramsQo);
        return map ;
    }
    @ApiOperation("活动分析")
    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    public Map findActivityData(@ApiParam(hidden=true) HttpServletRequest request,
                          @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        Map map = activityAnalysisService.findActivityData(shopId,paramsQo);
        return map ;
    }

    @ApiOperation("菜单分析")
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    public Map findMenuData(@ApiParam(hidden=true) HttpServletRequest request,
                          @RequestBody DataParamsQo paramsQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = user.getAppId();
        if("".equals(appid)){
            throw new Exception("session丢失appid");
        }
        Map map = menuAnalysisService.findMenuData(appid,paramsQo);
        return map ;
    }

    @ApiOperation("获取微信菜单")
    @RequestMapping(value = "/menuInfo", method = RequestMethod.GET)
    public List<WxMenuVo> findMenuInfo(@ApiParam(hidden=true) HttpServletRequest request,
                                       @ApiParam(hidden=true) HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = user.getAppId();
        if("".equals(appid)){
            throw new Exception("session丢失appid");
        }
        List<WxMenu> list = wxMenuRepository.findMenuByAppid(appid);
        List<WxMenuVo> wxMenuList = new ArrayList<>();
        for (WxMenu wm:list) {
            WxMenuVo   wxMenuVo = new WxMenuVo();
            BeanUtils.copyProperties(wm,wxMenuVo);
            wxMenuList.add(wxMenuVo);
        }
        return wxMenuList;
    }

}
