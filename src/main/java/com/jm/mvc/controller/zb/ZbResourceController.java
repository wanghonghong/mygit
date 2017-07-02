package com.jm.mvc.controller.zb;

import com.jm.business.service.zb.system.JmResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.zb.system.ShopResourceCo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>系统管理</p>
 * 
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */

@Api
@RestController
@RequestMapping(value = "/zb/resource")
public class ZbResourceController {

	@Autowired
	private JmResourceService resourceService;

    @ApiOperation("资源文件上传")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addResource(@ApiParam(hidden=true) HttpServletRequest request,
                                   @RequestParam("myfile") MultipartFile[] files,
                                   @RequestParam(required=false,value = "group_id") Integer groupId,
                                   @RequestParam(required=false,value = "res_url") String resUrl,
                                   @RequestParam(required=false,value = "compress") Integer compress,
                                   @RequestParam(required=false,value = "res_type") Integer resType) throws Exception {

        ShopResourceCo shopResourceCo = new ShopResourceCo();
        if ("5".equals(resType)) { //其它类型不显示
            shopResourceCo.setIsDel(1);
        }
        shopResourceCo.setGroupId(groupId);
        shopResourceCo.setResType(resType);
        shopResourceCo.setResUrl(resUrl);
        shopResourceCo.setCompress(compress);
        return resourceService.addImage(files,shopResourceCo);
    }


}
