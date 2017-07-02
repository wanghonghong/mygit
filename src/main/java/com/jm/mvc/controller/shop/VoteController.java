package com.jm.mvc.controller.shop;


import com.jm.business.service.shop.VoteService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.vote.*;
import com.jm.repository.po.shop.vote.VoteItem;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * zx
 */
@Api
@RestController
@RequestMapping(value = "/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;
    @ApiOperation("投票选项查询")
    @RequestMapping(value = "/items", method = RequestMethod.POST)
    public PageItem<VoteItemVo> getVoteItems(@ApiParam(hidden = true) HttpServletRequest request,
                                             @RequestBody @Valid VoteItemQo voteItemQo) throws IllegalAccessException, IOException, InstantiationException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopid=user.getShopId();
        voteItemQo.setShopId(shopid);
        voteItemQo.setStatus(0);
        PageItem<VoteItemVo> pageItem=voteService.getVoteItems(voteItemQo);
        return pageItem ;
    }

    @ApiOperation("查看一个投票主题的投票状态")
    @RequestMapping(value = "/rels", method = RequestMethod.POST)
    public PageItem<VoteRelVo> getVoteRels(@ApiParam(hidden = true) HttpServletRequest request,
                                             @ApiParam("获取一个主题id") @RequestBody @Valid VoteRelQo voteRelQo) throws IllegalAccessException, IOException, InstantiationException {
//        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
//        Integer shopid=user.getShopId();
//        voteItemQo.setShopId(shopid);
//        voteItemQo.setStatus(0);
        return voteService.getVoteRels(voteRelQo);
    }




    @ApiOperation("获取一个投票选项")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public  VoteItemVo getVoteItem(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("获取一个投票选项") @PathVariable("id") Integer id) throws IllegalAccessException, IOException, InstantiationException {
        VoteItemVo voteItemVo=voteService.getVoteItem(id);
       return  voteItemVo;
    }

    @ApiOperation("创建投票选项")
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public JmMessage saveVoteItem(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增投票选项") @RequestBody @Valid VoteItemCo voteItemCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        voteItemCo.setShopId(shopId);
        voteItemCo.setCreateTime(new Date());
       voteService.saveVoteItem(voteItemCo);
        return new JmMessage(0, "新增成功");
    }
    @ApiOperation("修改投票选项")
    @RequestMapping(value = "/item", method = RequestMethod.PUT)
    public JmMessage  updateVoteItem(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改投票选项") @RequestBody @Valid VoteItemUo voteItemUo) throws IllegalAccessException, IOException, InstantiationException {
        VoteItem voteItem=new VoteItem();
        voteItem.setId(voteItemUo.getId());
        voteItem.setStatus(2);
        if(voteService.allowOptItem(voteItem)){
            return new JmMessage(0, "该对象有被投票活动引用不能修改");
        }else{
            voteService.updateVoteItem(voteItemUo);
            return new JmMessage(0, "修改成功");
        }
    }

    @ApiOperation("删除投票选项")
    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public JmMessage  deletlVoteItem(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("投票选项Id") @PathVariable("id") Integer id) throws IllegalAccessException, IOException, InstantiationException {
        VoteItem voteItem=new VoteItem();
        voteItem.setId(id);
        VoteItemUo voteItemUo=new VoteItemUo();
        voteItemUo.setId(id);
        if(voteService.allowOptItem(voteItem)){
            return new JmMessage(0, "该对象被投票活动引用不能删除");
        }else {
            voteService.delVoteItem(voteItemUo);
            return new JmMessage(0, "删除成功");
        }
    }

    @ApiOperation("投票选项查询")
    @RequestMapping(value = "/themes", method = RequestMethod.POST)
    public PageItem<VoteThemeVo> getVoteThemes(@ApiParam(hidden = true) HttpServletRequest request,
                                               @RequestBody @Valid VoteThemeQo voteThemeQo) throws IllegalAccessException, IOException, InstantiationException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopid=user.getShopId();
        voteThemeQo.setShopId(shopid);
        PageItem<VoteThemeVo> pageItem=voteService.getVoteThemes(voteThemeQo);
        return pageItem ;
    }


    @ApiOperation("获取一个投票主题")
    @RequestMapping(value = "/theme/{id}", method = RequestMethod.GET)
    public  VoteThemeVo getVoteTheme(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("获取一个投票主题") @PathVariable("id") Integer id) throws IllegalAccessException, IOException, InstantiationException {
        VoteThemeVo voteThemeVo=voteService.getVoteTheme(id);
        List<VoteRelVo>  rels=  voteService.getVoteRels(id);
        voteThemeVo.setVoteRelVos(rels);
        return  voteThemeVo;
    }
    @ApiOperation("新增投票主题")
    @RequestMapping(value = "/theme", method = RequestMethod.POST)
    public JmMessage  saveVoteTheme(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增投票主题") @RequestBody @Valid VoteThemeCo voteThemeCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        voteThemeCo.setShopId(shopId);
        voteThemeCo.setCreateTime(new Date());
        voteThemeCo.setStatus(1);
        voteService.saveVoteTheme(voteThemeCo);
        return new JmMessage(0, "新增成功");
    }
    @ApiOperation("修改投票主题")
    @RequestMapping(value = "/theme", method = RequestMethod.PUT)
    public JmMessage  updateVoteTheme(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改投票主题") @RequestBody @Valid VoteThemeUo voteThemeUo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        voteThemeUo.setShopId(shopId);
        voteService.updateVoteTheme(voteThemeUo);
        return new JmMessage(0, "修改成功");
    }

    @ApiOperation("删除投票选项")
    @RequestMapping(value = "/theme/{id}", method = RequestMethod.DELETE)
    public JmMessage  deletlVoteTheme(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("投票主题Id") @PathVariable("id") Integer id) {
        VoteThemeUo voteThemeUo=new VoteThemeUo();
        voteThemeUo.setId(id);
        voteService.delVoteTheme(voteThemeUo);
        return new JmMessage(0, "删除成功");
    }

}
