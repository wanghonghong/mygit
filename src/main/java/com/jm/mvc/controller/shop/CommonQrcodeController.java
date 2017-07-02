package com.jm.mvc.controller.shop;

import com.jm.business.service.product.ProductService;
import com.jm.business.service.shop.CommonQrcodeDetailService;
import com.jm.business.service.shop.CommonQrcodeService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.commQrcode.*;
import com.jm.repository.jpa.user.ShopUserRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.repository.po.shop.CommonQrcode;
import com.jm.repository.po.shop.CommonQrcodeDetail;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.BaseUtil;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 
 *<p>通用商品条码</p>
 */
@Api
@RestController
@RequestMapping(value = "/commonQrcode")
public class CommonQrcodeController {

	@Autowired
	private CommonQrcodeService commonQrcodeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommonQrcodeDetailService commonQrcodeDetailService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private ShopUserRepository shopUserRepository;


	@ApiOperation("新增")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage addCommonQrcode(@ApiParam(hidden=true) HttpServletRequest request,
									@RequestBody CommonQrcodeCo co) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String basePath = Constant.APP_DOMAIN;
		co.setShopId(user.getShopId());
        CommonQrcode commonQrcode = commonQrcodeService.save(co);
        commonQrcodeDetailService.saveDetailAndImg(commonQrcode,co,basePath,user.getShopId());
		return new JmMessage(0,"新增成功!!");
	}


    @ApiOperation("查询条码列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public PageItem<CommonQrcodeRo> findAll(@ApiParam(hidden=true) HttpServletRequest request,
                                            @RequestBody @Valid CommonQrcodeQo qo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<CommonQrcodeRo> pageItem =  commonQrcodeService.findAll(qo,user);
        return pageItem;
    }


    @ApiOperation("查询条码明细列表")
    @RequestMapping(value = "/findDetailAll", method = RequestMethod.POST)
    public PageItem<CommonQrcodeDetailRo> findDetailAll(@ApiParam(hidden=true) HttpServletRequest request,
                                            @RequestBody @Valid CommonQrcodeQo qo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<CommonQrcodeDetailRo> pageItem =  commonQrcodeService.findDetailAll(qo,user);
        return pageItem;
    }


    @ApiOperation("获取")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonCheckDownLoadRo find(@ApiParam("条码id") @PathVariable("id") Integer id,
                                      @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        CommonQrcodeDetail commonQrcodeDetail = commonQrcodeDetailService.getCommonQrcodeDetail(id);
        CommonQrcode commonQrcode = commonQrcodeService.getCommonQrcode(commonQrcodeDetail.getCommonQrcodeId());
        Product product =  productService.getProduct(commonQrcode.getProductId());
        CommonCheckDownLoadRo ro = new CommonCheckDownLoadRo();
        ro.setCommonQrcodeId(commonQrcode.getId());
        ro.setName(commonQrcode.getName());
        ro.setDetailNum(commonQrcode.getDetailNum());
        ro.setProductImg(ImgUtil.appendUrl(product.getPicSquare(),720));
        ro.setProductName(product.getName());
        ro.setZipUrl(ImgUtil.appendUrl(commonQrcode.getZipUrl()));
        ro.setCommonQrcodeDetailId(id);
        return ro;
    }

    @ApiOperation("获取")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonCheckDownLoadRo findDetail(@ApiParam("条码id") @PathVariable("id") Integer id,
                                         @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        CommonQrcode commonQrcode = commonQrcodeService.getCommonQrcode(id);
        Product product =  productService.getProduct(commonQrcode.getProductId());
        CommonCheckDownLoadRo ro = new CommonCheckDownLoadRo();
        ro.setCommonQrcodeId(commonQrcode.getId());
        ro.setName(commonQrcode.getName());
        ro.setDetailNum(commonQrcode.getDetailNum());
        ro.setProductImg(ImgUtil.appendUrl(product.getPicSquare(),720));
        ro.setProductName(product.getName());
        ro.setZipUrl(ImgUtil.appendUrl(commonQrcode.getZipUrl()));
        ro.setCount(commonQrcode.getCount());
        return ro;
    }


    @ApiOperation("查询条码使用效果")
    @RequestMapping(value = "/findEffectList/{id}", method = RequestMethod.POST)
    public PageItem<EffectUserRo> findEffectList(@ApiParam("条码id") @PathVariable("id") Integer id,
                                                 @RequestBody @Valid CommonQrcodeQo qo) throws IOException, IllegalAccessException, InstantiationException {
        PageItem<EffectUserRo> pageItem =  commonQrcodeService.findEffectUser(qo,id);
        return pageItem;
    }


    @ApiOperation("获取使用次数")
    @RequestMapping(value = "/frequency/{id}", method = RequestMethod.GET)
    public int findFrequency(@ApiParam("条码id") @PathVariable("id") Integer id) throws Exception {
        CommonQrcodeDetail commonQrcodeDetail = commonQrcodeDetailService.getCommonQrcodeDetail(id);
        return commonQrcodeDetail.getFrequency();
    }

    @ApiOperation("取消授权")
    @RequestMapping(value = "/cancelAuth/{id}", method = RequestMethod.GET)
    public JmMessage cancelAuth(@ApiParam("条码id") @PathVariable("id") Integer id) throws Exception {
        CommonQrcodeDetail commonQrcodeDetail = commonQrcodeDetailService.getCommonQrcodeDetail(id);
        commonQrcodeDetail.setUserId(null);
        commonQrcodeDetail.setFrequency(0);
        commonQrcodeDetailService.save(commonQrcodeDetail);
        return new JmMessage(0,"取消成功!!");
    }

    @ApiOperation("更新印刷备注")
    @RequestMapping(value = "/saveRemark/{id}", method = RequestMethod.GET)
    public JmMessage saveRemark(@ApiParam("条码id") @PathVariable("id") Integer id,@RequestParam String remark) throws Exception {
        CommonQrcode commonQrcode = commonQrcodeService.getCommonQrcode(id);
        commonQrcode.setPrintRemark(remark);
        commonQrcodeService.save(commonQrcode);
        return new JmMessage(0,"备注成功!!");
    }


    @ApiOperation("用户查询")
    @RequestMapping(value = "/userData", method = RequestMethod.POST)
    public List<ChannelRo> userData(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody @Valid ChannelQo qo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        ShopUser shopUser = shopUserRepository.findByPhoneNumberAndShopId(qo.getPhoneNumber(),user.getShopId());
        List<ChannelRo> ros = new ArrayList<>();
        if(shopUser != null){
            List<WxUser> wxUsers = wxUserService.findWxUserByShopUserId(shopUser.getId());
            if(wxUsers!=null && wxUsers.size()>0){
                for (WxUser wxUser:wxUsers) {
                    ChannelRo channelRo = new ChannelRo();
                    channelRo.setUserId(wxUser.getUserId());
                    channelRo.setNickname(Base64Util.getFromBase64(wxUser.getNickname()));
                    channelRo.setAgentRole(shopUser.getAgentRole());
                    channelRo.setHeadimgurl(wxUser.getHeadimgurl());
                    channelRo.setPhoneNumber(shopUser.getPhoneNumber());
                    channelRo.setUserName(shopUser.getUserName());
                    ros.add(channelRo);
                }
            }
        }
        return  ros;
    }

    @ApiOperation("通码授权")
    @RequestMapping(value = "/commonAuth", method = RequestMethod.POST)
    public JmMessage commonAuth(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody @Valid CommonAuthUo uo) throws IOException {

        List<Integer> commonQrcodeDetailIds = new ArrayList<>();
        if(uo.getCommonQrcodeDetailIds()!=null){
            String ids [] = uo.getCommonQrcodeDetailIds().split("/");
            for (String id:ids) {
                if(!id.equals("")){
                    if(Toolkit.parseObjForInt(id)!=0){
                        commonQrcodeDetailIds.add(Toolkit.parseObjForInt(id));
                    }
                }
            }
        }
        if(commonQrcodeDetailIds.size()>0){
            List<CommonQrcodeDetail> details = commonQrcodeDetailService.findCommonQrcodeDetails(commonQrcodeDetailIds);
            List<CommonQrcodeDetail> newDetails = new ArrayList<>();
            for (CommonQrcodeDetail detail:details) {
                detail.setUserId(uo.getUserId());
                if(uo.getType() == 1){
                    detail.setProductId(uo.getGoodsId());
                }
                newDetails.add(detail);
            }
            commonQrcodeDetailService.save(newDetails);
        }

        return new JmMessage(0,"授权成功!!");
    }

	
}
