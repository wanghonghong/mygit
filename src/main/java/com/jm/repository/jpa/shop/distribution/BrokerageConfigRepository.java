package com.jm.repository.jpa.shop.distribution;

import com.jm.repository.po.shop.brokerage.BrokerageConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface BrokerageConfigRepository extends JpaRepository<BrokerageConfig, Integer>{

    BrokerageConfig findByFeeTypeAndShopId(int feeType, Integer shopId);
}
