package com.jm.repository.jpa.system;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.system.JmRechargeOrder;

public interface JmRechargeOrderRepository extends  JpaRepository<JmRechargeOrder, Integer> {

	JmRechargeOrder findByOrderInfoId(Long orderInfoId);
}
