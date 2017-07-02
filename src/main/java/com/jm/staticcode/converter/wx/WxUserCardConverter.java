package com.jm.staticcode.converter.wx;


import com.jm.mvc.vo.wx.wxuser.WxUserCardCo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardUo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardVo;
import com.jm.repository.po.wx.WxUserCard;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

public class WxUserCardConverter {

    public static WxUserCard u2p(WxUserCardUo cardUo){
        WxUserCard card = new WxUserCard();
        BeanUtils.copyProperties(cardUo,card);
        return card;
    }

    public static WxUserCard c2p(WxUserCardCo cardCo){
        WxUserCard card = new WxUserCard();
        BeanUtils.copyProperties(cardCo,card);
        return card;
    }

    public static WxUserCard v2p (WxUserCardVo cardVo){
        WxUserCard card = new WxUserCard();
        BeanUtils.copyProperties(cardVo,card);
        return card;
    }

    public static WxUserCardVo p2v(WxUserCard card) {
        WxUserCardVo cardVo = new WxUserCardVo();
        BeanUtils.copyProperties(card,cardVo);
        return cardVo;
    }
}
