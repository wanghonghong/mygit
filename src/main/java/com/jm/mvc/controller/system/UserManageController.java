package com.jm.mvc.controller.system;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.user.ShopUserRo;
import com.jm.mvc.vo.user.ShopUserVo;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.ShopUserConverter;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * <p>微信用户管理</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/12
 */

@Api
@RestController
@RequestMapping(value = "/user_manage")
public class UserManageController {

	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private ShopUserService shopUserService;
	@Autowired
	private ShopService shopService;

	@ApiOperation("账户管理页面   type 1:升级分销  2:升级代理 3:个人账户")
	@RequestMapping(value = "/app/{type}", method = RequestMethod.GET)
	public ModelAndView usermanage1(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("访问状态")  @PathVariable("type") Integer type){
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		if(wxUserSession.getUserId()>0){
			WxUser wxuser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
			if(wxuser.getShopUserId()!=null&&wxuser.getShopUserId()>0){
				ShopUser shopUser = shopUserService.findShopUser(wxuser.getShopUserId());
				if(shopUser.getPhoneNumber()!=null && !shopUser.getPhoneNumber().equals("")){//已经注册过
					String roleName = "关注客户";
					if(wxuser.getIsBuy()!=0){
						roleName = "购买客户";
					}
					if(shopUser.getAgentRole()==1){
						roleName = "代理商1档";
					}else if (shopUser.getAgentRole()==2){
						roleName = "代理商2档";
					}else if (shopUser.getAgentRole()==3){
						roleName = "代理商3档";
					}else if (shopUser.getAgentRole()==4){
						roleName = "代理商4档";
					}else if (shopUser.getAgentRole()==5){
						roleName = "分销商1档";
					}else if (shopUser.getAgentRole()==6){
						roleName = "分销商2档";
					}else if (shopUser.getAgentRole()==7){
						roleName = "分销商3档";
					}else if (shopUser.getAgentRole()==8){
						roleName = "分享客";
					}
					request.setAttribute("roleName",roleName);
					request.setAttribute("phoneNumber",shopUser.getPhoneNumber());
					request.setAttribute("alipay",shopUser.getAlipay());
					request.setAttribute("userName",shopUser.getUserName());
					return new ModelAndView("/app/user/usermanage");//账户管理页面
				}
			}
		}
		request.setAttribute("pagetype",type);
		return new ModelAndView("/app/user/register");//注册页面
	}

	@ApiOperation("保存店铺用户")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public JmMessage save(@ApiParam(hidden=true) HttpServletRequest request,
							@Valid @RequestBody @ApiParam("店铺用户VO") ShopUserVo vo){
		//判断验证码
		String jmcode=(String) request.getSession().getAttribute("jmRegCode");
		String jmRegPhone=(String) request.getSession().getAttribute("jmRegPhone");
		if(null!=jmcode){
			if(!vo.getCode().equals(jmcode)){
				return new JmMessage(3, "创建失败" ,"验证码输入错误！");
			}
		}else{
			return new JmMessage(3, "创建失败" ,"请先发送验证码！");
		}
		if(!jmRegPhone.equals(vo.getPhoneNumber())){
			return new JmMessage(3, "创建失败" ,"发送的验证码的手机号与注册填写的手机号不匹配！");
		}

		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
		Shop shop = shopService.getShopByAppId(wxUserSession.getAppid());
		ShopUser shopUser = shopUserService.saveShopUser(vo,shop,wxUser);
		if(shopUser!=null){
			wxUser.setShopUserId(shopUser.getId());
			wxUserService.saveUser(wxUser);
			return new JmMessage(0,"保存成功");
		}
		return new JmMessage(1,"保存失败");
	}




	@ApiOperation("修改手机号码页面")
	@RequestMapping(value = "/modifyphone", method = RequestMethod.GET)
	public ModelAndView modifyphone(@ApiParam(hidden=true) HttpServletRequest request){
		String phoneNumber = request.getParameter("phoneNumber");
		ModelAndView view = new ModelAndView();
		request.setAttribute("phoneNumber",phoneNumber);
		view.setViewName("/app/user/modifyphone");
		return view;
	}


	@ApiOperation("修改密码页面")
	@RequestMapping(value = "/modifypwd", method = RequestMethod.GET)
	public ModelAndView modifypwd(@ApiParam(hidden=true) HttpServletRequest request){
		String phoneNumber = request.getParameter("phoneNumber");
		ModelAndView view = new ModelAndView();
		request.setAttribute("phoneNumber",phoneNumber);
		view.setViewName("/app/user/modifypwd");
		return view;
	}

	@ApiOperation("修改用户信息页面")
	@RequestMapping(value = "/modify_user", method = RequestMethod.GET)
	public ModelAndView modifyUser(@ApiParam(hidden=true) HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		WxUser wxuser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
		ShopUser shopUser = shopUserService.findShopUser(wxuser.getShopUserId());
		ShopUserRo ro = ShopUserConverter.toShopUserVo(shopUser);
		request.setAttribute("shopUser", ro);
		view.setViewName("/app/user/modify_user");
		return view;
	}

    @ApiOperation("修改店铺用户")
    @RequestMapping(value = "/update_user", method = RequestMethod.POST)
    public JmMessage updateShopUser(@ApiParam(hidden=true) HttpServletRequest request,
                          @Valid @RequestBody @ApiParam("店铺用户VO") ShopUserVo vo){
        //判断验证码
        String jmcode=(String) request.getSession().getAttribute("jmModifyUserCode");
        String jmRegPhone=(String) request.getSession().getAttribute("jmModifyUserPhone");
        if(null!=jmcode){
            if(!vo.getCode().equals(jmcode)){
                return new JmMessage(3, "创建失败" ,"验证码输入错误！");
            }
        }else{
            return new JmMessage(3, "创建失败" ,"请先发送验证码！");
        }
        if(!jmRegPhone.equals(vo.getPhoneNumber())){
            return new JmMessage(3, "创建失败" ,"发送的验证码的手机号与注册填写的手机号不匹配！");
        }

        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Shop shop = shopService.getShopByAppId(wxUserSession.getAppid());
		WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
        ShopUser shopUser = shopUserService.saveShopUser(vo,shop,wxUser);
        if(shopUser!=null){
            wxUser.setShopUserId(shopUser.getId());
            wxUserService.saveUser(wxUser);
            return new JmMessage(0,"保存成功");
        }
        return new JmMessage(1,"保存失败");
    }



	@ApiOperation("修改手机号码")
	@RequestMapping(value = "/modifyphone", method = RequestMethod.POST)
	public JmMessage createUser(@ApiParam(hidden=true) HttpServletRequest request,
								@Valid @RequestBody @ApiParam("店铺用户VO") ShopUserVo vo){
		//判断验证码
		String jmcode=(String) request.getSession().getAttribute("jmModifyCode");
		String jmRegPhone=(String) request.getSession().getAttribute("jmModifyPhone");
		if(null!=jmcode){
			if(!vo.getCode().equals(jmcode)){
				return new JmMessage(3, "创建失败" ,"验证码输入错误！");
			}
		}else{
			return new JmMessage(3, "创建失败" ,"请先发送验证码！");
		}
		if(!jmRegPhone.equals(vo.getOldPhoneNumber())){
			return new JmMessage(3, "创建失败" ,"发送的验证码的手机号与原手机号不匹配！");
		}
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		if(wxUserSession.getUserId()>0){
			WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
			if(Toolkit.parseObjForInt(wxUser.getShopUserId())>0){
				ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
				Shop shop = shopService.getShopByAppId(wxUserSession.getAppid());
				shopUser = shopUserService.updatePhoneNumber(Toolkit.parseObjForStr(shopUser.getPhoneNumber()),Toolkit.parseObjForStr(vo.getPhoneNumber()),shop.getShopId());
				if(shopUser!=null){
					return new JmMessage(0, "修改成功！");
				}
			}
		}
		return new JmMessage(1, "修改失败！","失败");
	}


	@ApiOperation("修改手机密码")
	@RequestMapping(value = "/modifypwd", method = RequestMethod.POST)
	public JmMessage modifypwd(@ApiParam(hidden=true) HttpServletRequest request,
								@Valid @RequestBody @ApiParam("店铺用户VO") ShopUserVo vo){
		//判断验证码
		String jmcode=(String) request.getSession().getAttribute("jmBackCode");
		String jmRegPhone=(String) request.getSession().getAttribute("jmBackPhone");
		if(null!=jmcode){
			if(!vo.getCode().equals(jmcode)){
				return new JmMessage(3, "创建失败" ,"验证码输入错误！");
			}
		}else{
			return new JmMessage(3, "创建失败" ,"请先发送验证码！");
		}
		if(!jmRegPhone.equals(vo.getPhoneNumber())){
			return new JmMessage(3, "创建失败" ,"发送的验证码的手机号不匹配！");
		}
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		if(wxUserSession.getUserId()>0){
			WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
			if(Toolkit.parseObjForInt(wxUser.getShopUserId())>0){
				ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
				Shop shop = shopService.getShopByAppId(wxUserSession.getAppid());
				shopUser = shopUserService.updatePwd(Toolkit.parseObjForStr(shopUser.getPhoneNumber()),Toolkit.parseObjForStr(vo.getPassword()),shop.getShopId());
				if(shopUser!=null){
					return new JmMessage(0, "修改成功！");
				}
			}
		}
		return new JmMessage(1, "修改失败！","失败");
	}



}
