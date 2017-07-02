package com.jm.staticcode.converter.shop.vote;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.vote.VoteItemCo;
import com.jm.mvc.vo.shop.vote.VoteItemUo;
import com.jm.mvc.vo.shop.vote.VoteItemVo;
import com.jm.repository.po.shop.vote.VoteItem;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * zx
 */
public class VoteItemConverter {

    public static VoteItemVo toVoteItemVo(VoteItem voteItem){
        VoteItemVo voteItemVo=new VoteItemVo();
        BeanUtils.copyProperties(voteItem,voteItemVo);
        if(StringUtil.isNotNull(voteItemVo.getResUrl())&&voteItemVo.getVoteType()==1){
            String resUrl = ImgUtil.appendUrl(voteItemVo.getResUrl(), 720);
            voteItemVo.setResUrl(resUrl);
        }
        return  voteItemVo;
    }


    public static  VoteItem toVoteItem(VoteItemCo voteItemCo){
        VoteItem voteItem=new VoteItem();
        BeanUtils.copyProperties(voteItemCo,voteItem);
//        if(StringUtil.isNotNull(voteItem.getResUrl())&&voteItem.getVoteType()==1){
//            String resUrl = ImgUtil.substringUrl(voteItem.getResUrl());
//            voteItem.setResUrl(resUrl);
//        }
        return  voteItem;
    }



    public static  VoteItem toVoteItem(VoteItemUo voteItemUo, VoteItem voteItem){
        BeanUtils.copyProperties(voteItemUo,voteItem);
//        if(StringUtil.isNotNull(voteItem.getResUrl())&&voteItem.getVoteType()==1){
//            String resUrl = ImgUtil.substringUrl(voteItem.getResUrl());
//            voteItem.setResUrl(resUrl);
//        }
        return  voteItem;
    }

    public static PageItem<VoteItemVo> toVoteItemVo(PageItem<VoteItemVo> pageItem) {
        for (VoteItemVo voteItemVo : pageItem.getItems() ) {
            if(voteItemVo.getVoteType()==1||voteItemVo.getVoteType()==3){
                voteItemVo.setResUrl(ImgUtil.appendUrl(voteItemVo.getResUrl()));
            }
        }
        return  pageItem;
    }

    public static List<VoteItemVo> toVoteItemVo(List<VoteItemVo> list) {
        for (VoteItemVo voteItemVo : list ) {
            if(voteItemVo.getVoteType()==1||voteItemVo.getVoteType()==3){
                voteItemVo.setResUrl(ImgUtil.appendUrl(voteItemVo.getResUrl()));
            }
        }
        return  list;
    }
}
