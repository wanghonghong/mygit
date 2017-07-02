package com.jm.repository.jpa.system;

import com.jm.repository.po.shop.WxMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>创建微信菜单</p>
 *
 * @author liangrs
 * @version 1.1
 * @date 2016/8/10
 * */


public interface WxMenuRepository extends JpaRepository<WxMenu, Integer> {
    @Transactional
    @Modifying
    @Query(value="delete from wx_menu where appid=?1",nativeQuery=true)
    void deleteByAppid(String appid);

    List<WxMenu> findWxMenuByAppid(String appid);


    @Query(value = "select * from wx_menu where appid=?1 and url is not null and type='view'",nativeQuery=true)
    List<WxMenu> findMenuByAppid(String appid);   // cj -add 2017-04-17 报表展示菜单

    /*@Query(value = "select * from wx_menu where appid=?1",nativeQuery=true)
    List<WxMenu> findAppid(String appid);*/

   /* @Query(value = "select * from wx_menu where appid=?1 and parent_id=?2",nativeQuery=true)
    List<WxMenu> findAppidAndParentId(String appid,Integer parentId);*/
}
