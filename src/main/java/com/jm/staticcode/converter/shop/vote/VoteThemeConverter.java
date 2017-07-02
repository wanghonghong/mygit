package com.jm.staticcode.converter.shop.vote;

import com.jm.mvc.vo.shop.vote.VoteThemeCo;
import com.jm.mvc.vo.shop.vote.VoteThemeUo;
import com.jm.mvc.vo.shop.vote.VoteThemeVo;
import com.jm.repository.po.shop.vote.VoteTheme;
import org.springframework.beans.BeanUtils;

/**
 * zx
 */
public class VoteThemeConverter {

    public  static VoteThemeVo toVoteThemeVo(VoteTheme VoteTheme){
        VoteThemeVo voteThemeVo=new VoteThemeVo();
        BeanUtils.copyProperties(VoteTheme,voteThemeVo);
        return  voteThemeVo;
    }

    public static VoteTheme  toVoteTheme(VoteThemeCo voteThemeCo){
        VoteTheme voteTheme=new VoteTheme();
        BeanUtils.copyProperties(voteThemeCo,voteTheme);
        return    voteTheme;
    }

     public static VoteTheme  toVoteTheme(VoteThemeUo voteThemeUo, VoteTheme voteTheme){
        BeanUtils.copyProperties(voteThemeUo,voteTheme);
        return    voteTheme;
    }


}
