package com.jm.repository.jpa.shop;

import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.shop.ShopEntity;
import com.jm.repository.po.shop.Shop;






import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
public interface ShopEntityRepository extends JpaRepository<ShopEntity, Integer> {
   //opEntity> findAll(Specification<ShopEntity> specification, Pageable pageable);

    @Query(value="select a from ShopEntity a where a.shopId=?1 " )
    Page findAll(Integer shopId ,Pageable pageable);

    List<ShopEntity> findShopEntityByShopId(Integer shopId);
}
