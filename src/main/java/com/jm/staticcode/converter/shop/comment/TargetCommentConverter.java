package com.jm.staticcode.converter.shop.comment;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.comment.TargetCommentCo;
import com.jm.mvc.vo.shop.comment.TargetCommentUo;
import com.jm.mvc.vo.shop.comment.TargetCommentVo;
import com.jm.repository.po.shop.comment.TargetComment;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by zx on 2017/5/2.
 */
public class TargetCommentConverter {

    public  static TargetCommentVo toTargetCommentVo(TargetComment targetComment){
        TargetCommentVo targetCommentVo=new  TargetCommentVo();
        BeanUtils.copyProperties(targetComment,targetCommentVo);
        return  targetCommentVo;
    }

    public  static TargetComment toTargetComment(TargetCommentCo targetCommentCo){
        TargetComment targetComment=new  TargetComment();
        BeanUtils.copyProperties(targetCommentCo,targetComment);
        return  targetComment;
    }

    public  static TargetComment toTargetComment(TargetCommentUo targetCommentUo, TargetComment targetComment){
        BeanUtils.copyProperties(targetCommentUo,targetComment);
        return  targetComment;
    }

//     wxuser.put("nickname",Base64Util.getFromBase64((String) wxuser.get("nickname")));

   public  static PageItem<TargetCommentVo> toTargetCommentVos( PageItem<TargetCommentVo> pageItem){
    for (TargetCommentVo targetCommentVo:pageItem.getItems()){
        targetCommentVo.setNickname(Base64Util.getFromBase64((String) targetCommentVo.getNickname()));
    }
       return  pageItem;
   }
   public  static List<TargetCommentVo> toTargetCommentVos(List<TargetCommentVo> list){
    for (TargetCommentVo targetCommentVo:list){
        targetCommentVo.setNickname(Base64Util.getFromBase64((String) targetCommentVo.getNickname()));
    }
       return  list;
   }



}
