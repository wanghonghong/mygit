package com.jm.mvc.controller.wb;

import com.jm.business.service.wb.WbShopUserService;
import com.jm.business.service.wb.WbUserGroupService;
import com.jm.business.service.wb.WbUserRelService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.wb.WbUserGroupVo;
import com.jm.mvc.vo.wx.group.WxGroupVo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.jpa.wb.WbUserGroupRepository;
import com.jm.repository.po.wb.WbShopUser;
import com.jm.repository.po.wb.WbUserGroup;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserGroup;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<p>用户分组控制器</p>
 */
@Api
@RestController
@RequestMapping(value = "/wbUserGroup")
public class WbUserGroupController {
	
	@Autowired
	private WbUserGroupService wbUserGroupService;
	@Autowired
	private WbUserRelService wbUserRelService;
	@Autowired
	private WbShopUserService wbShopUserService;

	/**
	 * 获取微博用户分组
	 */
	@RequestMapping(value="/groups",method = RequestMethod.GET)
	public List<WbUserGroup> getGroups(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Long uid = jmUser.getUid();
		List<WbUserGroup> list = wbUserGroupService.findAllWbUserGroup(uid);
		return list;
	}

	/**
	 * 用户移动分组
	 */
	@RequestMapping(value="group/move",method = RequestMethod.PUT)
	public JmMessage moveGroup(@ApiParam(hidden=true) HttpServletRequest request,
							   @ApiParam("微博分组Vo") @RequestBody WbUserGroupVo group) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Long uid = jmUser.getUid();

		WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(uid);
		if(wbShopUser!=null){
			//移动微博下的分组
			return wbUserGroupService.moveUserGroup(wbShopUser.getAccessToken(),group.getRelId(),group.getGroupid(),uid);
		}else{
			return new JmMessage(1,"修改失败");
		}
	}

	/**
	 * 创建分组
	 */
	@RequestMapping(value="/group",method = RequestMethod.POST)
	public  JmMessage createGroup(@ApiParam(hidden=true) HttpServletRequest request,
									@ApiParam("分组名称") @RequestBody WbUserGroupVo group) throws Exception {
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		if(group.getName()!=null && !group.getName().equals("")){
			WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(jmUser.getUid());
			if(wbShopUser!=null){
				WbUserGroup wbUserGroup = wbUserGroupService.createWbGroup(wbShopUser.getAccessToken(),group.getName(),jmUser.getUid());
				if(wbUserGroup!=null){
					if(group.getRelId()!=null){
						WbUserRel wbUserRel = wbUserRelService.getWbUserRel(group.getRelId());
						wbUserRel.setGroupid(wbUserGroup.getId());
						wbUserRelService.save(wbUserRel);
					}
					return new JmMessage(0,"创建成功");
				}
			}
		}
		return new JmMessage(1,"创建失败");
	}

	/**
	 * @author chenyy
	 * 删除分组
	 * @throws Exception
	 */
	@RequestMapping(value="/group/{groupId}",method = RequestMethod.DELETE)
	public JmMessage deleteGroup(@ApiParam(hidden=true) HttpServletRequest request,
								 @ApiParam("分组id") @PathVariable("groupId") Long groupId) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(jmUser.getUid());
		if(wbShopUser!=null) {
			return wbUserGroupService.deleteGroup(wbShopUser.getAccessToken(),groupId,jmUser.getUid());
		}else{
			return new JmMessage(1,"删除失败！");
		}
	}

	/**
	 * 修改分组
	 */
	@RequestMapping(value="/group",method = RequestMethod.PUT)
	public JmMessage updateGroup(@ApiParam(hidden=true) HttpServletRequest request,
								   @ApiParam("分组Vo") @RequestBody WbUserGroupVo group) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WbShopUser wbShopUser = wbShopUserService.getWbShopUserByUid(jmUser.getUid());
		if(wbShopUser!=null) {
			return wbUserGroupService.updateGroup(wbShopUser.getAccessToken(),group.getGroupid(),group.getName(),jmUser.getUid());
		}else{
			return new JmMessage(1,"修改失败");
		}
	}


	/**
	 * 修改用户备注
	 */
	@RequestMapping(value="/user_remark",method = RequestMethod.PUT)
	public JmMessage updateRemark(@ApiParam(hidden=true) HttpServletRequest request,
								  @ApiParam("微博用户Vo") @RequestBody WbUserGroupVo wxUserVo) throws Exception{
		JmUserSession jmUser =(JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		WbUserRel wbUserRel = wbUserRelService.getWbUserRel(wxUserVo.getRelId());
		if(jmUser.getUid().equals(wbUserRel.getPid())){
			wbUserRel.setRemark(wxUserVo.getRemark());
			wbUserRelService.save(wbUserRel);
			return new JmMessage(0,"修改完成");
		}
		return new JmMessage(1,"修改失败");
	}


}
