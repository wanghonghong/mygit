package com.jm.mvc.controller.system;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.resource.ResourceCo;
import com.jm.mvc.vo.shop.resource.ShopResourceQo;
import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jm.business.service.system.ResourceService;
import com.jm.repository.po.system.user.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>系统管理</p>
 * 
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */

@Api
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

    @ApiOperation("资源文件上传")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addResource(@ApiParam(hidden=true) HttpServletRequest request,
                                 @RequestParam("myfile") MultipartFile[] files,
                                 @RequestParam(required=false,value = "group_id") Integer groupId,
                                 @RequestParam(required=false,value = "res_url") String resUrl,
                                 @RequestParam(required=false,value = "compress") Integer compress,
                                 @RequestParam(required=false,value = "res_type") Integer resType) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        ResourceCo resourceCo = new ResourceCo();
        resourceCo.setShopId(user.getShopId());
        if ("5".equals(resType)) { //其它类型不显示
            resourceCo.setIsDel(1);
        }
        resourceCo.setGroupId(groupId);
        resourceCo.setResType(resType);
        resourceCo.setResUrl(resUrl);
        resourceCo.setCompress(compress);
        return resourceService.addResource(files,resourceCo);
    }

    @ApiOperation("上传外连接图片")
    @RequestMapping(value = "/url", method = RequestMethod.POST)
    public JmMessage uploadLinkUrl(@ApiParam(hidden=true) HttpServletRequest request,
                                   @RequestBody ResourceCo resourceCo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        resourceCo.setShopId(user.getShopId());
        return resourceService.addUrlResource(resourceCo);
    }

    @ApiOperation("资源下载")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public PageItem<ShopResourceRo> queryResource(@ApiParam(hidden=true) HttpServletRequest request,
                                               @ApiParam("店铺资源查询QO") @RequestBody @Valid ShopResourceQo shopResourceQo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopResourceQo.setShopId(user.getShopId());
        return resourceService.findAll(shopResourceQo);
    }

    @ApiOperation("菜单查询")
    @RequestMapping(value = "/{status}", method = RequestMethod.GET)
    public List<Resource> queryUser(@ApiParam("状态") @PathVariable("status") Integer status) {
        return resourceService.getResource(status);
    }


    @ApiOperation("根据角色获取菜单")
    @RequestMapping(value = "/role_resource/{status}", method = RequestMethod.GET)
    public List<Resource> findResourceByRole(@ApiParam(hidden=true) HttpServletRequest request,@ApiParam("状态") @PathVariable("status") Integer status) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return resourceService.findResourceByRole(user,status);
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public JmMessage delete(@ApiParam(hidden=true) @PathVariable("ids") String ids ,
                            @ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response){
        return resourceService.delete(ids);
    }
}
