package com.jm.repository.jpa.shop.distribution;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.ChannelRecordQo;
import com.jm.mvc.vo.shop.distribution.ChannelRecordVo;
import com.jm.repository.po.shop.brokerage.BrokerageOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**订单佣金
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface BrokerageOrderRepository extends JpaRepository<BrokerageOrder, Integer>{

    List<BrokerageOrder> findByOrderInfoId(Long orderInfoId);

    @Query( value = "select * from brokerage_order where Date(take_date) <=date_sub(now(),interval 5 minute) and status =1",nativeQuery=true)
    List<BrokerageOrder> findByTakeDate();

    @Query(value = "select sum(r.commission_price) as price from brokerage_order r where date_format(r.order_date,'%Y-%m')=date_format(now(),'%Y-%m') and r.user_id=?1 and r.status!=3 ", nativeQuery = true)
    Object getNowMonthSumPrice(Integer wxuserid);

    @Query(value = "select u.headimgurl,u.nickname,b.total_price,b.commission_price,b.brokerage,b.order_date,o.status " +
            " from brokerage_order b " +
            " LEFT JOIN order_info o ON o.order_info_id = b.order_info_id " +
            " LEFT JOIN wx_user u ON u.user_id = o.user_id " +
            " where b.user_id=?1  and b.status!=3   order by b.id asc limit ?2,10 ", nativeQuery = true)
    List<Object> getCommTop20(Integer userid,Integer curpage);

    @Query(value = "select sum(r.commission_price) as price from brokerage_order r where r.user_id=?1 and r.status!=3 ", nativeQuery = true)
    Object getSumPriceNot3(Integer wxuserid);

}
