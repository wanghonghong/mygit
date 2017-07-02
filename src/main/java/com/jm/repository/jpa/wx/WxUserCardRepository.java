package com.jm.repository.jpa.wx;



import com.jm.mvc.vo.wx.wxuser.WxUserCardVo;
import com.jm.repository.po.wx.WxUserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WxUserCardRepository extends JpaRepository<WxUserCard, Integer> {



    @Query(value="select card.*,sc.shop_id,sc.buy_money,sc.card_money,sc.card_name,sc.user_condition,sc.user_limit from wx_user_card card left join shop_card sc on sc.id = card.card_id and sc.shop_id = ?1 where card.status=0 and card.user_id = ?2 ",nativeQuery=true)
    List<WxUserCardVo> findUserCardByUserId(int userId, int shopId);

    @Query(value="select * from wx_user_card card left join shop_card sc on sc.id = card.card_id and sc.shop_id = ?2 where card.status=0 and card.user_id = ?1 and card.start_time <= sysdate() and card.end_time>= sysdate()" ,nativeQuery=true)
    List<WxUserCardVo> findUserCardCanBeUse(int userId,int shopId);

    @Query(value = "select card.*,card.id as ticket_id,sc.shop_id,sc.buy_money,sc.card_money,sc.card_name,sc.user_condition,sc.user_limit from wx_user_card card left join  shop_card sc on sc.id = card.card_id  where card.status=1 and card.id =?1 " ,nativeQuery=true)
    WxUserCardVo findUserCardVoByUserCardId(int userCardId);

    @Query(value="select count(card.id) from wx_user_card card where card.card_id = ?1 and card.user_id = ?2 ",nativeQuery=true)
    int isHasTicket(int cardId,int userId);
    @Query(value="update wx_user_card set status=1 where id=?1",nativeQuery=true)
    void useCard(Integer wxUserCardId);

    @Query(value = "select count(*) from wx_user_card r where r.user_id=?1 ", nativeQuery = true)
    Object cardCountByUserId(Integer wxuserid);

    @Query(value = "select count(*) from wx_user_card r where r.user_id=?1 and r.status = ?2 ", nativeQuery = true)
    Object cardCountByUserIdAndStatus(Integer wxuserid,Integer status);
}
