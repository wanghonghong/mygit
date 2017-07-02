package com.jm.mvc.controller.system;
import com.google.code.kaptcha.Producer;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.mvc.vo.HxUserVo;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.UserQueryVo;
import com.jm.mvc.vo.system.*;
import com.jm.repository.po.system.user.User;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.UserConverter;
import com.jm.staticcode.converter.system.HxUserConverter;
import com.jm.staticcode.util.BaseUtil;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.SMSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;


/**
 * <p>系统管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */

@Api
@RestController
@RequestMapping(value = "/")
public class SystemController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private Producer captchaProducer;


	@ApiOperation("用户创建")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public JmMessage createUser(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("用户创建VO") @RequestBody @Valid UserCo userVo){
		//判断验证码
		String jmcode=(String) request.getSession().getAttribute("jmRegCode");
		String jmRegPhone=(String) request.getSession().getAttribute("jmRegPhone");
		if(null!=jmcode){
			if(!userVo.getCode().equals(jmcode)){
				return new JmMessage(3, "创建失败" ,"验证码输入错误！");
			}
		}else{
			return new JmMessage(3, "创建失败" ,"请先发送验证码！");
		}
		if(!jmRegPhone.equals(userVo.getPhoneNumber())){
			return new JmMessage(3, "创建失败" ,"发送的验证码的手机号与注册填写的手机号不匹配！");
		}
		User user1 = userService.findUserByPhoneNumber(userVo.getPhoneNumber());
		if(user1==null){
			User user = userService.saveUser(UserConverter.toUser(userVo));
			if (user == null) {
				return new JmMessage(1, "创建失败！","系统内部错误！");
			}else{
				request.getSession().setAttribute(Constant.SESSION_USER, UserConverter.toUserSession(user));
				return new JmMessage(0, "创建成功！");
			}
		}else{
			return new JmMessage(2, "创建失败！","手机号已被注册！");
		}
	}

	@ApiOperation("发送短信")
	@RequestMapping(value = "/sendmsg/{status}", method = RequestMethod.POST)
	public JmMessage sendmsg(@ApiParam("发送短信状态") @PathVariable("status") Integer status,
							 @ApiParam(hidden=true) HttpServletRequest request,
							 @ApiParam("用户更新") @RequestBody @Valid SendMsgCo userVo){
		String sixCode=SMSUtil.getSixCode();
		String state="0";
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3*60);//3分钟失效
		if(status.equals(0)){
			state = SMSUtil.sendMsg("【聚米为谷】注册验证码为"+sixCode, userVo.getPhoneNumber());
            session.setAttribute("jmRegCode", sixCode);
            session.setAttribute("jmRegPhone", userVo.getPhoneNumber());
		}
		if(status.equals(1)){
			state = SMSUtil.sendMsg("【聚米为谷】修改密码验证码为"+sixCode, userVo.getPhoneNumber());
            session.setAttribute("jmBackCode", sixCode);
            session.setAttribute("jmBackPhone", userVo.getPhoneNumber());
		}
		if(status.equals(2)){
			state = SMSUtil.sendMsg("【聚米为谷】修改手机号码验证码为"+sixCode, userVo.getPhoneNumber());
            session.setAttribute("jmModifyCode", sixCode);
            session.setAttribute("jmModifyPhone", userVo.getPhoneNumber());
		}
		if(status.equals(3)){
			state = SMSUtil.sendMsg("【聚米为谷】修改用户信息验证码为"+sixCode, userVo.getPhoneNumber());
            session.setAttribute("jmModifyUserCode", sixCode);
            session.setAttribute("jmModifyUserPhone", userVo.getPhoneNumber());
		}
		if(state.equals("1")){
			return new JmMessage(0, "发送成功");
		}else{
			return new JmMessage(1, "发送失败","发送失败");
		}
	}

    @ApiOperation("用户修改")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User updateUser(@ApiParam(hidden=true) HttpServletRequest request,
                           @ApiParam("用户修改VO") @RequestBody @Valid JmUserUo userVo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		userVo.setUserId(user.getUserId());
        return userService.updateUser(userVo);
    }

    @ApiOperation("用户获取")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUser(@ApiParam("用户标识") @PathVariable("userId") Integer userId){
        return userService.getUser(userId);
    }

	@ApiOperation("用户查询")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Page<User> queryUser(UserQueryVo userQueryVo){
		return userService.queryUser(0,userQueryVo);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public JmMessage login(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response,
						   @ApiParam("登录信息") @RequestBody @Valid LoginVo loginVo) {
        String code = (String) request.getSession().getAttribute(Constant.SESSION_LOGIN_CODE);
		if(code == null ){
			return new JmMessage(1, "验证码已过时,请刷新验证码" ,"验证码已过时,请刷新验证码");
		}else{
			if(!code.equalsIgnoreCase(loginVo.getCode())){
				return new JmMessage(1, "验证码错误" ,"登录失败");
			}
		}
		User user = userService.findUser(loginVo);
		if (user == null) {
			return new JmMessage(1, "用户名或密码不正确","登录失败");
		} else {
			request.getSession().setAttribute(Constant.SESSION_USER, UserConverter.toUserSession(user));
			return new JmMessage(0, "登录成功");
		}
	}

	@ApiOperation("注册页")
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView index(@ApiParam(hidden=true) HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		String path = BaseUtil.getPath(request);
		request.setAttribute("basePath", path);
		view.setViewName("/pc/system/register");
		return view;
	}




	@RequestMapping(value = "/loginout", method = RequestMethod.GET)
	public ModelAndView loginout(@ApiParam(hidden=true) HttpServletRequest request) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Equalizer.logout(user.getHxAccount(),user.getShopId());
		request.getSession().removeAttribute(Constant.SESSION_USER);
		ModelAndView view = new ModelAndView();
		String phoneNumber="";
		Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
		if(cookies!=null&&cookies.length>0){
			for(Cookie cookie : cookies){
				if(null!=cookie){
					if( cookie.getName().equals("phoneNumber")){
						phoneNumber=cookie.getValue();
					}
				}
			}
		}
		request.setAttribute("phoneNumber", phoneNumber);
		view.setViewName("/pc/system/new_login");
		return view;
	}

	@RequestMapping(value = "/code", method = RequestMethod.GET)
	public ModelAndView code () {
		ModelAndView view = new ModelAndView();
		view.setViewName("/pc/code");
		return view;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response){
		ModelAndView view = new ModelAndView();
		request.setAttribute("phoneNumber", Toolkit.getCookie(request,"phoneNumber"));
		if (Constant.PLAT_FORM.equals("0")){
			view.setViewName("/pc/system/new_login");
		}else {
			view.setViewName("/pc/zb/system/login");
		}
		return view;
	}


	@RequestMapping(value = "/system/backPwd", method = RequestMethod.GET)
	public ModelAndView backPwd(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam(hidden=true) HttpServletResponse response){
		ModelAndView view = new ModelAndView();
		view.setViewName("/pc/system/backPwd");
		return view;
	}


	@ApiOperation("修改密码")
	@RequestMapping(value = "/system/backPwd", method = RequestMethod.POST)
	public JmMessage backPwd(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("用户更新VO") @RequestBody @Valid UserForUpdateVo userVo){
		//判断验证码
		String jmcode=(String) request.getSession().getAttribute("jmBackCode");
		String jmBackPhone=(String) request.getSession().getAttribute("jmBackPhone");

		if(null!=jmcode){
			if(!userVo.getCode().equals(jmcode)){
				return new JmMessage(1, "失败" ,"验证码输入错误！");
			}
		}else{
			return new JmMessage(1, "失败" ,"请先发送验证码！");
		}

		if(!jmBackPhone.equals(userVo.getPhoneNumber())){
			return new JmMessage(3, "创建失败" ,"发送的验证码的手机号与注册填写的手机号不匹配！");
		}

		User user1 = userService.findUserByPhoneNumber(userVo.getPhoneNumber());
		if(user1==null){
			return new JmMessage(1, "失败！","当前手机号未注册！");
		}else{
			user1.setPassword(userVo.getPassword());
			User user = userService.saveUser(user1);
			if(user!=null){
				request.getSession().setAttribute(Constant.SESSION_USER, UserConverter.toUserSession(user));
				return new JmMessage(0, "成功","修改成功！");
			}else{
				return new JmMessage(-1, "失败","系统内部错误！");
			}
		}
	}



	@RequestMapping(value = "/system/session", method = RequestMethod.GET)
	public HxUserVo userSession(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		HxUserVo hxUserVo;
		userRoleService.initShopUserHxAcct(user);
		hxUserVo = HxUserConverter.toHxUser(user);
		//userRoleService.findShopMasterByShopId(user.getShopId());
		return hxUserVo;
	}


    /**
     * 验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/captcha-image")
	public ModelAndView getKaptchaImage(HttpServletRequest request,
										HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();
		try {
			/*Cookie cookie = new Cookie("captchaCode",capText);
            response.addCookie(cookie);*/
            request.getSession().setAttribute(Constant.SESSION_LOGIN_CODE, capText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}

}
