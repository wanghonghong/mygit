package com.jm.mvc.controller.order.recycle;

import com.jm.business.service.order.recycle.RecycleExplainService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.recycle.RecycleExplainCo;
import com.jm.repository.po.order.recycle.RecycleExplain;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.sun.imageio.plugins.common.ImageUtil;
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
 * <p>回收说明</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/recycleExplain")
public class RecycleExplainController {

	@Autowired
    private RecycleExplainService recycleExplainService;

    @ApiOperation("新增回收说明")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addCard(@ApiParam("新增回收说明Co") @RequestBody @Valid RecycleExplainCo recycleExplainCo,
                             @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        recycleExplainCo.setShopId(user.getShopId());
        RecycleExplain recycleExplain = recycleExplainService.addOrderBookConfig(recycleExplainCo);
        if(recycleExplain!=null){
            return new JmMessage(1,"保存成功！");
        }
        return new JmMessage(0,"保存失败！");
    }

    @ApiOperation("回收说明列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RecycleExplain findCardList(@ApiParam(hidden=true) HttpServletRequest request) throws IOException, InstantiationException, IllegalAccessException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        RecycleExplain recycleExplain = recycleExplainService.queryRecycleExplain(user.getShopId());
        if(recycleExplain!=null){
            return recycleExplain;
        }else{
            return new RecycleExplain();
        }
    }

}
