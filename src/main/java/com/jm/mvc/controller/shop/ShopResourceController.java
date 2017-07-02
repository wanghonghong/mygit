package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.ShopResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.repository.po.shop.ShopResource;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.JsonMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Api
@RestController
@RequestMapping(value = "/")
public class ShopResourceController {
	@Autowired
	private ShopResourceService shopResourceService;
	
	@RequestMapping(value = "/shopResource", method = RequestMethod.GET)
	public JmMessage findAllShopResourceByResType(@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam(hidden = true) HttpServletResponse response
			) throws IOException {
		String  resType = request.getParameter("resType");
		List<ShopResource>  shopResourceList = new ArrayList<ShopResource>();
		if(StringUtils.isEmpty(resType)){
		}else{
			JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			 shopResourceList = shopResourceService.getShopResourceList(user.getShopId(), Integer.parseInt(resType),0);

		}
		    Object json = JsonMapper.toJson(shopResourceList);
			return new JmMessage(0,"获取商品资源成功!!",json);

	}
	@RequestMapping(value = "/to_shopResource", method = RequestMethod.GET)
	public ModelAndView toShopResource(@ApiParam(hidden = true) HttpServletRequest request,
									   @ApiParam(hidden = true) HttpServletResponse response){

		String  resType = request.getParameter("resType");
		ModelAndView view  = new ModelAndView();
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<ShopResource>  shopResourceList = new ArrayList<ShopResource>();
		shopResourceList = shopResourceService.getShopResourceList(user.getShopId(), Integer.parseInt(resType),0);
		request.setAttribute("shopResourceList",shopResourceList);
		view.setViewName("/pc/shop/shopResourceConf");
		return view;
	}
	@RequestMapping(value = "/deleteShopResource", method = RequestMethod.POST)
	public JmMessage deleteShopResourceById(
			@ApiParam(hidden = true) HttpServletRequest request,
			@ApiParam(hidden = true) HttpServletResponse response
			) throws IOException {
		String  id = request.getParameter("id");
		boolean  flag = shopResourceService.delShopResource(Integer.parseInt(id));
		if (flag) {
    		return new JmMessage(0,"素材删除成功");
		}else{
			return new JmMessage(1,"素材删除失败！！");
		}

	}


}
