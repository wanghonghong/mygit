package com.jm.repository.jpa.wb;

import com.jm.repository.po.shop.WxMenu;
import com.jm.repository.po.wb.WbMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>微博菜单</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/6 11:11
 */
public interface WbMenuRepository extends JpaRepository<WbMenu,Integer> {

    List<WbMenu> findWbMenuByShopId(Integer shopId);

    @Transactional
    @Modifying
    @Query(value="delete from WbMenu where shopId=?1")
    void deleteByShopId(Integer shopId);
}
