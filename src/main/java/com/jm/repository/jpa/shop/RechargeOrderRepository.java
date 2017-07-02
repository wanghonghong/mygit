package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.shop.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


/**
 * <p>积分充值</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/2
 */
public interface RechargeOrderRepository extends JpaRepository<RechargeOrder, Long>{

    List<RechargeOrder> findByUserIdAndStatus(Integer userId,Integer status);
    
    RechargeOrder findByOrderInfoId(Long orderInfoId);

}
