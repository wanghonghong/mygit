package com.jm.mvc.controller.system;

import com.jm.business.service.system.CommunityService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.system.*;
import com.jm.repository.po.system.JmCommunity;
import com.jm.repository.po.system.JmPost;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <p>系统管理</p>
 * 
 * @author whh
 * @version latest
 * @date 2017/1/10
 */

@Api
@RestController
@RequestMapping(value = "/community")
public class CommunityController {

	@Autowired
	private CommunityService communityService;

	@ApiOperation("新增更新社区资料")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public JmMessage saveCommunity(@RequestBody @Valid CommunityCo communityCo,
                                   @ApiParam(hidden=true) HttpServletRequest request) throws IOException {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        JmCommunity jmCommunity = communityService.queryCommunity(user.getUserId());
        if (jmCommunity==null){
            communityCo.setUserId(user.getUserId());
            communityService.saveCommunity(communityCo);
        }else {
            communityService.updateCommunity(user.getUserId(),communityCo);
        }
        return new JmMessage(0,"保存成功！");
    }

    @ApiOperation("获取社区资料")
    @RequestMapping(value = "",method = RequestMethod.GET)
    public CommunityVo getCommunityVo(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        return  communityService.getCommunityVo(user.getUserId());
    }

    @ApiOperation("新增或更新帖子")
    @RequestMapping(value = "/post",method = RequestMethod.POST)
    public JmMessage savePost(@RequestBody @Valid PostCo postCo,
                              @ApiParam(hidden=true) HttpServletRequest request)  {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        postCo.setUserId(user.getUserId());
        if (postCo.getId()==null){
            communityService.savePost(postCo);
        }else {
            communityService.updatePost(postCo);
        }
        return  new JmMessage(0,"发布成功");
    }

    @ApiOperation("获取用户草稿帖子列表")
    @RequestMapping(value = "/post",method = RequestMethod.GET)
    public List<PostVo> getPost(@ApiParam(hidden=true) HttpServletRequest request){
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        return communityService.getPostList(user.getUserId());
    }

    @ApiOperation("分页查询用户帖子列表")
    @RequestMapping(value = "/posts",method = RequestMethod.POST)
    public PageItem<PostVo> getPostsByUserId(@RequestBody @Valid PostQo postQo,
                                     @ApiParam(hidden=true) HttpServletRequest request) throws Exception{
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        postQo.setUserId(user.getUserId());
        return communityService.getPostsByUserId(postQo);
    }

    @ApiOperation("分页查询已发布帖子列表")
    @RequestMapping(value = "/posts/all",method = RequestMethod.POST)
    public PageItem<PostVo> getPosts(@RequestBody @Valid PostQo postQo) throws Exception{
        return communityService.getPosts(postQo);
    }

    @ApiOperation("根据id获取用户已发布帖子")
    @RequestMapping(value = "/post/one",method = RequestMethod.POST)
    public PostVo getPost(@RequestBody @Valid ReadZanCo readZanCo,
                          @ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        readZanCo.setUserId(user.getUserId());
        communityService.saveReadZan(readZanCo);
        return communityService.getPostVo(readZanCo);
    }

    @ApiOperation("保存回复信息")
    @RequestMapping(value = "/reply",method = RequestMethod.POST)
    public JmMessage saveReplyVo(@RequestBody @Valid ReplyCo replyCo,
                                 @ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        replyCo.setUserId(user.getUserId());
        communityService.saveReplyVo(replyCo);
        return new JmMessage(0,"回复成功");
    }

    @ApiOperation("分页查询某个帖子回复列表")
    @RequestMapping(value = "/replys",method = RequestMethod.POST)
    public PageItem<ReplyVo> getReplys(@RequestBody @Valid ReplyQo replyQo) throws Exception{
        return communityService.getReplys(replyQo);
    }

    @ApiOperation("点赞保存")
    @RequestMapping(value = "/post/zan",method = RequestMethod.POST)
    public JmMessage savePostZan(@RequestBody @Valid ReadZanCo readZanCo,
                          @ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        readZanCo.setUserId(user.getUserId());
        communityService.saveReadZan(readZanCo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("聚社区首页vo")
    @RequestMapping(value = "/post/list",method = RequestMethod.GET)
    public CommunityVo getPostList(@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
        return communityService.queryCommunityVo(user.getUserId());
    }
}
