package com.jm.mvc.controller.shop;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.business.service.shop.*;
import com.jm.business.service.wx.WxAuthService;
import com.jm.business.service.wx.WxService;
import com.jm.mvc.vo.JmShopSession;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.erp.ErpShopQo;
import com.jm.mvc.vo.erp.ErpShopRo;
import com.jm.mvc.vo.erp.ErpShopUo;
import com.jm.mvc.vo.erp.ErpUserShopRo;
import com.jm.mvc.vo.shop.*;
import com.jm.repository.jpa.resource.JmResourceRepository;
import com.jm.repository.po.system.user.JmResource;
import com.jm.staticcode.util.ImgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.service.system.UserRoleService;
import com.jm.mvc.vo.JmMessage;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopCategory;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.system.user.UserRole;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.ShopConverter;

/**
 * <p>店铺</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/")
public class ShopController {
	@Autowired
    private ShopService shopService;
	@Autowired
    private UserRoleService userRoleService;
	@Autowired
	private WxAuthService wxAuthService;
	@Autowired
	private WxPubAccountService wxPubAccountService; 
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private InitDataService initDataService;
	@Autowired
	private WxService wxService;
	@Autowired
	private JmResourceRepository jmResourceRepository;



	@ApiOperation("店铺首页")
	@RequestMapping(value = "/shop", method = RequestMethod.GET)
    public ModelAndView shopmanage(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
		//查询所有顶级菜单
		List<JmResource>  leftResources = jmResourceRepository.findByParentResourceIdAndStatusAndTypeOrderBySeqAsc(0,0,0);
		List<JmResource>  rightResources1 = jmResourceRepository.findByParentResourceIdAndStatusAndTypeOrderBySeqAsc(0,0,1);
		List<JmResource>  rightResources2 = jmResourceRepository.findByParentResourceIdAndStatusAndTypeOrderBySeqAsc(0,0,2);
		request.setAttribute("leftResources", leftResources);
		request.setAttribute("rightResources1", rightResources1);
		request.setAttribute("rightResources2", rightResources2);
		view.setViewName("/pc/shop/shop_index");
        return view;
    }

    @ApiOperation("店铺首页列表")
    @RequestMapping(value = "/shop/list", method = RequestMethod.GET)
    public List<JmShopSession> shoplist(@ApiParam(hidden=true) HttpServletRequest request) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<JmShopSession> ls= shopService.getShopList(user.getUserId());
        List<JmShopSession> lss = new ArrayList<JmShopSession>();
        for (JmShopSession shopSession: ls) {
        	if(!shopSession.getImgUrl().contains("wx.qlogo.cn")){
				if(shopSession.getImgUrl().equals("/css/pc/img/logo1.png")){
					shopSession.setImgUrl(Constant.COS_PATH+shopSession.getImgUrl());
				}else{
					shopSession.setImgUrl(ImgUtil.appendUrl(shopSession.getImgUrl(),0));
				}
			}
            lss.add(shopSession);
        }
        user.setShopLs(lss);
        request.getSession().setAttribute(Constant.SESSION_USER, user);
        return lss;
    }



	@ApiOperation("获取父类ID的列表")
	@RequestMapping(value = "shop/shop_menu/{resouceId}", method = RequestMethod.GET)
	public List<JmResource> shopMenu(@ApiParam("菜单编号")  @PathVariable("resouceId") Integer resouceId){
		List<JmResource>  leftResources = jmResourceRepository.findByParentResourceIdAndStatus(resouceId,0);
		List<Integer> ids = new ArrayList<>();
		for (JmResource rs:leftResources) {
			ids.add(rs.getResourceId());
		}
		if(ids.size()>0){
			List<JmResource>  sonMenu =jmResourceRepository.findJmResourceByParentResourceIds(ids,0);
			leftResources.addAll(sonMenu);
		}
		return leftResources;
	}

	@ApiOperation("店铺指定菜单")
	@RequestMapping(value = "shop/shop_menu2/{resouceId}", method = RequestMethod.GET)
	public JmResource shopMenu2(@ApiParam("菜单编号")  @PathVariable("resouceId") Integer resouceId){
		JmResource  resource = jmResourceRepository.findOne(resouceId);
		return resource;
	}

	@ApiOperation("检查店铺授权是否成功")
    @RequestMapping(value = "/checkshop/{shopId}", method = RequestMethod.POST)
    public JmMessage checkshop(@ApiParam("店铺标识")  @PathVariable("shopId") Integer shopid,@ApiParam(hidden=true) HttpServletRequest request){
		Shop shop= shopService.findShopById(shopid);
		if(shop!=null){
			if(shop.getAppId()!=null && shop.getAppId()!=""){
				return new JmMessage(0, "成功","授权成功！");
			}
		}
		return new JmMessage(1, "失败！","授权失败！");
    }
	
	@ApiOperation("创建店铺")
    @RequestMapping(value = "/createshop", method = RequestMethod.POST)
    public JmMessage createshop(@ApiParam("店铺创建VO") @RequestBody @Valid ShopForCreateVo shopVo,
    						@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		if ( user == null) {
            return new JmMessage(1, "创建失败！","未登录！");
        }
        //店铺新创建 预置图片
		shopVo.setImgUrl("/css/pc/img/logo1.png");
		
		//店铺ID不为空 说明是预建的店铺
		if(shopVo.getShopId()!=null){
			Shop shop1 = shopService.saveShop(ShopConverter.toShop(shopVo));
			if(shop1!=null){
				return new JmMessage(0, shop1.getShopId()+"");
			}
		}else{
        	Shop shop1 = shopService.saveShop(ShopConverter.toShop(shopVo));
        	if(shop1!=null){
	    		UserRole userRole = new UserRole();
	    		userRole.setRoleId(2);
	    		userRole.setShopId(shop1.getShopId());
	    		userRole.setUserId(user.getUserId());
	    		userRoleService.createUserRole(userRole);
	    		user.setShopId(shop1.getShopId());
	    		request.getSession().setAttribute(Constant.SESSION_USER, user);
	    		return new JmMessage(0, shop1.getShopId()+"");
        	}
		}	
		return new JmMessage(1, "失败");
		
    }
	
	
	@ApiOperation("首页地图")
	@RequestMapping(value = "/map", method = RequestMethod.GET)
    public ModelAndView map(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/shop/map");
        return view;
    }
	
	
	@ApiOperation("创建店铺页面")
	@RequestMapping(value = "/createshop", method = RequestMethod.GET)
    public ModelAndView createshop(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView();
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<Shop> ls= shopService.getShopStatusList(user.getUserId(), 1);
        Shop shop = new Shop();
		if(ls.size()>0){
            shop= ls.get(0);
            ShopCategory cate =  shopCategoryService.findOne(shop.getShopType());
            request.setAttribute("catename", cate.getTypeName());
		}
        //主营商品
        List<ShopCategory> prls= shopCategoryService.findAll();
        request.setAttribute("prls", prls);
        //此处获取预授权码与平台AppId返回页面做授权
        //授权地址 https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx
        String authPageUrl= wxAuthService.getAuthPageUrl();
        request.setAttribute("authPageUrl", authPageUrl);
        request.setAttribute("shop", shop);
        request.setAttribute("componentAppid", Constant.COMPONENT_APP_ID);
        view.setViewName("/pc/shop/create_shop2");
        return view;
    }

	@ApiOperation("创建店铺数据")
	@RequestMapping(value = "shop/new_shop", method = RequestMethod.GET)
	public NewShopRo newShop(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		NewShopRo ro = new NewShopRo();
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<Shop> ls= shopService.getShopStatusList(user.getUserId(), 1);
		Shop shop = new Shop();
		if(ls.size()>0){
			shop= ls.get(0);
			ShopCategory cate =  shopCategoryService.findOne(shop.getShopType());
			ro.setCateName(cate.getTypeName());
		}
		ro.setShop(shop);
		//主营商品
		List<ShopCategory> prls= shopCategoryService.findAll();
		ro.setShopCategorys(prls);
		return ro;
	}


	@ApiOperation("店铺管理")
    @RequestMapping(value = "/shop_manager/{shopId}/{roleId}", method = RequestMethod.POST)
    public JmMessage test(@ApiParam(hidden=true) HttpServletRequest request,
							 @ApiParam(hidden=true) HttpServletResponse response,
    						@ApiParam("店铺标识")  @PathVariable("shopId") Integer shopId,
						  @ApiParam("角色标识")  @PathVariable("roleId") Integer roleId) throws Exception {
		ModelAndView view = new ModelAndView();
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		if(user == null){
			return new JmMessage(1, "未登陆");
		}
		Shop shop = shopService.findShopById(shopId);
		if(shop == null ){
			return new JmMessage(1, "店铺不存在");
		}
		if(shop.getAppId() == null || shop.getAppId().equals("") ){
			return new JmMessage(1, "店铺未授权");
		}
		if(shop.getShopStatus() == 1 ){
			return new JmMessage(1, "店铺已关闭");
		}

		WxPubAccount account = wxPubAccountService.findWxPubAccountByAppid(shop.getAppId());
		if(account == null){
			return new JmMessage(1, "店铺未授权");
		}

		 //获取用户基础二维码
		if(account.getPubQrcodeUrl()==null || "".equals(account.getPubQrcodeUrl())){
			String accessToken =wxAuthService.getAuthAccessToken(shop.getAppId());
			String pubQrcodeUrl = wxService.getForeverQrcode(accessToken);
			account.setPubQrcodeUrl(pubQrcodeUrl);
			WxPubAccount returnACcount = wxPubAccountService.save(account);
			user.setPubQrcodeUrl(returnACcount.getPubQrcodeUrl());
		}else{
			user.setPubQrcodeUrl(account.getPubQrcodeUrl());
		}

		List<JmShopSession> shopLs = user.getShopLs();
        if(shopLs!=null){
            for (JmShopSession jmShopSession : shopLs) {
                if(shopId.equals(jmShopSession.getShopId()) && jmShopSession.getStatus().equals(0) && jmShopSession.getRoleId().equals(roleId)){
                    user.setShopId(jmShopSession.getShopId());
                    user.setAppId(jmShopSession.getAppId());
					user.setImgUrl(jmShopSession.getImgUrl());
					user.setRoleId(roleId);
					user.setShopName(jmShopSession.getShopName());
					user.setUid(shop.getWbUid()); // cj 2017-03-04  微博商家用户uid 加入session
                    request.getSession().setAttribute(Constant.SESSION_USER,user);
                    view.setViewName("/pc/shop/shop_manager");
					return new JmMessage(0, "进入店铺");
                }
            }
        }

		return new JmMessage(1, "返回首页");
    }

	@ApiOperation("店铺管理页面")
	@RequestMapping(value = "/shop_manager", method = RequestMethod.GET)
	public ModelAndView shopManager( @ApiParam(hidden=true) HttpServletResponse response,@ApiParam(hidden=true) HttpServletRequest request) throws IOException {
		ModelAndView view = new ModelAndView();
		view.setViewName("/pc/shop/shop_manager");
		return view;
	}

	@ApiOperation("店铺编辑信息")
    @RequestMapping(value = "shop/shopedit", method = RequestMethod.GET)
    public Shop edit(@ApiParam(hidden=true) HttpServletRequest request){
		 JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		 Shop shop1 = shopService.findShopById(user.getShopId());
		if(shop1.getImgUrl()!=null){
			if(!shop1.getImgUrl().contains("wx.qlogo.cn")){
				shop1.setImgUrl(ImgUtil.appendUrl(shop1.getImgUrl(),0));
			}
		}
    	return shop1;
    }

	@ApiOperation("主营类目")
	@RequestMapping(value = "shop/prls", method = RequestMethod.GET)
	public List<ShopCategory> prls(){
		List<ShopCategory> prls= shopCategoryService.findAll();
		return prls;
	}

    @ApiOperation("店铺创建完成")
    @RequestMapping(value = "shop/okshop/{shopId}", method = RequestMethod.POST)
    public JmMessage okshop(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("店铺标识")  @PathVariable("shopId") Integer shopId){
        Shop shop =  shopService.findShopById(shopId);
        if(shop.getAppId()!=null&&!shop.getAppId().equals("")){
			shop.setStatus(0);
			shopService.saveShop(shop);
			JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			initDataService.initData(shop,user);
            return new JmMessage(0, "授权成功");
        }
        return new JmMessage(1, "您的授权未完成！");
    }
	
	
	@ApiOperation("店铺更新")
    @RequestMapping(value = "shop/shopupdate", method = RequestMethod.POST)
    public JmMessage updateshop(@ApiParam("店铺更新VO") @RequestBody @Valid ShopForUpdateVo shopVo,@ApiParam(hidden=true) HttpServletRequest request){
		Shop shop = shopService.updateShopall(shopVo);
		if(null!=shop){
			JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			user.setShopName(shop.getShopName());
			user.setImgUrl(shop.getImgUrl());
			request.getSession().setAttribute(Constant.SESSION_USER,user);
			return new JmMessage(0, "更新成功！");
		}
    	return new JmMessage(1, "更新失败！","系统内部错误");
    }
	
	
	@ApiOperation("更新微信公众号商户Id及支付秘钥")
    @RequestMapping(value = "/updateaccount/{shopId}", method = RequestMethod.POST)
    public JmMessage updateaccount(@ApiParam("店铺标识")  @PathVariable("shopId") Integer shopId,
    							   @ApiParam("店铺更新VO") @RequestBody @Valid WxPubAccount wxpubaccount,
    							   @ApiParam(hidden=true) HttpServletRequest request) throws Exception{
			Shop shop = shopService.findShopById(shopId);
			//JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			if(shop!=null){
				if(null!=shop.getAppId()&&!shop.getAppId().equals("")){
					WxPubAccount wxPubAccount2=wxPubAccountService.findWxPubAccountByAppid(shop.getAppId());
					wxPubAccount2.setMchId(wxpubaccount.getMchId());
					wxPubAccount2.setAppKey(wxpubaccount.getAppKey());
					wxPubAccountService.save(wxPubAccount2);
					shop.setShareLan1("");
					//imageTextService.saveImageText(shop,user);
					//wxService.defaultMenu(shop.getAppId(), shopId, request);//初始化菜单
					
					return new JmMessage(0, shopId+"");
				}else{
					return new JmMessage(1, "创建失败！","商户未进行微信授权无法获取授权编号！");
				}
			}
		return new JmMessage(1, "创建失败！","未登录！");
    }


	@ApiOperation("更新微信公众号商户Id及支付秘钥")
	@RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
	public JmMessage updateaccount(@ApiParam("店铺更新VO") @RequestBody @Valid WxPubAccount wxpubaccount,
								   @ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Shop shop = shopService.findShopById(user.getShopId());
		if(shop!=null){
			if(null!=shop.getAppId()&&!shop.getAppId().equals("")){
				WxPubAccount wxPubAccount2=wxPubAccountService.findWxPubAccountByAppid(shop.getAppId());
				wxPubAccount2.setMchId(wxpubaccount.getMchId());
				wxPubAccount2.setAppKey(wxpubaccount.getAppKey());
				wxPubAccountService.save(wxPubAccount2);
				return new JmMessage(0, "更新成功！");
			}else{
				return new JmMessage(1, "创建失败！","商户未进行微信授权无法获取授权编号！");
			}
		}
		return new JmMessage(1, "创建失败！","未登录！");
	}


	@ApiOperation("店铺编辑页面")
	@RequestMapping(value = "/shopedit", method = RequestMethod.GET)
	public ModelAndView shopedit(@ApiParam(hidden=true) HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/pc/shop/shopedit");
		return view;
	}

	@ApiOperation("认证保险获取")
	@RequestMapping(value = "shop/shop_auth", method = RequestMethod.GET)
	public Shop auth(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Shop shop = shopService.findShopById(user.getShopId());
		return shop;
	}

	@ApiOperation("认证保险更新")
	@RequestMapping(value = "shop/shop_auth", method = RequestMethod.PUT)
	public JmMessage authupdate(@ApiParam(hidden=true) HttpServletRequest request,
						   @RequestBody ShopAuthUo shopAuthUo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Shop shop = shopService.findShopById(user.getShopId());
		if (shop!=null){
			shop.setCompanyAuth( shopAuthUo.getCompanyAuth() );
			shop.setUserAuth( shopAuthUo.getUserAuth() );
			shop.setJmAuth( shopAuthUo.getJmAuth() );
			shop.setPromise( shopAuthUo.getPromise() );
			shop.setExchange( shopAuthUo.getExchange() );
			shop.setDirectSell( shopAuthUo.getDirectSell() );
			shop.setSafety( shopAuthUo.getSafety() );
			Shop shop1 =shopService.saveShop(shop);
			if (shop1 != null){
				return new JmMessage(0, "更新成功！");
			}
		}
		return new JmMessage(1, "更新失败！");
	}


	@ApiOperation("店铺二维码")
	@RequestMapping(value = "shop/shop_qrcode", method = RequestMethod.GET)
	public Map getShopQrcode(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		if(user == null){
			return null;
		}
		Map map = new HashMap();
		map.put("wxShopQrcode",user.getPubQrcodeUrl());
		return map;
	}

	@ApiOperation("总部系统查询商家类列表")
	@RequestMapping(value = "shop/erpGetShopList", method = RequestMethod.POST)
	public PageItem<ErpShopRo> erpGetShopList(@RequestBody @Valid ErpShopQo qo) throws ParseException, IOException {
		PageItem<ErpShopRo> pageItem =  shopService.erpGetShopList(qo);
		return pageItem;
	}

	@ApiOperation("总部系统 经营状态列表")
	@RequestMapping(value = "shop/erpUserShops", method = RequestMethod.POST)
	public PageItem<ErpUserShopRo> erpUserShops(@RequestBody @Valid ErpShopQo qo) throws ParseException, IOException {
		PageItem<ErpUserShopRo> pageItem =  shopService.erpUserShops(qo);
		return pageItem;
	}

	@ApiOperation("总部系统 开店状态列表")
	@RequestMapping(value = "shop/erpShops", method = RequestMethod.POST)
	public PageItem<ErpUserShopRo> erpShops(@RequestBody @Valid ErpShopQo qo) throws ParseException, IOException {
		PageItem<ErpUserShopRo> pageItem =  shopService.erpShops(qo);
		return pageItem;
	}

    @ApiOperation("总部系统 店铺审核列表")
    @RequestMapping(value = "shop/erpAuditShops", method = RequestMethod.POST)
    public PageItem<ErpUserShopRo> auditShops(@RequestBody @Valid ErpShopQo qo) throws ParseException, IOException {
        PageItem<ErpUserShopRo> pageItem =  shopService.auditShops(qo);
        return pageItem;
    }

	@ApiOperation("总部系统 店铺开启/关闭")
	@RequestMapping(value = "shop/setShopStatus", method = RequestMethod.POST)
	public JmMessage setShopStatus(@RequestBody @Valid ErpShopUo uo) {
		shopService.updateShopStatus(uo);
		return new JmMessage(0,"修改成功");
	}

}
