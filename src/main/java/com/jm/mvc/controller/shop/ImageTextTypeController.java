package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.imageText.ImageTextService;
import com.jm.business.service.shop.imageText.ImageTextTypeService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.imageText.*;
import com.jm.repository.po.shop.imageText.ImageTextType;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.imageText.ImageTextTypeConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>官方图文分类</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 10:22
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/image_text_type")
public class ImageTextTypeController {
    @Autowired
    ImageTextTypeService imageTextTypeService;
    @Autowired
    ImageTextService imageTextService;
    @Autowired
    ShopService shopService;
    @ApiOperation("新增图文分类")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addImageTextType(@ApiParam(hidden=true) HttpServletRequest request,
             @ApiParam(hidden=true)HttpServletResponse response,
             @RequestBody ImageTextTypeCo imageTextTypeCo){

        ImageTextType imageTextType = ImageTextTypeConverter.toImageTextType(imageTextTypeCo);
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        imageTextTypeService.saveImgTextType(user,imageTextType);
        return new JmMessage(0,"分类新增成功!");
    }
    @ApiOperation("是否可删除图文分类")
    @RequestMapping(value = "/checkIsDel/{id}", method = RequestMethod.GET)
    public JmMessage checkIsDel(@ApiParam("图文分类id") @PathVariable("id") Integer id,
                                      @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response){
        Integer count = imageTextService.getCountByTypeId(id);
        if(0<count){
            // 该分类下有具体图文，是否确认删除？ N
            return new JmMessage(1,"该分类下有具体图文，是否确认删除？");
        }
        return new JmMessage(0,"是否确认删除？");
    }
    @ApiOperation("删除图文分类")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delImageTextType(@ApiParam("图文分类id") @PathVariable("id") Integer id,
                                      @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response){
        imageTextService.updateImageTextByTypeId(id);
        imageTextTypeService.delete(id);

    }

    @ApiOperation("获取图文分类")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ImageTextTypeRo getImageTextType(@ApiParam("图文分类id") @PathVariable("id") Integer id,
                                            @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response){
        ImageTextType imageTextType = imageTextTypeService.findImageTextTypeById(id);
        ImageTextTypeRo imageTextTypeRo = new ImageTextTypeRo();
        String imageUrl = Toolkit.parseObjForStr(imageTextType.getImageUrl());
        if(!"".equals(imageUrl)){
            String noImgUrl = Constant.STATIC_URL+"/css/pc/img/no_picture.png";
            if(!noImgUrl.equals(imageUrl)){
                imageTextType.setImageUrl(ImgUtil.appendUrl(imageUrl));
            }
        }
        BeanUtils.copyProperties(imageTextType,imageTextTypeRo);
        return imageTextTypeRo;
    }

    @ApiOperation("编辑图文分类")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateImageTextType(@RequestBody @Valid ImageTextTypeUo imageTextTypeUo,
                                     @ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response){

        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return imageTextTypeService.updateImageTextType(imageTextTypeUo);
    }


    @ApiOperation("查询图文分类列表")
    @RequestMapping(value = "/type_id/{typeId}", method = RequestMethod.GET)
    public ImageTextTypeRos getImageTextList(@ApiParam(hidden=true) HttpServletRequest request,
                                             @ApiParam(hidden=true)HttpServletResponse response, @PathVariable("typeId") Integer typeId){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if(null != user){shopId = Toolkit.parseObjForInt(user.getShopId());}
        return imageTextTypeService.getImageTextList(shopId,typeId);
    }

    @ApiOperation("查询图文分类分页列表")
    @RequestMapping(value = "/list/type_id/{typeId}", method = RequestMethod.POST)
    public ImageTextTypeRos getImageTextList(@ApiParam(hidden=true) HttpServletRequest request,
                                             @ApiParam(hidden=true)HttpServletResponse response,
                                             @RequestBody ImageTextTypeQo imageTextTypeQo,
                                             @PathVariable("typeId") Integer typeId){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if(null != user){shopId = Toolkit.parseObjForInt(user.getShopId());}
        return imageTextTypeService.getImageTextPage(shopId,typeId,imageTextTypeQo);

    }
    @ApiOperation("图文分类列表显示板式：（1：仿微信 2：画报风）")
    @RequestMapping(value = "saveShowFormat/{showFormat}/type_id/{typeId}", method = RequestMethod.PUT)
    public  JmMessage saveImageTextShowFormat(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true)HttpServletResponse response,
                                         @ApiParam("列表显示板式")   @PathVariable("showFormat") Integer showFormat,
                                              @PathVariable("typeId") Integer typeId){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if(null != user){shopId = Toolkit.parseObjForInt(user.getShopId());}
        imageTextTypeService.saveShowFormat(showFormat,shopId,typeId);
        return new JmMessage(0,"成功!");
    }
}
