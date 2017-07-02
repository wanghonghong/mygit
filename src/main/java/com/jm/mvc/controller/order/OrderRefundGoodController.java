package com.jm.mvc.controller.order;

import com.jm.business.service.order.OrderRefundGoodService;
import com.jm.business.service.order.OrderService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.order.OrderRefundGoodUpdateVo;
import com.jm.mvc.vo.order.OrderRefundGoodVo;
import com.jm.mvc.vo.order.OrderRefundGoodsCo;
import com.jm.repository.po.order.OrderRefundGoods;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>退款</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/14
 */


@Api
@Slf4j
@RestController
@RequestMapping(value = "/refundGood")
public class OrderRefundGoodController {


    @Autowired
    private OrderRefundGoodService orderRefundGoodService;

    @Autowired
    private OrderService orderService;


    @ApiOperation("添加退货信息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage add(@ApiParam(hidden=true) HttpServletRequest request,
                         @ApiParam("添加退款") @RequestBody OrderRefundGoodsCo orderRefundCo){
        OrderRefundGoods orderRefundGoods = orderRefundGoodService.addOrderRefundGoods(orderRefundCo);
        if(orderRefundGoods!=null){
            return new JmMessage(0, "添加成功",orderRefundGoods.getOrderInfoId());
        }
        return new JmMessage(1, "添加失败");
    }

    @ApiOperation("修改退货信息")
    @RequestMapping(value = "/refund_update/{id}", method = RequestMethod.PUT)
    public JmMessage update(@ApiParam("退款标识")  @PathVariable("id") Integer id,
                            @ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response,
                            @RequestBody @Valid OrderRefundGoodUpdateVo updateVo) throws Exception {
        OrderRefundGoods orderRefundGoods =orderRefundGoodService.updateOrderRefundGoods(updateVo);
        //退货已收微信推送信息
        if(null!=orderRefundGoods){
            if(orderRefundGoods.getGoodStatus()==1){
                String url = Constant.DOMAIN;
                orderService.pushRefundWxMsg(orderRefundGoods.getOrderInfoId(),url);
                //orderService.pushRefundHxMsg(user,orderRefundGoods.getOrderInfoId());
            }
        }
        if(null==orderRefundGoods){
            return new JmMessage(0, "添加失败");
        }
        return new JmMessage(1, "操作成功！");
    }

    @ApiOperation("根据订单id查询订单发货信息")
    @RequestMapping(value = "/good_list/{order_info_id}", method = RequestMethod.GET)
    public OrderRefundGoodVo queryOrderDetail(@ApiParam("订单标识")  @PathVariable("order_info_id") Long orderInfoId){
        OrderRefundGoodVo orderRefundGoodVo = orderRefundGoodService.findOrderRefundGoods(orderInfoId);
        return orderRefundGoodVo;
    }

    @ApiOperation("pc端保存订单中的卖家备注信息")
    @RequestMapping(value = "/save_storage_note/{id}", method = RequestMethod.PUT)
    public JmMessage saveSendNote(@ApiParam(hidden=true) HttpServletRequest request,
                                  @ApiParam("订单标识id") @PathVariable("id") Integer id,
                                  @RequestBody @Valid OrderRefundGoodUpdateVo updateVo){
        OrderRefundGoods  orderRefundGood = orderRefundGoodService.getOrderRefundGoods(id);
        orderRefundGood.setStorageNote(updateVo.getStorageNote());
        OrderRefundGoods org = orderRefundGoodService.createOrderRefundGood(orderRefundGood);
        if(null==org){
            return new JmMessage(0, "添加失败");
        }
        return new JmMessage(1, "1");
    }
}
