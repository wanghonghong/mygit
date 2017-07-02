package com.jm.mvc.controller.zb;

import com.jm.business.service.zb.ZbRoleService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbRoleType;
import com.jm.staticcode.constant.ZbConstant;
import com.jm.staticcode.converter.zb.RoleConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <p>角色</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@RestController
@RequestMapping(value = "/zb")
public class RoleController {

    @Autowired
    private ZbRoleService zbRoleService;

    @ApiOperation("角色管理页面")
    @RequestMapping(value = "/role_manage", method = RequestMethod.GET)
    public ModelAndView roleManager(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response){
        ModelAndView view = new ModelAndView();
        view.setViewName("/system/roleManage");
        return view;
    }

    @ApiOperation("角色新增")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public JmMessage addRole(@ApiParam(hidden=true) HttpServletRequest request,
                             @RequestBody @Valid RoleCo roleCo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        ZbRole zbRole = RoleConverter.toRole(roleCo);
        zbRole.setUserId(user.getUserId());
        zbRoleService.addRole(zbRole);
        return new JmMessage(0,"角色新增成功!");
    }

    @ApiOperation("角色批量删除")
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    public JmMessage deleteRoles(@ApiParam("角色标识")  @RequestBody @Valid List<Integer> ids){
        zbRoleService.deleteRoles(ids);
        return new JmMessage(0,"删除成功！");
    }

    @ApiOperation("角色查询")
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public List<ZbRole> queryRole(){
        return zbRoleService.queryRole();
    }

    @ApiOperation("角色分页查询")
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public PageItem<RoleVo> queryRoles(@RequestBody @Valid RoleQo roleQo) throws IOException{
        PageItem<RoleVo> roleVoPageItem = zbRoleService.queryRoles(roleQo);
        return roleVoPageItem;
    }

    @ApiOperation("角色更新")
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    public JmMessage updateRole(@RequestBody RoleUo roleUo){
        zbRoleService.update(roleUo);
        return new JmMessage(0,"修改成功");
    }

    @ApiOperation("角色查询")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public RoleVo updateRole(@ApiParam("角色id") @PathVariable("id") int id){
        return zbRoleService.queryRoleById(id);
    }

    @ApiOperation("新增角色类型")
    @RequestMapping(value = "/role_type", method = RequestMethod.POST)
    public JmMessage addRoleType(@ApiParam(hidden=true) HttpServletRequest request,
                                 @RequestBody @Valid RoleTypeCo roleTypeCo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        ZbRoleType zbRoleType = RoleConverter.toRoleType(roleTypeCo);
        zbRoleType.setUserId(user.getUserId());
        zbRoleService.addRoleType(zbRoleType);
        return new JmMessage(0,"角色类型新增成功!");
    }

    @ApiOperation("角色类型更新")
    @RequestMapping(value = "/role_type", method = RequestMethod.PUT)
    public JmMessage updateRoleType(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody RoleTypeUo typeUo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        typeUo.setUserId(user.getUserId());
        zbRoleService.updateType(typeUo);
        return new JmMessage(0,"修改成功");
    }

    @ApiOperation("角色类型分页查询")
    @RequestMapping(value = "/role_types", method = RequestMethod.POST)
    public PageItem<RoleTypeVo> queryRoleTypes(@RequestBody @Valid RoleTypeQo roleTypeQo)throws Exception{
        PageItem<RoleTypeVo> roleVoPageItem = zbRoleService.queryRoleTypes(roleTypeQo);
        return roleVoPageItem;
    }

    @ApiOperation("角色类型删除")
    @RequestMapping(value = "/role_type/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteRoleTypes(@ApiParam("角色类型id") @PathVariable("id") int id){
        return zbRoleService.deleteRoleType(id);
    }

    @ApiOperation("角色类型查询")
    @RequestMapping(value = "/role_type", method = RequestMethod.GET)
    public List<ZbRoleType> getRoleTypes(){
        return zbRoleService.getRoleType();
    }

}
