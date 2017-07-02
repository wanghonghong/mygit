package com.jm.mvc.controller.order;

import com.jm.business.service.order.ShoppingCartService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.business.service.wx.WxService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.*;
import com.jm.mvc.vo.online.market.ShoppingCartQueryVo;
import com.jm.mvc.vo.online.market.ShoppingCartVo;
import com.jm.mvc.vo.order.OrderVo;
import com.jm.mvc.vo.qo.ShoppingCartQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.order.ShoppingCart;
import com.jm.repository.po.system.user.User;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.ShoppingCartConverter;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * 
 *<p>购物车商品管理</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/shopCart")
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
    private WxUserService wxUserService;
	
	@Autowired
    private WxService wxservice;
	
	@Autowired
    private ShopService shopService;

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;

	@ApiOperation("app端购物车显示")
	@RequestMapping(value = "",method = RequestMethod.GET)
	public ModelAndView shopCartList(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam(hidden=true) HttpServletResponse response) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer shopId = wxUserSession.getShopId();
		Integer userId = wxUserSession.getUserId();
		Integer shareId = wxUserSession.getShareid();
		ModelAndView view  = new ModelAndView();
		if(null != userId && null != shopId){
			List<ShoppingCartQo> shoppingCartQo = shoppingCartService.queryShoppingCart(userId,shopId);//用户id和店铺id暂时默认赋值
			view.getModelMap().addAttribute("shoppingCartQo", shoppingCartQo);
			if(shoppingCartQo.size()>0){
				view.setViewName("/app/shopcart/shopcartlist");
			}else{
				view.setViewName("/app/shopcart/shopcartNull");
			}
		}else{
			view.setViewName("/app/shopcart/shopcartNull");
		}
		return view;
	}

	@ApiOperation("pc端购物车显示查询")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageItemExt shopCartList(@ApiParam(hidden=true) HttpServletRequest request,
									@RequestBody @Valid ShoppingCartQueryVo shoppingCartQueryVo) throws Exception{
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer userid = user.getUserId();
		if(null!=user){
			shoppingCartQueryVo.setShopId(user.getShopId());
		}
		User shopUser = userService.getUser(userid);
		OrderVo orderVos = new OrderVo();
		String hxAcct = userRoleService.initShopUserHxAcct(user);
		PageItem<ShoppingCartVo> pageItem = shoppingCartService.queryShoppingCart(shoppingCartQueryVo);
		orderVos.setHxAccount(hxAcct);
		orderVos.setUserid(userid);
		orderVos.setNickname(shopUser.getUserName());
		orderVos.setHeadImg(ImgUtil.appendUrl(shopUser.getHeadImg(),100));
		orderVos.setShopId(user.getShopId());
		PageItemExt<ShoppingCartVo,OrderVo> ext = JdbcUtil.pageItem2Ext(pageItem,orderVos);
		return ext;
	}
	
	@ApiOperation("把商品添加到购物车")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ShoppingCart saveOrderCar(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("购物车一条记录标识")
										@RequestBody @Valid ShoppingCartCreateVo shopCartVo){
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer shopId = wxUserSession.getShopId();
		Integer userId = wxUserSession.getUserId();
		return shoppingCartService.addShoppringCart(ShoppingCartConverter.toShopCart(shopCartVo),shopId,userId);
	}
	
	@ApiOperation("删除购物车中的商品")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteOrderCart(@ApiParam("购物车一条记录标识") @PathVariable("id") Long id){
		Integer msg = shoppingCartService.deleteShoppingCart(id);
		return new JmMessage(msg, "购物车移除商品成功！");
	}
	
	@ApiOperation("修改购物车中的商品数量")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT )
	public void updateShoppingCart(@ApiParam("购物车一条记录标识") @PathVariable("id") Long id,Integer count){
		shoppingCartService.updateShoppingCart(id, count);
	}

}
