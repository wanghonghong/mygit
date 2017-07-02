package com.jm.mvc.controller.shop;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.service.shop.ShopResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.resource.ShopResourceVo;
import com.jm.repository.po.shop.ShopResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.JsonMapper;
@Api
@RestController
@RequestMapping(value = "/")
public class ResourceCenterController {
	@Autowired
	private ShopResourceService shopResourceService;
	
	@ApiOperation("获取模板")
	@RequestMapping(value = "/shop_resource_list", method = RequestMethod.GET)
	public ModelAndView getCommissionList(@ApiParam(hidden=true) HttpServletRequest request,
			  @ApiParam(hidden=true) HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/pc/shop/commissionNoPutList");
		return view;
	}
	
	@RequestMapping(value = "/shop_resource/{resType}", method = RequestMethod.GET)
	public JmMessage findAllShopResourceByResType(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam(hidden = true) HttpServletResponse response,
			@ApiParam("类型") @PathVariable("resType")  Integer resType
			){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		int shopId = user.getShopId();
		ShopResourceVo resVo = new ShopResourceVo();
		resVo.setShopId(1);
		resVo.setResType(resType);
		List<ShopResource>  shopResourceList = shopResourceService.getResourceList(resVo);
		try {	
			 Object json = JsonMapper.toJson(shopResourceList);
			return new JmMessage(0,"获取商品资源成功!!",json);
		}catch (Exception e) {
			
			return new JmMessage(1,"获取商品资源失败!!");
		}
	}
	
	@RequestMapping(value = "/delete_shop_sesource/{id}", method = RequestMethod.POST)
	public JmMessage deleteShopResourceById(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam(hidden = true) HttpServletResponse response,
			@ApiParam("图片标识") @PathVariable("id")  Integer id
			) throws IOException {
		boolean  flag = shopResourceService.delShopResource(id);
		if (flag) {
    		return new JmMessage(0,"素材删除成功");
		}else{
			return new JmMessage(1,"素材删除失败！！");
		}
	}

}
