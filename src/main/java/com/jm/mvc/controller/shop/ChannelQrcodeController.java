package com.jm.mvc.controller.shop;

import com.jm.business.service.product.ProductService;
import com.jm.business.service.shop.ChannelQrcodeService;
import com.jm.business.service.wx.WxAuthService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.commQrcode.*;
import com.jm.repository.client.WxClient;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 
 *<p>渠道商品条码</p>
 */

@Api
@RestController
@RequestMapping(value = "/channelQrcode")
public class ChannelQrcodeController {

	@Autowired
	private ChannelQrcodeService channelQrcodeService;

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private WxClient wxClient;

	@Autowired
	private WxAuthService wxAuthService;

	@Autowired
	private ProductService productService;


	@ApiOperation("渠道信息")
	@RequestMapping(value = "/channerlData", method = RequestMethod.POST)
	public PageItem<ChannelRo> channerlData(@ApiParam(hidden=true) HttpServletRequest request, @RequestBody @Valid ChannelQo qo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<ChannelRo> pageItem = new PageItem<>();
		if(user!=null && user.getShopId()!=null && user.getShopId()>0){
			pageItem = wxUserService.getChannelData(user.getShopId(),qo);
		}
		return pageItem;
	}

    @ApiOperation("查询条码列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public PageItem<ChannelQrcodeRo> findAll(@ApiParam(hidden=true) HttpServletRequest request,
											 @RequestBody @Valid ChannelQrcodeQo qo) throws IOException, IllegalAccessException, InstantiationException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<ChannelQrcodeRo> pageItem =  channelQrcodeService.findAll(qo,user.getShopId());
        return pageItem;
    }

    @ApiOperation("新增")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage addGoodsQrcode(@ApiParam(hidden=true) HttpServletRequest request,
								  @RequestBody ChannelQrcodeCo co) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		co.setShopId(user.getShopId());
		channelQrcodeService.save(co);
		return new JmMessage(0,"新增成功!!");
	}


	@ApiOperation("获取")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CheckDownLoadRo find(@ApiParam("条码id") @PathVariable("id") Integer id,
									 @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		ChannelQrcode goodsQrcode = channelQrcodeService.getChannelQrcode(id);
		CheckDownLoadRo ro = new CheckDownLoadRo();
		if(goodsQrcode.getQrcode()==null || goodsQrcode.getQrcode().equals("")){
			//1.生成微信二维码   （扫码  关注并推送商品消息）
			String accessToken = wxAuthService.getAuthAccessToken(user.getAppId());
			String qrcode = wxClient.getStrTicket(accessToken,goodsQrcode.getUserId()+","+goodsQrcode.getProductId()+","+goodsQrcode.getId());//生成基础二维码
			goodsQrcode.setQrcode(qrcode);
			channelQrcodeService.save(goodsQrcode);
			ro.setQrcode(qrcode);
		}else{
			ro.setQrcode(goodsQrcode.getQrcode());
		}

		Product product = productService.getProduct(goodsQrcode.getProductId());
		ro.setName(goodsQrcode.getName());
		ro.setNumber(id+"");
		ro.setProductImg(ImgUtil.appendUrl(product.getPicSquare(),720));
		ro.setProductName(product.getName());
		ro.setGoodsQrcodeId(id);
		return ro;
	}


	@ApiOperation("查询条码使用效果")
	@RequestMapping(value = "/findEffectList/{id}", method = RequestMethod.POST)
	public PageItem<EffectUserRo> findEffectList(@ApiParam(hidden=true) HttpServletRequest request,
												  @ApiParam("条码id") @PathVariable("id") Integer id,
												  @RequestBody @Valid ChannelQrcodeQo qo) throws IOException, IllegalAccessException, InstantiationException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<EffectUserRo> pageItem =  channelQrcodeService.findEffectUser(qo,id);
		return pageItem;
	}

	@ApiOperation("获取使用次数")
	@RequestMapping(value = "/frequency/{id}", method = RequestMethod.GET)
	public int findFrequency(@ApiParam("条码id") @PathVariable("id") Integer id) throws Exception {
		ChannelQrcode channelQrcode = channelQrcodeService.getChannelQrcode(id);
		return channelQrcode.getFrequency();
	}


	@ApiOperation("取消授权")
	@RequestMapping(value = "/cancelAuth/{id}", method = RequestMethod.GET)
	public JmMessage cancelAuth(@ApiParam("条码id") @PathVariable("id") Integer id) throws Exception {
		ChannelQrcode channelQrcode = channelQrcodeService.getChannelQrcode(id);
		channelQrcode.setStatus(1);
		channelQrcodeService.save(channelQrcode);
		return new JmMessage(0,"取消成功!!");
	}

	@ApiOperation("更新印刷备注")
	@RequestMapping(value = "/saveRemark/{id}", method = RequestMethod.GET)
	public JmMessage saveRemark(@ApiParam("条码id") @PathVariable("id") Integer id,@RequestParam String remark) throws Exception {
		ChannelQrcode channelQrcode = channelQrcodeService.getChannelQrcode(id);
		channelQrcode.setPrintRemark(remark);
		channelQrcodeService.save(channelQrcode);
		return new JmMessage(0,"备注成功!!");
	}


}
