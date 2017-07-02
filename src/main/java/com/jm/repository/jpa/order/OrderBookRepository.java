package com.jm.repository.jpa.order;

import com.jm.repository.po.order.OrderBook;
import com.jm.repository.po.order.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> {

    Page<OrderBook> findAll(Specification<OrderBook> specification, Pageable pageable);

    List<OrderBook> findAll(Specification<OrderBook> orderVo);

    List<OrderBook> findOrderInfoByUserIdAndStatus(Integer userId, Integer status);
    
    @Query( value = "select * from order_book where id=?1",nativeQuery=true)
    OrderBook findOrderInfoById(Long id);
    
    @Modifying
    @Query( value = "update order_book o set o.status=1 where o.id=?1",nativeQuery=true)
    void updateStatus(Integer id);

}