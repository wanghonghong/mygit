package com.jm.repository.jpa.shop.activity;
import com.jm.repository.po.shop.activity.CardProduct;
import com.jm.repository.po.shop.activity.ShopCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>卡卷商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
public interface CardProductRepository extends JpaRepository<CardProduct, Integer>{

    List<CardProduct> findByCardId(Integer cardId);

}
