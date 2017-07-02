package com.jm.mvc.controller.wx;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jm.business.service.wb.WbUserRelService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.wb.WbUserLevelUo;
import com.jm.repository.jpa.wx.WxUserRepository;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jm.business.service.wx.WxUserLevelService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.wx.level.WxUserLevelCo;
import com.jm.mvc.vo.wx.level.WxUserLevelUo;
import com.jm.mvc.vo.wx.level.WxUserLevelVo;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserLevel;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.wx.WxUserLevelConverter;

/**
 * 微信会员等级
 * @author chenyy
 *
 */
@Api
@RestController
@RequestMapping(value = "/wx")
public class WxUserLevelController {
	
	@Autowired
	private WxUserLevelService wxUserLevelService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxUserRepository wxUserRepository;
	@Autowired
	private WbUserRelService wbUserRelService;
	
	/**
	 * 根据appid获取所有等级
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/level",method = RequestMethod.GET)
	public List<WxUserLevelVo> getLevelList(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(appid);
		List<WxUserLevelVo> levelVos = WxUserLevelConverter.listToWxUserLevelVo(levels);
		return levelVos;
	}
	
	/**
	 * 新增等级
	 * @param request JmMessage
	 * @param wxUserLevelCo
	 * @return
	 */
	@RequestMapping(value="/level",method = RequestMethod.POST)
	public JmMessage createLevel(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("等级Co") @RequestBody WxUserLevelCo wxUserLevelCo){
		
		WxUserLevel wxUserLevel = WxUserLevelConverter.coToWxUserLevel(wxUserLevelCo);
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		wxUserLevel.setAppid(appid);
		wxUserLevelService.saveLevel(wxUserLevel);
		return new JmMessage(0, "ok");
		
	}
	
	/**
	 * 修改等级
	 * @param request JmMessage
	 * @param wxUserLevelUo 等级的id与等级名称
	 * @return
	 */
	@RequestMapping(value="/level",method = RequestMethod.PUT)
	public JmMessage updateLevel(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("等级Uo") @RequestBody WxUserLevelUo wxUserLevelUo){
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		WxUserLevel returnLevel =  wxUserLevelService.findWxUserLevelById(wxUserLevelUo.getId());
		//为空或者与session的appid不相等
		if(returnLevel!=null && appid.equals(returnLevel.getAppid())){
			returnLevel.setLevelName(wxUserLevelUo.getLenvelName());
			wxUserLevelService.saveLevel(returnLevel);
			return new JmMessage(0, "ok");
		}else{
			return new JmMessage(-1, "信息不存在或无权操作");
		}
	}
	/**
	 * 删除
	 * @param request
	 * @param id 等级id
	 * @return
	 */
	@RequestMapping(value="/level/{id}",method = RequestMethod.DELETE)
	public JmMessage deleteLevel(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("id") @PathVariable("id") Integer id){
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		WxUserLevel returnLevel =  wxUserLevelService.findWxUserLevelById(id);
		if(returnLevel!=null && appid.equals(returnLevel.getAppid())){
			wxUserLevelService.deleteLevel(id);
			return new JmMessage(0, "ok");
		}else{
			return new JmMessage(-1, "信息不存在或无权操作");
		}
		
	}
	/**
	 * 移动用户等级分组
	 * @param wxUserLevelUo
	 * @return
	 */
	@RequestMapping(value="/level/move_user",method = RequestMethod.PUT)
	public JmMessage moveUser(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("等级Uo") @RequestBody WxUserLevelUo wxUserLevelUo){
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		
		WxUserLevel wxUserLevel = wxUserLevelService.findWxUserLevelById(wxUserLevelUo.getId());
		if(wxUserLevel==null){
			return new JmMessage(-1, "分组信息不存在");
		}

		if(wxUserLevelUo.getWxUserId()!=null){
			WxUser wxUser = wxUserService.findWxUserByUserId(wxUserLevelUo.getWxUserId());
			if(wxUser!=null && appid.equals(wxUser.getAppid())){
				wxUser.setLevelId(wxUserLevelUo.getId());
				wxUserService.saveUser(wxUser);
				return new JmMessage(0, "ok");
			}else{
				return new JmMessage(-1, "用户不存在或无权操作");
			}
		}

		if(wxUserLevelUo.getWxUserIds()!=null && !wxUserLevelUo.getWxUserIds().equals("")){
			String userIds[] = wxUserLevelUo.getWxUserIds().split(",");
			List<Integer> ids = new ArrayList<>();
			for (String id:userIds){
				ids.add(Toolkit.parseObjForInt(id));
			}
			List<WxUser> wxusers = wxUserService.findWxUserByUserIds(ids);
			List<WxUser>  newUsers = new ArrayList<>();
			for (WxUser user:wxusers) {
				user.setLevelId(wxUserLevelUo.getId());
				newUsers.add(user);
			}
			wxUserRepository.save(newUsers);
			return new JmMessage(0, "ok");
		}
		return new JmMessage(-1, "操作错误");
	}




	/**
	 * 移动微博用户等级分组
	 */
	@RequestMapping(value="/level/move_wb_user",method = RequestMethod.PUT)
	public JmMessage moveWbUser(@ApiParam(hidden=true) HttpServletRequest request,
							  @ApiParam("等级Uo") @RequestBody WbUserLevelUo wbUserLevelUo){
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WxUserLevel wxUserLevel = wxUserLevelService.findWxUserLevelById(wbUserLevelUo.getId());
		if(wxUserLevel==null){
			return new JmMessage(-1, "分组信息不存在");
		}

		if(wbUserLevelUo.getWbUserRelId()!=null){
			WbUserRel wbUserRel = wbUserRelService.getWbUserRel(wbUserLevelUo.getWbUserRelId());
			if(wbUserRel!=null && jmUser.getUid().equals(wbUserRel.getPid())){
				wbUserRel.setLevelId(wbUserLevelUo.getId());
				wbUserRelService.save(wbUserRel);
				return new JmMessage(0, "ok");
			}else{
				return new JmMessage(-1, "用户不存在或无权操作");
			}
		}

		if(wbUserLevelUo.getWbUserRelIds()!=null && !wbUserLevelUo.getWbUserRelIds().equals("")){
			String userIds[] = wbUserLevelUo.getWbUserRelIds().split(",");
			List<Long> ids = new ArrayList<>();
			for (String id:userIds){
				ids.add(Toolkit.obj2Long(id));
			}
			List<WbUserRel>  rels = wbUserRelService.findByIds(ids);
			List<WbUserRel>  newRels = new ArrayList<>();
			for (WbUserRel rel:rels) {
				rel.setLevelId(wbUserLevelUo.getId());
				newRels.add(rel);
			}
			wbUserRelService.save(newRels);
			return new JmMessage(0, "ok");
		}
		return new JmMessage(-1, "操作错误");
	}

}
