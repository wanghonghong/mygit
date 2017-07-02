package com.jm.repository.jpa.shop.activity.question;

import com.jm.repository.po.shop.activity.question.ShopSign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 *
 * @author wukf
 * @version latest
 * @date 2016/12/03
 */
public interface ShopSignRepository extends JpaRepository<ShopSign, Integer>{


    List<ShopSign> findByShopId(Integer shopId);
}
