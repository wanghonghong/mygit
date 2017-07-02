package com.jm.mvc.controller.product;

import com.jm.business.service.product.impl.ProductTradServiceImpl;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.trad.*;
import com.jm.repository.po.product.TradSign;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
@RequestMapping(value = "/product/trad")
public class ProductTradController {

	@Autowired
	private ProductTradServiceImpl productTradServiceImpl;

	@ApiOperation("活动详情查看")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TradActivityDetailVo getTradActivityDetail(@ApiParam(hidden = true) HttpServletRequest request,@PathVariable("id") Integer id) throws IOException {
		return productTradServiceImpl.getTradActivityDetail(id);
	}

	@ApiOperation("新增活动获取")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<TradSign> getProductTrad(@ApiParam(hidden = true) HttpServletRequest request) throws IOException {
		return productTradServiceImpl.getCacheTradSign();
	}

	@ApiOperation("新增活动新增")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage setTradActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增活动") @RequestBody @Valid TradActivityCo tradActivityCo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		productTradServiceImpl.setTradActivity(tradActivityCo,user.getShopId());
		return new JmMessage(0,"新增成功");
	}

	@ApiOperation("修改活动")
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public JmMessage updateTradActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改活动") @RequestBody @Valid TradActivityUo tradActivityUo) throws IOException {
		productTradServiceImpl.updateTradActivity(tradActivityUo);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("删除活动")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JmMessage deleteTradActivity(@ApiParam(hidden = true) HttpServletRequest request, @PathVariable("id") Integer id) throws IOException {
		productTradServiceImpl.deleteTradActivity(id);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("单个、批量修改商品状态(伪删除)")
	@RequestMapping(value = "/status", method = RequestMethod.PUT)
	public JmMessage updateStatus(@RequestBody TradActivityStatusUo tradActivityStatusUo
	) {
		productTradServiceImpl.updateStatus(tradActivityStatusUo);
		return new JmMessage(0, "修改成功");
	}

	@ApiOperation("商品列表查询")
	@RequestMapping(value = "/trad_products", method = RequestMethod.POST)
	public PageItem<TradProductDetailVo> getTradProduct(@ApiParam(hidden = true) HttpServletRequest request,
												  @RequestBody @Valid TradProductQo tradProductQo) throws IOException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		PageItem<TradProductDetailVo> tradProductList = productTradServiceImpl.getTradProduct(tradProductQo, user.getShopId());
		return tradProductList;
	}

	@ApiOperation("商品列表查询")
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public List<TradProductDetailVo> getTradProductByPids(@ApiParam("活动商品查询") @RequestBody @Valid TradProductTypeQo tradProductTypeQo)   throws IOException {
		return productTradServiceImpl.getTradProductByPids(tradProductTypeQo);
	}

}