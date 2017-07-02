package com.jm.mvc.controller.shop;

import com.jm.business.service.shop.CommentService;
import com.jm.business.service.shop.imageText.ImageTextService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.comment.*;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * zx
 */
@Api
@RestController
@RequestMapping(value = "/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageTextService imageTextService;


    @ApiOperation("评论列表查询")
    @RequestMapping(value = "/image_comments", method = RequestMethod.POST)
    public  PageItem<ImageCommentVo> getImageComment(@ApiParam(hidden = true) HttpServletRequest request,
                                                     @RequestBody @Valid ImageCommentQo imageCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if(user!=null){
            shopId = Toolkit.parseObjForInt(user.getShopId());
        }
        imageCommentQo.setShopId(shopId);
        return  commentService.getImageComment(imageCommentQo);
    }

    @ApiOperation("评论列表")
    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public PageItem<TargetCommentVo> getTargetComments(@ApiParam(hidden = true) HttpServletRequest request,
                                                  @RequestBody @Valid TargetCommentQo targetCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        PageItem<TargetCommentVo> pageItem=commentService.getTargetComments(targetCommentQo);
        return pageItem ;
    }

    @ApiOperation("评论列表")
    @RequestMapping(value = "/commentlist", method = RequestMethod.POST)
    public List<TargetCommentVo> getTargetCommentList(@ApiParam(hidden = true) HttpServletRequest request,
                                                  @RequestBody @Valid TargetCommentQo targetCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        List<TargetCommentVo> pageItem=commentService.getTargetCommentList(targetCommentQo);
        return pageItem ;
    }

    @ApiOperation("新增评论")
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public JmMessage  saveComment(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增评论") @RequestBody @Valid TargetCommentCo targetCommentCo){
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        targetCommentCo.setCreateTime(new Date());
        targetCommentCo.setStatus(0);
        targetCommentCo.setSort(999);
        targetCommentCo.setTargetType(1);
        targetCommentCo.setUserId(user.getUserId());
        commentService.saveTargetComment(targetCommentCo);
        return new JmMessage(0, "新增评论成功");
    }

    @ApiOperation("修改评论")
    @RequestMapping(value = "/comment", method = RequestMethod.PUT)
    public JmMessage  updateComment(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改评论") @RequestBody @Valid TargetCommentUo targetCommentUo){
        commentService.updateTargetComment(targetCommentUo);
        return new JmMessage(0, "修改评论成功");
    }
//    @ApiOperation("修改排序")
//    @RequestMapping(value = "/comment_sort", method = RequestMethod.PUT)
//    public JmMessage  updateCommentSort(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("修改排序") @RequestBody @Valid TargetCommentUo targetCommentUo){
//        commentService.updateCommentSort(targetCommentUo);
//        return new JmMessage(0, "修改评论成功");
//    }

    @ApiOperation("删除评论")
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
    public JmMessage  deletlVoteTheme(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("删除评论") @PathVariable("id") Integer id) {
        TargetCommentUo targetCommentUo=new TargetCommentUo();
        targetCommentUo.setId(id);
        commentService.delTargetComment(targetCommentUo);
        return new JmMessage(0, "删除成功");
    }




}
