package com.jm.mvc.controller.zb;

import com.jm.business.domain.ZtreeNode;
import com.jm.business.service.shop.shopSet.UserCenterVersionService;
import com.jm.business.service.system.ResourceService;
import com.jm.business.service.system.RoleService;
import com.jm.business.service.zb.ZbSoftMenuService;
import com.jm.business.service.zb.ZbSoftService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.jpa.resource.ResourceRepository;
import com.jm.repository.jpa.resource.RoleResourceRepository;
import com.jm.repository.jpa.shop.shopSetting.UserCenterFunsRepository;
import com.jm.repository.jpa.system.RoleRepository;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import com.jm.repository.po.shop.shopSet.UserCenterVersion;
import com.jm.repository.po.system.user.Resource;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.system.user.RoleResource;
import com.jm.repository.po.zb.system.ZbSoft;
import com.jm.repository.po.zb.system.ZbSoftMenu;
import com.jm.staticcode.converter.zb.ZbSoftConverter;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>软件版本</p>
 */
@RestController
@RequestMapping(value = "/zbSoft")
public class ZbSoftController {

    @Autowired
    private ZbSoftService zbSoftService;
    @Autowired
    private ZbSoftMenuService zbSoftMenuService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceRepository roleResourceRepository;
    @Autowired
    private UserCenterFunsRepository userCenterFunsRepository;
    @Autowired
    private UserCenterVersionService userCenterVersionService;

    @ApiOperation("删除")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JmMessage delete(@PathVariable("id") Integer id)throws Exception{
        zbSoftService.delete(id);
        return new JmMessage(0,"操作成功");
    }

    @ApiOperation("根据编号获取")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ZbSoft getSoft(@PathVariable("id") Integer id)throws Exception{
        return zbSoftService.getZbSoft(id);
    }

    @ApiOperation("保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JmMessage save(@RequestBody ZbSoftVo vo)throws Exception{
        zbSoftService.save(ZbSoftConverter.v2p(vo));
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public PageItem<ZbSoft> list(@RequestBody ZbSoftQo qo)throws Exception{
        PageItem<ZbSoft> zbSofts =  zbSoftService.findZbSoft(qo);
        return zbSofts;
    }

    @ApiOperation("菜单查询")
    @RequestMapping(value = "/res/{status}", method = RequestMethod.GET)
    public List<ZtreeNode> ress(@ApiParam("状态") @PathVariable("status") Integer status) {
        List<Resource> resourceList = resourceService.getResource(status);
        List<ZtreeNode> ztreeNodes = new ArrayList<>();
        for (Resource resource:resourceList) {
            ZtreeNode z = new ZtreeNode();
            z.setId(resource.getResourceId());
            z.setPId(resource.getParentRsourceId());
            z.setName(resource.getResourceName());
            z.setOpen("false");
            ztreeNodes.add(z);
        }
        return ztreeNodes;
    }


    @ApiOperation("保存")
    @RequestMapping(value = "/saveSoftMenu", method = RequestMethod.POST)
    public JmMessage saveSoftMenu(@RequestBody ZbSoftMenuVo vo){
        String[] ids =  vo.getResourceIds().split(",");
        List<ZbSoftMenu> menus = new ArrayList<>();
        for (String id:ids) {
            ZbSoftMenu zbSoftMenu = new ZbSoftMenu();
            zbSoftMenu.setSoftId(vo.getSoftId());
            zbSoftMenu.setResourceId(Toolkit.parseObjForInt(id));
            menus.add(zbSoftMenu);
        }
        zbSoftMenuService.deleteBySoftId(vo.getSoftId());
        zbSoftMenuService.save(menus);
        return new JmMessage(0,"保存成功");
    }


    @ApiOperation("获取已保存的资源Id")
    @RequestMapping(value = "/getResIds/{softId}", method = RequestMethod.GET)
    public JmMessage getResIds(@ApiParam("版本Id") @PathVariable("softId") Integer softId)throws Exception{
        List<ZbSoftMenu> zbSoftMenus = zbSoftMenuService.findBySoftId(softId);
        String resIds = "";
        for (ZbSoftMenu zbSoftMenu:zbSoftMenus) {
            resIds+=zbSoftMenu.getResourceId()+",";
        }
        if(!resIds.equals("")){
            return new JmMessage(0,resIds.substring(0,resIds.length()-1));
        }
        return new JmMessage(0,"");
    }

    @ApiOperation("获取角色保存的资源Id")
    @RequestMapping(value = "/getRoleResIds/{roleId}/{softId}", method = RequestMethod.GET)
    public JmMessage getRoleResIds(@ApiParam("角色Id") @PathVariable("roleId") Integer roleId,
                                   @ApiParam("版本Id") @PathVariable("softId") Integer softId){
        List<RoleResource> roleResources = roleResourceRepository.findByRoleIdAndSoftId(roleId,softId);
        String ids = "";
        for (RoleResource roleResource:roleResources) {
            ids+=roleResource.getResourceId()+",";
        }
        if(!ids.equals("")){
            return new JmMessage(0,ids.substring(0,ids.length()-1));
        }
        return new JmMessage(0,"");
    }

    @ApiOperation("获取当前版本的所有菜单")
    @RequestMapping(value = "/getSoftResIds/{softId}", method = RequestMethod.GET)
    public List<ZtreeNode> getSoftResIds(@ApiParam("版本Id") @PathVariable("softId") Integer softId)throws Exception{
        List<ZbSoftMenu> zbSoftMenus = zbSoftMenuService.findBySoftId(softId);
        List<Integer> ids = new ArrayList<>();
        for (ZbSoftMenu zbSoftMenu:zbSoftMenus) {
            ids.add(zbSoftMenu.getResourceId());
        }
        List<Resource> resourceList = new ArrayList<>();
        if(ids.size()>0){
            resourceList = resourceRepository.findResourceByIdsAndStatus(ids,0);
        }
        List<ZtreeNode> ztreeNodes = new ArrayList<>();
        for (Resource resource:resourceList) {
            ZtreeNode z = new ZtreeNode();
            z.setId(resource.getResourceId());
            z.setPId(resource.getParentRsourceId());
            z.setName(resource.getResourceName());
            z.setOpen("false");
            ztreeNodes.add(z);
        }
        return ztreeNodes;
    }

    @ApiOperation("查询版本角色")
    @RequestMapping(value = "/findRole/{softId}", method = RequestMethod.GET)
    public List<Role> findRole(@ApiParam("版本Id") @PathVariable("softId") Integer softId){
        List<Role> roles =  roleService.findBySoftId(softId,0);
        return roles;
    }

    @ApiOperation("保存版本角色")
    @RequestMapping(value = "/saveSoftRole", method = RequestMethod.POST)
    public JmMessage saveSoftMenu(@RequestBody ZbSoftRoleVo vo){
        roleService.saveSoftRole(vo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("保存角色设定的菜单资源")
    @RequestMapping(value = "/saveSoftRoleRes", method = RequestMethod.POST)
    @Transactional
    public JmMessage saveSoftMenu1(@RequestBody ZbSoftRoleResVo vo){
        String[] ids =  vo.getResourceIds().split(",");
        if(ids.length>0){
            roleResourceRepository.deleteByRoleId(vo.getRoleId());
        }
        List<RoleResource> rs = new ArrayList<>();
        for (String id:ids) {
            RoleResource roleResource = new RoleResource();
            roleResource.setResourceId(Toolkit.parseObjForInt(id));
            roleResource.setRoleId(vo.getRoleId());
            roleResource.setSoftId(vo.getSoftId());
            rs.add(roleResource);
        }
        if(rs.size()>0){
            roleResourceRepository.save(rs);
        }
        return new JmMessage(0,"保存成功");
    }



    @ApiOperation("会员中心菜单")
    @RequestMapping(value = "/getUserCenterFuns/{status}", method = RequestMethod.GET)
    public List<ZtreeNode> getUserCenterFuns(@ApiParam("状态") @PathVariable("status") Integer status)throws Exception{
        List<UserCenterFuns> userCenterFuns = userCenterFunsRepository.findUserCenterFunsList(status);
        List<ZtreeNode> ztreeNodes = new ArrayList<>();
        for (UserCenterFuns funs:userCenterFuns) {
            ZtreeNode z = new ZtreeNode();
            z.setId(funs.getFunId());
            z.setPId(0);
            z.setName(funs.getFunName());
            z.setOpen("false");
            ztreeNodes.add(z);
        }
        return ztreeNodes;
    }


    @ApiOperation("保存角色设定的菜单资源")
    @RequestMapping(value = "/saveUserCenterFuns", method = RequestMethod.POST)
    public JmMessage saveUserCenter(@RequestBody UserCenterVersionVo vo){
        userCenterVersionService.saveVersion(vo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("获取角色保存的资源Id")
    @RequestMapping(value = "/findUserCenterFuns/{softId}", method = RequestMethod.GET)
    public JmMessage getRoleResIds(@ApiParam("版本Id") @PathVariable("softId") Integer softId){
        List<UserCenterVersion> userCenterVersions = userCenterVersionService.findBySoftId(softId);
        String ids = "";
        for (UserCenterVersion version:userCenterVersions) {
            ids+=version.getFunsId()+",";
        }
        if(!ids.equals("")){
            return new JmMessage(0,ids.substring(0,ids.length()-1));
        }
        return new JmMessage(0,"");
    }

}
