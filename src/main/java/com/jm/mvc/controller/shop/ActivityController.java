package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.ActivityService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.activity.*;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.staticcode.constant.Constant;

import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

/**
 * <p>活动管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @ApiOperation("新增活动")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addActivity(@ApiParam("新增活动VO") @RequestBody @Valid ActivityCo activityCo,
                                        @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        activityCo.setShopId(user.getShopId());
        activityCo.setAppId(user.getAppId());
        return activityService.addActivity(activityCo,user.getUserId());
    }

    @ApiOperation("编辑活动")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateActivity(@ApiParam("编辑活动VO") @RequestBody @Valid ActivityUo activityUo,
                                    @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        activityUo.setShopId(user.getShopId());
        return activityService.updateActivity(activityUo);
    }

    @ApiOperation("查询活动详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ActivityRo findActivityById(@ApiParam("活动id") @PathVariable("id") Integer id,
                                       @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return activityService.getActivity(id,user.getShopId(),user.getAppId());
    }

    @ApiOperation("删除活动")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteActivity(@ApiParam("活动id") @PathVariable("id") Integer id,
                                    @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return activityService.deleteActivity(id,user.getShopId());
    }
    
    @ApiOperation("停止活动")
    @RequestMapping(value = "/stop/{id}", method = RequestMethod.DELETE)
    public JmMessage stopActivity(@ApiParam("活动id") @PathVariable("id") Integer id,
            @ApiParam(hidden=true) HttpServletRequest request) throws Exception{
    	 JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
    	return activityService.stopActivity(id, user.getAppId());
    }

    @ApiOperation("活动列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public PageItem<Activity> findActivityList(@RequestBody @Valid ActivityQo activityQo,
                                                  @ApiParam(hidden=true) HttpServletRequest request) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(null!=user.getShopId()){//进入聚客红包时候会把shopid置空，所以查询聚客红包的时候shop是空的
        	activityQo.setShopId(user.getShopId());
        }
        activityQo.setAppid(user.getAppId());
        return activityService.queryActivity(activityQo);

    }

    @ApiOperation("暂停/开启活动")
    @RequestMapping(value = "/pause/{id}", method = RequestMethod.GET)
    public JmMessage pauseActivity(@ApiParam("活动id") @PathVariable("id") Integer id,
                                   @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return activityService.pause(id,user.getShopId(),user.getAppId());
    }


     @ApiOperation("关注推送新增")
     @RequestMapping(value = "/push", method = RequestMethod.POST)
     public JmMessage addPush(@ApiParam("新增关注推送VO") @RequestBody @Valid SubscribePushCo subscribePushCo,
                              @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
         JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
         subscribePushCo.setShopId(user.getShopId());
         activityService.addPush(subscribePushCo);
         return new JmMessage(0,"新增成功!!");
     }

     @ApiOperation("关注推送修改")
     @RequestMapping(value = "/push", method = RequestMethod.PUT)
     public JmMessage updatePush(@ApiParam("新增活动VO") @RequestBody @Valid SubscribePushCo subscribePushCo,
                              @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
         JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
         subscribePushCo.setShopId(user.getShopId());
         activityService.updatePush(subscribePushCo);
         return new JmMessage(0,"修改成功!!");
     }

     @ApiOperation("关注推送获取")
     @RequestMapping(value = "/push", method = RequestMethod.GET)
     public SubscribePushCo getPush(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
         JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
         return activityService.getPush(user.getShopId());
     }

    @ApiOperation("根据活动条件获取用户数量")
    @RequestMapping(value = "/user_count", method = RequestMethod.POST)
    public int userCount(@RequestBody @Valid ActivityConditionQo qo,@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = Toolkit.parseObjForStr(user.getAppId());
        if("".equals(appid)){
            throw new Exception("session丢失appid");
        }
        return activityService.userCount(qo,appid);
    }

    @ApiOperation("现金红包发放统计")
    @RequestMapping(value = "/money_count/{id}", method = RequestMethod.GET)
    public ActivityEo moneyCount(@ApiParam("活动id") @PathVariable("id") Integer id,
                                       @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        return activityService.getActivityCount(id);
    }

    @ApiOperation("现金红包发放记录列表")
    @RequestMapping(value = "/money_list", method = RequestMethod.POST)
    public PageItem<ActivityUserEo> findActivityUserList(@RequestBody @Valid ActivityUserEo activityUserEo,
                                               @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        Integer flag = Toolkit.parseObjForInt(request.getParameter("flag"));
        return activityService.queryActivityUserList(activityUserEo,flag);
    }

    /**
     * 报表使用查询活动名字
     * @param type
     * @param request
     * @return
     * @throws Exception
     * @author cj 2017-02-08
     */
    @ApiOperation("活动列表")
    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public List<Activity> findActivityList(@PathVariable("type") Integer type,
                                           @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return activityService.queryActivityName(shopId,type);
    }
}
