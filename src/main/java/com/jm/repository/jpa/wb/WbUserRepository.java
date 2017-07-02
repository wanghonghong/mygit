package com.jm.repository.jpa.wb;

import com.jm.repository.po.wb.WbShopUser;
import com.jm.repository.po.wb.WbUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>微博用户</p>
 *
 * @version latest
 * @Author whh
 * @Date 2017/3/6
 */
public interface WbUserRepository extends JpaRepository<WbUser,Long> {

}
