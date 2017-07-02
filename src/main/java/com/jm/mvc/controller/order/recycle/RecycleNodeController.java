package com.jm.mvc.controller.order.recycle;

import com.jm.business.service.order.recycle.RecycleNodeService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.recycle.RecycleNodeCo;
import com.jm.mvc.vo.order.recycle.RecycleNodeQo;
import com.jm.mvc.vo.order.recycle.RecycleNodeUo;
import com.jm.mvc.vo.order.recycle.RecycleNodeVo;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

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
@RequestMapping(value = "/recycleNode")
public class RecycleNodeController {

	@Autowired
    private RecycleNodeService recycleNodeService;

    @ApiOperation("回收网点列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public PageItem<RecycleNodeVo> findRecycleNodeList(@ApiParam(hidden=true) HttpServletRequest request,
                                                       @RequestBody @Valid RecycleNodeQo recycleNodeQo) throws IOException, InstantiationException, IllegalAccessException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        recycleNodeQo.setShopId(user.getShopId());
        return recycleNodeService.findRecycleNodeByShopId(recycleNodeQo);
    }

    @ApiOperation("查看回收网点")
    @RequestMapping(value = "/query/{id}", method = RequestMethod.GET)
    public RecycleNodeVo findRecycleNode(@ApiParam(hidden=true) HttpServletRequest request,
                                          @PathVariable("id") Integer id) throws IOException, InstantiationException, IllegalAccessException {
        return recycleNodeService.findRecycleNode(id);
    }

    @ApiOperation("新增回收网点")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage add(@ApiParam("回收网点CO") @RequestBody @Valid RecycleNodeCo recycleNodeCo,
                         @ApiParam(hidden=true) HttpServletRequest request) throws ParseException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        recycleNodeCo.setShopId(user.getShopId());
        recycleNodeService.addRecycleNode(recycleNodeCo);
        return new JmMessage(0, "保存成功");
    }

    @ApiOperation("修改回收网点")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JmMessage edit(@ApiParam("订单标识id") @PathVariable("id") Integer id,
                          @ApiParam("回收网点UO") @RequestBody @Valid RecycleNodeUo recycleNodeUo,
                         @ApiParam(hidden=true) HttpServletRequest request) throws ParseException {
        recycleNodeService.updateRecycleNode(id,recycleNodeUo);
        return new JmMessage(0, "修改成功");
    }

    @ApiOperation("修改上下架")
    @RequestMapping(value = "/update_status/{id}", method = RequestMethod.PUT)
    public JmMessage updateStatus(@ApiParam("订单标识id") @PathVariable("id") Integer id,
                                  @ApiParam("回收网点UO") @RequestBody @Valid RecycleNodeUo recycleNodeUo){
        recycleNodeService.updateRecycleNodeStatus(id,recycleNodeUo);
        return new JmMessage(0,"修改成功");
    }

    @ApiOperation("删除回收网点")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JmMessage delete(@ApiParam("回收网点一条记录标识") @PathVariable("id") Integer id){
        recycleNodeService.deleteRecycleNode(id);
        return new JmMessage(0, "删除成功");
    }

}
