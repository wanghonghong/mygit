package com.jm.repository.jpa.shop.distribution;

import com.jm.repository.po.shop.brokerage.PutSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface PutSetRepository extends JpaRepository<PutSet, Integer>{

//    PutSet findByShopId(Integer shopId);

    List<PutSet> findPutSetByPayType(int payType);

    PutSet findByShopIdAndPayType(Integer shopId, int payType);
}
