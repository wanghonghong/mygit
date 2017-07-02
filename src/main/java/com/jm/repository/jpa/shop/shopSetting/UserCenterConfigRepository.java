package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface UserCenterConfigRepository extends JpaRepository<UserCenterConfig, Integer>{


    UserCenterConfig findUserCenterConfigByShopId(Integer shopId);
}
