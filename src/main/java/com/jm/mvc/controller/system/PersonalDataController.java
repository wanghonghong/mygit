package com.jm.mvc.controller.system;

import com.jm.business.service.system.*;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.join.JoinStatusVo;
import com.jm.mvc.vo.join.JoinVo;
import com.jm.mvc.vo.system.LeftMenuVo;
import com.jm.mvc.vo.system.UserForQueryVo;
import com.jm.repository.po.system.*;
import com.jm.repository.po.system.user.JmResource;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * <p>个人资料菜单</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/25
 */

@Api
@RestController
@RequestMapping(value = "/")
public class PersonalDataController {

    @Autowired
    private UserService userService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JmJoinService jmJoinService;

    @ApiOperation("首页")
    @RequestMapping(value = "/jm_index", method = RequestMethod.GET)
    public ModelAndView jmIndex(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        //取出顶部菜单
        List<JmResource> rsls= systemService.findByParentResourceIdAndStatus(0,0);
        //取出左菜单
        List<LeftMenuVo> leftMenus= systemService.findLeftMenu(1,0);
        request.setAttribute("rsls", rsls);
        request.setAttribute("leftMenus", leftMenus);
        view.setViewName("/pc/personalData/index");
        return view;
    }

    @RequestMapping(value = "/user_data", method = RequestMethod.GET)
    public JoinStatusVo getJoinVo(@ApiParam(hidden = true) HttpServletRequest request){
        JmUserSession jmuser= (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        JmJoin agent = jmJoinService.findJmJoin(jmuser.getUserId(),1);
        JmJoin service = jmJoinService.findJmJoin(jmuser.getUserId(),2);
        JoinStatusVo joinStatusVo = new JoinStatusVo();
        joinStatusVo.setUserId(jmuser.getUserId());
        if(agent==null){
            joinStatusVo.setSoftwareStatus(0);
        }else {
            joinStatusVo.setSoftwareStatus(agent.getStatus());
            joinStatusVo.setSoftwareType(agent.getSubType());
        }
        if(service==null){
            joinStatusVo.setOperationStatus(0);
        }else {
            joinStatusVo.setOperationStatus(service.getStatus());
            joinStatusVo.setOperationType(service.getSubType());
        }
        return joinStatusVo;
    }

    @RequestMapping(value = "/basic_data", method = RequestMethod.GET)
    public UserForQueryVo userForUpdateVo(@ApiParam(hidden = true) HttpServletRequest request){
        JmUserSession jmuser= (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return userService.getUserQueryVo(jmuser.getUserId());
    }

    @RequestMapping(value = "/join/{type}", method = RequestMethod.GET)
    public JoinVo getJoinVo(@ApiParam(hidden = true) @PathVariable("type") Integer type,
                          @ApiParam(hidden = true) HttpServletRequest request){
        JmUserSession jmuser= (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return jmJoinService.getJoinVoForData(jmuser.getUserId(),type);
    }

    @ApiOperation("账户余额页面")
    @RequestMapping(value = "/account_balance", method = RequestMethod.GET)
    public ModelAndView index2(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/personalData/accountBalance");
        return view;
    }

    @ApiOperation("米币市场页面")
    @RequestMapping(value = "/mb_market", method = RequestMethod.GET)
    public ModelAndView index3(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/personalData/mbMarket");
        return view;
    }

    @ApiOperation("收入记录页面")
    @RequestMapping(value = "/income_record", method = RequestMethod.GET)
    public ModelAndView index4(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/personalData/incomeRecord");
        return view;
    }

    @ApiOperation("支出记录页面")
    @RequestMapping(value = "/pay_record", method = RequestMethod.GET)
    public ModelAndView index5(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/personalData/payRecord");
        return view;
    }

    @ApiOperation("欠款记录页面")
    @RequestMapping(value = "/debt_record", method = RequestMethod.GET)
    public ModelAndView index6(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/personalData/debtRecord");
        return view;
    }
}
