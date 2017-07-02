/**
 * 
 */
package com.jm.mvc.controller.shop;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jm.business.service.shop.LinkService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.shop.LinkCreateVo;
import com.jm.repository.po.system.Link;
import com.jm.staticcode.converter.LinkConverter;

/**
 * 
 *<p>店铺链接</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月23日
 */

@Api
@RestController
@RequestMapping(value = "/link")
public class LinkController {
	
	private LinkService linkService;
	
	@ApiOperation("创建店铺链接")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Link saveOrderCar(@ApiParam("店铺链接一条记录标识") @RequestBody @Valid LinkCreateVo linkCreateVo){
		linkCreateVo.setId(1);//暂时写死
		return linkService.addShoppringCart(LinkConverter.toShopLink(linkCreateVo));
	}
	
	@ApiOperation("删除店铺链接")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteOrderCart(@ApiParam("店铺链接一条记录标识") @PathVariable("id") Long id){
		linkService.deleteLink(id);
		return new JmMessage(0, "店铺链接删除成功！");
	}
	
	@ApiOperation("修改店铺链接")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT )
	public void updateShoppingCart(@ApiParam("店铺链接一条记录标识") @PathVariable("id") Link link){
		linkService.updateShopLink(link);
	}
	
}
