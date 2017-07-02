package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.impl.QuestionServiceImpl;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.activity.question.*;
import com.jm.repository.po.shop.activity.question.QuestionActivity;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

//import CosCloud;
//import CosBaseConf;

/**
 * <p>
 * </p>
 *
 * @author zhengww
 * @version latest
 * @date 2016年5月20日
 */
@Api
@RestController
@RequestMapping(value = "/question")
public class QuestionActivityController {

	@Autowired
	private QuestionServiceImpl questionServiceImpl;

	@ApiOperation("新增问题")
	@RequestMapping(value = "/question", method = RequestMethod.POST)
	public JmMessage saveQuestion(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增问题VO") @RequestBody @Valid ShopQuestionCo shopQuestionCo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		questionServiceImpl.saveQuestions(shopQuestionCo, user.getShopId());
		return new JmMessage(0,"新增成功");
	}

	@ApiOperation("修改问题")
	@RequestMapping(value = "/question/{id}", method = RequestMethod.PUT)
	public JmMessage updateQuestion(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改问题VO") @RequestBody @Valid ShopQuestionUo shopQuestionUo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		questionServiceImpl.updateQuestion(shopQuestionUo, user.getShopId());
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("获取单个问题")
	@RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
	public ShopQuestionVo getQuestion(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("问题id") @PathVariable("id") Integer id) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		return questionServiceImpl.getQuestion(id,user.getShopId());
	}

	@ApiOperation("删除问题,支持批量，伪删除")
	@RequestMapping(value = "/question/{ids}", method = RequestMethod.DELETE)
	public JmMessage deleteQuestion(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("问题id") @PathVariable("id") String ids) {
		questionServiceImpl.updateStatus(ids);
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("新增问卷")
	@RequestMapping(value = "/question_activity", method = RequestMethod.POST)
	public JmMessage saveQuestionActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增问卷VO") @RequestBody @Valid QuestionActivityCo questionActivityCo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		questionServiceImpl.saveQuestionActivity(questionActivityCo, user.getShopId());
		return new JmMessage(0,"新增成功");
	}

	@ApiOperation("修改问卷")
	@RequestMapping(value = "/question_activity/{id}", method = RequestMethod.PUT)
	public JmMessage updateQuestionActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改问卷VO") @RequestBody @Valid QuestionActivityUo questionActivityUo) {
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		questionServiceImpl.updateQuestionActivity(questionActivityUo, user.getShopId());
		return new JmMessage(0,"修改成功");
	}

	@ApiOperation("获取单个问卷")
	@RequestMapping(value = "/question_activity/{id}", method = RequestMethod.GET)
	public QuestionActivityVo getQuestionActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("问卷id") @PathVariable("id") Integer id) {
		return questionServiceImpl.getQuestionActivity(id);
	}

	@ApiOperation("删除问卷,支持批量，伪删除")
	@RequestMapping(value = "/question_activity/{ids}", method = RequestMethod.DELETE)
	public JmMessage deleteQuestionActivity(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("问卷id") @PathVariable("id") String ids) {
		questionServiceImpl.deleteQuestionActivity(ids);
		return new JmMessage(0,"修改成功");
	}
}