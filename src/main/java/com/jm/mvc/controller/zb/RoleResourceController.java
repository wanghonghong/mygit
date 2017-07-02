package com.jm.mvc.controller.zb;

import com.jm.business.service.zb.ZbResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.zb.system.ResourceUo;
import com.jm.mvc.vo.zb.system.ZbResourceCo;
import com.jm.repository.po.zb.system.ZbResource;
import com.jm.staticcode.constant.ZbConstant;
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
 * <p>菜单权限</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@RestController
@RequestMapping(value = "/zb")
public class RoleResourceController {

    @Autowired
    private ZbResourceService zbResourceService;

    @ApiOperation("权限配置页面")
    @RequestMapping(value = "/resource_role", method = RequestMethod.GET)
    public ModelAndView roleManager(@ApiParam(hidden=true) HttpServletRequest request, @ApiParam(hidden=true) HttpServletResponse response){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/system/re");
        return view;
    }
    @ApiOperation("权限查询")
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public ModelAndView resourcelist (@ApiParam(hidden=true) HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        //查询权限列表
        List<ZbResource> zbResources = zbResourceService.queryResource();
        view.setViewName("/pc/system/zbResources");
        return view;
    }

    @ApiOperation("权限删除")
    @RequestMapping(value = "/resource_role/{id}", method = RequestMethod.DELETE)
    public void deleteResource(@ApiParam("菜单权限标识")  @PathVariable("id") Integer resourceId){
        zbResourceService.deleteResource(resourceId);
    }

    @ApiOperation("权限查询")
    @RequestMapping(value = "/resource_role/list", method = RequestMethod.GET)
    public List<ZbResource> queryResource(){
        return zbResourceService.queryResource();
    }

    @ApiOperation("权限更新")
    @RequestMapping(value = "/resource_role/{id}", method = RequestMethod.PUT)
    public void updateResource(@PathVariable("{id}") int id, @RequestBody ResourceUo resourceUo){
        zbResourceService.update(id,resourceUo);
    }

    /**
     * 菜单树展示
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="/get_resource_list",method=RequestMethod.POST)
    public List getResourceList(@ApiParam(hidden=true) HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        return  ZbConstant.RESOURCE_ZTREE_LIST;
    }

    @ApiOperation("权限配置")
    @RequestMapping(value = "resource_role", method = RequestMethod.POST)
    public JmMessage addResource(@ApiParam(hidden=true) HttpServletRequest request,
                                 @RequestBody @Valid ZbResourceCo zbResourceCo){
        zbResourceService.saveRoleResource(zbResourceCo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("根据角色查询菜单权限")
    @RequestMapping(value = "resource_role/{id}", method = RequestMethod.GET)
    public JmMessage queryResourceByRoleId(@PathVariable("id") int roleId){
        String ids= zbResourceService.getResourceIds(roleId);
        return new JmMessage(0,"获取成功",ids);
    }

}
