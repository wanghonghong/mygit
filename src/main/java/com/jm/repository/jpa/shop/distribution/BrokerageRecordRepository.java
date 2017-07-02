package com.jm.repository.jpa.shop.distribution;

import com.jm.repository.po.shop.brokerage.Brokerage;
import com.jm.repository.po.shop.brokerage.BrokerageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrokerageRecordRepository extends JpaRepository<BrokerageRecord, Integer>{


    List<BrokerageRecord> findByUserId(int userId);


    @Query(value = "select * " +
            " from brokerage_record b " +
            " where b.user_id=?1 order by b.id asc limit ?2,10 ", nativeQuery = true)
    List<BrokerageRecord> findRecordByUserIdLimit(Integer userid,Integer curpage);


}
