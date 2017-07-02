package com.jm.mvc.controller.order.recycle;

import com.jm.business.service.order.recycle.OrderBookConfigService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.recycle.OrderBookConfigCo;
import com.jm.repository.po.order.recycle.OrderBookConfig;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

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
@RequestMapping(value = "/orderBookConfig")
public class OrderBookConfigController {

	@Autowired
    private OrderBookConfigService orderBookConfigService;

    @ApiOperation("新增地区限定")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addCard(@ApiParam("新增地区限定Co") @RequestBody @Valid OrderBookConfigCo orderBookConfigCo,
                             @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        orderBookConfigCo.setShopId(user.getShopId());
        OrderBookConfig orderBookConfig = orderBookConfigService.addOrderBookConfig(orderBookConfigCo);
        if(orderBookConfig!=null){
            return new JmMessage(1,"保存成功！");
        }
        return new JmMessage(0,"保存失败！");
    }

    @ApiOperation("地区限定列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public OrderBookConfig findCardList(@ApiParam(hidden=true) HttpServletRequest request) throws IOException, InstantiationException, IllegalAccessException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        OrderBookConfig orderBookConfig = orderBookConfigService.queryOrderBookConfig(user.getShopId());
        if(orderBookConfig!=null){
            return orderBookConfig;
        }else{
            return new OrderBookConfig();
        }
    }

}
