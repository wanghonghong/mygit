package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.ShopResGroupService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.resource.ShopResGroupCo;
import com.jm.mvc.vo.shop.resource.ShopResGroupRo;
import com.jm.mvc.vo.shop.resource.ShopResGroupUo;
import com.jm.repository.po.shop.ShopResGroup;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/shop_res_group")
public class ShopResGroupController {
	@Autowired
	private ShopResGroupService shopResGroupService;

	@ApiOperation("新增资源组")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage addShopResGroup(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam(hidden = true) HttpServletResponse response,
									 @RequestBody ShopResGroupCo shopResGroupCo){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		ShopResGroup srg = new ShopResGroup();
		Integer shopId = 0;
		if(null != user){shopId = user.getShopId();}
		BeanUtils.copyProperties(shopResGroupCo,srg);
		srg.setShopId(shopId);
		srg.setGroupFlag("Y");
		shopResGroupService.add(srg);
		return new JmMessage(0,"新增资源分组成功!!");
	}

	@ApiOperation("根据资源类型查询资源组")
	@RequestMapping(value = "/{resType}", method = RequestMethod.GET)
	public List<ShopResGroupRo> findAllShopResourceByResType(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam(hidden = true) HttpServletResponse response,
							 @ApiParam("资源类型1：图片 2：视频 3：语音")	@PathVariable("resType") Integer resType){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId = 0;
		if(null != user){shopId = user.getShopId();}
		return shopResGroupService.getShopResGroup(shopId,resType);
	}

	@ApiOperation("删除资源组")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage delete(@ApiParam(hidden = true) HttpServletRequest request,
							@ApiParam(hidden = true) HttpServletResponse response,
							@ApiParam("素材id") @PathVariable("id") Integer id){
		return shopResGroupService.delShopResGroup(id);
	}

	@ApiOperation("修改资源组")
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public JmMessage updateShopResGroup(@ApiParam(hidden = true) HttpServletRequest request,
										@ApiParam(hidden = true) HttpServletResponse response,
										@RequestBody ShopResGroupUo shopResGroupUo){
		Integer id = shopResGroupUo.getId();
		ShopResGroup srg = shopResGroupService.findShopResGroupById(id);
		ShopResGroup shopResGroup = new ShopResGroup();
		BeanUtils.copyProperties(shopResGroupUo,shopResGroup);
		shopResGroup.setShopId(srg.getShopId());
		shopResGroup.setResType(srg.getResType());
		shopResGroup.setGroupFlag(srg.getGroupFlag()==null ? "Y":srg.getGroupFlag());
		shopResGroupService.saveShopResGroup(shopResGroup);
		return new JmMessage(0,"编辑店铺素材资源分组成功!");
	}

	@ApiOperation("图片换组")
	@RequestMapping(value = "/changeGroup/{ids}/{resGroupId}", method = RequestMethod.GET)
	public JmMessage changeShopResGroup(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam(hidden = true) HttpServletResponse response,
								   @ApiParam("素材ids") @PathVariable("ids") String ids,
								   @ApiParam("素材组id") @PathVariable("resGroupId") Integer resGroupId){
		String[] strIds = ids.split(",");
		List<Integer> shopResIdList = new ArrayList();
		for (Object obj :strIds) {
			Integer id = Toolkit.parseObjForInt(obj);
			shopResIdList.add(id);
		}
		return shopResGroupService.changeShopResGroup(shopResIdList,resGroupId);
	}

}
