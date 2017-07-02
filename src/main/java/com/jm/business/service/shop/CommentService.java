package com.jm.business.service.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.comment.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.comment.TargetCommentRepository;
import com.jm.repository.po.shop.comment.TargetComment;
import com.jm.staticcode.converter.shop.comment.TargetCommentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * zx
 */
@Service
public class CommentService {

    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private TargetCommentRepository targetCommentRepository;

    public PageItem<ImageCommentVo> getImageComment(ImageCommentQo imageCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT i.id,i.image_text_tile,i.create_time,0 as pv ,0 as uv,i.reward, IFNULL(count(t.target_id), 0) AS comment_num " +
                "FROM image_text i LEFT JOIN target_comment t ON i.id = t.target_id AND t.`status` != 9 and t.target_type=1 and t.pid=0 where 1=1 " ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("i.shop_id", imageCommentQo.getShopId()));
        sqlCondition.append(JdbcUtil.appendLike("i.image_text_tile", imageCommentQo.getImageTextTile()));
        sqlCondition.append(JdbcUtil.appendAnd("i.create_time", imageCommentQo.getBeginCreateTime(), imageCommentQo.getEndCreateTime()));
        sqlCondition.append(JdbcUtil.appendGroupBy("i.id"));
        sqlCondition.append(jdbcUtil.appendOrderBy("i.create_time"));
        PageItem<ImageCommentVo> pageItem = jdbcUtil.queryPageItem(sql + sqlCondition, imageCommentQo.getCurPage(), imageCommentQo.getPageSize(), ImageCommentVo.class);
        return pageItem;
    }


    public TargetComment saveTargetComment(TargetCommentCo targetCommentCo) {
        TargetComment targetComment = TargetCommentConverter.toTargetComment(targetCommentCo);
        return targetCommentRepository.save(targetComment);
    }

    public TargetComment updateTargetComment(TargetCommentUo targetCommentUo) {
        TargetComment targetComment = targetCommentRepository.findOne(targetCommentUo.getId());
        targetComment = TargetCommentConverter.toTargetComment(targetCommentUo, targetComment);
        return targetCommentRepository.save(targetComment);
    }

    public PageItem<TargetCommentVo> getTargetComments(TargetCommentQo targetCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT t.*,w.nickname,w.headimgurl FROM target_comment t LEFT JOIN wx_user w on t.user_id=w.user_id  where 1=1 and t.`status`!=9 and t.pid=0";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("t.target_id", targetCommentQo.getTargetId()));
        sqlCondition.append(JdbcUtil.appendAnd("t.target_type", targetCommentQo.getTargetType()));
        sqlCondition.append(JdbcUtil.appendAnd("t.id", targetCommentQo.getId()));
//        sqlCondition.append(JdbcUtil.appendIn("t.pid", targetCommentQo.getPids()));
        sqlCondition.append(JdbcUtil.appendAnd("t.`status`", targetCommentQo.getStatus()));
        sqlCondition.append(JdbcUtil.appendOrderBy(new String[]{" t.sort", " t.create_time desc"}));
        PageItem<TargetCommentVo> pageItem = jdbcUtil.queryPageItem(sql + sqlCondition, targetCommentQo.getCurPage(), targetCommentQo.getPageSize(), TargetCommentVo.class);
        return TargetCommentConverter.toTargetCommentVos(pageItem);
    }
    public List<TargetCommentVo> getTargetCommentList(TargetCommentQo targetCommentQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT t.*,w.nickname,w.headimgurl FROM target_comment t LEFT JOIN wx_user w on t.user_id=w.user_id  where 1=1 and t.`status`!=9 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("t.target_id", targetCommentQo.getTargetId()));
        sqlCondition.append(JdbcUtil.appendAnd("t.target_type", targetCommentQo.getTargetType()));
        sqlCondition.append(JdbcUtil.appendAnd("t.id", targetCommentQo.getId()));
        sqlCondition.append(JdbcUtil.appendIn("t.pid", targetCommentQo.getPids()));
        sqlCondition.append(JdbcUtil.appendAnd("t.`status`", targetCommentQo.getStatus()));
        sqlCondition.append(JdbcUtil.appendOrderBy(new String[]{" t.sort", " t.create_time desc"}));
        List<TargetCommentVo> list = jdbcUtil.queryList(sql + sqlCondition,   TargetCommentVo.class);
        return TargetCommentConverter.toTargetCommentVos(list);
    }
    public TargetComment  delTargetComment(TargetCommentUo targetCommentUo){
        TargetComment targetComment = targetCommentRepository.findOne(targetCommentUo.getId());
        targetComment.setStatus(9);
        return targetCommentRepository.save(targetComment);
    }
//    public TargetComment  updateCommentSort(TargetCommentUo targetCommentUo){
//        TargetComment targetComment = targetCommentRepository.findOne(targetCommentUo.getId());
//        targetComment.setSort(targetCommentUo.getSort());
//        return targetCommentRepository.save(targetComment);
//    }

}
