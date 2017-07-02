package com.jm.repository.jpa.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jm.repository.po.order.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderInfoForQueryVo;
import com.jm.repository.po.order.OrderInfo;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
public interface OrderRepository extends JpaRepository<OrderInfo, Long> {

    
    Page<OrderInfo> findAll(Specification<OrderInfo> specification, Pageable pageable);

    List<OrderInfo> findAll(Specification<OrderInfo> orderVo);

    List<OrderInfo> findOrderInfoByUserIdAndStatus(Integer userId,Integer status);
    
    @Query( value = "select * from order_info where order_info_id=?1",nativeQuery=true)
    OrderInfo findOrderInfoById(Long id);
    
    @Query( value = "select count(*),o.status,o.look_status from order_info o where o.user_id=?1 and shop_id=?2 and o.look_status!=12 GROUP BY o.status order by o.order_info_id desc",nativeQuery=true)
    List<Object> queryOrderStatus(Integer userId,Integer shopId);

    @Query( value = "select count(*),o.status,o.look_status,o.good_status from order_info o where o.user_id=?1 and shop_id=?2 and o.look_status!=12 and o.status=5 order by o.order_info_id desc",nativeQuery=true)
    List<Object> queryOrderAfterSaleStatus(Integer userId,Integer shopId);

    @Query( value = "select * from order_info o where o.status=2 and o.parent_order_id=?1",nativeQuery=true)
    List<OrderInfo> queryOrderDispatch(Long orderInfoId);

    @Modifying
    @Query( value = "update order_info o set o.look_status=12 where o.user_id=?1 and o.shop_id=?2 and o.status=?3",nativeQuery=true)
    void updateLookStatus(Integer userId,Integer shopId,Integer status);
    
	OrderInfo findOrderInfoByOrderInfoId(Long orderInfoId);

    @Query( value = "select * from order_info o where o.status=0 and o.push_status=0 and TIMESTAMPDIFF(MINUTE,o.create_date,?1)>30",nativeQuery=true)
    List<OrderInfo> queryOrderStamp(Date date);

    @Modifying
    @Query( value = " UPDATE order_info o SET o.status=2,o.look_status=2,o.send_date=?2 WHERE o.order_info_id =?1"
                  + " AND o.status=1 AND (SELECT count(od.order_delivery_id) FROM order_delivery od"
                  + " LEFT JOIN order_delivery_detail odd ON odd.order_delivery_id = od.order_delivery_id"
                  + " WHERE od.order_info_id = o.order_info_id )=(SELECT count(ode.order_detail_id) FROM"
                  + " order_detail ode WHERE ode.order_info_id=o.order_info_id)",nativeQuery=true)
    int updateOrder(Long orderInfoId,Date sendTime);

    @Modifying
    @Query( value = " UPDATE order_info o SET o.status=2,o.look_status=2,o.send_date=?2 WHERE o.order_info_id =?1 and o.status=1",nativeQuery=true)
    int updateOrder1(Long orderInfoId,Date sendTime);

    @Modifying
    @Query(value = "UPDATE order_info o SET o.status=2,o.look_status=2,o.send_date=?2 WHERE o.order_info_id in(?1)"
                 + " AND o.status=1",nativeQuery=true)
    int updateOrderInfo(List<Long> orderInfoId,Date sendTime);

}