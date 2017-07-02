package com.jm.mvc.controller.product;

import com.jm.business.service.product.ProductGroupService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.group.*;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

//import CosCloud;
//import CosBaseConf;

/**
 * <p>
 * </p>
 * 
 * @author zhengww
 * @version latest
 * @date 2016年5月20日
 */
@Api
@RestController
@RequestMapping(value = "/product/group")
public class ProductGroupController {

	@Autowired
	private ProductGroupService productGroupService;

	@ApiOperation("获取商品列表分组列表/不带分页")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ProductGroupVo> getGroupList(@ApiParam(hidden = true) HttpServletRequest request) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<ProductGroupVo> list = productGroupService.queryGroupListByShopId(user.getShopId());
		return list;
	}

	@ApiOperation("获取商品分组/分页")
	@RequestMapping(value = "/groups", method = RequestMethod.POST)
	public PageItem<Map<String, Object>> getProductGroup(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody ProductGroupQo productGroupQo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<Map<String, Object>> list = productGroupService.queryGroupList(productGroupQo, user.getShopId());
		if (null != list.getItems() && list.getItems().size()>0){
			for(Map<String, Object> map : list.getItems() ){
				String groupImagePath =  ImgUtil.appendUrl(map.get("group_image_path").toString(),720);
				map.put("group_image_path", groupImagePath);
			}
		}
		return list;
	}

	@ApiOperation("获取商品单个分组信息")
	@RequestMapping(value = "/{groupId}", method = RequestMethod.GET)
	public ProductGroupVo getGroup(@ApiParam("商品分组标识") @PathVariable("groupId") Integer groupId) {
		return productGroupService.queryGroupByGroupId(groupId);
	}

	@ApiOperation("新增商品分组")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ProductGroupVo saveProductGroup(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增商品分组VO") @RequestBody @Valid ProductGroupCo productGroupCo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return productGroupService.saveProductGroup(productGroupCo, user.getShopId());
	}

	@ApiOperation("修改商品分组")
	@RequestMapping(value = "/{groupId}", method = RequestMethod.PUT)
	public ProductGroupVo updateProductGroup(@ApiParam(hidden = true) HttpServletRequest request,
											 @ApiParam("商品分组标识") @PathVariable("groupId") Integer groupId,
											 @ApiParam("商品分组VO") @RequestBody @Valid ProductGroupUo productGroupUo) {
		productGroupUo.setGroupId(groupId);
		return productGroupService.updateProductGroup(productGroupUo);
	}

	@ApiOperation("判断某分组下是否存在商品，不存在则删除商品分组")
	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	public JmMessage delProductGroup(@ApiParam("商品分组标识") @PathVariable("groupId") Integer groupId) {
		Boolean flag = productGroupService.delProductGroup(groupId);
		if (flag) {
			return new JmMessage(0, "删除成功");
		} else {
			return new JmMessage(1, "该分类下存在商品无法删除");
		}
	}
}