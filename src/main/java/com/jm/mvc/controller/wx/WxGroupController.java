package com.jm.mvc.controller.wx;

import com.jm.business.service.wx.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.jm.business.service.wx.WxGroupService;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.wx.group.WxGroupVo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserGroup;
import com.jm.staticcode.constant.Constant;

/**
 *<p>用户分组控制器</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年7月29日
 */
@Api
@RestController
@RequestMapping(value = "/wx")
public class WxGroupController {
	
	@Autowired
	private WxGroupService wxGroupService;
	@Autowired
	private WxUserService wxUserService;

 
	
	/**
	 * @author chenyy
	 * 获取微信用户分组
	 * @throws Exception 
	 */
	@RequestMapping(value="/groups",method = RequestMethod.GET)
	public List<WxUserGroup> getGroups(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		List<WxUserGroup> list = wxGroupService.findAllGroup(appid);
		return list;
	}
	/**
	 * @author chenyy
	 * 创建分组
	 * @throws Exception 
	 */
	@RequestMapping(value="/group",method = RequestMethod.POST)
	public  WxUserGroup createGroup(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("分组名称") @RequestBody WxGroupVo group) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> groupMap = new HashMap<>();
		map.put("name", group.getName());
		groupMap.put("group", map);
		//创建微信上的分组
		WxGroupVo groupVo = wxGroupService.createWxGroup(appid, groupMap);
		Integer groupId = groupVo.getId();
		//拿到微信上的分组id，同步本地数据库 创建分组
		WxUserGroup wxUserGroup=wxGroupService.createGroup(appid, group.getName(), groupId);

		if(group.getUserId()!=null && wxUserGroup!=null){
			WxUser wxUser = wxUserService.findWxUserByUserId(group.getUserId());
			Map<String,Object> map1 = new HashMap<>();
			map1.put("openid", wxUser.getOpenid());
			map1.put("to_groupid", wxUserGroup.getGroupid());
			wxGroupService.moveWxGroup(appid, map1);//修改微信数据
			//修改本地数据
            group.setGroupid(wxUserGroup.getGroupid());
			wxGroupService.moveGroup(appid, group);
		}

		//批量移动分组
		if(group.getUserIds()!=null && !group.getUserIds().equals("")&& wxUserGroup!=null){
			wxGroupService.plMoveGroup(group.getUserIds(),appid,wxUserGroup.getGroupid());
		}

		return wxUserGroup;
	}

	/**
	 * @author chenyy
	 * 修改分组
	 * @throws Exception 
	 */
	@RequestMapping(value="/group",method = RequestMethod.PUT)
	public WxUserGroup updateGroup(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("分组Vo") @RequestBody WxGroupVo group) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> groupMap = new HashMap<>();
		map.put("id", group.getGroupid());
		map.put("name", group.getName());
		groupMap.put("group", map);
		wxGroupService.updateWxGroup(appid, groupMap);//修改微信分组
		return wxGroupService.updateGroup(appid, group.getName(), group.getGroupid());//修改本地数据库分组
	}
	
	/**
	 * @author chenyy
	 * 删除分组
	 * @throws Exception 
	 */
	@RequestMapping(value="/group/{groupId}",method = RequestMethod.DELETE)
	public ResultMsg deleteGroup(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("分组id") @PathVariable("groupId") Integer groupId) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> groupMap = new HashMap<>();
		map.put("id", groupId);
		groupMap.put("group", map);
		wxGroupService.deleteWxGroup(appid, groupMap);
		wxGroupService.deleteGroupByGroupId(appid, groupId);
		ResultMsg sss = new ResultMsg();
		sss.setErrcode(0);
		sss.setErrmsg("ok");
		return sss;
	}
	
	/**
	 * @author chenyy
	 * 用户移动分组
	 * @throws Exception 
	 */
	@RequestMapping(value="group/move",method = RequestMethod.PUT)
	public ResultMsg moveGroup(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("微信分组Vo") @RequestBody WxGroupVo group) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();

		if(group.getUserId()!=null){
			WxUser wxUser = wxUserService.findWxUserByUserId(group.getUserId());
			String openid = wxUser.getOpenid();
			Map<String,Object> map = new HashMap<>();
			map.put("openid", openid);
			map.put("to_groupid", group.getGroupid());
			wxGroupService.moveWxGroup(appid, map);//修改微信数据
			//修改本地数据
			wxGroupService.moveGroup(appid, group);
		}

		if(group.getUserIds()!=null && !group.getUserIds().equals("")){
            wxGroupService.plMoveGroup(group.getUserIds(),appid,group.getGroupid());//用户批量移动分组
		}

		ResultMsg sss = new ResultMsg();
		sss.setErrcode(0);
		sss.setErrmsg("ok");
		return sss;
		
	}
/*	*//**
	 * @author chenyy
	 * 批量移动分组
	 * @throws Exception 
	 *//*
	@RequestMapping(value="wx/group/batch_move",method = RequestMethod.PUT)
	public ResultMsg batchMoveGroup(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("userIds")  @PathVariable("userIds") String userIds,
			@ApiParam("目标分组id")@PathVariable("toGroupId") Integer toGroupId) throws Exception{
		
			String ids []= userIds.split(",");
			List<Integer> idList = new ArrayList<>();
			JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			String appid = jmUser.getAppId();
		for (int i = 0; i < ids.length; i++) {
			idList.add(Integer.parseInt(ids[i]));
		}
		List<WxUser> wxUsers = wxUserService.getWxUsers(idList);
		List<String> openidList = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		for (int i = 0; i < wxUsers.size(); i++) {
			openidList.add(wxUsers.get(i).getOpenid());
		}
		map.put("openid_list", openidList);
		map.put("to_groupid", toGroupId);
		return wxGroupService.batchMoveWxGroup(appid, map);
	}*/
	
	
	/**
	 * @author chenyy
	 * 修改用户备注
	 * @throws Exception 
	 */
	@RequestMapping(value="/user_remark",method = RequestMethod.PUT)
	public ResultMsg updateRemark(@ApiParam(hidden=true) HttpServletRequest request,
			@ApiParam("微信用户Vo") @RequestBody WxUserVo wxUserVo) throws Exception{
		WxUser wxUser = wxUserService.findWxUserByUserId(wxUserVo.getUserId());
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String appid = jmUser.getAppId();
		String openid = wxUser.getOpenid();
		Map<String,Object> map = new HashMap<>();
		map.put("openid", openid);
		map.put("remark", wxUserVo.getRemark());
		wxUser.setRemark(wxUserVo.getRemark());
		wxUserService.saveUser(wxUser);
		return wxGroupService.updateWxMark(appid, map);
	}
	
	

}
