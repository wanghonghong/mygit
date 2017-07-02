package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>微博商家用户</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/4 10:59
 */
public interface WbShopUserRepository extends JpaRepository<WbShopUser,Long> {

    WbShopUser findWbShopUserByUid(Long uid);

}
