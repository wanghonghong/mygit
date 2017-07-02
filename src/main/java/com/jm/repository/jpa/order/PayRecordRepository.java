package com.jm.repository.jpa.order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jm.repository.po.order.PayRecord;

/**
 *<p>支付流水</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月23日
 */
public interface PayRecordRepository extends JpaRepository<PayRecord, Long>  {



}
