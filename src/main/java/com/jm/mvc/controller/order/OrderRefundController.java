package com.jm.mvc.controller.order;

import com.jm.business.service.order.OrderRefundService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.OrderRefundUpdateVo;
import com.jm.mvc.vo.order.OrderRefundCo;
import com.jm.repository.po.order.OrderRefund;
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
 * @date 2016/9/8
 */

@Api
@Slf4j
@RestController
@RequestMapping(value = "/refund")
public class OrderRefundController {

    @Autowired
    private OrderRefundService orderRefundService;


    @ApiOperation("添加退款信息")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage add(@ApiParam(hidden=true) HttpServletRequest request,
                         @ApiParam("添加退款") @RequestBody OrderRefundCo orderRefundCo){
        OrderRefund refund = orderRefundService.addRefund(orderRefundCo);
        if(refund!=null){
            return new JmMessage(0, "添加成功",refund.getOrderInfoId());
        }
        return new JmMessage(1, "添加失败");
    }

    @ApiOperation("修改退款信息")
    @RequestMapping(value = "/refund_update/{id}", method = RequestMethod.PUT)
    public JmMessage update(@ApiParam("订单标识")  @PathVariable("id") Integer id,
                            @ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam(hidden=true) HttpServletResponse response,
                            @RequestBody @Valid OrderRefundUpdateVo updateVo) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appId = user.getAppId();
        String clientIp = request.getRemoteHost();
        JmMessage jmMessage =  orderRefundService.updateOrderRefund(user,id,appId,clientIp,updateVo);
        return jmMessage;
    }
}
