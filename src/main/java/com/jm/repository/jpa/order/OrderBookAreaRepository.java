package com.jm.repository.jpa.order;

import com.jm.repository.po.order.OrderBook;
import com.jm.repository.po.order.OrderBookArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface OrderBookAreaRepository extends JpaRepository<OrderBookArea, Integer> {

    @Query( value = "select * from order_book where id=?1",nativeQuery=true)
    OrderBookArea findOrderBookAreaById(Integer id);
    
    @Modifying
    @Query( value = "update order_book_area o set o.status=1 where o.id=?1",nativeQuery=true)
    void updateStatus(Integer id);

    List<OrderBookArea> findByShopId(Integer shopId);

    @Modifying
    @Query( value = "delete from order_book_area where shop_id =?1 and id = ?2 ",nativeQuery=true)
    void deleteByShopId(Integer shopId,Integer id);

    OrderBookArea findByIdAndShopId(Integer id,Integer shopId);
}