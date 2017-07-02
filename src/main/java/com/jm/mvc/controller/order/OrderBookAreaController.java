package com.jm.mvc.controller.order;

import com.jm.business.service.order.OrderBookAreaService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookAreaCo;
import com.jm.mvc.vo.order.OrderBookAreaQo;
import com.jm.mvc.vo.order.OrderBookAreaUo;
import com.jm.mvc.vo.order.OrderBookAreaVo;
import com.jm.repository.po.order.OrderBookArea;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.OrderBookAreaConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/orderBookArea")
public class OrderBookAreaController {

	@Autowired
    private OrderBookAreaService orderBookAreaService;


	@ApiOperation("服务列表")
	@RequestMapping(value = "/service_list", method = RequestMethod.POST)
	public PageItem<OrderBookAreaVo> serviceList(@ApiParam(hidden=true) HttpServletRequest request,
											   @RequestBody @Valid OrderBookAreaQo orderBookAreaQo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		orderBookAreaQo.setShopId(user.getShopId());
		PageItem<OrderBookAreaVo> pageItem = orderBookAreaService.findListByShopId(orderBookAreaQo);
		return pageItem;
	}

	@ApiOperation("保存服务地区")
	@RequestMapping(value = "/save_service_area", method = RequestMethod.POST)
	public JmMessage saveServiceArea(@ApiParam(hidden=true) HttpServletRequest request,
									 @RequestBody @Valid OrderBookAreaCo co) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		if(co.getId()!=null){
			OrderBookArea area = orderBookAreaService.findByIdAndShopId(co.getId(),user.getShopId());
			BeanUtils.copyProperties(co,area);
			orderBookAreaService.save(area);
		}else{
			OrderBookArea area = OrderBookAreaConverter.toOrderBookArea(co);
			area.setShopId(user.getShopId());
			orderBookAreaService.save(area);
		}
		return new JmMessage(0,"保存成功");
	}

	@ApiOperation("删除服务地区")
	@RequestMapping(value = "/del_service_area/{id}", method = RequestMethod.DELETE)
	public JmMessage delServiceArea(@ApiParam(hidden=true) HttpServletRequest request ,@ApiParam("id") @PathVariable("id") Integer id) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		orderBookAreaService.deleteByShopId(user.getShopId(),id);
		return new JmMessage(0,"删除成功");
	}

	@ApiOperation("更新服务地区快递")
	@RequestMapping(value = "/update_service_kd/{id}", method = RequestMethod.PUT)
	public JmMessage updateKd(@ApiParam(hidden=true) HttpServletRequest request,
							  @ApiParam("id") @PathVariable("id") Integer id,
								@RequestBody @Valid OrderBookAreaUo uo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		OrderBookArea area = orderBookAreaService.findByIdAndShopId(id,user.getShopId());
		area.setKdId(uo.getKdId());
		area.setKdName(uo.getKdName());
		orderBookAreaService.save(area);
		return new JmMessage(0,"保存成功");
	}


	@ApiOperation("已用的地区")
	@RequestMapping(value = "/used_area", method = RequestMethod.GET)
	public JmMessage usedArea(@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<OrderBookArea> areas = orderBookAreaService.findListByShopId(user.getShopId());
		String allArea = "";
		for (OrderBookArea area:areas) {
			allArea+=area.getAreaCode()+",";
		}
		if(!allArea.equals("")){
			allArea = allArea.substring(0,allArea.length()-1);
			return new JmMessage(0,allArea);
		}
		return new JmMessage(1,allArea);
	}

}
