package com.jm.staticcode.converter.shop.vote;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.vote.VoteRelCo;
import com.jm.mvc.vo.shop.vote.VoteRelUo;
import com.jm.mvc.vo.shop.vote.VoteRelVo;
import com.jm.repository.po.shop.vote.VoteRel;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * zx
 */
public class VoteRelConverter {


    public static VoteRelVo toVoteRelVo(VoteRel voteRel){
        VoteRelVo voteRelVo=new VoteRelVo();
        BeanUtils.copyProperties(voteRel,voteRelVo);
        return  voteRelVo;
    }

    public static  VoteRel toVoteRel(VoteRelCo voteRelCo){
        VoteRel voteRel=new VoteRel();
        BeanUtils.copyProperties(voteRelCo,voteRel);
        return  voteRel;
    }

    public static  VoteRel toVoteRel(VoteRelUo voteItemUo ){
        VoteRel voteRel=new VoteRel();
        if(voteItemUo.getVotesNum()==null){
            voteItemUo.setVotesNum(0);
        }
        BeanUtils.copyProperties(voteItemUo,voteRel);
        return  voteRel;
    }

    public static  List<VoteRelVo>  toVoteRelVo(List<VoteRelVo> list) {
        for (VoteRelVo roteRelVo : list ) {
            if(roteRelVo.getVoteType()==1||roteRelVo.getVoteType()==3){
                roteRelVo.setResUrl(ImgUtil.appendUrl(roteRelVo.getResUrl()));
            }
        }
        return  list;
    }
    public static PageItem<VoteRelVo> toVoteRelVo(PageItem<VoteRelVo> pageItem) {
        for (VoteRelVo roteRelVo : pageItem.getItems() ) {
            if(roteRelVo.getVoteType()==1||roteRelVo.getVoteType()==3){
                roteRelVo.setResUrl(ImgUtil.appendUrl(roteRelVo.getResUrl()));
            }
        }
        return  pageItem;
    }
}
