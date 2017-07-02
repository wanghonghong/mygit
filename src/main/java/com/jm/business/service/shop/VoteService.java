package com.jm.business.service.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.vote.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.vote.VoteItemRepository;
import com.jm.repository.jpa.shop.vote.VoteRelRepository;
import com.jm.repository.jpa.shop.vote.VoteThemeRepository;
import com.jm.repository.po.shop.vote.VoteItem;
import com.jm.repository.po.shop.vote.VoteRel;
import com.jm.repository.po.shop.vote.VoteTheme;
import com.jm.staticcode.converter.shop.vote.VoteItemConverter;
import com.jm.staticcode.converter.shop.vote.VoteRelConverter;
import com.jm.staticcode.converter.shop.vote.VoteThemeConverter;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zx on 2017/3/31.
 */
@Service
public class VoteService {

    @Autowired
    private JdbcUtil jdbcUtil;

    @Autowired
    private VoteThemeRepository voteThemeRepository;

    @Autowired
    private VoteItemRepository voteItemRepository;

    @Autowired
    private VoteRelRepository voteRelRepository;

    public VoteItem saveVoteItem(VoteItemCo voteItemCo) {
        VoteItem voteItem = VoteItemConverter.toVoteItem(voteItemCo);
        return voteItemRepository.save(voteItem);
    }

    public VoteItem updateVoteItem(VoteItemUo voteItemUo) {
        VoteItem voteItem = voteItemRepository.findOne(voteItemUo.getId());
        voteItem = VoteItemConverter.toVoteItem(voteItemUo, voteItem);
        return voteItemRepository.save(voteItem);
    }


    public VoteItem delVoteItem(VoteItemUo voteItemUo) {
        VoteItem voteItem = voteItemRepository.findOne(voteItemUo.getId());
        voteItem.setStatus(9);
        return voteItemRepository.save(voteItem);
    }

    public PageItem<VoteItemVo> getVoteItems(VoteItemQo voteItemQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select  v.*,s.res_type,s.res_name,s.res_url from  vote_item v " +
                "LEFT JOIN shop_resource s on v.res_id=s.id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("v.shop_id", voteItemQo.getShopId()));
        if(voteItemQo.getVoteType()!=0){
            sqlCondition.append(JdbcUtil.appendAnd("v.vote_type", voteItemQo.getVoteType()));
        }
        sqlCondition.append(JdbcUtil.appendLike("v.name", voteItemQo.getName()));
        sqlCondition.append(JdbcUtil.appendAnd("v.create_time", voteItemQo.getBeginCreateTime(), voteItemQo.getEndCreateTime()));
        sqlCondition.append(JdbcUtil.appendAnd("v.status", voteItemQo.getStatus()));
        PageItem<VoteItemVo> pageItem = jdbcUtil.queryPageItem(sql + sqlCondition, voteItemQo.getCurPage(), voteItemQo.getPageSize(), VoteItemVo.class);
        return VoteItemConverter.toVoteItemVo(pageItem);
    }

    public VoteItemVo getVoteItem(Integer Id) throws IllegalAccessException, IOException, InstantiationException {
//        VoteItemVo voteItemVo=new VoteItemVo();
//        VoteItem voteItem=voteItemRepository.findOne(Id);
//        voteItemVo=VoteItemConverter.toVoteItemVo(voteItem);
        String sql = "select  v.*,s.res_type,s.res_name,s.res_url from  vote_item v " +
                "LEFT JOIN shop_resource s on v.res_id=s.id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("v.id", Id));
        VoteItemVo voteItemVo = jdbcUtil.findOne(sql+sqlCondition,VoteItemVo.class);
        if(voteItemVo.getVoteType()==1||voteItemVo.getVoteType()==3){
            voteItemVo.setResUrl(ImgUtil.appendUrl(voteItemVo.getResUrl()));
        }
        return  voteItemVo;
    }

    public PageItem<VoteThemeVo> getVoteThemes(VoteThemeQo voteThemeQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select  v.*,COUNT(r.theme_id) as votenum from  vote_theme v LEFT JOIN vote_rel r on v.id=r.theme_id  where 1=1 and v.status!=9";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("v.shop_id", voteThemeQo.getShopId()));
        if(voteThemeQo.getVoteType()!=0){
            sqlCondition.append(JdbcUtil.appendAnd("v.vote_type", voteThemeQo.getVoteType()));
        }
        sqlCondition.append(JdbcUtil.appendLike("v.name", voteThemeQo.getName()));
        sqlCondition.append(JdbcUtil.appendAnd("v.start_time", voteThemeQo.getBeginStartTime(), voteThemeQo.getEndStartTime()));
        sqlCondition.append(JdbcUtil.appendAnd("v.end_time", voteThemeQo.getBeginEndTime(), voteThemeQo.getEndEndTime()));
        if(voteThemeQo.getStatus()!=0){
            sqlCondition.append(JdbcUtil.appendAnd("v.status", voteThemeQo.getStatus()));
        }
        sqlCondition.append(JdbcUtil.appendGroupBy("r.theme_id"));
        PageItem<VoteThemeVo> pageItem = jdbcUtil.queryPageItem(sql + sqlCondition, voteThemeQo.getCurPage(), voteThemeQo.getPageSize(), VoteThemeVo.class);
        return pageItem;
    }

    /**
     * 获取一个投票主题关联的投票选项
     * @param themeId
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InstantiationException
     */
    public List<VoteRelVo> getVoteRels(Integer themeId) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT v.`name`,v.res_id,v.vote_type,s.res_type,s.res_name,s.res_url,r.item_id,r.id,r.theme_id,r.votes_num    from vote_item v " +
                " LEFT JOIN vote_rel r on v.id=r.item_id " +
                " LEFT JOIN shop_resource s on v.res_id=s.id where 1=1 "   ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("r.theme_id",themeId));
        List<VoteRelVo> list=   jdbcUtil.queryList(sql+sqlCondition,VoteRelVo.class);
        VoteRelConverter.toVoteRelVo(list);
        return   list;
    }

    public VoteThemeVo getVoteTheme(Integer Id){
        VoteThemeVo voteThemeVo=new VoteThemeVo();
        VoteTheme voteTheme=voteThemeRepository.findOne(Id);
        voteThemeVo=VoteThemeConverter.toVoteThemeVo(voteTheme);
        return voteThemeVo;
    }

    @Transactional
    public void saveVoteTheme(VoteThemeCo voteThemeCo) {
        VoteTheme voteTheme = VoteThemeConverter.toVoteTheme(voteThemeCo);
        voteTheme = voteThemeRepository.save(voteTheme);
        Integer themeId = voteTheme.getId();
        List<VoteRelCo> voteRelCoList = voteThemeCo.getVoteRelList();
        List<VoteRel> voteRelList = new ArrayList<VoteRel>();
        if (voteRelCoList != null && voteRelCoList.size() > 0) {
            for (VoteRelCo voteRelCo : voteRelCoList) {
                VoteRel voteRel= VoteRelConverter.toVoteRel(voteRelCo);
                voteRel.setThemeId(themeId);
                voteRel.setVotesNum(0);
                voteRelList.add(voteRel);
            }
            voteRelRepository.save(voteRelList);
        }
    }

    /**
     * 是否允许操作内容对象
     * @return
     */
    public Boolean  allowOptItem(VoteItem voteItem) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT i.*  from vote_item i  LEFT JOIN vote_rel r on i.id=r.item_id " +
                "LEFT JOIN vote_theme t on r.theme_id=t.id   where 1=1 and  t.`status`!=9" ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("i.id",voteItem.getId()));
        if(voteItem.getStatus()!=0){
            sqlCondition.append(JdbcUtil.appendAnd("t.`status`",voteItem.getStatus()));
        }
        List<VoteItemVo> list=   jdbcUtil.queryList(sql+sqlCondition,VoteItemVo.class);
        if( list!=null&&list.size()>0){
            return  true;
        }else{
            return  false;
        }
    }

    @Transactional
    public void  updateVoteTheme(VoteThemeUo voteThemeUo){
        Integer themeId = voteThemeUo.getId();
        VoteTheme voteTheme=voteThemeRepository.findOne(themeId );
        voteThemeUo.setStatus(voteTheme.getStatus());
        voteThemeUo.setCreateTime(voteTheme.getCreateTime());
        voteTheme=VoteThemeConverter.toVoteTheme(voteThemeUo,voteTheme);
        voteThemeRepository.save(voteTheme);
        List<VoteRel> voteRelDelList= voteThemeUo.getVoteRelDelList();
        List<VoteRelUo> voteRelUoList=voteThemeUo.getVoteRelList();
        List<VoteRel> voteRelList=new ArrayList<VoteRel>();
        if(voteRelDelList!=null&&voteRelDelList.size()>0){
            voteRelRepository.delete(voteRelDelList);
        }
        if(voteRelUoList!=null&&voteRelUoList.size()>0){
            for (VoteRelUo voteRelUo:voteRelUoList){
                voteRelUo.setThemeId(themeId);
                VoteRel VoteRel=VoteRelConverter.toVoteRel(voteRelUo);
                voteRelList.add(VoteRel);
            }
            voteRelRepository.save(voteRelList);
        }
    }


    public VoteTheme  delVoteTheme(VoteThemeUo voteThemeUo){
        VoteTheme voteTheme=voteThemeRepository.findOne(voteThemeUo.getId());
        voteTheme.setStatus(9);
         return voteThemeRepository.save(voteTheme);
    }

    /**
     * 根据一个主题id 返回一个投票结果列表
     * @param voteRelQo
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InstantiationException
     */
    public PageItem<VoteRelVo> getVoteRels(VoteRelQo voteRelQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "  SELECT v.`name`,v.res_id,v.vote_type,v.detail,s.res_type,s.res_name,s.res_url,r.item_id,r.id,r.theme_id,r.votes_num  from vote_item v  " +
                " LEFT JOIN vote_rel r on v.id=r.item_id  " +
                "  LEFT JOIN shop_resource s on v.res_id=s.id where 1=1 " ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("r.theme_id",voteRelQo.getThemeId()));
        sqlCondition.append(JdbcUtil.appendOrderBy("r.votes_num"));
//        List<VoteRelVo> list=   jdbcUtil.queryList(sql+sqlCondition,VoteRelVo.class);
        PageItem<VoteRelVo> pageItem = jdbcUtil.queryPageItem(sql + sqlCondition, voteRelQo.getCurPage(), voteRelQo.getPageSize(), VoteRelVo.class);
        VoteRelConverter.toVoteRelVo(pageItem);
        return pageItem;
    }

    /**
     * 定时修改投票主题状态
     */
    @Transactional
    public void updateVoteThemeStatus() {
        voteThemeRepository.updateThemeTaskIng();
        voteThemeRepository.updateThemeTaskOver();
    }


}
