package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.shopSet.ShopTipSet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>店铺打赏配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/20 10:20
 */
public interface ShopTipSetRepository extends JpaRepository<ShopTipSet,Integer> {
    ShopTipSet findShopTipSetByShopId(Integer shopId);
}
