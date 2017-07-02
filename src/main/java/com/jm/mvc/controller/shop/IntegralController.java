package com.jm.mvc.controller.shop;

import com.jm.business.domain.shop.AwardIntegralDo;
import com.jm.business.service.shop.IntegralService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.WxUserAccountVo;
import com.jm.mvc.vo.shop.integral.*;
import com.jm.mvc.vo.shop.recharge.RechargeOrderCo;
import com.jm.mvc.vo.shop.recharge.RechargeOrderVo;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.shop.integral.IntegralProduct;
import com.jm.repository.po.shop.integral.IntegralRecord;
import com.jm.repository.po.shop.integral.IntegralSet;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <p>积分控制器</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016.09.30
 */
@Api
@RestController
@RequestMapping(value = "/integral")
public class IntegralController {

    @Autowired
    private IntegralService integralService;

    @ApiOperation("积分签到模板获取")
    @RequestMapping(value = "/sign/tpl", method = RequestMethod.GET)
    public ModelAndView getSignTpl(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        List<IntegralRecord> integralRecords =integralService.getLoginAward(user.getUserId());
        IntegralSet integralSet = integralService.getCacheIntegralSet(user.getShopId());
        ModelAndView view = new ModelAndView();
        if (integralRecords ==null ||integralRecords.size()==0){
            view.getModelMap().put("isSign",0);
        }else {
            view.getModelMap().put("isSign",1);
        }
        if (integralSet!=null){
            view.getModelMap().put("integralSet",integralSet);
        }
        view.setViewName("/app/shop/integral/integralSign");
        return view;
    }

    @ApiOperation("签到获取积分")
    @RequestMapping(value = "/sign", method = RequestMethod.GET)
    public JmMessage integralSign(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        AwardIntegralDo awardIntegralDo = new AwardIntegralDo();
        awardIntegralDo.setUserId(user.getUserId());
        awardIntegralDo.setShopId(user.getShopId());
        awardIntegralDo.setIntegralType(1);
        return integralService.awardIntegral(awardIntegralDo);
    }

    @ApiOperation("获取用户积分")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public IntegralInfoVo getIntegralInfo(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        return integralService.getIntegralInfo(user.getUserId(),user.getShopId());
    }

    @ApiOperation("查询积分奖励列表")
    @RequestMapping(value = "/app/award", method = RequestMethod.POST)
    public PageItem<IntegralRecordVo> queryAppIntegralAward(@ApiParam(hidden=true) HttpSession httpSession,
                                                         @ApiParam("积分设置") @RequestBody @Valid IntegralRecordQo integralRecordQo) throws IOException {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        if (integralRecordQo==null){
            integralRecordQo = new IntegralRecordQo();
        }
        integralRecordQo.setUserId(user.getUserId());
        integralRecordQo.setShopId(user.getShopId());
        return integralService.queryAppIntegralAward(integralRecordQo);
    }

    @ApiOperation("获取用户积分")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public WxUserAccountVo updateIntegralSet(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        JmUserSession user = (JmUserSession) httpSession.getAttribute(Constant.SESSION_USER);
        return integralService.getUserIntegral(user.getUserId());
    }

    @ApiOperation("保存积分设置")
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public JmMessage saveIntegralSet(@ApiParam(hidden=true) HttpServletRequest request,
                                     @ApiParam("积分设置") @RequestBody @Valid IntegralSetCo integralSetCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        integralSetCo.setShopId(user.getShopId());
        IntegralSet integralSet = integralService.saveIntegralSet(integralSetCo);
        return new JmMessage(0,"保存成功",integralSet.getId());
    }

    @ApiOperation("修改积分设置")
    @RequestMapping(value = "/set", method = RequestMethod.PUT)
    public IntegralSet updateIntegralSet(@ApiParam(hidden=true) HttpSession httpSession,
                                         @ApiParam("积分设置") @RequestBody @Valid IntegralSetUo integralSetUo) throws Exception {
        JmUserSession user = (JmUserSession) httpSession.getAttribute(Constant.SESSION_USER);
        integralSetUo.setShopId(user.getShopId());
        return integralService.updateIntegralSet(integralSetUo);
    }

    @ApiOperation("获取积分设置")
    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public JmMessage getIntegralSet(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        JmUserSession user = (JmUserSession) httpSession.getAttribute(Constant.SESSION_USER);
        IntegralSetVo integralSetVo = integralService.getIntegralSet(user.getShopId());
        return new JmMessage(0,"获取数据成功",integralSetVo);
    }

    @ApiOperation("查询积分奖励列表")
    @RequestMapping(value = "/award", method = RequestMethod.POST)
    public PageItem<IntegralRecordVo> queryIntegralAward(@ApiParam(hidden=true) HttpSession httpSession,
                                                         @ApiParam("积分设置") @RequestBody @Valid IntegralRecordQo integralRecordQo) throws IOException {
        JmUserSession user = (JmUserSession) httpSession.getAttribute(Constant.SESSION_USER);
        integralRecordQo.setShopId(user.getShopId());
        return integralService.queryIntegralAward(integralRecordQo);
    }

    @ApiOperation("查询积分奖励列表")
    @RequestMapping(value = "/award/tpl", method = RequestMethod.GET)
    public ModelAndView queryIntegralAwardTpl(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/app/shop/integral/integralList");
        return view;
    }

    @ApiOperation("商品积分换购设置")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public IntegralProduct saveIntegralProduct(@ApiParam(hidden=true) HttpServletRequest request,
                                               @ApiParam("积分设置") @RequestBody @Valid IntegralProductCo integralProductCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        integralProductCo.setShopId(user.getShopId());
        return integralService.saveIntegralProduct(integralProductCo);
    }

    @ApiOperation("商品积分换购设置")
    @RequestMapping(value = "/product/list", method = RequestMethod.POST)
    public List<IntegralProduct> saveIntegralProduct(@ApiParam(hidden=true) HttpServletRequest request,
                                                     @ApiParam("积分设置") @RequestBody @Valid List<IntegralProductCo> integralProductCos){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return integralService.saveIntegralProducts(integralProductCos,user.getShopId());
    }

    @ApiOperation("商品积分换购设置")
    @RequestMapping(value = "/product", method = RequestMethod.PUT)
    public JmMessage updateIntegralProduct(@ApiParam(hidden=true) HttpServletRequest request,
                                           @ApiParam("积分设置") @RequestBody @Valid IntegralProductUo integralProductUo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        integralProductUo.setShopId(user.getShopId());
        IntegralProduct integralProduct  = integralService.updateIntegralProduct(integralProductUo);
        return new JmMessage(0,"商品积分换购设置成功",integralProduct);
    }

    @ApiOperation("商品积分换购列表查询")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public PageItem<IntegralProductVo> queryIntegralProduct(@ApiParam(hidden=true) HttpServletRequest request,
                                                            @ApiParam("商品积分查询条件") @RequestBody IntegralProductQo integralProductQo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        integralProductQo.setShopId(user.getShopId());
        return integralService.queryIntegralProduct(integralProductQo);
    }

    @ApiOperation("签到获取积分")
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public JmMessage delIntegralProduct(@ApiParam(hidden=true) HttpSession httpSession,
                                        @ApiParam("商品积分换购标识") @PathVariable("id") int id) {
        JmUserSession user = (JmUserSession) httpSession.getAttribute(Constant.SESSION_USER);
        return integralService.delete(id,user.getShopId());
    }


    @ApiOperation("积分充值模版")
    @RequestMapping(value = "/recharge/tpl", method = RequestMethod.GET)
    public ModelAndView getRechargeTpl(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        IntegralSetVo integralSet = integralService.getIntegralSet(user.getShopId());
        ModelAndView view = new ModelAndView();
        if (integralSet!=null){
            view.getModelMap().put("integralSet",integralSet);
        }
        view.setViewName("/app/shop/integral/recharge");
        return view;
    }

    @ApiOperation("积分充值")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public JmMessage integralRecharge(@ApiParam(hidden=true) HttpSession httpSession,
                                         @ApiParam("积分设置") @RequestBody @Valid RechargeOrderCo rechargeOrderCo) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        rechargeOrderCo.setShopId(user.getShopId());
        rechargeOrderCo.setUserId(user.getUserId());
        RechargeOrder orderRecharge = integralService.integralRecharge(rechargeOrderCo);
        return new JmMessage(0,"积分充值置成功", orderRecharge);
    }
    @ApiOperation("积分充值记录模板")
    @RequestMapping(value = "/rechargelist/tpl", method = RequestMethod.GET)
    public ModelAndView getRechargeListTpl(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        IntegralSetVo integralSet = integralService.getIntegralSet(user.getShopId());
        ModelAndView view = new ModelAndView();
        if (integralSet!=null){
            view.getModelMap().put("integralSet",integralSet);
        }
        view.setViewName("/app/shop/integral/rechargeList");
        return view;
    }
    @ApiOperation("积分充值记录获取")
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public List<RechargeOrderVo> getIntegralRecharge(@ApiParam(hidden=true) HttpSession httpSession) throws Exception {
        WxUserSession user = (WxUserSession) httpSession.getAttribute(Constant.SESSION_WX_USER);
        return integralService.getIntegralRecharge(user.getUserId());
    }

}
