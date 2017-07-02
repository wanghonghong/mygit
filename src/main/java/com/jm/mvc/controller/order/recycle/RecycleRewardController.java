package com.jm.mvc.controller.order.recycle;

import com.jm.business.service.order.recycle.RecycleRewardService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.order.recycle.RecycleRewardCo;
import com.jm.mvc.vo.order.recycle.RecycleRewardVo;
import com.jm.repository.po.order.recycle.RecycleReward;
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
 * <p>奖励方式</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6/006
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/recycleReward")
public class RecycleRewardController {

	@Autowired
    private RecycleRewardService recycleRewardService;

	@ApiOperation("新增奖励方式")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JmMessage addRecycleReward(@ApiParam("新增奖励方式Co") @RequestBody @Valid RecycleRewardCo recycleRewardCo,
							 @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		recycleRewardCo.setShopId(user.getShopId());
		RecycleReward recycleReward = recycleRewardService.addRecycleReward(recycleRewardCo);
		if(recycleReward!=null){
			return new JmMessage(1,"保存成功！");
		}
		return new JmMessage(0,"保存失败！");
	}

	@ApiOperation("奖励方式列表")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public RecycleRewardVo findCardList(@ApiParam(hidden=true) HttpServletRequest request) throws IOException, InstantiationException, IllegalAccessException {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		RecycleRewardVo recycleRewardVo = recycleRewardService.queryRecycleReward(user.getShopId());
		if(recycleRewardVo!=null){
			return recycleRewardVo;
		}else{
			return  new RecycleRewardVo();
		}
	}
}
