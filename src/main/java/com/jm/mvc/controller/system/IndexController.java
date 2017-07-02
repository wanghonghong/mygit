package com.jm.mvc.controller.system;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.RoleService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.mvc.vo.JmUserSession;
import com.jm.staticcode.constant.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * <p>聚米首页</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */

@Api
@RestController
@RequestMapping(value = "/")
public class IndexController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ShopService shopService;
    
    
    @ApiOperation("首页过滤")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView indexfi(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
    	ModelAndView view = new ModelAndView();
    	if(user==null){
    		view.setViewName("/pc/index/index");
    		return view;
		}else{
			 return new ModelAndView("redirect:/shop");
		}
    }

    
    @ApiOperation("官方首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@ApiParam(hidden=true) HttpServletRequest request){
    	ModelAndView view = new ModelAndView();
		view.setViewName("/pc/index/index");
		return view;
    }
    
}
