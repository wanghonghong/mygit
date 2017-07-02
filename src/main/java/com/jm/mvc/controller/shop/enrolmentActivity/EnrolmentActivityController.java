package com.jm.mvc.controller.shop.enrolmentActivity;

import com.jm.business.service.shop.enrolmentActivity.EnrolmentActivityService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.enrolmentActivity.*;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>报名活动管理</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 14:45
 */
@RestController
@RequestMapping(value = "/enrolmentActivity")
public class EnrolmentActivityController {

    @Autowired
    private EnrolmentActivityService enrolmentActivityService;

    @ApiOperation("新增报名活动配置")
    @RequestMapping(value = "/conf", method = RequestMethod.POST)
    public JmMessage addEnrolmentConf(@ApiParam("新增报名活动配置CO") @RequestBody @Valid EnrolmentConfCo enrolmentConfCo,
                                        @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        enrolmentConfCo.setShopId(shopId);
        return enrolmentActivityService.addEnrolmentConf(enrolmentConfCo);
    }

    @ApiOperation("获取报名活动配置")
    @RequestMapping(value = "/conf/{id}", method = RequestMethod.GET)
    public EnrolmentConfVo getEnrolmentConf(@ApiParam("配置id") @PathVariable("id") Integer id,
                                         @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.getEnrolmentConf(id,shopId);
    }

    @ApiOperation("编辑报名活动配置")
    @RequestMapping(value = "/conf", method = RequestMethod.PUT)
    public JmMessage updateEnrolmentConf(@ApiParam("编辑活动配置UO") @RequestBody @Valid EnrolmentConfUo enrolmentConfUo,
                                    @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.updateEnrolmentConf(enrolmentConfUo,shopId);
    }

    @ApiOperation("删除报名活动配置")
    @RequestMapping(value = "/conf/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteEnrolmentConf(@ApiParam("配置id") @PathVariable("id") Integer id,
                                    @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return enrolmentActivityService.deleteEnrolmentConf(id,user.getShopId());
    }

    @ApiOperation("报名活动配置列表")
    @RequestMapping(value = "/conf/list", method = RequestMethod.POST)
    public PageItem<EnrolmentConfVo> findEnrolmentConfList(@RequestBody @Valid EnrolmentConfQo enrolmentConfQo,
                                                              @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("找不到店铺!!");
        }
        return enrolmentActivityService.findEnrolmentConfList(enrolmentConfQo,shopId);
    }

    @ApiOperation("新增报名活动")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addEnrolmentActivity(@ApiParam("新增报名活动") @RequestBody @Valid EnrolmentActivityCo enrolmentActivityCo,
                                      @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.addEnrolmentActivity(enrolmentActivityCo,shopId);
    }

    @ApiOperation("删除报名活动")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JmMessage delEnrolmentActivity(@ApiParam("报名活动id") @PathVariable("id") Integer id,
                                      @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.delEnrolmentActivity(id,shopId);
    }

    @ApiOperation("报名活动列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public PageItem<EnrolmentActivityVo> findEnrolmentActivityList(@RequestBody @Valid EnrolmentActivityQo enrolmentActivityQo,
                                                              @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("找不到店铺!!");
        }
        return enrolmentActivityService.findEnrolmentActivityList(enrolmentActivityQo,shopId);
    }

    @ApiOperation("获取报名活动信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EnrolmentActivityVo getEnrolmentActivity(@ApiParam("报名活动id") @PathVariable("id") Integer id,
                                                                  @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.getEnrolmentActivity(id,shopId);
    }

    @ApiOperation("编辑报名活动信息")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateEnrolmentActivity(@ApiParam("编辑报名活动UO") @RequestBody @Valid EnrolmentActivityUo enrolmentActivityUo,
                                                                  @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        return enrolmentActivityService.updateEnrolmentActivity(enrolmentActivityUo,shopId);
    }


    @ApiOperation("报名活动人员列表")
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public PageItem<EnrolmentUserVo> findEnrolmentUserList(@RequestBody @Valid EnrolmentUserQo enrolmentUserQo,
                                                              @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        return enrolmentActivityService.findEnrolmentUserList(enrolmentUserQo);
    }

}
