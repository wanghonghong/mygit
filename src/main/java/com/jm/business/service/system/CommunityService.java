package com.jm.business.service.system;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.system.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.CommunityRepository;
import com.jm.repository.jpa.system.PostRepository;
import com.jm.repository.jpa.system.ReadZanRepository;
import com.jm.repository.jpa.system.ReplyRepository;
import com.jm.repository.po.system.JmCommunity;
import com.jm.repository.po.system.JmPost;
import com.jm.repository.po.system.PostReply;
import com.jm.repository.po.system.ReadZan;
import com.jm.staticcode.converter.system.CommunityConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/10
 */
@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReadZanRepository readZanRepository;

    public JmCommunity queryCommunity(Integer userId){
        return communityRepository.findByUserId(userId);
    }
    public JmCommunity saveCommunity(CommunityCo communityCo){
        return communityRepository.save(CommunityConverter.toJmCommunity(communityCo));
    }
    public JmCommunity updateCommunity(Integer userId,CommunityCo communityCo){
        JmCommunity jmCommunity = communityRepository.findByUserId(userId);
        Toolkit.copyPropertiesIgnoreNull(communityCo,jmCommunity);
        jmCommunity.setHeadImg(ImgUtil.substringUrl(jmCommunity.getHeadImg()));
        return communityRepository.save(jmCommunity);
    }

    public CommunityVo getCommunityVo(Integer userId){
        JmCommunity jmCommunity = communityRepository.findByUserId(userId);
        return CommunityConverter.toCommunityVo(jmCommunity);
    }

    public JmPost savePost(PostCo postCo){
        return postRepository.save(CommunityConverter.toJmPost(postCo));
    }

    public JmPost updatePost(PostCo postCo){
        JmPost jmPost = postRepository.findById(postCo.getId());
        Toolkit.copyPropertiesIgnoreNull(postCo,jmPost);
        jmPost.setStatus(postCo.getStatus());
        jmPost.setCreateDate(new Date());
        return postRepository.save(jmPost);
    }

    public List<PostVo> getPostList(Integer userId){
        List<JmPost> jmPosts = postRepository.findByUserIdAndStatus(userId,2);
        return CommunityConverter.toPostVos(jmPosts);
    }

    public PageItem<PostVo> getPostsByUserId(PostQo postQo) throws Exception{
        String sqlList = "select * from jm_post where status=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("user_id",postQo.getUserId()));
        sqlCondition.append(" order by create_date desc");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, postQo.getCurPage(), postQo.getPageSize());
        return CommunityConverter.toPostVos(pageItem);
    }

    public PageItem<PostVo> getPosts(PostQo postQo) throws Exception{
        String sqlList = "select c.head_img,c.nickname,p.*, " +
                " ifnull(d.read_count,0) as read_count, " +
                " ifnull(d.zan_count,0) as zan_count, " +
                " ifnull(e.reply_count,0) as reply_count " +
                " from jm_post p " +
                " LEFT JOIN jm_community c on p.user_id=c.user_id " +
                " Left join (select post_id,sum(case when type=1 then 1 end) as read_count,sum(case when type=2 then 1 end) as zan_count " +
                " from read_zan group by post_id )d on p.id = d.post_id " +
                " left join (select post_id,count(1) as reply_count from post_reply group by post_id ) " +
                " e on p.id = e.post_id " +
                " where p.status=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("p.feature",postQo.getFeature()));
        sqlCondition.append(JdbcUtil.appendLike("p.title",postQo.getTitle()));
        sqlCondition.append(" order by p.create_date desc");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, postQo.getCurPage(), postQo.getPageSize());
        return CommunityConverter.toPostVos(pageItem);
    }

    public PostVo getPostVo(ReadZanCo readZanCo) {
        PostVo postVo = CommunityConverter.toPostVo(postRepository.findById(readZanCo.getPostId()));
        postVo.setReadCount(readZanRepository.queryReadCount(1,readZanCo.getPostId()));
        postVo.setZanCount(readZanRepository.queryZanCount(2,readZanCo.getPostId()));
        postVo.setReplyCount(replyRepository.queryReplyCount(readZanCo.getPostId()));
        ReadZan readZan = readZanRepository.findByTypeAndUserIdAndPostId(2,readZanCo.getUserId(),readZanCo.getPostId());
        if (readZan!=null){
            postVo.setIsZan(1);
        }else{
            postVo.setIsZan(0);
        };
        JmCommunity jmCommunity = communityRepository.findByUserId(readZanCo.getUserId());
        postVo.setHeadImg(ImgUtil.appendUrl(jmCommunity.getHeadImg()));
        postVo.setNickname(jmCommunity.getNickname());
        postVo.setPostNum(postRepository.queryPostNum(readZanCo.getUserId(),1));
        return  postVo;
    }

    public PostReply saveReplyVo(ReplyCo replyCo) {
        JmCommunity jmCommunity = communityRepository.findByUserId(replyCo.getUserId());
        replyCo.setHeadImg(jmCommunity.getHeadImg());
        replyCo.setNickname(jmCommunity.getNickname());
        return replyRepository.save(CommunityConverter.toPostReply(replyCo));
    }

    public PageItem<ReplyVo> getReplys(ReplyQo replyQo) throws Exception{
        String sqlList = "select * from post_reply where 1=1 " ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("post_id",replyQo.getPostId()));
        sqlCondition.append(" order by create_date desc");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, replyQo.getCurPage(), replyQo.getPageSize());
        return CommunityConverter.toReplyVos(pageItem);
    }

    public ReadZan saveReadZan(ReadZanCo readZanCo) {
        ReadZan readZan = readZanRepository.findByTypeAndUserIdAndPostId(readZanCo.getType(),readZanCo.getUserId(),readZanCo.getPostId());
        if (readZan==null){
            readZanRepository.save(CommunityConverter.toReadZan(readZanCo));
        }
        return readZan;
    }

    public CommunityVo queryCommunityVo(Integer userId){
        CommunityVo communityVo = new CommunityVo();
        communityVo.setJmPosts(postRepository.queryPostList());
        communityVo.setPostNum(postRepository.queryPostNums());
        communityVo.setNickname(communityRepository.findByUserId(userId).getNickname());
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("EEEE");
        communityVo.setDate(format.format(date));
        communityVo.setWeek(format2.format(date));
        return communityVo;
    }
}
