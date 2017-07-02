package com.jm.staticcode.converter.system;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.join.DispatchJoinVo;
import com.jm.mvc.vo.system.*;
import com.jm.repository.po.system.*;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 聚社区转化
 * @author whh
 *
 */
public class CommunityConverter {

	public static JmCommunity toJmCommunity(CommunityCo communityCo) {
		JmCommunity jmCommunity = new JmCommunity();
		BeanUtils.copyProperties(communityCo, jmCommunity);
		jmCommunity.setHeadImg(ImgUtil.substringUrl(jmCommunity.getHeadImg()));
		return jmCommunity;
	}

	public static CommunityVo toCommunityVo(JmCommunity jmCommunity) {
		CommunityVo communityVo = new CommunityVo();
		Toolkit.copyPropertiesIgnoreNull(jmCommunity, communityVo);
		communityVo.setHeadImg(ImgUtil.appendUrl(communityVo.getHeadImg()));
		return communityVo;
	}

	public static JmPost toJmPost(PostCo postCo) {
		JmPost jmPost = new JmPost();
		BeanUtils.copyProperties(postCo,jmPost);
		jmPost.setStatus(postCo.getStatus());
		return jmPost;
	}

	public static List<PostVo> toPostVos(List<JmPost> jmPosts) {
		List<PostVo> postVos = new ArrayList<>();
		for(JmPost post : jmPosts){
			PostVo postVo = new PostVo();
			BeanUtils.copyProperties(post,postVo);
			postVos.add(postVo);
		}
		return postVos;
	}

	public static PageItem<PostVo> toPostVos(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<PostVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<PostVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			PostVo postVo = JsonMapper.map2Obj(map,PostVo.class);
			postVo.setHeadImg(ImgUtil.appendUrl(postVo.getHeadImg()));
			list.add(postVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static PostVo toPostVo(JmPost jmPost) {
		PostVo postVo = new PostVo();
		BeanUtils.copyProperties(jmPost,postVo);
		return postVo;
	}

	public static PostReply toPostReply(ReplyCo replyCo) {
		PostReply postReply = new PostReply();
		BeanUtils.copyProperties(replyCo,postReply);
		return postReply;
	}

	public static PageItem<ReplyVo> toReplyVos(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<ReplyVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<ReplyVo> list = new ArrayList<>();
		Integer index = 1;
		for (Map<String,Object> map : maps){
			ReplyVo replyVo = JsonMapper.map2Obj(map,ReplyVo.class);
			replyVo.setHeadImg(ImgUtil.appendUrl(replyVo.getHeadImg()));
			replyVo.setIndex(index);
			list.add(replyVo);
			index++;
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static ReadZan toReadZan(ReadZanCo readZanCo) {
		ReadZan readZan  = new ReadZan();
		BeanUtils.copyProperties(readZanCo,readZan);
		return readZan;
	}
}
