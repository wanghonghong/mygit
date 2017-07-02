package com.jm.mvc.controller.order.recycle;

import com.jm.business.service.order.recycle.RecycleDetailService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.order.OrderInfoForUpdateVo;
import com.jm.repository.po.order.recycle.RecycleDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>回收订单详情</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/recycleOrder")
public class RecycleOrderController {

	@Autowired
    private RecycleDetailService recycleDetailService;

	@ApiOperation("pc端保存订单中的客服备注信息")
	@RequestMapping(value = "/save_customer/{id}", method = RequestMethod.PUT)
	public JmMessage addcostomer(@ApiParam(hidden=true) HttpServletRequest request,
								 @ApiParam("订单标识id") @PathVariable("id") Long id,
								 @RequestBody @Valid OrderInfoForUpdateVo updateVo){
		RecycleDetail recycleDetail = recycleDetailService.findOrderId(id);
		recycleDetail.setCustomRemark(updateVo.getSellerNote());
		RecycleDetail od=recycleDetailService.createRecycleOrder(recycleDetail);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
		return new JmMessage(1, "1");
	}

	@ApiOperation("pc端保存订单中的用户备注信息")
	@RequestMapping(value = "/save_user_customer/{id}", method = RequestMethod.PUT)
	public JmMessage addUserCostomer(@ApiParam(hidden=true) HttpServletRequest request,
								 @ApiParam("订单标识id") @PathVariable("id") Long id,
								 @RequestBody @Valid OrderInfoForUpdateVo updateVo){
		RecycleDetail recycleDetail = recycleDetailService.findOrderId(id);
		recycleDetail.setUserRemark(updateVo.getRemark());
		RecycleDetail od=recycleDetailService.createRecycleOrder(recycleDetail);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
		return new JmMessage(1, "1");
	}

	@ApiOperation("pc端保存订单中的收货备注信息")
	@RequestMapping(value = "/save_receive_customer/{id}", method = RequestMethod.PUT)
	public JmMessage add_receipt_costomer(@ApiParam(hidden=true) HttpServletRequest request,
								 @ApiParam("订单标识id") @PathVariable("id") Long id,
								 @RequestBody @Valid OrderInfoForUpdateVo updateVo){
		RecycleDetail recycleDetail = recycleDetailService.findOrderId(id);
		recycleDetail.setReceiveRemark(updateVo.getSellerNote());
		RecycleDetail od=recycleDetailService.createRecycleOrder(recycleDetail);
		if(null==od){
			return new JmMessage(0, "添加失败");
		}
		return new JmMessage(1, "1");
	}

}
