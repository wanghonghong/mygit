package com.jm.mvc.controller.product;

import com.jm.business.service.online.HxUserService;
import com.jm.business.service.product.ProductService;
import com.jm.business.service.product.ProductSpecService;
import com.jm.business.service.product.ProductTransPriceService;
import com.jm.business.service.shop.imageText.ImgTextMsgService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.product.product.ProductSpecPcVo;
import com.jm.mvc.vo.product.product.ProductVo;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductSpec;
import com.jm.repository.po.product.ProductTrans;
import com.jm.repository.po.wx.ImgTextMsg;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.product.ProductConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>系统管理</p>
 *
 * @author chenyy
 * @version latest
 * @date 2016/5/7
 */
@Api
@Log4j
@RestController
@RequestMapping(value = "")
public class ProductController{
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductSpecService productSpecService;
	@Autowired
	private ProductTransPriceService productTransPriceService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private ImgTextMsgService imgTextMsgService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private HxUserService hxUserService;


	  /**
	   *<p>获取商品规格</p>
	   *
	   * @author chenyy
	   * @version latest
	 * @throws IOException 
	   * @data 2016年5月11日
	   */
	@RequestMapping(value= "/product/{pid}/product_spec", method = RequestMethod.GET)
	public List<ProductSpecPcVo> getProductSpec(@ApiParam("商品标识") @PathVariable("pid")
							Integer pid){
		return productSpecService.getList(pid);
	}

	@RequestMapping(value= "/product/trans", method = RequestMethod.POST)
	public Integer getTransPrice(@ApiParam("商品购买数量计算运费使用") @RequestBody List<ProductTrans> productTrans){
		Integer transFree = 0;
		if (productTrans!=null){
			transFree  = productTransPriceService.getTransPrice(productTrans);
		}
		return transFree;
	}

	@ApiOperation("删除")
	@RequestMapping(value = "/product/delImgText", method = RequestMethod.POST)
	public JmMessage delImgText(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		ImgTextMsg textmsg  = imgTextMsgService.findMsgbyAppidAndOpenId(wxUserSession.getAppid(),wxUserSession.getOpenid());
		if(textmsg!=null){
			imgTextMsgService.del(textmsg.getImgTextId());
		}
        return new JmMessage(0,"");
	}

	
	@ApiOperation("当前用户未关注公众号且分享人不为空")
    @RequestMapping(value = "/product/qrcode", method = RequestMethod.POST)
    public JmMessage goshop(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		String appid =wxUserSession.getAppid();
		String openid = wxUserSession.getOpenid();
		String qrcodeurl="";
		if (wxUserSession.getUserId()==0){ //游客
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getShareid(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,0);

			//系统通知回推图文消息
			ImgTextMsg textmsg = null;
			if (appid != null && openid != null) {
				textmsg = imgTextMsgService.findMsgbyAppidAndOpenId(appid, openid);
			}
			if (textmsg != null) {
				imgTextMsgService.del(textmsg.getImgTextId());
			}
			String pid = request.getParameter("pid");
			String type = request.getParameter("type");
			ImgTextMsg msg = new ImgTextMsg();
			if(pid!=null && !pid.equals("")){
				msg.setId(Toolkit.parseObjForInt(pid));
			}else{
				msg.setId(0);
			}
			msg.setOpenid(openid);
			msg.setAppid(appid);
			msg.setCreateTime(new Date());
			if(type!=null && !type.equals("") ){
				msg.setType(Toolkit.parseObjForInt(type));
			}else{
				msg.setType(0);
			}
			imgTextMsgService.save(msg);
		}else{
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getUserId(),wxUserSession.getAppid());//自己的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,0);
		}
   		return new JmMessage(0, qrcodeurl);
    }

	@ApiOperation("获取商品库存数量")
	@RequestMapping(value = "/product/stock/{pid}", method = RequestMethod.GET)
	public int getProductStockCount(@ApiParam(hidden=true) HttpServletRequest request,
									@ApiParam("商品标识pid") @PathVariable("pid")  Integer pid){
		Product product = productService.getProduct(pid);
		return product.getTotalCount();
	}

	@ApiOperation("获取商品规格库存数量")
	@RequestMapping(value = "/productSpec/Stock/{specId}", method = RequestMethod.GET)
	public int getBuyCount(@ApiParam(hidden=true) HttpServletRequest request,
						   @ApiParam("商品规格标识specId") @PathVariable("specId")  Integer specId){
		ProductSpec productSpec = productSpecService.getOneSpec(specId);
		return productSpec.getTotalCount();
	}

	@RequestMapping(value="/product/info/{pid}",method = RequestMethod.GET)
	public ProductVo getProductDetail (@ApiParam("商品标识") @PathVariable("pid") int pid,@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		Product product = productService.getProduct(pid);
		ProductVo productVo = ProductConverter.toProductVo(product);
		return  productVo;
	}
}
