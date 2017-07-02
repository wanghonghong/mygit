package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.ShopMain;
import org.springframework.data.jpa.repository.JpaRepository;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface ShopMainRepository extends JpaRepository<ShopMain, Integer>{


    ShopMain findShopMainByShopId(Integer shopId);
}
