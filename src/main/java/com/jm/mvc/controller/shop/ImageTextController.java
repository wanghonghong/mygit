package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.*;
import com.jm.business.service.shop.imageText.ImageTextService;
import com.jm.business.service.shop.imageText.ImageTextTipService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.imageText.*;
import com.jm.repository.jpa.shop.DzRepository;
import com.jm.repository.jpa.shop.imageText.ImageTextTemplateRepository;
import com.jm.repository.po.shop.imageText.DzUser;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ImageTextTemplate;
import com.jm.repository.po.shop.shopSet.ShopTipSet;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>官方图文</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 10:22
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/image_text")
public class ImageTextController {
    @Autowired
    private ImageTextService imageTextService;
    @Autowired
    ShopService shopService;
    @Autowired
    DzRepository dzRepository;
    @Autowired
    private ImageTextTipService imageTextTipService;
    @Autowired
    private ShopTipSetService shopTipSetService;
    @Autowired
    private ImageTextTemplateRepository imageTextTemplateRepository;

    @ApiOperation("新增图文")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addImageText(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response,
            @RequestBody ImageTextCo imageTextCo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        imageTextService.saveImageText(user,imageTextCo);
        return new JmMessage(0,"新增成功!!");
    }

    @ApiOperation("获取图文")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ImageTextRo findImageText(@ApiParam("图文id") @PathVariable("id") Integer id,
                                     @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if(user!=null){
            shopId = Toolkit.parseObjForInt(user.getShopId());
        }
        return imageTextService.getImageText(id,shopId);
    }

    @ApiOperation("编辑图文")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateImageText(@RequestBody @Valid ImageTextUo imageTextUo,
                                     @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return  imageTextService.updateImageText(user,imageTextUo);
    }
    @ApiOperation("编辑图文上下架")
    @RequestMapping(value = "/id/{id}/status/{status}", method = RequestMethod.PUT)
    public JmMessage updateImageTextStatus(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response
                                        ,@PathVariable("id") Integer id,@PathVariable("status") Integer status) throws Exception {
        return  imageTextService.updateImageTextStatus(id,status);
    }

    @ApiOperation("删除图文")
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public JmMessage delImageText(@ApiParam("图文id") @PathVariable("ids") String ids,
                                     @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        imageTextService.delImageText(ids,user.getShopId());
        return new JmMessage(0, "ok");
    }

    @ApiOperation("查询图文列表PC")
    @RequestMapping(value = "/findAll/{typeId}", method = RequestMethod.POST)
    public PageItem<ImageTextRos> getImageTextList( @ApiParam(hidden=true) HttpServletRequest request,
                              @ApiParam(hidden=true)HttpServletResponse response,
                              @RequestBody @Valid ImageTextQo imageTextQo,
                              @ApiParam("官方图文分类 1：项目图文 2：乐享图文 3：培训通知") @PathVariable Integer typeId)  throws Exception {
        PageItem<ImageTextRos> pageItem =  imageTextService.findAll(request,imageTextQo,typeId);
        return pageItem;
    }

    @ApiOperation("查询图文列表WAP")
    @RequestMapping(value = "/findall_wap/{typeId}", method = RequestMethod.POST)
    public ImageTextWapRo getImageTextListWap( @ApiParam(hidden=true) HttpServletRequest request,
                              @ApiParam(hidden=true)HttpServletResponse response,
                              @RequestBody @Valid ImageTextQo imageTextQo, @ApiParam("图文分类 1：官方图文 2：乐享图文 3：培训通知 0:未知") @PathVariable("typeId") Integer typeId){
        Integer shopId = 0;
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        if(null != wxUserSession){shopId = Toolkit.parseObjForInt(wxUserSession.getShopId());}
        ImageTextWapRo imageTextWapRo = imageTextService.getWapImageTextList(shopId,imageTextQo,typeId);
        return imageTextWapRo;
    }

    @ApiOperation("WAP端编辑图文点赞人数")
    @RequestMapping(value = "/updateRewardCount", method = RequestMethod.PUT)
    public JmMessage updateImageText(@RequestBody @Valid ImageText imageText,
                                     @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
         Integer id = Toolkit.parseObjForInt(imageText.getId());
         ImageText imageTextOld = imageTextService.findImageTextById(id);
         WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
         String openId = wxUserSession.getOpenid();
         String appId = wxUserSession.getAppid();
         DzUser dzUser = new DzUser();
         dzUser.setOpenId(openId);
         dzUser.setCreateDate(new Date());
         dzUser.setImageTextId(id);
         dzUser.setAppId(appId);
         if(imageTextOld !=null){
             dzRepository.save(dzUser);
             imageTextOld.setReward(Toolkit.parseObjForInt(imageTextOld.getReward())+1);
             imageTextService.save(imageTextOld);
             return new JmMessage(0,"编辑成功");
         }
         return new JmMessage(1,"查找不到该活动，操作失败!!!");
    }

    @ApiOperation("图文点赞收入分页")
    @RequestMapping(value = "/allTip", method = RequestMethod.POST)
    public Map findAllTipData(@ApiParam(hidden=true) HttpServletRequest request,
                              @RequestBody ImageTextTipQo imageTextTipQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return imageTextTipService.findPageImageTextTip(shopId,imageTextTipQo);
    }

    @ApiOperation("针对某个图文点赞数据")
    @RequestMapping(value = "/tip", method = RequestMethod.POST)
    public Map findTipData(@ApiParam(hidden=true) HttpServletRequest request,
                           @RequestBody ImageTextTipQo imageTextTipQo) throws Exception {
        return imageTextTipService.findPageTip(imageTextTipQo);
    }

    @ApiOperation("实时收入店铺图文点赞数据")
    @RequestMapping(value = "/tipList", method = RequestMethod.POST)
    public Map findTipList(@ApiParam(hidden=true) HttpServletRequest request,
                           @RequestBody ImageTextTipQo imageTextTipQo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return imageTextTipService.findPageShopTipList(imageTextTipQo,shopId);
    }


    @ApiOperation("保存打赏配置")
    @RequestMapping(value = "/tipSet", method = RequestMethod.POST)
    public JmMessage saveShopTip(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody ShopTipSet shopTipSet) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        shopTipSet.setShopId(shopId);
        return  shopTipSetService.saveShopTip(shopTipSet,shopId);
    }

    @ApiOperation("pc显示打赏配置")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map showShopTip(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        Map map = new HashMap();
        ShopTipSet shopTipSet = shopTipSetService.findTipConfByShopId(shopId);
        if(shopTipSet!=null){
            shopTipSet.setShopId(null);
        }
        map.put("shopTipSetInfo",shopTipSet);
        return map;
    }
    @ApiOperation("保存H5模板")
    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public JmMessage saveH5template(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody ImageTextTemplateCo h5templateCo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return imageTextService.saveH5template(h5templateCo,shopId);
    }

    @ApiOperation("删除H5主题模板")
    @RequestMapping(value = "/template/{id}", method = RequestMethod.DELETE)
    public JmMessage delImageTextTemplate(@ApiParam("主题模板菜单id") @PathVariable("id") Integer id,
                                  @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return imageTextService.delImageTextTemplate(id,user.getShopId());
    }

    @ApiOperation("获取H5主题模板")
    @RequestMapping(value = "/template/{menuId}", method = RequestMethod.GET)
    public ImageTextTemplateRo getImageTextTemplate(@ApiParam("主题模板菜单id") @PathVariable("menuId") Integer menuId,
                                          @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return imageTextService.getImageTextTemplate(menuId,shopId);
    }

    @ApiOperation("获取主题菜单")
    @RequestMapping(value = "/themeMenu", method = RequestMethod.GET)
    public List<ThemeMenuRo> getImageTextTemplate(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = Toolkit.parseObjForInt(user.getShopId());
        if(0==shopId.intValue()){
            throw new Exception("session丢失shopid");
        }
        return imageTextService.getThemeMenu(shopId);
    }

    @ApiOperation("生成h5图文二维码")
    @RequestMapping(value = "/template_qrcode/{menuId}", method = RequestMethod.GET)
    public JmMessage goodQrcode(@ApiParam(hidden = true) HttpServletRequest request,
                                @ApiParam("主题模板菜单id") @PathVariable("menuId") Integer menuId) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        String url = Constant.APP_DOMAIN + "/image_text/template/" + menuId + "?shopId=" + shopId;
        ImageTextTemplate imageTextTemplate = imageTextTemplateRepository.findImageTextTemplateByMenuId(menuId);
        if(imageTextTemplate.getQrcodeUrl()!=null && !imageTextTemplate.getQrcodeUrl().equals("")){
            return new JmMessage(0, ImgUtil.appendUrl(imageTextTemplate.getQrcodeUrl(), 720));
        }else{
            JmMessage msg = imageTextService.getQrcoce(url);
            if (msg.getCode().equals(0)) {
                imageTextTemplate.setQrcodeUrl(ImgUtil.substringUrl(msg.getMsg()));
                imageTextTemplateRepository.save(imageTextTemplate);
                msg.setMsg(ImgUtil.appendUrl(msg.getMsg(), 720));
            }
            return msg;
        }
    }

    @ApiOperation("跳转自定义h5")
    @RequestMapping(value = "/h5", method = RequestMethod.GET)
    public ModelAndView shopIndex(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response) throws Exception{
        ModelAndView view  = new ModelAndView();
        view.setViewName("/pc/h5poster/h5index");
        return view;
    }

}
