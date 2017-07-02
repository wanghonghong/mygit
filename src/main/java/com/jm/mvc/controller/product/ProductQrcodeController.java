package com.jm.mvc.controller.product;

import com.jm.business.service.product.ProductQrcodeService;
import com.jm.business.service.product.ProductService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.WxUserSession;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductQrcode;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>商品二维码</p>
 *
 * @author htp
 * @version latest
 * @date 2016/8/8
 */
@Api
@RestController
@RequestMapping(value = "productQrcode")
public class ProductQrcodeController{
	@Autowired
	private ProductQrcodeService productQrcodeService;
	@Autowired
	private ProductService productService;
	
	
	
	@ApiOperation("生成商品详情二维码 并 保存到商品二维码表")
    @RequestMapping(value = "/qrcode/{pid}", method = RequestMethod.POST)
    public JmMessage goodQrcode( @ApiParam(hidden = true) HttpServletRequest request ,
    							 @ApiParam("商品编号") @PathVariable("pid") Integer pid) throws Exception {
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer shopId = wxUserSession.getShopId();
		Integer userId = wxUserSession.getUserId();
		Product product = productService.getProduct(pid);
		String url = Constant.APP_DOMAIN+"/product/"+pid+"?shopId="+shopId+"&shareId="+userId;
		List<ProductQrcode> prqrcode=productQrcodeService.findProductQrcodeByPidAndShareId(pid, userId);
		//已经存在二维码
		if(prqrcode!=null && prqrcode.size()>0){
			return new JmMessage(0, ImgUtil.appendUrl(prqrcode.get(0).getQrcodeUrl(),0));
		}

		JmMessage msg =productQrcodeService.getQrcoce(url,product);
		if(msg.getCode().equals(0)){
			ProductQrcode pq = new ProductQrcode();
			pq.setQrcodeUrl(msg.getMsg());
			pq.setShareId(userId);
			pq.setShopId(shopId);
			pq.setPid(pid);
			productQrcodeService.save(pq);
		}
		msg.setMsg(ImgUtil.appendUrl(msg.getMsg()));
		return msg;
    }
}
