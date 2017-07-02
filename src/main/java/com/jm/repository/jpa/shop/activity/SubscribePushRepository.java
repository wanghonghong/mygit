package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.SubscribePush;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * <p>关注推送</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public interface SubscribePushRepository extends JpaRepository<SubscribePush, Integer>{

    List<SubscribePush> findByShopId(Integer shopId);

    @Modifying
    @Query(value="delete from SubscribePush where shopId= ?1 ")
    void deleteByShopId(int shopId);

    @Modifying
    @Query(value="update shop set is_subscribe_push = ?1 where shop_id=?2",nativeQuery=true)
    void updateSubscribePush(Integer subscribePush,Integer shopId);
}
