package com.jm.mvc.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.business.service.order.OrderBookService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookQo;
import com.jm.mvc.vo.order.OrderBookUo;
import com.jm.mvc.vo.order.OrderBookVo;
import com.jm.mvc.vo.order.recycle.RecycleDetailUo;
import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.csv.CsvToolUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
@RequestMapping(value = "/orderBook")
public class OrderBookController {

	@Autowired
    private OrderBookService orderBookService;

	@ApiOperation("pc端订单回收页面或按订单回收订单状态查询")
	@RequestMapping(value = "/order_book_list/{status}", method = RequestMethod.POST)
	public PageItem orderManage(@ApiParam(hidden=true) HttpServletRequest request,
								   @ApiParam(hidden=true) HttpServletResponse response,
								   @PathVariable("status") Integer status,
								   @RequestBody @Valid OrderBookQo orderBookQo) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		orderBookQo.setShopId(user.getShopId());
		PageItem<OrderBookVo> pageItem = orderBookService.queryOrder(orderBookQo,status);
		return pageItem;
	}

	@ApiOperation("修改订单信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public JmMessage update(@ApiParam("订单标识")  @PathVariable("id") Long id,
							@ApiParam(hidden=true) HttpServletRequest request,
							@RequestBody @Valid OrderBookUo orderBookUo) throws Exception {
		//确认收货后调用红包接口
		orderBookService.updateOrderBook(id,orderBookUo);
		return new JmMessage(2, "操作成功！");
	}

	@ApiOperation("触发奖励")
	@RequestMapping(value = "/trigger_reward/{id}", method = RequestMethod.PUT)
	public JmMessage triggerReward(@ApiParam("订单标识")  @PathVariable("id") Long id,
							@ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		//确认收货后调用红包接口
		orderBookService.triggerReward(id);
		return new JmMessage(2, "操作成功！");
	}

	@ApiOperation("pc端订单导出功能")
	@RequestMapping(value = "/export_order", method = RequestMethod.GET)
	public void exportOrder(@ApiParam(hidden=true) HttpServletRequest request,
							@ApiParam(hidden=true) HttpServletResponse response) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String json = request.getParameter("orderBookQo").toString();
		Integer status = Integer.parseInt(request.getParameter("status"));
		ObjectMapper objectMapper=new ObjectMapper();
		OrderBookQo orderBookQo =objectMapper.readValue(json, OrderBookQo.class);
		orderBookQo.setShopId(user.getShopId());
		List<Map<String, Object>> mapList = orderBookService.exportOrderBook(orderBookQo,status);
		orderBookService.exportOrderBook(mapList,response);
	}

    @ApiOperation("查看客服备注/收货备注")
    @RequestMapping(value = "queryRecycleDetail/{id}", method = RequestMethod.GET)
    public RecycleDetailVo queryRecycleDetail(@ApiParam("订单标识")  @PathVariable("id") Long id) throws Exception {
        RecycleDetailVo recycleDetailVo = orderBookService.getRecycleDetail(id);
        return recycleDetailVo;
    }

	@ApiOperation("pc端订单导入功能")
	@RequestMapping(value = "/import_order", method = RequestMethod.POST)
	public JmMessage importOrder(@ApiParam(hidden=true) HttpServletRequest request,
							@ApiParam(hidden=true) HttpServletResponse response,
							@RequestParam("myfile") MultipartFile file) throws Exception {
		InputStream inputStream = file.getInputStream();
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		String dataList=csvToolUtil.importCsv(inputStream);
		orderBookService.updateOrderBook(dataList);
		return new JmMessage(1, "操作成功！");
	}


	@ApiOperation("客服备注/收货备注")
	@RequestMapping(value = "saveRecycleDetail/{id}", method = RequestMethod.PUT)
	public JmMessage saveRecycleDetail(@ApiParam("订单标识")  @PathVariable("id") Long id,
							  @ApiParam(hidden=true) HttpServletRequest request,
							  @RequestBody @Valid RecycleDetailUo recycleDetailUo) throws Exception {
		//确认收货后调用红包接口
		orderBookService.updateRecycleDetailUo(id,recycleDetailUo);
		return new JmMessage(2, "操作成功！");
	}

}
