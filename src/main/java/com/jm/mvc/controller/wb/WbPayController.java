package com.jm.mvc.controller.wb;

import com.jm.business.service.wb.WbPayService;
import com.jm.mvc.vo.JmMessage;
import com.jm.repository.client.dto.wb.WbResultMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>微博支付</p>
 *
 * @version latest
 * @Author whh
 * @Date 2017/4/13
 */
@Api
@RestController
@RequestMapping(value = "/")
public class WbPayController {

    @Autowired
    private WbPayService wbPayService;

    /*@ApiOperation("发起微博支付退款")
    @RequestMapping(value = "wb/refund/{order_refund_id}", method = RequestMethod.GET)
    public WbResultMsg getWbRefund( @ApiParam(hidden=true) @PathVariable("order_refund_id") Long orderRefundId) throws Exception{
        WbResultMsg wbResultMsg = wbPayService.toRefund(orderRefundId);
        return wbResultMsg;
    }*/

}
