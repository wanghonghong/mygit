package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.ShopTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface ShopTemplateRepository extends JpaRepository<ShopTemplate, Integer>{

    @Query(value="select * from shop_template order by sort" ,nativeQuery = true)
    List<ShopTemplate> findShopTemplateList();



    ShopTemplate findShopTemplateByTempId(Integer tempId);

}
