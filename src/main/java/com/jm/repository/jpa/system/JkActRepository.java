package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JkActCommission;
import com.jm.repository.po.system.JmRechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenyy on 2017/5/19.
 */
public interface JkActRepository extends JpaRepository<JkActCommission, Integer> {

}
