package com.jm.mvc.controller.zb;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.zb.ReviewService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.erp.ErpShopQo;
import com.jm.mvc.vo.erp.ErpShopUo;
import com.jm.mvc.vo.erp.ErpUserShopRo;
import com.jm.mvc.vo.join.DispatchJoinVo;
import com.jm.mvc.vo.join.DispatchVo;
import com.jm.mvc.vo.join.JoinVo;
import com.jm.mvc.vo.zb.dispatch.*;
import com.jm.mvc.vo.zb.join.CheckUo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.zb.system.ZbDispatchHistory;
import com.jm.repository.po.zb.system.ZbJoinType;
import com.jm.staticcode.constant.ZbConstant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by ME on 2016/9/20.
 */
@RestController
@RequestMapping(value = "/zb")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ShopService shopService;

    @ApiOperation("获取商家系统平台加盟列表")
    @RequestMapping(value = "join/list", method = RequestMethod.POST)
    public PageItem getJointList(@RequestBody @Valid ZbJoinVo zbJoinVo) throws Exception {
        PageItem joins = reviewService.getJoinDtos(zbJoinVo);
        return joins;
    }

    @ApiOperation("获取商家系统平台加盟数据")
    @RequestMapping(value = "join", method = RequestMethod.POST)
    public JoinVo getJoinDto(@RequestBody @Valid ZbJoinVo zbJoinVo) throws IOException {
        JoinVo zbJoinDto = reviewService.getJoinDto(zbJoinVo);
        return zbJoinDto;
    }

    @ApiOperation("新增审核建议到商家系统")
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public JmMessage saveReview(@RequestBody @Valid CheckUo checkUo,
                                @ApiParam(hidden=true) HttpServletRequest request) throws IOException {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(ZbConstant.SESSION_USER);
        checkUo.setCheckerId(user.getUserId());
        reviewService.saveCheck(checkUo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("删除审核加盟信息")
    @RequestMapping(value = "delete_join", method = RequestMethod.POST)
    public JmMessage deleteReview(@RequestBody @Valid ZbJoinVo zbJoinVo) throws IOException {
        JmMessage JmMessage = reviewService.deleteJoin(zbJoinVo);
        return JmMessage;
    }

    @ApiOperation("获取商家系统服务商角色信息")
    @RequestMapping(value = "service/roles", method = RequestMethod.GET)
    public List<Role> queryRoles() throws IOException {
        List<Role> list = reviewService.queryRoles();
        return list;
    }

    @ApiOperation("总部派单新增")
    @RequestMapping(value = "dispatch", method = RequestMethod.POST)
    public JmMessage saveDispatch(@ApiParam("总部派单Co") @RequestBody @Valid DispatchCo dispatchCo,
                                    @ApiParam(hidden=true) HttpServletRequest request){
        reviewService.saveDispatch(dispatchCo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("总部派单分页查询")
    @RequestMapping(value = "dispatch/list", method = RequestMethod.POST)
    public PageItem<ZbDispatchVo> getDispatchList(@RequestBody @Valid DispatchQo dispatchQo) throws Exception {
        return  reviewService.getDispatchList(dispatchQo);
    }

    @ApiOperation("获取总部派单商家系统平台加盟列表")
    @RequestMapping(value = "dispatch/joins", method = RequestMethod.POST)
    public PageItem<DispatchJoinVo> getJoins(@RequestBody @Valid DispatchJoinQo dispatchJoinQo) throws Exception {
        PageItem<DispatchJoinVo> joinDtos = reviewService.getJoins(dispatchJoinQo);
        return joinDtos;
    }

    @ApiOperation("获取商家系统角色和派单历史等数据")
    @RequestMapping(value = "dispatch/roles", method = RequestMethod.POST)
    public DispatchVo queryDispatchVo(@RequestBody @Valid Integer dispatchId) throws IOException {
        DispatchVo dispatchVo = reviewService.queryDispatchVo();
        List<ZbDispatchHistory> zbDispatchHistoryList = reviewService.getDispatchHistoryList(dispatchId);
        dispatchVo.setZbDispatchHistoryList(zbDispatchHistoryList);
        return dispatchVo;
    }

    @ApiOperation("总部派单历史新增")
    @RequestMapping(value = "dispatch_history", method = RequestMethod.POST)
    public JmMessage saveDispatchHistory(@ApiParam("总部派单历史Co") @RequestBody @Valid DispatchHistoryCo dispatchHistoryCo,
                                           @ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(ZbConstant.SESSION_USER);
        dispatchHistoryCo.setCheckId(user.getUserId());
        reviewService.saveDispatchHistory(dispatchHistoryCo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("获取派单历史数据列表")
    @RequestMapping(value = "dispatch_history/list", method = RequestMethod.POST)
    public List<ZbDispatchHistory> getDispatchHistory(@RequestBody @Valid DispatchHistoryQo dispatchHistoryQo)  {
        return reviewService.getDispatchHistoryList(dispatchHistoryQo.getDispatchId());
    }

    @ApiOperation("更新派单数据")
    @RequestMapping(value = "dispatch", method = RequestMethod.PUT)
    public JmMessage updateDispatch(@RequestBody @Valid DispatchHistoryQo dispatchHistoryQo)  {
        reviewService.updateDispatch(dispatchHistoryQo.getDispatchId());
        return new JmMessage(0,"保存成功");
    }
    @ApiOperation("总部系统 店铺审核列表")
    @RequestMapping(value = "shop_list", method = RequestMethod.POST)
    public PageItem<ErpUserShopRo> queryShopList(@RequestBody @Valid ErpShopQo qo) throws IOException {
        PageItem<ErpUserShopRo> pageItem =  shopService.auditShops(qo);
        return pageItem;
    }
    @ApiOperation("店铺关停和开启操作")
    @RequestMapping(value = "shop_set", method = RequestMethod.POST)
    public JmMessage setShopStatus(@RequestBody @Valid ErpShopUo uo) throws IOException {
        shopService.updateShopStatus(uo);
        return new JmMessage(0,"修改成功");
    }
    @ApiOperation("业审中心公告权限查看")
    @RequestMapping(value = "join/{id}", method = RequestMethod.GET)
    public List<ZbJoinType> geteJoins(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  reviewService.queryJoins(id);
    }


}
