package com.jm.repository.jpa.shop.integral;

import com.jm.repository.po.shop.integral.IntegralRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


/**
 * <p>微信用户积分列表</p>
 *
 * @author 吴克府
 * @version latest
 * @date 2016/9/30
 */
public interface IntegralRecordRepository extends JpaRepository<IntegralRecord, Integer>{

    @Query( value = "select * from integral_record w where w.user_id=?1 and w.integral_type=1 and w.create_time BETWEEN date_sub(curdate(),interval 0 day) and date_sub(curdate(),interval -1 day)",nativeQuery=true)
    List<IntegralRecord> queryLoginWxUserIntegral(Integer userId);


    @Query( value = "select r.order_info_id,r.shop_id,r.user_id,r.total_price from order_info r,integral_set s where r.shop_id= s.shop_id and s.is_buy=1 and r.take_date < date_sub(curdate(),interval 7 day)",nativeQuery=true)
    List<Map<String,Object>> queryBuyIntegralList();


}
